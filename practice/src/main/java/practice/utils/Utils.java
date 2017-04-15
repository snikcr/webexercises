package practice.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;

import practice.exception.ProcessException;

public class Utils {

	/**
	 * The DateTimeFormatter to transform representations from {@link LocalTime}
	 * to {@link String} and vice versa.
	 */
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	/**
	 * Parses a {@link String}
	 * @param str The string to parse
	 * @return The generated {@link LocalDate}
	 */
	public synchronized static LocalDate stringToLocalDate(String str) {

		LocalDate localDate = null;
		try {
			localDate = LocalDate.parse(str, formatter);
		} catch (Exception e) {
			throw new ProcessException(HttpStatus.INTERNAL_SERVER_ERROR, 
					e, "error.conversion", String.class.getName(), LocalDate.class.getName());
		}
		return localDate;
	}

	/**
	 * Generates a {@link String} representation of a {@link LocalDate}
	 * @param localDate The date to transform
	 * @return The generated {@link String}
	 */
	public synchronized static String localDateToString(LocalDate localDate) {

		String str = null;

		try {
			
			str = localDate.format(formatter);
			
		} catch (Exception e) {
			
			throw new ProcessException(HttpStatus.INTERNAL_SERVER_ERROR, 
					e, "error.conversion", LocalDate.class.getName(), String.class.getName());
			
		}
		return str;
	}
}
