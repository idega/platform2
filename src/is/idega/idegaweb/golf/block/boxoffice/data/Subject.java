package is.idega.idegaweb.golf.block.boxoffice.data;


public interface Subject extends com.idega.data.IDOLegacyEntity
{
 public int getContentId();
 public int getContentImage()throws java.sql.SQLException;
 public java.lang.String getContentName()throws java.sql.SQLException;
 public int getImageId();
 public java.lang.String getIncludeImage();
 public int getIssueCategoryId();
 public int getIssueCategoryImage()throws java.sql.SQLException;
 public java.lang.String getIssueCategoryName()throws java.sql.SQLException;
 public int getIssueId();
 public int getIssueImage()throws java.sql.SQLException;
 public java.lang.String getIssueName()throws java.sql.SQLException;
 public java.lang.String getSubjectAuthor();
 public java.sql.Timestamp getSubjectDate();
 public java.lang.String getSubjectName();
 public java.lang.String getSubjectValue();
 public void setContentId(int p0);
 public void setImageId(int p0);
 public void setIncludeImage(java.lang.String p0);
 public void setIssueCategoryId(int p0);
 public void setIssueId(int p0);
 public void setSubjectAuthor(java.lang.String p0);
 public void setSubjectDate(java.sql.Timestamp p0);
 public void setSubjectName(java.lang.String p0);
 public void setSubjectValue(java.lang.String p0);
}
