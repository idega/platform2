package se.idega.idegaweb.commune.childcare.check.data;


public interface GrantedCheck extends com.idega.data.IDOEntity
{
 public int getChildId();
 public void setCheckId(int p0);
 public int getCheckId();
 public void initializeAttributes();
 public void setDateGranted(java.sql.Timestamp p0);
 public void setCheck(se.idega.idegaweb.commune.childcare.check.data.Check p0);
 public void setChild(com.idega.user.data.User p0);
 public se.idega.idegaweb.commune.childcare.check.data.Check getCheck();
 public java.sql.Timestamp getDateLastUsed();
 public com.idega.user.data.User getChild();
 public void setDateLastUsed(java.sql.Timestamp p0);
 public java.sql.Timestamp getDateGranted();
 public void setChildId(int p0);
}
