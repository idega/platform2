package is.idega.block.nationalregister.data;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

/**
 * A data class to hold the national register data, for Iceland, which is read
 * in from the E128 and E36 file from SKYRR.
 * 
 * @author palli
 */
public class NationalRegisterBMPBean extends GenericEntity implements NationalRegister {
	protected static final String ENTITY_NAME = "reg_national_is";

	protected static final String SYMBOL = "symbol";
	protected static final String OLD_ID = "old_id";
	protected static final String SSN = "ssn";
	protected static final String FAMILY_ID = "family_id";
	protected static final String NAME = "name";
	protected static final String COMMUNE = "commune";
	protected static final String STREET = "street";
	protected static final String BUILDING = "building";
	protected static final String FLOOR = "floor";
	protected static final String SEX = "sex";
	protected static final String MARITAL_STATUS = "marital_status";
	protected static final String EMPTY = "empty";
	protected static final String PROHIBIT_MARKING = "prohibit";
	protected static final String NATIONALITY = "nationality";
	protected static final String PLACE_OF_BIRTH = "birth_place";
	protected static final String SPOUSE_SSN = "spouse_ssn";
	protected static final String FATE = "status";
	protected static final String PARISH = "parish";
	protected static final String PO = "po";
	protected static final String ADDRESS = "address";
	
	protected static final String ADDRESS_CODE = "address_dode";
	protected static final String DATE_OF_MODIFICATION = "date_of_modification";
	protected static final String PLACEMENT_CODE = "placement_code";
	protected static final String DATE_OF_CREATION = "date_of_creation";
	protected static final String LAST_DOMESTIC_ADDRESS = "last_domestic_address";
	protected static final String AGENT_SSN = "agent_ssn";
	protected static final String IS_NEW = "is_new";
	protected static final String ADDRESS_NAME = "address_name";
	protected static final String DATE_OF_DELETION = "date_of_deletion";
	protected static final String NEW_SSN_OR_NAME = "new_ssn";
	protected static final String DATE_OF_BIRTH = "date_of_birth";
	/**
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/**
	 * @see com.idega.data.IDOLegacyEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());

		addAttribute(SYMBOL, "Einkenni", true, true, java.lang.String.class, 2);
		addAttribute(OLD_ID, "Nafnnumer", true, true, java.lang.String.class, 8);
		addAttribute(SSN, "Kennitala", true, true, java.lang.String.class, 10);
		addAttribute(FAMILY_ID, "Fjolskyldunumer", true, true, java.lang.String.class, 10);
		addAttribute(NAME, "Nafn", true, true, java.lang.String.class, 31);
		addAttribute(COMMUNE, "Sveitarfelag", true, true, java.lang.String.class, 4);
		addAttribute(STREET, "Gata", true, true, java.lang.String.class, 4);
		addAttribute(BUILDING, "Hus", true, true, java.lang.String.class, 4);
		addAttribute(FLOOR, "Haed", true, true, java.lang.String.class, 3);
		addAttribute(SEX, "Kyn", true, true, java.lang.String.class, 1);
		addAttribute(MARITAL_STATUS, "Hjuskaparstada", true, true, java.lang.String.class, 1);
		addAttribute(EMPTY, "AUTT", true, true, java.lang.String.class, 2);
		addAttribute(PROHIBIT_MARKING, "Bannmerking", true, true, java.lang.String.class, 1);
		addAttribute(NATIONALITY, "Rikisfang", true, true, java.lang.String.class, 2);
		addAttribute(PLACE_OF_BIRTH, "Faedingarstadur", true, true, java.lang.String.class, 4);
		addAttribute(SPOUSE_SSN, "Kennitala maka", true, true, java.lang.String.class, 10);
		addAttribute(FATE, "Afdrif", true, true, java.lang.String.class, 4);
		addAttribute(PARISH, "Sokn", true, true, java.lang.String.class, 3);
		addAttribute(PO, "Postnumer", true, true, java.lang.String.class, 3);
		addAttribute(ADDRESS, "Heimili", true, true, java.lang.String.class, 21);
		
		addAttribute(ADDRESS_CODE, "Logheimiliskodi", true, true, java.lang.String.class, 12);
		addAttribute(DATE_OF_MODIFICATION, "Dagsetning breytinga", true, true, String.class, 6);
		addAttribute(PLACEMENT_CODE, "Adseturskodi", true, true, String.class, 12);
		addAttribute(DATE_OF_CREATION, "Dagsetning nàskr‡ningar", true, true, String.class, 6);
		addAttribute(LAST_DOMESTIC_ADDRESS, "Sidasta logheimili a landinu", true, true, String.class, 12);
		addAttribute(AGENT_SSN, "Kennitala umbodsmann", true, true, String.class, 10);
		addAttribute(IS_NEW, "Nyr a skra", true, true, String.class, 1);
		addAttribute(ADDRESS_NAME, "Heimilisfang i nefnifalli", true, true, String.class, 21);
		addAttribute(DATE_OF_DELETION, "Dagsetning brottfellinguar", true, true, String.class, 6);
		addAttribute(NEW_SSN_OR_NAME, "Kennitala/Nafn ef breyting", true, true, String.class, 18);
		addAttribute(DATE_OF_BIRTH, "Faedingardagur", true, true, String.class, 6);
		
		addIndex(SSN);
		setUnique(SSN, true);
	}

	public void setSymbol(String symbol) {
		setColumn(SYMBOL, symbol);
	}

	public String getSymbol() {
		return getStringColumnValue(SYMBOL);
	}

	public void setOldId(String id) {
		setColumn(OLD_ID, id);
	}

	public String getOldId() {
		return getStringColumnValue(OLD_ID);
	}

	public void setSSN(String ssn) {
		setColumn(SSN, ssn);
	}

	public String getSSN() {
		return getStringColumnValue(SSN);
	}

	public void setFamilyId(String id) {
		setColumn(FAMILY_ID, id);
	}

	public String getFamilyId() {
		return getStringColumnValue(FAMILY_ID);
	}

	public void setName(String name) {
		setColumn(NAME, name);
	}

	public String getName() {
		return getStringColumnValue(NAME);
	}

	public void setCommune(String commune) {
		setColumn(COMMUNE, commune);
	}

	public String getCommune() {
		return getStringColumnValue(COMMUNE);
	}

	public void setStreet(String street) {
		setColumn(STREET, street);
	}

	public String getStreet() {
		return getStringColumnValue(STREET);
	}

	public void setBuilding(String building) {
		setColumn(BUILDING, building);
	}

	public String getBuilding() {
		return getStringColumnValue(BUILDING);
	}

	public void setFloor(String floor) {
		setColumn(FLOOR, floor);
	}

	public String getFloor() {
		return getStringColumnValue(FLOOR);
	}

	public void setSex(String sex) {
		setColumn(SEX, sex);
	}

	public String getSex() {
		return getStringColumnValue(SEX);
	}

	public void setMaritalStatus(String status) {
		setColumn(MARITAL_STATUS, status);
	}

	public String getMaritalStatus() {
		return getStringColumnValue(MARITAL_STATUS);
	}

	public void setProhibitMarking(String marking) {
		setColumn(PROHIBIT_MARKING, marking);
	}

	public String getProhibitMarking() {
		return getStringColumnValue(PROHIBIT_MARKING);
	}

	public void setNationality(String nationality) {
		setColumn(NATIONALITY, nationality);
	}

	public String getNationality() {
		return getStringColumnValue(NATIONALITY);
	}

	public void setPlaceOfBirth(String pob) {
		setColumn(PLACE_OF_BIRTH, pob);
	}

	public String getPlaceOfBirth() {
		return getStringColumnValue(PLACE_OF_BIRTH);
	}

	public void setSpouseSSN(String ssn) {
		setColumn(SPOUSE_SSN, ssn);
	}

	public String getSpouseSSN() {
		return getStringColumnValue(SPOUSE_SSN);
	}

	public void setFate(String fate) {
		setColumn(FATE, fate);
	}

	public String getFate() {
		return getStringColumnValue(FATE);
	}

	public void setParish(String parish) {
		setColumn(PARISH, parish);
	}

	public String getParish() {
		return getStringColumnValue(PARISH);
	}

	public void setPO(String po) {
		setColumn(PO, po);
	}

	public String getPO() {
		return getStringColumnValue(PO);
	}

	public void setAddress(String address) {
		setColumn(ADDRESS, address);
	}

	public String getAddress() {
		return getStringColumnValue(ADDRESS);
	}

	public void setAddressCode(String code) {
		setColumn(ADDRESS_CODE, code);
	}
	
	public String getAddressCode() {
		return getStringColumnValue(ADDRESS_CODE);
	}
	
	public void setDateOfModification(String date) {
		setColumn(DATE_OF_MODIFICATION, date);
	}
	
	public String getDateOfModification() {
		return getStringColumnValue(DATE_OF_MODIFICATION);
	}
	
	public void setPlacementCode(String code) {
		setColumn(PLACEMENT_CODE, code);
	}
	
	public String getPlacementCode() {
		return getStringColumnValue(PLACEMENT_CODE);
	}
	
	public void setDateOfCreation(String date) {
		setColumn(DATE_OF_CREATION, date);
	}
	
	public String getDateOfCreation() {
		return getStringColumnValue(DATE_OF_CREATION);
	}
	
	public void setLastDomesticAddress(String address) {
		setColumn(LAST_DOMESTIC_ADDRESS, address);
	}
	
	public String getLastDomesticAddress() {
		return getStringColumnValue(LAST_DOMESTIC_ADDRESS);
	}
	
	public void setAgentSSN(String ssn) {
		setColumn(AGENT_SSN, ssn);
	}
	
	public String getAgentSSN() {
		return getStringColumnValue(AGENT_SSN);
	}
	
	public void setIsNew(String isNew) {
		setColumn(IS_NEW, isNew);
	}
	
	public String getIsNew() {
		return getStringColumnValue(IS_NEW);
	}
	
	public void setAddressName(String addressName) {
		setColumn(ADDRESS_NAME, addressName);
	}
	
	public String getAddressName() {
		return getStringColumnValue(ADDRESS_NAME);
	}

	public void setDateOfDeletion(String date) {
		setColumn(DATE_OF_DELETION, date);
	}
	
	public String getDateOfDeletion() {
		return getStringColumnValue(DATE_OF_DELETION);
	}
	
	public void setNewSsnOrName(String ssnOrName) {
		setColumn(NEW_SSN_OR_NAME, ssnOrName);
	}
	
	public String getNewSsnOrName() {
		return getStringColumnValue(NEW_SSN_OR_NAME);
	}
	
	public void setDateOfBirth(String date) {
		setColumn(DATE_OF_BIRTH, date);
	}
	
	public String getDateOfBirth() {
		return getStringColumnValue(DATE_OF_BIRTH);
	}

	public Collection ejbFindAll() throws FinderException, RemoteException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection ejbFindAllBySSN(String ssn) throws FinderException, RemoteException {
		StringBuffer query = new StringBuffer("select * from ");
		query.append(this.getEntityName());
		query.append(" where ");
		query.append(SSN);
		query.append(" = '");
		query.append(ssn);
		query.append("'");
		
		return idoFindPKsBySQL(query.toString());
	}
}