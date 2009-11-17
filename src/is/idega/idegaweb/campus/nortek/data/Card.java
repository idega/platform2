package is.idega.idegaweb.campus.nortek.data;


import com.idega.data.IDOEntity;
import com.idega.user.data.User;

public interface Card extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#getPrimaryKeyClass
	 */
	public Class getPrimaryKeyClass();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#getCardSerialNumber
	 */
	public String getCardSerialNumber();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#getDecodedCardSerialNumber
	 */
	public String getDecodedCardSerialNumber();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#getIsValid
	 */
	public boolean getIsValid();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#getIsDeleted
	 */
	public boolean getIsDeleted();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#setCardSerialNumber
	 */
	public void setCardSerialNumber(String serialNumber);

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#setDecodedCardSerialNumber
	 */
	public void setDecodedCardSerialNumber(String serialNumber);

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#setIsValid
	 */
	public void setIsValid(boolean isValid);

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#setIsDeleted
	 */
	public void setIsDeleted(boolean isDeleted);
}