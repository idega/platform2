package is.idega.idegaweb.project.data;


public interface IPProject extends com.idega.data.IDOLegacyEntity
{
 public java.sql.Date getCreationDate();
 public boolean getDeleted();
 public int getDeletedBy();
 public java.sql.Timestamp getDeletedWhen();
 public java.lang.String getDescription();
 public java.lang.String getName();
 public int getParentId();
 public java.lang.String getProjectNumber();
 public boolean hasParent();
 public void setCreationDate(java.sql.Date p0);
 public void setDefaultValues();
 public void setDeleted(boolean p0);
 public void setDeletedBy(int p0);
 public void setDeletedWhen(java.sql.Timestamp p0);
 public void setDescription(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setParentId(int p0);
 public void setParentIdAsNull()throws java.sql.SQLException;
 public void setProjectNumber(java.lang.String p0);
}
