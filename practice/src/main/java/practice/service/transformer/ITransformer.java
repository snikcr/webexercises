package practice.service.transformer;

/**
 * Transformers interface.
 * @param <T> The source object class.
 * @param <V> The target object class.
 */
public interface ITransformer<T, V> {

	/**
	 * Generates an object of class V from an object of class T.
	 * @param source The source object of class.
	 * @return The generated object.
	 */
	public V transform(T source);
}
