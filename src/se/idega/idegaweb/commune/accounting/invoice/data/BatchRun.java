package se.idega.idegaweb.commune.accounting.invoice.data;


public interface BatchRun extends com.idega.data.IDOEntity
{
 public java.sql.Date getEnd();
 public int getOperationID();
 public int getOrder();
 public java.sql.Date getStart();
 public void initializeAttributes();
 public void setEnd(java.sql.Date p0);
 public void setOperationID(int p0);
 public void setOrder(int p0);
 public void setStart(java.sql.Date p0);
}
