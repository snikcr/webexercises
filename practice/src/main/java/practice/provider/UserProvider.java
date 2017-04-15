package practice.provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import practice.exception.ProcessException;

/**
 * Repository to access the users table.
 */
@Repository("usersProvider")
public class UserProvider extends AbstractProvider<UserDb> {
	
	/**
	 * Retrieves all the records in the users table.
	 * @return The list of {@link UserDb}
	 * @throws Exception if an error occurs.
	 */
	@Override
	public List<UserDb> findAll() {
		
		List<UserDb> userDbs = null;
		
		try {
			
			userDbs = jdbcTemplate.query("select id, name, birthdate from users", 
					(rs, row) -> new UserDb(rs.getInt("id"), rs.getString("name"), rs.getString("birthdate")));
			
			logger.info(String.format("Returning %d users.", userDbs.size()));
			
		} catch (DataAccessException e) {
			
			ProcessException pe = new ProcessException(HttpStatus.INTERNAL_SERVER_ERROR, e,  
					"error.user.retrieveall");
			throw pe;
		}
		
		return userDbs;
	}
	
	/**
	 * Retrieves a single record from the users table.
	 * @param id The id of the user to query.
	 * @return The {@link UserDb}
	 * @throws Exception if an error occurs.
	 */
	public UserDb find(int id) {

		UserDb userDb = null;
		
		try {
			
			List<UserDb> list = jdbcTemplate.query("select id, name, birthdate from users where id = ?", new Object[] { id }, 
					(rs, row) -> new UserDb(rs.getInt("id"), rs.getString("name"), rs.getString("birthdate")));
			
			if (!list.isEmpty()) {
				userDb = list.get(0);
			}
			
//			userDb = jdbcTemplate.queryForObject("select id, name, birthdate from users where id = ?", new Object[] { id }, 
//					(rs, row) -> new UserDb(rs.getInt("id"), rs.getString("name"), rs.getString("birthdate")));
			logger.info(String.format("Returning %d users.", (userDb == null ? 0 : 1)));
			
		} catch (DataAccessException e) {
			ProcessException pe = new ProcessException(HttpStatus.INTERNAL_SERVER_ERROR, e,  
					"error.user.retrieve", String.valueOf(id));
			throw pe;
		}
		
		return userDb;
	}
	
	/**
	 * Adds a new record to the users table.
	 * @param dataDb The data to add.
	 * @return The created {@link UserDb} with generated id.
	 * @throws Exception if an error occurs.
	 */
	public UserDb insert(UserDb dataDb) {
		
		UserDb userDb = null;
		
		try {
			
			GeneratedKeyHolder holder = new GeneratedKeyHolder();
			
			PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement preparedStatement = con.prepareStatement("insert into users(name, birthdate) values(?, ?)");
					preparedStatement.setString(1, dataDb.getName());
					preparedStatement.setString(2, dataDb.getBirthdate());
					return preparedStatement;
				}
			};
			
			jdbcTemplate.update(preparedStatementCreator, holder);
			
			Number generatedKey = holder.getKey();
			
			if (generatedKey != null) {
				
				logger.info(String.format("Created user with id %d", generatedKey.intValue()));
				userDb = find(generatedKey.intValue());
			}
			
			
		} catch (Exception e) {
			ProcessException pe = new ProcessException(HttpStatus.INTERNAL_SERVER_ERROR, e,  
					"error.user.creation", String.valueOf(dataDb.getName()), String.valueOf(dataDb.getBirthdate()));
			throw pe;
		}
		
		return userDb;
	}
	
	/**
	 * Updates an existing user in the users table.
	 * @param dataDb The values to update.
	 * @return The updated {@link UserDb}.
	 * @throws Exception if an error occurs.
	 */
	public UserDb update(UserDb dataDb) {
		
		UserDb userDb = null;
		try {
			
			int affected = jdbcTemplate.update("update users set name = ?, birthdate = ? where id = ?", dataDb.getName(), dataDb.getBirthdate(), dataDb.getId());
			
			if (affected > 0) {
				
				logger.info(String.format("Updated user with id %d", dataDb.getId()));
				userDb = find(dataDb.getId());
			} else {
				
				logger.warn(String.format("User with id %d not found", dataDb.getId()));
			}
			
		} catch (Exception e) {
			
			ProcessException pe = new ProcessException(HttpStatus.INTERNAL_SERVER_ERROR, e,  
					"error.user.update", String.valueOf(dataDb.getId()), String.valueOf(dataDb.getName()),
					String.valueOf(dataDb.getBirthdate()));
			throw pe;

		}
		
		return userDb;
	}
	
	/**
	 * Deletes an existing user.
	 * @param id The id of the user to delete.
	 * @throws Exception if an error occurs.
	 */
	public boolean delete(int id) {
		
		boolean deleted = false;
		
		try {
			
			int affected = jdbcTemplate.update("delete from users where id = ?", String.valueOf(id));
			
			if (affected > 0) {
				
				deleted = true;
				logger.info(String.format("Removed user with id %d", id));
				
			} else {
				
				logger.warn(String.format("User with id %d not found", id));
			}
			
		} catch (DataAccessException e) {
			
			ProcessException pe = new ProcessException(HttpStatus.INTERNAL_SERVER_ERROR, e,  
					"error.user.deletion", String.valueOf(id));
			throw pe;
		}
		
		return deleted;
	}
}
