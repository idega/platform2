package is.idega.idegaweb.campus.block.allocation.data;


public interface Contract extends com.idega.data.IDOEntity
{
 public com.idega.block.building.data.Apartment getApartment();
 public java.lang.Integer getApartmentId();
 public com.idega.block.application.data.Applicant getApplicant();
 public java.lang.Integer getApplicantId();
 public java.sql.Timestamp getDeliverTime();
 public java.lang.Integer getFileId();
 public boolean getIsRented();
 public java.sql.Date getMovingDate();
 public java.lang.String getResignInfo();
 public java.sql.Timestamp getReturnTime();
 public java.lang.String getStatus();
 public java.sql.Date getStatusDate();
 public com.idega.user.data.User getUser();
 public java.lang.Integer getUserId();
 public java.sql.Date getValidFrom();
 public java.sql.Date getValidTo();
 public void setApartmentId(java.lang.Integer p0);
 public void setApartmentId(int p0);
 public void setApplicantId(java.lang.Integer p0);
 public void setApplicantId(int p0);
 public void setDeliverTime(java.sql.Timestamp p0);
 public void setEnded();
 public void setFileId(java.lang.Integer p0);
 public void setFileId(int p0);
 public void setIsRented(boolean p0);
 public void setMovingDate(java.sql.Date p0);
 public void setResignInfo(java.lang.String p0);
 public void setReturnTime(java.sql.Timestamp p0);
 public void setStarted();
 public void setStarted(java.sql.Timestamp p0);
 public void setStatus(java.lang.String p0)throws java.lang.IllegalStateException;
 public void setStatusCreated();
 public void setStatusDate(java.sql.Date p0);
 public void setStatusDenied();
 public void setStatusEnded();
 public void setStatusFinalized();
 public void setStatusGarbage();
 public void setStatusPrinted();
 public void setStatusRejected();
 public void setStatusResigned();
 public void setStatusSigned();
 public void setStatusStorage();
 public void setStatusTerminated();
 public void setUserId(int p0);
 public void setUserId(java.lang.Integer p0);
 public void setValidFrom(java.sql.Date p0);
 public void setValidTo(java.sql.Date p0);
}
