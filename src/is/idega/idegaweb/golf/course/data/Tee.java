package is.idega.idegaweb.golf.course.data;


public interface Tee extends com.idega.data.IDOEntity
{
 public is.idega.idegaweb.golf.course.data.Course getCourse();
 public Object getCourseID();
 public float getCourseRating();
 public com.idega.user.data.Gender getGender();
 public Object getGenderID();
 public java.lang.String getIDColumnName();
 public int getPar();
 public int getSlope();
 public is.idega.idegaweb.golf.course.data.TeeColor getTeeColor();
 public Object getTeeColorID();
 public java.lang.String getTeeName();
 public java.sql.Date getValidFrom();
 public java.sql.Date getValidTo();
 public void initializeAttributes();
 public void setCourseID(Object p0);
 public void setCourseRating(float p0);
 public void setGenderID(Object p0);
 public void setPar(int p0);
 public void setSlope(int p0);
 public void setTeeColorID(Object p0);
 public void setTeeName(java.lang.String p0);
 public void setValidFrom(java.sql.Date p0);
 public void setValidTo(java.sql.Date p0);
}
