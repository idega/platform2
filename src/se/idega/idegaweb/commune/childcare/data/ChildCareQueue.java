package se.idega.idegaweb.commune.childcare.data;


public interface ChildCareQueue extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public java.lang.String getCaseCodeDescription();
 public java.lang.String getCaseCodeKey();
 public com.idega.user.data.User getChild();
 public int getChildId();
 public int getChoiceNumber();
 public int getContractId();
 public java.sql.Date getImportDate();
 public java.lang.String getPriority();
 public int getProviderId();
 public java.lang.String getProviderName();
 public java.sql.Date getQueueDate();
 public int getQueueType();
 public java.lang.String getSchoolAreaId();
 public java.lang.String getSchoolAreaName();
 public java.sql.Date getStartDate();
 public void initializeAttributes();
 public boolean isExported();
 public void setChildId(int p0);
 public void setChoiceNumber(int p0);
 public void setContractId(int p0);
 public void setExported(boolean p0);
 public void setImportedDate(java.sql.Date p0);
 public void setPriority(java.lang.String p0);
 public void setProviderId(int p0);
 public void setProviderName(java.lang.String p0);
 public void setQueueDate(java.sql.Date p0);
 public void setQueueType(int p0);
 public void setSchoolAreaId(int p0);
 public void setSchoolAreaName(java.lang.String p0);
 public void setStartDate(java.sql.Date p0);
}
