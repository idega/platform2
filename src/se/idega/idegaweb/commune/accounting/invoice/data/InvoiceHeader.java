package se.idega.idegaweb.commune.accounting.invoice.data;


public interface InvoiceHeader extends com.idega.data.IDOEntity
{
 public java.lang.String getChangedBy();
 public java.lang.String getCreatedBy();
 public int getCustodianId();
 public java.sql.Date getDateAdjusted();
 public java.sql.Date getDateCreated();
 public java.sql.Date getDateJournalEntry();
 public java.lang.String getDoublePosting();
 public java.lang.String getOwnPosting();
 public java.sql.Date getPeriod();
 public int getSchoolCategoryID();
 public char getStatus();
 public void initializeAttributes();
 public void setChangedBy(java.lang.String p0);
 public void setCreatedBy(java.lang.String p0);
 public void setCustodianId(com.idega.user.data.User p0);
 public void setCustodianId(int p0);
 public void setDateAdjusted(java.sql.Date p0);
 public void setDateCreated(java.sql.Date p0);
 public void setDateTransactionEntry(java.sql.Date p0);
 public void setDoublePosting(java.lang.String p0);
 public void setOwnPosting(java.lang.String p0);
 public void setPeriod(java.sql.Date p0);
 public void setSchoolCagtegoryID(int p0);
 public void setSchoolCagtegoryID(com.idega.block.school.data.SchoolCategory p0);
 public void setStatus(char p0);
}
