package se.idega.idegaweb.commune.accounting.invoice.data;


public interface InvoiceHeader extends com.idega.data.IDOEntity
{
 public java.lang.String getChangedBy();
 public java.lang.String getCreatedBy();
 public com.idega.user.data.User getCustodian();
 public int getCustodianId();
 public java.sql.Date getDateAdjusted();
 public java.sql.Date getDateCreated();
 public java.sql.Date getDateJournalEntry();
 public java.sql.Date getPeriod();
 public com.idega.block.school.data.SchoolCategory getSchoolCategory();
 public java.lang.String getSchoolCategoryID();
 public char getStatus();
 public void setChangedBy(java.lang.String p0);
 public void setCreatedBy(java.lang.String p0);
 public void setCustodian(com.idega.user.data.User p0);
 public void setCustodianId(int p0);
 public void setDateAdjusted(java.sql.Date p0);
 public void setDateCreated(java.sql.Date p0);
 public void setDateTransactionEntry(java.sql.Date p0);
 public void setPeriod(java.sql.Date p0);
 public void setSchoolCategory(com.idega.block.school.data.SchoolCategory p0);
 public void setSchoolCategoryID(java.lang.String p0);
 public void setStatus(char p0);
}
