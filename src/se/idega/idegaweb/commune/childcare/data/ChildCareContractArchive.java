package se.idega.idegaweb.commune.childcare.data;


public interface ChildCareContractArchive extends com.idega.data.IDOEntity
{
 public se.idega.idegaweb.commune.childcare.data.ChildCareApplication getApplication();
 public int getApplicationID();
 public int getCareTime();
 public com.idega.user.data.User getChild();
 public int getChildID();
 public com.idega.core.data.ICFile getContractFile();
 public int getContractFileID();
 public java.sql.Date getCreatedDate();
 public java.sql.Date getTerminatedDate();
 public java.sql.Date getValidFromDate();
 public void initializeAttributes();
 public void setApplication(se.idega.idegaweb.commune.childcare.data.ChildCareApplication p0);
 public void setApplicationID(int p0);
 public void setCareTime(int p0);
 public void setChild(com.idega.user.data.User p0);
 public void setChildID(int p0);
 public void setContractFile(com.idega.core.data.ICFile p0);
 public void setContractFileID(int p0);
 public void setCreatedDate(java.sql.Date p0);
 public void setTerminatedDate(java.sql.Date p0);
 public void setValidFromDate(java.sql.Date p0);
}
