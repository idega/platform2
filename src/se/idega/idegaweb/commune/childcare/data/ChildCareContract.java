package se.idega.idegaweb.commune.childcare.data;


public interface ChildCareContract extends com.idega.data.IDOEntity
{
 public se.idega.idegaweb.commune.childcare.data.ChildCareApplication getApplication();
 public int getApplicationID();
 public int getCareTime();
 public com.idega.user.data.User getChild();
 public int getChildID();
 public com.idega.block.contract.data.Contract getContract();
 public com.idega.core.file.data.ICFile getContractFile();
 public int getContractFileID();
 public int getContractID();
 public java.sql.Date getCreatedDate();
 public se.idega.idegaweb.commune.childcare.data.EmploymentType getEmploymentType();
 public com.idega.user.data.User getInvoiceReceiver();
 public int getInvoiceReceiverID();
 public com.idega.block.school.data.SchoolClassMember getSchoolClassMmeber();
 public java.sql.Date getTerminatedDate();
 public java.sql.Date getValidFromDate();
 public void setApplication(se.idega.idegaweb.commune.childcare.data.ChildCareApplication p0);
 public void setApplicationID(int p0);
 public void setCareTime(int p0);
 public void setChild(com.idega.user.data.User p0);
 public void setChildID(int p0);
 public void setContract(com.idega.block.contract.data.Contract p0);
 public void setContractFile(com.idega.core.file.data.ICFile p0);
 public void setContractFileID(int p0);
 public void setContractID(int p0);
 public void setCreatedDate(java.sql.Date p0);
 public void setEmploymentType(int p0);
 public void setSchoolClassMember(com.idega.block.school.data.SchoolClassMember p0);
 public void setSchoolClassMemberID(int p0);
 public void setTerminatedDate(java.sql.Date p0);
 public void setTerminationDateAsNull(boolean p0);
 public void setValidFromDate(java.sql.Date p0);
}
