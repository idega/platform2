package se.idega.idegaweb.commune.care.data;


public interface ApplicationPriority extends com.idega.data.IDOEntity
{
 public se.idega.idegaweb.commune.care.data.ChildCareApplication getApplication();
 public int getApplicationId();
 public java.lang.String getIDColumnName();
 public java.lang.String getMessage();
 public java.sql.Date getPriorityDate();
 public void initializeAttributes();
 public void setApplication(se.idega.idegaweb.commune.care.data.ChildCareApplication p0);
 public void setApplicationId(int p0);
 public void setMessage(java.lang.String p0);
 public void setPriorityDate(java.sql.Date p0);
}
