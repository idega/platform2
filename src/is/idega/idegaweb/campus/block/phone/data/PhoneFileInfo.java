package is.idega.idegaweb.campus.block.phone.data;


public interface PhoneFileInfo extends com.idega.data.IDOLegacyEntity
{
 public int getErrorCount();
 public java.lang.String getFileName();
 public int getLineCount();
 public java.lang.String getName();
 public int getNoAccountCount();
 public int getNumberCount();
 public java.sql.Timestamp getReadTime();
 public float getTotalAmount();
 public void setDateRead(java.sql.Timestamp p0);
 public void setErrorCount(int p0);
 public void setFileName(java.lang.String p0);
 public void setLineCount(int p0);
 public void setNoAccountCount(int p0);
 public void setNumberCount(int p0);
 public void setTotalAmount(float p0);
}
