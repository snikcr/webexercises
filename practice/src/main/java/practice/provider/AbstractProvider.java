package practice.provider;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Abstract class to provide basic attributes for implementing classes.
 * @param <T>
 */
public abstract class AbstractProvider<T> implements IProvider<T> {

	/**
	 * The class logger.
	 */
	protected final Logger logger = Logger.getLogger(getClass());
	
	/**
	 * JDBC template providing simple access the database.
	 */
	@Autowired
	protected JdbcTemplate jdbcTemplate;

}
