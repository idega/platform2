package is.idega.idegaweb.campus.block.application.data;

import javax.ejb.*;

public interface WaitingList extends com.idega.data.IDOLegacyEntity
{
 public java.lang.Integer getApartmentTypeId();
 public java.lang.Integer getApplicantId();
 public java.lang.Integer getChoiceNumber();
 public java.lang.Integer getComplexId();
 public java.sql.Timestamp getLastConfirmationDate();
 public int getNumberOfRejections();
 public java.lang.Integer getOrder();
 public boolean getRemovedFromList();
 public java.lang.String getType();
 public void setApartmentTypeId(java.lang.Integer p0);
 public void setApartmentTypeId(int p0);
 public void setApplicantId(int p0);
 public void setApplicantId(java.lang.Integer p0);
 public void setChoiceNumber(java.lang.Integer p0);
 public void setChoiceNumber(int p0);
 public void setComplexId(int p0);
 public void setLastConfirmationDate(java.sql.Timestamp p0);
 public void setNumberOfRejections(java.lang.Integer p0);
 public void setNumberOfRejections(int p0);
 public void setOrder(java.lang.Integer p0);
 public void setOrder(int p0);
 public void setRemovedFromList(java.lang.String p0);
 public void setType(java.lang.String p0);
}
