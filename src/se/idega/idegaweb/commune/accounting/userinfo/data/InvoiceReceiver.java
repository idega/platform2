package se.idega.idegaweb.commune.accounting.userinfo.data;


public interface InvoiceReceiver extends com.idega.data.IDOEntity
{
 public java.lang.String getIDColumnName();
 public boolean getIsReceiver();
 public com.idega.user.data.User getUser();
 public int getUserId();
 public void initializeAttributes();
 public void setIsReceiver(boolean p0);
 public void setUser(com.idega.user.data.User p0);
 public void setUser(int p0);
 public void setUser(java.lang.Integer p0);
}
