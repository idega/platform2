package is.idega.idegaweb.campus.block.request.data;


public interface Request extends com.idega.data.IDOEntity
{
 public java.sql.Timestamp getDateFailure();
 public java.sql.Timestamp getDateProcessed();
 public java.sql.Timestamp getDateSent();
 public java.lang.String getDescription();
 public java.lang.String getRequestType();
 public java.lang.String getSpecialTime();
 public java.lang.String getStatus();
 public boolean getReportedViaTelephone();
 public int getUserId();
 public void initializeAttributes();
 public void setDateFailure(java.sql.Timestamp p0);
 public void setDateProcessed(java.sql.Timestamp p0);
 public void setDateSent(java.sql.Timestamp p0);
 public void setDescription(java.lang.String p0);
 public void setRequestType(java.lang.String p0);
 public void setSpecialTime(java.lang.String p0);
 public void setStatus(java.lang.String p0);
 public void setUserId(int p0);
 public void setUserId(java.lang.Integer p0);
 public void setReportedViaTelephone(boolean reported);
 
}
