package se.idega.idegaweb.commune.childcare.data;


public interface ChangeChildCare extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public int getCareTime();
 public java.lang.String getCaseCodeDescription();
 public java.lang.String getCaseCodeKey();
 public com.idega.user.data.User getChild();
 public int getChildId();
 public java.sql.Date getFromDate();
 public com.idega.block.school.data.School getProvider();
 public int getProviderId();
 public void initializeAttributes();
 public void setCareTime(int p0);
 public void setChild(com.idega.user.data.User p0);
 public void setChildId(int p0);
 public void setFromDate(java.sql.Date p0);
 public void setProvider(com.idega.block.school.data.School p0);
 public void setProviderId(int p0);
}
