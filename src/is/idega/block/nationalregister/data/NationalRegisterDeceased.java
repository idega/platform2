package is.idega.block.nationalregister.data;


import com.idega.data.IDOEntity;

public interface NationalRegisterDeceased extends IDOEntity {

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#setSymbol
	 */
	public void setSymbol(String symbol);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#getSymbol
	 */
	public String getSymbol();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#setSSN
	 */
	public void setSSN(String ssn);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#getSSN
	 */
	public String getSSN();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#setDateOfDeath
	 */
	public void setDateOfDeath(String date);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#getDateOfDeath
	 */
	public String getDateOfDeath();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#setCommune
	 */
	public void setCommune(String commune);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#getCommune
	 */
	public String getCommune();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#setStreet
	 */
	public void setStreet(String street);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#getStreet
	 */
	public String getStreet();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#setGender
	 */
	public void setGender(String gender);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#getGender
	 */
	public String getGender();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#setMaritalStatus
	 */
	public void setMaritalStatus(String status);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#getMaritalStatus
	 */
	public String getMaritalStatus();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#setSpouseSSN
	 */
	public void setSpouseSSN(String ssn);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#getSpouseSSN
	 */
	public String getSpouseSSN();
}