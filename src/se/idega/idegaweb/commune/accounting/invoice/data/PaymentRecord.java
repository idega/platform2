package se.idega.idegaweb.commune.accounting.invoice.data;


public interface PaymentRecord extends com.idega.data.IDOEntity
{
 public java.lang.String getChangedBy();
 public java.lang.String getCreadedBy();
 public java.sql.Date getDateChanged();
 public java.sql.Date getDateCreated();
 public java.sql.Date getDateTransaction();
 public java.lang.String getDoublePosting();
 public java.lang.String getNotes();
 public java.lang.String getOwnPosting();
 public int getPaymentHeader();
 public java.lang.String getPaymentText();
 public java.sql.Date getPeriod();
 public float getPieceAmount();
 public int getPlacements();
 public java.lang.String getRuleSpecType();
 public char getStatus();
 public float getTotalAmount();
 public float getTotalAmountVAT();
 public int getVATType();
 public void initializeAttributes();
 public void setChangedBy(java.lang.String p0);
 public void setCreatedBy(java.lang.String p0);
 public void setDateChanged(java.sql.Date p0);
 public void setDateCreated(java.sql.Date p0);
 public void setDateTransaction(java.sql.Date p0);
 public void setDoublePosting(java.lang.String p0);
 public void setNotes(java.lang.String p0);
 public void setOwnPosting(java.lang.String p0);
 public void setPaymentHeader(se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader p0);
 public void setPaymentHeader(int p0);
 public void setPaymentText(java.lang.String p0);
 public void setPeriod(java.sql.Date p0);
 public void setPieceAmount(float p0);
 public void setPlacements(int p0);
 public void setRuleSpecType(java.lang.String p0);
 public void setStatus(char p0);
 public void setTotalAmount(float p0);
 public void setTotalAmountVAT(float p0);
 public void setVATType(int p0);
}
