/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.export.data;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;

import java.sql.Timestamp;


import com.idega.block.finance.data.BankInfo;
import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface Batch extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setBatchNumber
	 */
	public void setBatchNumber(String batchNumber);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setCreated
	 */
	public void setCreated(Timestamp created);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setSent
	 */
	public void setSent(Timestamp sent);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setTypeBank
	 */
	public void setTypeBank();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setTypeCreditCard
	 */
	public void setTypeCreditCard();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setCreditcardTypeID
	 */
	public void setCreditcardTypeID(int typeID);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setCreditCardType
	 */
	public void setCreditCardType(CreditCardType type);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setCreditCardContractId
	 */
	public void setCreditCardContractId(int contractID);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setCreditCardContract
	 */
	public void setCreditCardContract(CreditCardContract contract);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setCreditCardFileName
	 */
	public void setCreditCardFileName(String fileName);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setBankInfoID
	 */
	public void setBankInfoID(int infoID);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setBankInfo
	 */
	public void setBankInfo(BankInfo info);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getBatchNumber
	 */
	public String getBatchNumber();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getCreated
	 */
	public Timestamp getCreated();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getSent
	 */
	public Timestamp getSent();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getType
	 */
	public String getType();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getCreditcardTypeID
	 */
	public int getCreditcardTypeID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getCreditCardType
	 */
	public CreditCardType getCreditCardType();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getCreditCardContractID
	 */
	public int getCreditCardContractID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getCreditCardContract
	 */
	public CreditCardContract getCreditCardContract();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getCreditCardFileName
	 */
	public String getCreditCardFileName();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getBankInfoID
	 */
	public int getBankInfoID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getBankInfo
	 */
	public BankInfo getBankInfo();

}
