package practice.provider;

/**
 * Database representation of a User
 */
public class UserDb {

	/**
	 * User id.
	 */
	private int id;
	
	/**
	 * User name.
	 */
	private String name;
	
	/**
	 * User birth date stored as a string.
	 */
	private String birthdate;

	/**
	 * Constructor
	 */
	public UserDb() {
	}

	/**
	 * Constructor with fields.
	 * @param id
	 * @param name
	 * @param birthdate
	 */
	public UserDb(int id, String name, String birthdate) {
		super();
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
	 * @return the birthdate
	 */
	public String getBirthdate() {
		return birthdate;
	}

	/**
	 * @param birthdate the birthdate to set
	 */
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("UserDb[");
		sb.append(" id = ");
		sb.append(String.valueOf(this.id));
		sb.append(", name = ");
		sb.append(String.valueOf(this.name));
		sb.append(", birthdate = ");
		sb.append(String.valueOf(this.birthdate));
		
		sb.append(" ] ");
		
		return sb.toString();
	}

}
