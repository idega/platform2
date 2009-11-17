package is.idega.idegaweb.campus.nortek.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

public class CardBMPBean extends GenericEntity implements Card {
	public static final String ENTITY_NAME = "nt_card";

	private static final String COLUMN_CARD = "card_serial_number";
	
	private static final String COLUMN_DECODED_SERIAL = "card_decoded_number";

	private static final String COLUMN_USER = "card_user";

	private static final String COLUMN_VALID = "valid";

	private static final String COLUMN_DELETED = "deleted";
	
	public CardBMPBean() {
	}

	public void initializeAttributes() {
		addAttribute(COLUMN_CARD, "Card serial number", String.class, 255);		
		setAsPrimaryKey(COLUMN_CARD, true);
		addAttribute(COLUMN_DECODED_SERIAL, "Decoded card serial number", String.class, 255);
		addAttribute(COLUMN_VALID, "Valid", Boolean.class);
		addManyToOneRelationship(COLUMN_USER, User.class);
		addAttribute(COLUMN_DELETED, "Deleted", Boolean.class);
		
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
	
	public String getDecodedCardSerialNumber() {
		return getStringColumnValue(COLUMN_DECODED_SERIAL);
	}
	
	public User getUser() {
		return (User) getColumnValue(COLUMN_USER);
	}
	
	public boolean getIsValid() {
		return getBooleanColumnValue(COLUMN_VALID, true);
	}
	
	public boolean getIsDeleted() {
		return getBooleanColumnValue(COLUMN_DELETED, false);
	}
	
	//setters
	public void setCardSerialNumber(String serialNumber) {
		setColumn(COLUMN_CARD, serialNumber);
	}
	
	public void setDecodedCardSerialNumber(String serialNumber) {
		setColumn(COLUMN_DECODED_SERIAL, serialNumber);
	}
	
	public void setUser(User user) {
		setColumn(COLUMN_USER, user);
	}
	
	public void setIsValid(boolean isValid) {
		setColumn(COLUMN_VALID, isValid);
	}
	
	public void setIsDeleted(boolean isDeleted) {
		if (isDeleted) {
			setIsValid(!isDeleted);
		}
		setColumn(COLUMN_DELETED, isDeleted);
	}
	
	//ejb
	public Collection ejbFindAll() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhere();
		query.appendLeftParenthesis();
		query.append(COLUMN_DELETED);
		query.appendIsNull();
		query.appendOr();
		query.appendEquals(COLUMN_DELETED, false);
		query.appendRightParenthesis();
		
		query.appendOrderBy(COLUMN_DECODED_SERIAL);
		System.out.println("query = " + query.toString()); 

		return idoFindPKsByQuery(query);
	}
	
	public Object ejbFindByUser(User user) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_USER, user);
		query.appendAnd();
		query.appendLeftParenthesis();
		query.append(COLUMN_DELETED);
		query.appendIsNull();
		query.appendOr();
		query.appendEquals(COLUMN_DELETED, false);
		query.appendRightParenthesis();
		
		System.out.println("query = " + query.toString()); 
		return idoFindOnePKByQuery(query);
	}
	
	public Collection ejbFindAllByValid(boolean valid) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_VALID, valid);
		query.appendAnd();
		query.appendLeftParenthesis();
		query.append(COLUMN_DELETED);
		query.appendIsNull();
		query.appendOr();
		query.appendEquals(COLUMN_DELETED, false);
		query.appendRightParenthesis();
		query.appendOrderBy(COLUMN_DECODED_SERIAL);

		System.out.println("query = " + query.toString()); 
		return idoFindPKsByQuery(query);
	}
}