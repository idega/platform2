package is.idega.idegaweb.golf.block.text.data;


public interface TextModule extends com.idega.data.IDOLegacyEntity
{
 public int getImageId();
 public java.lang.String getIncludeImage();
 public java.lang.String getTextBody();
 public java.sql.Timestamp getTextDate();
 public java.lang.String getTextHeadline();
 public void setImageId(int p0);
 public void setIncludeImage(java.lang.String p0);
 public void setTextBody(java.lang.String p0);
 public void setTextDate(java.sql.Timestamp p0);
 public void setTextHeadline(java.lang.String p0);
}
