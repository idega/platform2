package is.idega.idegaweb.campus.nortek.data;


import com.idega.user.data.User;
import com.idega.data.IDOEntity;

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
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#getIsValid
	 */
	public boolean getIsValid();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#setCardSerialNumber
	 */
	public void setCardSerialNumber(String serialNumber);

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.CardBMPBean#setIsValid
	 */
	public void setIsValid(boolean isValid);
}