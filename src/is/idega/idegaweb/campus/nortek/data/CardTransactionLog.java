package is.idega.idegaweb.campus.nortek.data;


import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface CardTransactionLog extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#getCard
	 */
	public Card getCard();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#getEntryDate
	 */
	public Timestamp getEntryDate();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#getExternalEntryDate
	 */
	public Timestamp getExternalEntryDate();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#getAction
	 */
	public String getAction();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#getValue
	 */
	public String getValue();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#getTerminal
	 */
	public String getTerminal();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#getIsError
	 */
	public boolean getIsError();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#getErrorMessage
	 */
	public String getErrorMessage();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#setCard
	 */
	public void setCard(Card card);

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#setEntryDate
	 */
	public void setEntryDate(Timestamp date);

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#setExternalEntryDate
	 */
	public void setExternalEntryDate(Timestamp date);

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#setAction
	 */
	public void setAction(String action);

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#setValue
	 */
	public void setValue(String value);

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#setTerminal
	 */
	public void setTerminal(String terminal);

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#setIsError
	 */
	public void setIsError(boolean isError);

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardTransactionLogBMPBean#setErrorMessage
	 */
	public void setErrorMessage(String errorMessage);
}