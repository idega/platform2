package se.idega.idegaweb.commune.accounting.userinfo.data;


public interface BruttoIncome extends com.idega.data.IDOEntity
{
 public java.sql.Timestamp getCreated();
 public com.idega.user.data.User getCreator();
 public java.lang.Integer getCreatorID();
 public java.lang.Float getIncome();
 public com.idega.user.data.User getUser();
 public java.lang.Integer getUserID();
 public java.sql.Date getValidFrom();
 public void initializeAttributes();
 public void setCreated(java.sql.Timestamp p0);
 public void setCreator(java.lang.Integer p0);
 public void setCreator(com.idega.user.data.User p0);
 public void setCreator(int p0);
 public void setIncome(java.lang.Float p0);
 public void setIncome(float p0);
 public void setUser(java.lang.Integer p0);
 public void setUser(com.idega.user.data.User p0);
 public void setUser(int p0);
 public void setValidFrom(java.sql.Date p0);
}
