package se.idega.idegaweb.commune.accounting.invoice.data;


public interface BatchRun extends com.idega.data.IDOEntity
{
 public java.sql.Date getEnd();
 public int getOperationID();
 public java.sql.Date getPeriod();
 public java.sql.Date getStart();
 public void initializeAttributes();
 public void setEnd(java.sql.Date p0);
 public void setOperationID(int p0);
 public void setPeriod(java.sql.Date p0);
 public void setStart(java.sql.Date p0);
}
