package is.idega.idegaweb.golf.course.data;


public interface Course extends com.idega.data.IDOEntity
{
 public com.idega.user.data.Group getClub();
 public int getClubID();
 public java.lang.String getCourseName();
 public java.lang.String getCourseType();
 public java.lang.String getIDColumnName();
 public boolean getIsValid();
 public int getNumberOfHoles();
 public void initializeAttributes();
 public void setClub(com.idega.user.data.Group p0);
 public void setClubID(int p0);
 public void setCourseName(java.lang.String p0);
 public void setCourseType(java.lang.String p0);
 public void setIsValid(boolean p0);
 public void setNumberOfHoles(int p0);
}
