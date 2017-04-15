package practice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import practice.model.User;
import practice.provider.UserDb;
import practice.provider.UserProvider;
import practice.service.transformer.UserDbTransformer;
import practice.service.transformer.UserTransformer;

/**
 * Provides functionality for {@link User}
 */
@Service("usersService")
public class UserService {
	
	@Autowired
	private UserProvider provider;
	
	/**
	 * The User -> UserDb transformer
	 */
	private UserTransformer userTransformer = new UserTransformer();
	
	/**
	 * The UserDb -> User transformer
	 */
	private UserDbTransformer userDbTransformer = new UserDbTransformer();
	
	/** 
	 * The class logger.
	 */
	private static final Logger logger = Logger.getLogger(UserService.class);
	
	/**
	 * Asynchronously retrieves all the existing users from the repository.
	 * @return The list of all {@link User}
	 */
	@Async
	public Future<List<User>> findAll() {
		
		logger.info("Retrieving all users");
		
		List<User> users = new ArrayList<>();
		
		users.addAll(provider
				.findAll()
				.stream()
				.map(userDb -> userDbTransformer.transform(userDb))
				.collect(Collectors.toList()));
		
		return new AsyncResult<List<User>>(users);
	}
	
	/**
	 * Retrieves an existing user from the repository and returns it.
	 * @param id The user id to retrieve.
	 * @return A {@link User} if it is found, null otherwise.
	 * @throws Exception
	 */
	public User find(int id) {
		
		logger.info("Retrieving user");

		User user = null;

		UserDb userDb = provider.find(id);
		
		if (userDb != null) {
			user = userDbTransformer.transform(userDb);
		}

		return user;
	}
	
	/**
	 * Creates a new user in the repository and returns the new instance.
	 * @param data The user data for the new user.
	 * @return
	 * @throws Exception
	 */
	public User create(User data) {
		
		logger.info("Creating user");
		
		User user = null;
		
		UserDb dataDb = userTransformer.transform(data);
		UserDb createdUserDb = provider.insert(dataDb);

		user = userDbTransformer.transform(createdUserDb);
		
		return user;
	}
	
	/**
	 * Updates an existing user in the repository and returns the instance.
	 * @param data The user data to update. The id must be provided.
	 * @return
	 * @throws Exception
	 */
	public User update(User data) {

		logger.info("Updating user");
		logger.info("UserService " + this.toString());
		
		UserDb dataDb = userTransformer.transform(data);
		UserDb createdUserDb = provider.update(dataDb);

		User user = null;
		if (createdUserDb != null) {
			user = userDbTransformer.transform(createdUserDb);
		}
		
		return user;
	}
	
	/**
	 * Asynchronously removes the user with the given id from the repository.
	 * @param id The id of the user to remove.
	 * @throws Exception
	 */
	@Async
	public Future<Boolean> remove(int id) {
		
		logger.info("Removing user");
		boolean deleted = provider.delete(id);
		
		return new AsyncResult<Boolean>(Boolean.valueOf(deleted));
	}
}
