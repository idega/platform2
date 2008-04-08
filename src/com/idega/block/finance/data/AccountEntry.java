package com.idega.block.finance.data;


import java.sql.Date;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface AccountEntry extends IDOEntity, Entry {
	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getAccountId
	 */
	public int getAccountId();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setAccountId
	 */
	public void setAccountId(Integer account_id);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setAccountId
	 */
	public void setAccountId(int account_id);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getEntryGroupId
	 */
	public int getEntryGroupId();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setEntryGroupId
	 */
	public void setEntryGroupId(int entry_group_id);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getEntryType
	 */
	public String getEntryType();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setEntryType
	 */
	public void setEntryType(String entryType);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getAccountKey
	 */
	public AccountKey getAccountKey();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getAccountKeyId
	 */
	public int getAccountKeyId();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setAccountKeyId
	 */
	public void setAccountKeyId(Integer account_key_id);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setAccountKeyId
	 */
	public void setAccountKeyId(int account_key_id);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getPaymentDate
	 */
	public Timestamp getPaymentDate();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setPaymentDate
	 */
	public void setPaymentDate(Timestamp payment_date);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getLastUpdated
	 */
	public Timestamp getLastUpdated();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setLastUpdated
	 */
	public void setLastUpdated(Timestamp last_updated);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getCashierId
	 */
	public int getCashierId();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setCashierId
	 */
	public void setCashierId(Integer member_id);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setCashierId
	 */
	public void setCashierId(int member_id);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getInfo
	 */
	public String getInfo();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setInfo
	 */
	public void setInfo(String info);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setNetto
	 */
	public void setNetto(float netto);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getNetto
	 */
	public float getNetto();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setPrice
	 */
	public void setPrice(Float netto);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setPrice
	 */
	public void setPrice(float netto);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getVAT
	 */
	public float getVAT();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setVAT
	 */
	public void setVAT(Float vat);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setVAT
	 */
	public void setVAT(float vat);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getTotal
	 */
	public float getTotal();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setTotal
	 */
	public void setTotal(Float total);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setTotal
	 */
	public void setTotal(float total);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getRoundId
	 */
	public int getRoundId();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setRoundId
	 */
	public void setRoundId(Integer round);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setRoundId
	 */
	public void setRoundId(int round);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getStatus
	 */
	public String getStatus();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setDivisionForAccounting
	 */
	public void setDivisionForAccounting(String division);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getDivisionForAccounting
	 */
	public String getDivisionForAccounting();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getAccountBook
	 */
	public int getAccountBook();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setAccountBook
	 */
	public void setAccountBook(int accountBook);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getInvoiceNumber
	 */
	public Integer getInvoiceNumber();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setInvoiceNumber
	 */
	public void setInvoiceNumber(Integer invoiceNumber);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getFinalDueDate
	 */
	public Timestamp getFinalDueDate();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setFinalDueDate
	 */
	public void setFinalDueDate(Timestamp finalDueDate);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getUserId
	 */
	public int getUserId();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setUserId
	 */
	public void setUserId(int userId);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getDisallowanceDate
	 */
	public Timestamp getDisallowanceDate();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setDisallowanceDate
	 */
	public void setDisallowanceDate(Timestamp disallowanceDate);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getBatchNumber
	 */
	public int getBatchNumber();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setBatchNumber
	 */
	public void setBatchNumber(int batchNr);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getDueDate
	 */
	public Date getDueDate();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setDueDate
	 */
	public void setDueDate(Date dueDate);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getPenalIntrestCode
	 */
	public String getPenalIntrestCode();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setPenalIntrestCode
	 */
	public void setPenalIntrestCode(String penalIntrestCode);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getPenalIntrestRule
	 */
	public String getPenalIntrestRule();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setPenalIntrestRule
	 */
	public void setPenalIntrestRule(String penalIntrestRule);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getPenalIntrestProsent
	 */
	public double getPenalIntrestProsent();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setPenalIntrestProsent
	 */
	public void setPenalIntrestProsent(double penalIntrestProsent);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getPaymentCode
	 */
	public String getPaymentCode();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setPaymentCode
	 */
	public void setPaymentCode(String paymentCode);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getNotificationAndPaymentFee1
	 */
	public double getNotificationAndPaymentFee1();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setNotificationAndPaymentFee1
	 */
	public void setNotificationAndPaymentFee1(double fee);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getNotificationAndPaymentFee2
	 */
	public double getNotificationAndPaymentFee2();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setNotificationAndPaymentFee2
	 */
	public void setNotificationAndPaymentFee2(double fee);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getOtherCost
	 */
	public double getOtherCost();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setOtherCost
	 */
	public void setOtherCost(double otherCost);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getInvoiceStatus
	 */
	public String getInvoiceStatus();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setInvoiceStatus
	 */
	public void setInvoiceStatus(String status);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setStatus
	 */
	public void setStatus(String status) throws IllegalStateException;

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setQuantity
	 */
	public void setQuantity(double quantity);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getQuantity
	 */
	public double getQuantity();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#setItemPrice
	 */
	public void setItemPrice(double itemPrice);

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getItemPrice
	 */
	public double getItemPrice();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getType
	 */
	public String getType();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getFieldNameLastUpdated
	 */
	public String getFieldNameLastUpdated();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getFieldNameAccountId
	 */
	public String getFieldNameAccountId();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getTableName
	 */
	public String getTableName();

	/**
	 * @see com.idega.block.finance.data.AccountEntryBMPBean#getFieldNameStatus
	 */
	public String getFieldNameStatus();
}