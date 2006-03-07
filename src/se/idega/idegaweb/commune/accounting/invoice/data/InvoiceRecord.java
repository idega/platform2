/**
 * 
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;


import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.care.data.ChildCareContract;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolType;
import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface InvoiceRecord extends IDOEntity {
	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getInvoiceHeader
	 */
	public InvoiceHeader getInvoiceHeader();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getInvoiceHeaderId
	 */
	public int getInvoiceHeaderId();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getPaymentRecordId
	 */
	public int getPaymentRecordId();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getPaymentRecord
	 */
	public PaymentRecord getPaymentRecord();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getProviderId
	 */
	public int getProviderId();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getProvider
	 */
	public School getProvider();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getSchoolClassMemberId
	 */
	public int getSchoolClassMemberId();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getSchoolClassMember
	 */
	public SchoolClassMember getSchoolClassMember();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getSchoolTypeId
	 */
	public int getSchoolTypeId();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getSchoolType
	 */
	public SchoolType getSchoolType();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getInvoiceText
	 */
	public String getInvoiceText();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getInvoiceText2
	 */
	public String getInvoiceText2();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getRuleText
	 */
	public String getRuleText();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getDays
	 */
	public int getDays();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getPeriodStartCheck
	 */
	public Date getPeriodStartCheck();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getPeriodEndCheck
	 */
	public Date getPeriodEndCheck();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getPeriodStartPlacement
	 */
	public Date getPeriodStartPlacement();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getPeriodEndPlacement
	 */
	public Date getPeriodEndPlacement();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getDateCreated
	 */
	public Date getDateCreated();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getCreatedBy
	 */
	public String getCreatedBy();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getDateChanged
	 */
	public Date getDateChanged();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getChangedBy
	 */
	public String getChangedBy();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getAmount
	 */
	public float getAmount();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getAmountVAT
	 */
	public float getAmountVAT();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getNotes
	 */
	public String getNotes();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getOrderId
	 */
	public int getOrderId();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getRegSpecTypeId
	 */
	public int getRegSpecTypeId();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getRegSpecType
	 */
	public RegulationSpecType getRegSpecType();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getOwnPosting
	 */
	public String getOwnPosting();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getDoublePosting
	 */
	public String getDoublePosting();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getChildCareContract
	 */
	public ChildCareContract getChildCareContract();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getVUXPayment
	 */
	public boolean getVUXPayment();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getVUXGradePayment
	 */
	public boolean getVUXGradePayment();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setInvoiceHeaderId
	 */
	public void setInvoiceHeaderId(int i);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setInvoiceHeader
	 */
	public void setInvoiceHeader(InvoiceHeader i);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setPaymentRecordId
	 */
	public void setPaymentRecordId(int i);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setPaymentRecord
	 */
	public void setPaymentRecord(PaymentRecord p);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setProviderId
	 */
	public void setProviderId(int i);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setProvider
	 */
	public void setProvider(School s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setSchoolClassMemberId
	 */
	public void setSchoolClassMemberId(int i);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setSchoolClassMember
	 */
	public void setSchoolClassMember(SchoolClassMember scm);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setSchoolTypeId
	 */
	public void setSchoolTypeId(int i);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setSchoolType
	 */
	public void setSchoolType(SchoolType st);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setInvoiceText
	 */
	public void setInvoiceText(String s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setInvoiceText2
	 */
	public void setInvoiceText2(String s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setRuleText
	 */
	public void setRuleText(String s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setDays
	 */
	public void setDays(int i);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setPeriodStartCheck
	 */
	public void setPeriodStartCheck(Date d);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setPeriodEndCheck
	 */
	public void setPeriodEndCheck(Date d);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setPeriodStartPlacement
	 */
	public void setPeriodStartPlacement(Date d);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setPeriodEndPlacement
	 */
	public void setPeriodEndPlacement(Date d);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setDateCreated
	 */
	public void setDateCreated(Date d);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setCreatedBy
	 */
	public void setCreatedBy(String s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setDateChanged
	 */
	public void setDateChanged(Date d);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setChangedBy
	 */
	public void setChangedBy(String s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setAmount
	 */
	public void setAmount(float f);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setAmountVAT
	 */
	public void setAmountVAT(float f);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setNotes
	 */
	public void setNotes(String s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setOrderId
	 */
	public void setOrderId(int i);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setRegSpecTypeId
	 */
	public void setRegSpecTypeId(int i);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setRegSpecType
	 */
	public void setRegSpecType(RegulationSpecType r);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setOwnPosting
	 */
	public void setOwnPosting(String s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setDoublePosting
	 */
	public void setDoublePosting(String s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getVATRuleRegulationId
	 */
	public int getVATRuleRegulationId();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setVATRuleRegulation
	 */
	public void setVATRuleRegulation(int regulationId);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#getVATRuleRegulation
	 */
	public Regulation getVATRuleRegulation();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setVATRuleRegulation
	 */
	public void setVATRuleRegulation(Regulation regulation);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setChildCareContract
	 */
	public void setChildCareContract(ChildCareContract contract);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setVUXPayment
	 */
	public void setVUXPayment(boolean payment);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#setVUXGradePayment
	 */
	public void setVUXGradePayment(boolean payment);

}
