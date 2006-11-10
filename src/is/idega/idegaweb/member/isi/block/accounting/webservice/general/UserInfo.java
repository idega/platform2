package is.idega.idegaweb.member.isi.block.accounting.webservice.general;

public class UserInfo {

	private String socialsecurity;

	private String firstName;

	private String middleName;

	private String lastName;

	private AddressInfo address;

	private String error;

	private boolean valid = false;

	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @param firstName
	 *            The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @param lastName
	 *            The lastName to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return Returns the middleName.
	 */
	public String getMiddleName() {
		return this.middleName;
	}

	/**
	 * @param middleName
	 *            The middleName to set.
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return Returns the socialsecurity.
	 */
	public String getSocialsecurity() {
		return this.socialsecurity;
	}

	/**
	 * @param socialsecurity
	 *            The socialsecurity to set.
	 */
	public void setSocialsecurity(String socialsecurity) {
		this.socialsecurity = socialsecurity;
	}

	/**
	 * @return Returns the error.
	 */
	public String getError() {
		return this.error;
	}

	/**
	 * @param error
	 *            The error to set.
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @return Returns the valid.
	 */
	public boolean isValid() {
		return this.valid;
	}

	/**
	 * @param valid
	 *            The valid to set.
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * @return Returns the address.
	 */
	public AddressInfo getAddress() {
		return this.address;
	}

	/**
	 * @param address
	 *            The address to set.
	 */
	public void setAddress(AddressInfo address) {
		this.address = address;
	}
}