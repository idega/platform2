package se.idega.idegaweb.commune.childcare.data;


public interface AfterSchoolChoice extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public java.lang.Character getApplicationStatus();
 public java.lang.String getCaseCodeDescription();
 public java.lang.String getCaseCodeKey();
 public se.idega.idegaweb.commune.childcare.check.data.GrantedCheck getCheck();
 public java.lang.Integer getCheckId();
 public com.idega.user.data.User getChild();
 public java.lang.Integer getChildId();
 public java.lang.Integer getChoiceNumber();
 public com.idega.block.contract.data.Contract getContract();
 public java.lang.Integer getContractFileId();
 public java.lang.Integer getContractId();
 public java.sql.Date getFromDate();
 public java.lang.String getMessage();
 public java.lang.Integer getMethod();
 public java.sql.Date getPlacementDate();
 public java.lang.String getPresentation();
 public java.lang.String getPrognosis();
 public com.idega.block.school.data.School getProvider();
 public java.lang.Integer getProviderId();
 public java.sql.Date getRejectionDate();
 public java.lang.Integer getSchoolSeasonId();
 public void initializeAttributes();
 public boolean isAcceptedByParent()throws java.rmi.RemoteException;
 public boolean isActive();
 public boolean isCancelledOrRejectedByParent()throws java.rmi.RemoteException;
 public void setApplicationStatus(char p0);
 public void setCheck(se.idega.idegaweb.commune.childcare.check.data.GrantedCheck p0);
 public void setCheckId(java.lang.Integer p0);
 public void setChild(com.idega.user.data.User p0);
 public void setChildId(java.lang.Integer p0);
 public void setChoiceNumber(java.lang.Integer p0);
 public void setContractFileId(java.lang.Integer p0);
 public void setContractId(java.lang.Integer p0);
 public void setFromDate(java.sql.Date p0);
 public void setMessage(java.lang.String p0);
 public void setMethod(java.lang.Integer p0);
 public void setPlacementDate(java.sql.Date p0);
 public void setPresentation(java.lang.String p0);
 public void setPrognosis(java.lang.String p0);
 public void setProvider(com.idega.block.school.data.School p0);
 public void setProviderID(java.lang.Integer p0);
 public void setRejectionDate(java.sql.Date p0);
 public void setRejectionDateAsNull(boolean p0);
 public void setSchoolSeasonId(java.lang.Integer p0);
}
