package is.idega.block.nationalregister.data;

import com.idega.data.IDOEntity;


/**
 * @author Joakim
 *
 */
public interface NationalRegister extends IDOEntity {

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setSymbol
	 */
	public void setSymbol(String symbol);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getSymbol
	 */
	public String getSymbol();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setOldId
	 */
	public void setOldId(String id);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getOldId
	 */
	public String getOldId();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setSSN
	 */
	public void setSSN(String ssn);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getSSN
	 */
	public String getSSN();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setFamilyId
	 */
	public void setFamilyId(String id);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getFamilyId
	 */
	public String getFamilyId();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setCommune
	 */
	public void setCommune(String commune);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getCommune
	 */
	public String getCommune();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setStreet
	 */
	public void setStreet(String street);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getStreet
	 */
	public String getStreet();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setBuilding
	 */
	public void setBuilding(String building);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getBuilding
	 */
	public String getBuilding();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setFloor
	 */
	public void setFloor(String floor);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getFloor
	 */
	public String getFloor();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setSex
	 */
	public void setSex(String sex);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getSex
	 */
	public String getSex();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setMaritalStatus
	 */
	public void setMaritalStatus(String status);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getMaritalStatus
	 */
	public String getMaritalStatus();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setProhibitMarking
	 */
	public void setProhibitMarking(String marking);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getProhibitMarking
	 */
	public String getProhibitMarking();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setNationality
	 */
	public void setNationality(String nationality);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getNationality
	 */
	public String getNationality();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setPlaceOfBirth
	 */
	public void setPlaceOfBirth(String pob);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getPlaceOfBirth
	 */
	public String getPlaceOfBirth();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setSpouseSSN
	 */
	public void setSpouseSSN(String ssn);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getSpouseSSN
	 */
	public String getSpouseSSN();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setFate
	 */
	public void setFate(String fate);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getFate
	 */
	public String getFate();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setParish
	 */
	public void setParish(String parish);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getParish
	 */
	public String getParish();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setPO
	 */
	public void setPO(String po);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getPO
	 */
	public String getPO();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setAddress
	 */
	public void setAddress(String address);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getAddress
	 */
	public String getAddress();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setAddressCode
	 */
	public void setAddressCode(String code);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getAddressCode
	 */
	public String getAddressCode();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setDateOfModification
	 */
	public void setDateOfModification(String date);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getDateOfModification
	 */
	public String getDateOfModification();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setPlacementCode
	 */
	public void setPlacementCode(String code);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getPlacementCode
	 */
	public String getPlacementCode();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setDateOfCreation
	 */
	public void setDateOfCreation(String date);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getDateOfCreation
	 */
	public String getDateOfCreation();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setLastDomesticAddress
	 */
	public void setLastDomesticAddress(String address);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getLastDomesticAddress
	 */
	public String getLastDomesticAddress();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setAgentSSN
	 */
	public void setAgentSSN(String ssn);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getAgentSSN
	 */
	public String getAgentSSN();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setIsNew
	 */
	public void setIsNew(String isNew);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getIsNew
	 */
	public String getIsNew();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setAddressName
	 */
	public void setAddressName(String addressName);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getAddressName
	 */
	public String getAddressName();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setDateOfDeletion
	 */
	public void setDateOfDeletion(String date);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getDateOfDeletion
	 */
	public String getDateOfDeletion();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setNewSsnOrName
	 */
	public void setNewSsnOrName(String ssnOrName);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getNewSsnOrName
	 */
	public String getNewSsnOrName();

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#setDateOfBirth
	 */
	public void setDateOfBirth(String date);

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#getDateOfBirth
	 */
	public String getDateOfBirth();
}
