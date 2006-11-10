package is.idega.idegaweb.member.isi.block.accounting.webservice.general;

public class AddressInfo {

	private String streetName;

	private String streetNumber;

	private String city;

	private String postalcode;

	private String country;

	/**
	 * @return Returns the city.
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * @param city
	 *            The city to set.
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return Returns the country.
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 * @param country
	 *            The country to set.
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return Returns the postalcode.
	 */
	public String getPostalcode() {
		return this.postalcode;
	}

	/**
	 * @param postalcode
	 *            The postalcode to set.
	 */
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	/**
	 * @return Returns the streetName.
	 */
	public String getStreetName() {
		return this.streetName;
	}

	/**
	 * @param streetName
	 *            The streetName to set.
	 */
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	/**
	 * @return Returns the streetNumber.
	 */
	public String getStreetNumber() {
		return this.streetNumber;
	}

	/**
	 * @param streetNumber
	 *            The streetNumber to set.
	 */
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
}
