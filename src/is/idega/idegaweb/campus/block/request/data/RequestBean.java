package is.idega.idegaweb.campus.block.request.data;

import javax.ejb.*;

public interface RequestBean extends com.idega.data.IDOLegacyEntity,is.idega.idegaweb.campus.block.request.data.Request
{
 public java.sql.Timestamp getDateFailure();
 public java.sql.Timestamp getDateProcessed();
 public java.sql.Timestamp getDateSent();
 public java.lang.String getDescription();
 public java.lang.String getRequestType();
 public java.lang.String getSpecialTime();
 public java.lang.String getStatus();
 public int getUserId();
 public void setDateFailure(java.sql.Timestamp p0);
 public void setDateProcessed(java.sql.Timestamp p0);
 public void setDateSent(java.sql.Timestamp p0);
 public void setDescription(java.lang.String p0);
 public void setRequestType(java.lang.String p0);
 public void setSpecialTime(java.lang.String p0);
 public void setStatus(java.lang.String p0);
 public void setUserId(int p0);
 public void setUserId(java.lang.Integer p0);
}
