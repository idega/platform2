package se.idega.idegaweb.commune.accounting.invoice.data;


public interface BatchRunError extends com.idega.data.IDOEntity
{
 public int getBatchRunID();
 public java.lang.String getDescription();
 public int getOrder();
 public java.lang.String getRelated();
 public void initializeAttributes();
 public void setBatchRunID(int p0);
 public void setDescription(java.lang.String p0);
 public void setOrder(int p0);
 public void setRelated(java.lang.String p0);
}
