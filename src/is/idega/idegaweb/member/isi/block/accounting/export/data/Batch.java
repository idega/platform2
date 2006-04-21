/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.export.data;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;

import java.sql.Timestamp;


import com.idega.block.finance.data.BankInfo;
import com.idega.core.file.data.ICFile;
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
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setCreditCardFile
	 */
	public void setCreditCardFile(ICFile file);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setCreditCardFileId
	 */
	public void setCreditCardFileId(int fileId);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setBankInfoID
	 */
	public void setBankInfoID(int infoID);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setBankInfo
	 */
	public void setBankInfo(BankInfo info);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setFinBatchID
	 */
	public void setFinBatchID(int batchID);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setFinBatch
	 */
	public void setFinBatch(com.idega.block.finance.data.Batch batch);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#setMarked
	 */
	public void setMarked(boolean marked);

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
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getIsBankType
	 */
	public boolean getIsBankType();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getIsCreditcardType
	 */
	public boolean getIsCreditcardType();

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
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getCreditCardFile
	 */
	public ICFile getCreditCardFile();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getCreditCardFileId
	 */
	public int getCreditCardFileId();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getBankInfoID
	 */
	public int getBankInfoID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getBankInfo
	 */
	public BankInfo getBankInfo();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getFinBatchID
	 */
	public int getFinBatchID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getFinBatch
	 */
	public com.idega.block.finance.data.Batch getFinBatch();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#getMarked
	 */
	public boolean getMarked();

}
