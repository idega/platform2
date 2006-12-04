package is.idega.idegaweb.campus.nortek.data;

import java.sql.Timestamp;

import com.idega.data.GenericEntity;

public class CardTransactionLogBMPBean extends GenericEntity implements
		CardTransactionLog {
	
	protected static final String ENTITY_NAME = "nt_transaction_log";
	
	protected static final String COLUMN_CARD = "card";
	
	protected static final String COLUMN_SERIAL_NUMBER = "serial_number";
	
	protected static final String COLUMN_ENTRY_DATE = "entry_date";
	
	protected static final String COLUMN_EXTERNAL_ENTRY_DATE = "ext_entry_date";
	
	protected static final String COLUMN_ACTION = "card_action";
	
	protected static final String COLUMN_VALUE = "action_value";
	
	protected static final String COLUMN_TERMINAL = "terminal";
	
	protected static final String COLUMN_IS_ERROR = "isError";
	
	protected static final String COLUMN_ERROR_MESSAGE = "errorMsg";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_CARD, Card.class);
		addAttribute(COLUMN_SERIAL_NUMBER, "Card serial number", String.class);
		addAttribute(COLUMN_ENTRY_DATE, "Entry date", Timestamp.class);
		addAttribute(COLUMN_EXTERNAL_ENTRY_DATE, "Extenal entry date", Timestamp.class);
		addAttribute(COLUMN_ACTION, "Action", String.class);
		addAttribute(COLUMN_VALUE, "Value", String.class);
		addAttribute(COLUMN_TERMINAL, "Terminal", String.class);
		addAttribute(COLUMN_IS_ERROR, "Is error", Boolean.class);
		addAttribute(COLUMN_ERROR_MESSAGE, "Error message", String.class, 1000);
	}
	
	//getters
	public Card getCard() {
		return (Card) getColumnValue(COLUMN_CARD);
	}
	
	public String getSerialNumber() {
		return getStringColumnValue(COLUMN_SERIAL_NUMBER);
	}
	
	public Timestamp getEntryDate() {
		return getTimestampColumnValue(COLUMN_ENTRY_DATE);
	}
	
	public Timestamp getExternalEntryDate() {
		return getTimestampColumnValue(COLUMN_EXTERNAL_ENTRY_DATE);
	}
	
	public String getAction() {
		return getStringColumnValue(COLUMN_ACTION);
	}
	
	public String getValue() {
		return getStringColumnValue(COLUMN_VALUE);
	}
	
	public String getTerminal() {
		return getStringColumnValue(COLUMN_TERMINAL);
	}
	
	public boolean getIsError() {
		return getBooleanColumnValue(COLUMN_IS_ERROR, false);
	}
	
	public String getErrorMessage() {
		return getStringColumnValue(COLUMN_ERROR_MESSAGE);
	}
	
	//setters
	public void setCard(Card card) {
		setColumn(COLUMN_CARD, card);
	}
	
	public void setSerialNumber(String serialNumber) {
		setColumn(COLUMN_SERIAL_NUMBER, serialNumber);
	}
	
	public void setEntryDate(Timestamp date) {
		setColumn(COLUMN_ENTRY_DATE, date);
	}
	
	public void setExternalEntryDate(Timestamp date) {
		setColumn(COLUMN_EXTERNAL_ENTRY_DATE, date);
	}
	
	public void setAction(String action) {
		setColumn(COLUMN_ACTION, action);
	}
	
	public void setValue(String value) {
		setColumn(COLUMN_VALUE, value);
	}
	
	public void setTerminal(String terminal) {
		setColumn(COLUMN_TERMINAL, terminal);
	}
	
	public void setIsError(boolean isError) {
		setColumn(COLUMN_IS_ERROR, isError);
	}
	
	public void setErrorMessage(String errorMessage) {
		setColumn(COLUMN_ERROR_MESSAGE, errorMessage);
	}
	
	//ejb
}