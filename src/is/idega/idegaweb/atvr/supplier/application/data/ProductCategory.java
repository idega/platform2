package is.idega.idegaweb.atvr.supplier.application.data;


public interface ProductCategory extends com.idega.data.IDOEntity
{
 public is.idega.idegaweb.atvr.supplier.application.data.ProductCategory getBelongsToCategory();
 public int getBelongsToCategoryId();
 public java.lang.String getCategory();
 public java.lang.String getDescription();
 public void initializeAttributes();
 public void setBelongsToCategory(int p0);
 public void setCategory(java.lang.String p0);
 public void setDescription(java.lang.String p0);
}
