/**
 * 
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;


import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;

import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface PaymentRecord extends IDOEntity {
	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getPaymentHeaderId
	 */
	public int getPaymentHeaderId();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getPaymentHeader
	 */
	public PaymentHeader getPaymentHeader();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getStatus
	 */
	public char getStatus();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getPeriod
	 */
	public Date getPeriod();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getPaymentText
	 */
	public String getPaymentText();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getDateCreated
	 */
	public Date getDateCreated();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getCreatedBy
	 */
	public String getCreatedBy();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getDateChanged
	 */
	public Date getDateChanged();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getChangedBy
	 */
	public String getChangedBy();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getDateTransaction
	 */
	public Date getDateTransaction();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getPlacements
	 */
	public int getPlacements();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getPieceAmount
	 */
	public float getPieceAmount();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getTotalAmount
	 */
	public float getTotalAmount();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getTotalAmountVAT
	 */
	public float getTotalAmountVAT();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getNotes
	 */
	public String getNotes();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getRuleSpecType
	 */
	public String getRuleSpecType();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getOwnPosting
	 */
	public String getOwnPosting();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getDoublePosting
	 */
	public String getDoublePosting();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getVATRuleRegulation
	 */
	public Regulation getVATRuleRegulation();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getVATRuleRegulationId
	 */
	public int getVATRuleRegulationId();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setVATRuleRegulationId
	 */
	public void setVATRuleRegulationId(int regulationId);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setVATRuleRegulation
	 */
	public void setVATRuleRegulation(Regulation vatRegulation);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getOrderId
	 */
	public int getOrderId();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setPaymentHeaderId
	 */
	public void setPaymentHeaderId(int i);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setPaymentHeader
	 */
	public void setPaymentHeader(PaymentHeader p);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setPeriod
	 */
	public void setPeriod(Date d);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setStatus
	 */
	public void setStatus(char c);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setPaymentText
	 */
	public void setPaymentText(String s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setDateCreated
	 */
	public void setDateCreated(Date d);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setCreatedBy
	 */
	public void setCreatedBy(String s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setDateChanged
	 */
	public void setDateChanged(Date d);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setChangedBy
	 */
	public void setChangedBy(String s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setDateTransaction
	 */
	public void setDateTransaction(Date d);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setPlacements
	 */
	public void setPlacements(int i);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setPieceAmount
	 */
	public void setPieceAmount(float f);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setTotalAmount
	 */
	public void setTotalAmount(float f);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setTotalAmountVAT
	 */
	public void setTotalAmountVAT(float f);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setNotes
	 */
	public void setNotes(String s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setRuleSpecType
	 */
	public void setRuleSpecType(String s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setOwnPosting
	 */
	public void setOwnPosting(String s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setDoublePosting
	 */
	public void setDoublePosting(String s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setOrderId
	 */
	public void setOrderId(int i);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#setVernr
	 */
	public void setVernr(String vernr);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#getVernr
	 */
	public String getVernr();

}
