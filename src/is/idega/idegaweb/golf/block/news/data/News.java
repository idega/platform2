package is.idega.idegaweb.golf.block.news.data;


public interface News extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getAuthor();
 public java.sql.Timestamp getDate();
 public int getDaysShown();
 public java.lang.String getHeadline();
 public int getImageId();
 public java.lang.String getIncludeImage();
 public java.lang.String getName();
 public int getNewsCategoryId();
 public java.sql.Timestamp getNewsDate();
 public java.lang.String getSource();
 public java.lang.String getText();
 public int getUnionId();
 public void initializeAttributes();
 public void setAuthor(java.lang.String p0);
 public void setDate(java.sql.Timestamp p0);
 public void setDaysShown(java.lang.Integer p0);
 public void setDaysShown(int p0);
 public void setHeadline(java.lang.String p0);
 public void setImageId(java.lang.Integer p0);
 public void setImageId(int p0);
 public void setIncludeImage(java.lang.String p0);
 public void setNewsCategoryId(java.lang.Integer p0);
 public void setNewsCategoryId(int p0);
 public void setNewsDate(java.sql.Timestamp p0);
 public void setSource(java.lang.String p0);
 public void setText(java.lang.String p0);
 public void setUnionId(int p0);
 public void setUnionId(java.lang.Integer p0);
}
