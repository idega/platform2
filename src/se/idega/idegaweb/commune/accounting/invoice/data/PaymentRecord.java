package se.idega.idegaweb.commune.accounting.invoice.data;


public interface PaymentRecord extends com.idega.data.IDOEntity
{
 public float getAmount();
 public float getAmountVAT();
 public java.lang.String getChangedBy();
 public java.lang.String getCreadedBy();
 public java.sql.Date getDateChanged();
 public java.sql.Date getDateCreated();
 public java.lang.String getDoublePosting();
 public java.lang.String getNotes();
 public java.lang.String getOwnPosting();
 public int getPaymentHeader();
 public java.lang.String getPaymentText();
 public java.lang.String getRuleSpecType();
 public int getVATType();
 public void initializeAttributes();
 public void setAmount(float p0);
 public void setAmountVAT(float p0);
 public void setChangedBy(java.lang.String p0);
 public void setCreatedBy(java.lang.String p0);
 public void setDateChanged(java.sql.Date p0);
 public void setDateCreated(java.sql.Date p0);
 public void setDoublePosting(java.lang.String p0);
 public void setNotes(java.lang.String p0);
 public void setOwnPosting(java.lang.String p0);
 public void setPaymentHeader(int p0);
 public void setPaymentText(java.lang.String p0);
 public void setRuleSpecType(java.lang.String p0);
 public void setVATType(int p0);
}
