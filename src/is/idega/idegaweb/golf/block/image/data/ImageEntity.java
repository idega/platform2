package is.idega.idegaweb.golf.block.image.data;


public interface ImageEntity extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getContentType();
 public java.sql.Timestamp getDate();
 public java.sql.Timestamp getDateAdded();
 public java.lang.String getFromFile();
 public java.lang.String getHeight();
 public java.lang.String getImageLink();
 public java.lang.String getImageLinkOwner();
 public java.lang.String getImageName();
 public java.lang.String getImageText();
 public java.io.InputStream getImageValue()throws java.lang.Exception;
 public java.lang.String getLink();
 public java.lang.String getName();
 public int getParentId();
 public int getSize();
 public java.lang.String getText();
 public java.lang.String getWidth();
 public void setContentType(java.lang.String p0);
 public void setDate(java.sql.Timestamp p0);
 public void setDateAdded(java.sql.Timestamp p0);
 public void setFromFile(java.lang.String p0);
 public void setFromFile(boolean p0);
 public void setHeight(java.lang.String p0);
 public void setImageLink(java.lang.String p0);
 public void setImageLinkOwner(java.lang.String p0);
 public void setImageName(java.lang.String p0);
 public void setImageText(java.lang.String p0);
 public void setImageValue(java.io.InputStream p0);
 public void setName(java.lang.String p0);
 public void setParentId(int p0);
 public void setSize(int p0);
 public void setWidth(java.lang.String p0);
}
