package is.idega.idegaweb.golf.block.file.data;


public interface FileEntity extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getContentType();
 public java.sql.Date getDateAdded();
 public com.idega.data.BlobWrapper getFileValue();
 public java.lang.String getName();
 public void initializeAttributes();
 public void setContentType(java.lang.String p0);
 public void setDateAdded(java.sql.Date p0);
 public void setFileValue(com.idega.data.BlobWrapper p0);
 public void setName(java.lang.String p0);
}
