package practice.provider;

import java.util.List;

/**
 * Providers interface.
 * @param <T> The database object class related.
 */
public interface IProvider<T> {

	/**
	 * Retrieves all the records in the related table.
	 * @return The list of retrieved records.
	 */
	public List<T> findAll();
	
	/**
	 * Retrieves a single record from the related table.
	 * @param key The key value for the query.
	 * @return The retrieved object, null if the record was not found.
	 */
	public T find(int key);
	
	/**
	 * Adds a new record to the related table.
	 * @param data The values for the new record.
	 * @return The created object for the record with generated key.
	 */
	public T insert(T data);
	
	/**
	 * Updates an existing record in the related table.
	 * @param data The values to update.
	 * @return The updated object, null if the record was not found.
	 */
	public T update(T data);
	
	/**
	 * Deletes an existing record.
	 * @param key The key of the record to delete.
	 * @return Whether the deletion was successful or not.
	 */
	public boolean delete(int key);
}
