package practice.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import practice.controller.serialization.LocalDateDeserializer;
import practice.controller.serialization.LocalDateSerializer;

/**
 * User object representation.
 */
public class User {
	
	
	public interface DefaultValidation {}
	public interface CreationValidation {}

	/**
	 * User id.
	 */
	@Min(value = 1, message="error.id.minsize", groups = { DefaultValidation.class })
	private int id;
	
	/**
	 * User name.
	 */
	@NotNull(message="error.name.notnull", groups = { DefaultValidation.class, CreationValidation.class })
	@Size(min = 1, message = "error.name.notnull", groups = { DefaultValidation.class, CreationValidation.class } )
	private String name;
	
	/**
	 * User birth date.
	 */
	@NotNull(message="error.birthdate.notnull", groups = { DefaultValidation.class,CreationValidation.class })
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonSerialize(using=LocalDateSerializer.class)
	@JsonDeserialize(using=LocalDateDeserializer.class)
	private LocalDate birthdate;
	
	/**
	 * Constructor.
	 */
	public User() {
	}
	
	/**
	 * Constructor with fields.
	 * @param id The User id.
	 * @param name The User name.
	 * @param birthdate The User birth date.
	 */
	public User(int id, String name, LocalDate birthdate) {
		this.id = id;
		this.name = name;
		this.birthdate = birthdate;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the birth date
	 */
	public LocalDate getBirthdate() {
		return birthdate;
	}

	/**
	 * @param birthdate the birth date to set
	 */
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("User[");
		sb.append(" id = ");
		sb.append(String.valueOf(this.id));
		sb.append(", name = ");
		sb.append(String.valueOf(this.name));
		sb.append(", birthdate = ");
		
		if (this.birthdate == null) {
			sb.append("null");
		} else {
			sb.append(this.birthdate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		}
		
		sb.append(" ] ");
		
		return sb.toString();
	}
}
