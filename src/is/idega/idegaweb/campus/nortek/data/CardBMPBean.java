package is.idega.idegaweb.campus.nortek.data;

import com.idega.data.GenericEntity;
import com.idega.user.data.User;

public class CardBMPBean extends GenericEntity implements Card {
	public static final String ENTITY_NAME = "nt_card";

	private static final String COLUMN_CARD = "card_serial_number";

	private static final String COLUMN_USER = "user";

	private static final String COLUMN_VALID = "valid";

	public CardBMPBean() {
	}

	public void initializeAttributes() {
		addAttribute(COLUMN_CARD, "Card serial number", String.class, 255);
		setAsPrimaryKey(COLUMN_CARD, true);
		addAttribute(COLUMN_VALID, "Valid", Boolean.class);
		addManyToOneRelationship(COLUMN_USER, User.class);
		
		setUnique(COLUMN_USER, true);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public Class getPrimaryKeyClass() {
		return String.class;
	}

	public String getIDColumnName() {
		return COLUMN_CARD;
	}

	public void setDefaultValues() {
	}

	//getters
	public String getCardSerialNumber() {
		return getStringColumnValue(COLUMN_CARD);
	}
	
	public User getUser() {
		return (User) getColumnValue(COLUMN_USER);
	}
	
	public boolean getIsValid() {
		return getBooleanColumnValue(COLUMN_VALID, true);
	}
	
	//setters
	public void setCardSerialNumber(String serialNumber) {
		setColumn(COLUMN_CARD, serialNumber);
	}
	
	public void setUser(User user) {
		setColumn(COLUMN_USER, user);
	}
	
	public void setIsValid(boolean isValid) {
		setColumn(COLUMN_VALID, isValid);
	}
	
	//ejb
}