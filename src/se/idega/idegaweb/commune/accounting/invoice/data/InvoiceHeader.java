package se.idega.idegaweb.commune.accounting.invoice.data;


public interface InvoiceHeader extends com.idega.data.IDOEntity,com.idega.data.IDOLegacyEntity
{
 public java.lang.String getDateAdjusted();
 public java.lang.String getDateCreated();
 public java.lang.String getDateJournalEntry();
 public java.lang.String getMainActivity();
 public java.lang.String getPeriod();
 public java.lang.String getReference();
 public java.lang.String getStatus();
 public java.lang.String getUser();
 public void initializeAttributes();
 public void setDateAdjusted(java.lang.String p0);
 public void setDateCreated(java.lang.String p0);
 public void setDateJournalEntry(java.lang.String p0);
 public void setMainActivity(java.lang.String p0);
 public void setPeriod(java.lang.String p0);
 public void setReference(java.lang.String p0);
 public void setStatus(java.lang.String p0);
 public void setUser(java.lang.String p0);
}
