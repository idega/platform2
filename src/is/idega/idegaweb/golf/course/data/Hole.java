package is.idega.idegaweb.golf.course.data;


public interface Hole extends com.idega.data.IDOEntity
{
 public is.idega.idegaweb.golf.course.data.Course getCourse();
 public int getCourseID();
 public int getHandicap();
 public java.lang.String getHoleName();
 public int getHoleNumber();
 public int getPar();
 public is.idega.idegaweb.golf.course.data.TeeColor getTeeColor();
 public int getTeeColorID();
 public java.sql.Date getValidFrom();
 public java.sql.Date getValidTo();
 public void initializeAttributes();
 public void setCourseID(int p0);
 public void setHandicap(int p0);
 public void setHoleName(java.lang.String p0);
 public void setHoleNumber(int p0);
 public void setPar(int p0);
 public void setTeeColorID(int p0);
 public void setValidFrom(java.sql.Date p0);
 public void setValidTo(java.sql.Date p0);
}
