package se.idega.idegaweb.commune.accounting.invoice.data;


public interface InvoiceRecord extends com.idega.data.IDOEntity
{
 public float getAmount();
 public float getAmountVAT();
 public java.lang.String getChangedBy();
 public int getContractId();
 public java.lang.String getCreadedBy();
 public java.sql.Date getDateChanged();
 public java.sql.Date getDateCreated();
 public int getDays();
 public java.lang.String getDoublePosting();
 public java.lang.String getInvoiceText();
 public int getInvoiceheader();
 public java.lang.String getNotes();
 public int getOrderId();
 public java.lang.String getOwnPosting();
 public int getPaymentRecordId();
 public java.sql.Date getPeriodEndCheck();
 public java.sql.Date getPeriodEndPlacement();
 public java.sql.Date getPeriodStartCheck();
 public java.sql.Date getPeriodStartPlacement();
 public int getProviderId();
 public java.lang.String getRuleSpecType();
 public java.lang.String getRuleText();
 public int getVATType();
 public void initializeAttributes();
 public void setAmount(float p0);
 public void setAmountVAT(float p0);
 public void setChangedBy(java.lang.String p0);
 public void setColumnProviderId(int p0);
 public void setContractId(int p0);
 public void setCreatedBy(java.lang.String p0);
 public void setDateChanged(java.sql.Date p0);
 public void setDateCreated(java.sql.Date p0);
 public void setDays(int p0);
 public void setDoublePosting(java.lang.String p0);
 public void setInvoiceHeader(int p0);
 public void setInvoiceHeader(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0);
 public void setInvoiceText(java.lang.String p0);
 public void setNotes(java.lang.String p0);
 public void setOrderId(int p0);
 public void setOwnPosting(java.lang.String p0);
 public void setPaymentRecordId(int p0);
 public void setPeriodEndCheck(java.sql.Date p0);
 public void setPeriodEndPlacement(java.sql.Date p0);
 public void setPeriodStartCheck(java.sql.Date p0);
 public void setPeriodStartPlacement(java.sql.Date p0);
 public void setProviderId(com.idega.block.school.data.School p0);
 public void setProviderId(int p0);
 public void setRuleSpecType(java.lang.String p0);
 public void setRuleText(java.lang.String p0);
 public void setVATType(int p0);
}
