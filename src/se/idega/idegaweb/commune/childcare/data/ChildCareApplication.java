package se.idega.idegaweb.commune.childcare.data;


public interface ChildCareApplication extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public char getApplicationStatus();
 public int getCareTime();
 public java.lang.String getCaseCodeDescription();
 public java.lang.String getCaseCodeKey();
 public se.idega.idegaweb.commune.childcare.check.data.GrantedCheck getCheck();
 public int getCheckId();
 public com.idega.user.data.User getChild();
 public int getChildId();
 public int getChoiceNumber();
 public com.idega.block.contract.data.Contract getContract();
 public int getContractFileId();
 public int getContractId();
 public java.sql.Date getFromDate();
 public boolean getHasDateSet();
 public boolean getHasPriority();
 public boolean getHasQueuePriority();
 public java.lang.String getMessage();
 public int getMethod();
 public java.sql.Date getOfferValidUntil();
 public java.lang.String getPresentation();
 public java.lang.String getPrognosis();
 public com.idega.block.school.data.School getProvider();
 public int getProviderId();
 public java.sql.Date getQueueDate();
 public int getQueueOrder();
 public java.sql.Date getRejectionDate();
 public void initializeAttributes();
 public boolean isAcceptedByParent()throws java.rmi.RemoteException;
 public boolean isCancelledOrRejectedByParent()throws java.rmi.RemoteException;
 public void setApplicationStatus(char p0);
 public void setCareTime(int p0);
 public void setCheck(se.idega.idegaweb.commune.childcare.check.data.Check p0);
 public void setCheckId(int p0);
 public void setChild(com.idega.user.data.User p0);
 public void setChildId(int p0);
 public void setChoiceNumber(int p0);
 public void setContractFileId(int p0);
 public void setContractId(int p0);
 public void setFromDate(java.sql.Date p0);
 public void setHasDateSet(boolean p0);
 public void setHasPriority(boolean p0);
 public void setHasQueuePriority(boolean p0);
 public void setMessage(java.lang.String p0);
 public void setMethod(int p0);
 public void setOfferValidUntil(java.sql.Date p0);
 public void setPresentation(java.lang.String p0);
 public void setPrognosis(java.lang.String p0);
 public void setProvider(com.idega.block.school.data.School p0);
 public void setProviderId(int p0);
 public void setQueueDate(java.sql.Date p0);
 public void setQueueOrder(int p0);
 public void setRejectionDate(java.sql.Date p0);
}
