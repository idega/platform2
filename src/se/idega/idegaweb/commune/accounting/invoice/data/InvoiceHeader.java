package se.idega.idegaweb.commune.accounting.invoice.data;


public interface InvoiceHeader extends com.idega.data.IDOEntity,com.idega.data.IDOLegacyEntity
{
 public java.lang.String getChangedBy();
 public java.lang.String getCreatedBy();
 public int getCustodianId();
 public java.sql.Date getDateAdjusted();
 public java.sql.Date getDateCreated();
 public java.sql.Date getDateJournalEntry();
 public java.lang.String getDoublePosting();
 public int getMainActivity();
 public java.lang.String getOwnPosting();
 public java.sql.Date getPeriod();
 public int getReference();
 public char getStatus();
 public float getTotalAmountWithoutVAT();
 public float getTotalVATAmount();
 public void initializeAttributes();
 public void setChangedBy(java.lang.String p0);
 public void setCreatedBy(java.lang.String p0);
 public void setCustodianId(com.idega.user.data.User p0);
 public void setCustodianId(int p0);
 public void setDateAdjusted(java.sql.Date p0);
 public void setDateCreated(java.sql.Date p0);
 public void setDateTransactionEntry(java.sql.Date p0);
 public void setDoublePosting(java.lang.String p0);
 public void setMainActivity(int p0);
 public void setOwnPosting(java.lang.String p0);
 public void setPeriod(java.sql.Date p0);
 public void setReference(com.idega.block.school.data.School p0);
 public void setReference(int p0);
 public void setStatus(char p0);
 public void setTotalAmountWithoutVAT(float p0);
 public void setTotalVATAmount(float p0);
}
