package is.idega.idegaweb.campus.block.application.data;

import java.sql.Timestamp;

public interface WaitingList extends com.idega.data.IDOLegacyEntity
{
 public void setChoiceNumber(java.lang.Integer p0);
 public void setRemovedFromList(java.lang.String p0);
 public void setPriorityLevelE();
 public void setPriorityLevelD();
 public java.sql.Timestamp getLastConfirmationDate();
 public void setPriorityLevelC();
 public java.lang.Integer getComplexId();
 public void setPriorityLevelB();
 public void setPriorityLevelA();
 public void setApartmentTypeId(java.lang.Integer p0);
 public java.lang.String getPriorityLevel();
 public void setLastConfirmationDate(java.sql.Timestamp p0);
 public int getNumberOfRejections();
 public void setOrder(int p0);
 public java.lang.String getType();
 public void setApartmentTypeId(int p0);
 public void setOrder(java.lang.Integer p0);
 public void setComplexId(int p0);
 public java.lang.Integer getOrder();
 public void setApplicantId(java.lang.Integer p0);
 public void setChoiceNumber(int p0);
 public void setTypeApplication();
 public java.lang.Integer getChoiceNumber();
 public void setTypeTransfer();
 public void setNumberOfRejections(int p0);
 public void setNumberOfRejections(java.lang.Integer p0);
 public boolean getRemovedFromList();
 public java.lang.Integer getApplicantId();
 public void setApplicantId(int p0);
 public java.lang.Integer getApartmentTypeId();
 public void setRejectFlag(boolean flag);
 public boolean getRejectFlag();
 public void incrementRejections(boolean flagAsRejected);
 public void setAcceptedDate(Timestamp stamp);
 public Timestamp getAcceptedDate();
 public void setSamePriority(WaitingList listEntry);
}
