package is.idega.idegaweb.golf.block.news.data;


public interface NewsCategory extends com.idega.data.IDOLegacyEntity
{
 public java.sql.Date getDate();
 public java.lang.String getDescription();
 public java.lang.String getName();
 public java.lang.String getNewsCategoryName();
 public int getUnionId();
 public java.lang.String getValid();
 public void initializeAttributes();
 public void setDate(java.sql.Date p0);
 public void setDescription(java.lang.String p0);
 public void setNewsCategoryName(java.lang.String p0);
 public void setUnionId(java.lang.Integer p0);
 public void setValid(java.lang.String p0);
}
