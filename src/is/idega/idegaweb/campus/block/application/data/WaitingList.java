package is.idega.idegaweb.campus.block.application.data;


import java.sql.Timestamp;


public interface WaitingList extends com.idega.data.IDOLegacyEntity

{
 public java.lang.Integer getApartmentTypeId();
 public java.lang.Integer getApplicantId();
 public java.lang.Integer getChoiceNumber();
 public java.lang.Integer getComplexId();
 public int getCountOfRecords(java.lang.String p0)throws javax.ejb.FinderException;
 public java.sql.Timestamp getLastConfirmationDate();
 public int getNumberOfRejections();
 public java.lang.Integer getOrder();
 public java.lang.String getPriorityLevel();
 public boolean getRemovedFromList();
 public java.lang.String getType();
 public void initializeAttributes();
 public void setApartmentTypeId(int p0);
 public void setApartmentTypeId(java.lang.Integer p0);
 public void setApplicantId(int p0);
 public void setApplicantId(java.lang.Integer p0);
 public void setChoiceNumber(int p0);
 public void setChoiceNumber(java.lang.Integer p0);
 public void setComplexId(int p0);
 public void setLastConfirmationDate(java.sql.Timestamp p0);
 public void setNumberOfRejections(int p0);
 public void setNumberOfRejections(java.lang.Integer p0);
 public void setOrder(int p0);
 public void setOrder(java.lang.Integer p0);
 public void setPriorityLevelA();
 public void setPriorityLevelB();
 public void setPriorityLevelC();
 public void setPriorityLevelD();
 public void setPriorityLevelE();
 public void setRemovedFromList(java.lang.String p0);
 public void setTypeApplication();
 public void setTypeTransfer();

 
 public void setRejectFlag(boolean flag);
 public boolean getRejectFlag();
 public void incrementRejections(boolean flagAsRejected);
 public void setAcceptedDate(Timestamp stamp);
 public Timestamp getAcceptedDate();

 public void setSamePriority(WaitingList listEntry);

}
