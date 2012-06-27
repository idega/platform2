package is.idega.idegaweb.campus.data;


import com.idega.data.IDOEntity;
import com.idega.data.UniqueIDCapable;
import com.idega.user.data.User;
import java.sql.Timestamp;
import is.idega.idegaweb.campus.block.allocation.data.Contract;

public interface ContractRenewalOffer extends IDOEntity, UniqueIDCapable {
	/**
	 * @see is.idega.idegaweb.campus.data.ContractRenewalOfferBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see is.idega.idegaweb.campus.data.ContractRenewalOfferBMPBean#getContract
	 */
	public Contract getContract();

	/**
	 * @see is.idega.idegaweb.campus.data.ContractRenewalOfferBMPBean#getOfferSentDate
	 */
	public Timestamp getOfferSentDate();

	/**
	 * @see is.idega.idegaweb.campus.data.ContractRenewalOfferBMPBean#getOfferAnsweredDate
	 */
	public Timestamp getOfferAnsweredDate();

	/**
	 * @see is.idega.idegaweb.campus.data.ContractRenewalOfferBMPBean#getAnswer
	 */
	public boolean getAnswer();

	/**
	 * @see is.idega.idegaweb.campus.data.ContractRenewalOfferBMPBean#getIsOfferClosed
	 */
	public boolean getIsOfferClosed();

	/**
	 * @see is.idega.idegaweb.campus.data.ContractRenewalOfferBMPBean#getRenewalGranted
	 */
	public String getRenewalGranted();

	/**
	 * @see is.idega.idegaweb.campus.data.ContractRenewalOfferBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see is.idega.idegaweb.campus.data.ContractRenewalOfferBMPBean#setContract
	 */
	public void setContract(Contract contract);

	/**
	 * @see is.idega.idegaweb.campus.data.ContractRenewalOfferBMPBean#setOfferSentDate
	 */
	public void setOfferSentDate(Timestamp date);

	/**
	 * @see is.idega.idegaweb.campus.data.ContractRenewalOfferBMPBean#setOfferAnsweredDate
	 */
	public void setOfferAnsweredDate(Timestamp date);

	/**
	 * @see is.idega.idegaweb.campus.data.ContractRenewalOfferBMPBean#setAnswer
	 */
	public void setAnswer(boolean answer);

	/**
	 * @see is.idega.idegaweb.campus.data.ContractRenewalOfferBMPBean#setIsOfferClosed
	 */
	public void setIsOfferClosed(boolean closed);

	/**
	 * @see is.idega.idegaweb.campus.data.ContractRenewalOfferBMPBean#setRenewalGranted
	 */
	public void setRenewalGranted(String granted);
}