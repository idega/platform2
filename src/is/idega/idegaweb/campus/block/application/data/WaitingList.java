package is.idega.idegaweb.campus.block.application.data;

import javax.ejb.*;

public interface WaitingList extends com.idega.data.IDOLegacyEntity
{
 public void setChoiceNumber(int p0);
 public void setRemovedFromList(java.lang.String p0);
 public void setPriorityLevelD();
 public java.sql.Timestamp getLastConfirmationDate();
 public void setPriorityLevelC();
 public java.lang.Integer getComplexId();
 public void setOrder(java.lang.Integer p0);
 public void setPriorityLevelB();
 public void setChoiceNumber(java.lang.Integer p0);
 public void setPriorityLevelA();
 public void setApartmentTypeId(java.lang.Integer p0);
 public java.lang.String getPriorityLevel();
 public void setLastConfirmationDate(java.sql.Timestamp p0);
 public int getNumberOfRejections();
 public java.lang.String getType();
 public void setApartmentTypeId(int p0);
 public void setType(java.lang.String p0);
 public void setOrder(int p0);
 public void setComplexId(int p0);
 public java.lang.Integer getOrder();
 public void setApplicantId(int p0);
 public java.lang.Integer getChoiceNumber();
 public void setNumberOfRejections(int p0);
 public void setNumberOfRejections(java.lang.Integer p0);
 public boolean getRemovedFromList();
 public java.lang.Integer getApplicantId();
 public java.lang.Integer getApartmentTypeId();
 public void setApplicantId(java.lang.Integer p0);
}
