package se.idega.idegaweb.commune.childcare.data;

import javax.ejb.*;

public interface ChildCareApplication extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public int getContractId();
 public void setChild(com.idega.user.data.User p0);
 public void setProviderId(int p0);
 public int getCheckId();
 public com.idega.user.data.User getChild();
 public void setChoiceNumber(int p0);
 public java.sql.Date getQueueDate();
 public void setPrognosis(java.lang.String p0);
 public com.idega.block.school.data.School getProvider();
 public void setCheck(se.idega.idegaweb.commune.childcare.check.data.Check p0);
 public java.lang.String getCaseCodeKey();
 public java.sql.Date getFromDate();
 public se.idega.idegaweb.commune.childcare.check.data.GrantedCheck getCheck();
 public void setContractId(int p0);
 public java.lang.String getPresentation();
 public void setContractFileId(int p0);
 public int getChildId();
 public int getMethod();
 public void setRejectionDate(java.sql.Date p0);
 public void setProvider(com.idega.block.school.data.School p0);
 public void setCheckId(int p0);
 public void setQueueDate(java.sql.Date p0);
 public void setMethod(int p0);
 public void setFromDate(java.sql.Date p0);
 public int getProviderId();
 public int getCareTime();
 public void initializeAttributes();
 public void setPresentation(java.lang.String p0);
 public int getChoiceNumber();
 public java.lang.String getPrognosis();
 public com.idega.block.contract.data.Contract getContract();
 public void setChildId(int p0);
 public void setCareTime(int p0);
 public java.sql.Date getRejectionDate();
 public java.lang.String getCaseCodeDescription();
 public int getContractFileId();
}
