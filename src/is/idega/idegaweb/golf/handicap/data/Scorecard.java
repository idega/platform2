package is.idega.idegaweb.golf.handicap.data;


public interface Scorecard extends com.idega.data.IDOEntity
{
 public boolean getCanDecreaseHandicap();
 public boolean getCanIncreaseHandicap();
 public java.sql.Timestamp getDatePlayed();
 public float getHandicapAfter();
 public float getHandicapBefore();
 public boolean getIsCorrection();
 public is.idega.idegaweb.golf.course.data.Tee getTee();
 public Object getTeeID();
 public int getTotalPoints();
 public int getTotalStrokes();
 public com.idega.user.data.User getUser();
 public Object getUserID();
 public void initializeAttributes();
 public void setCanDecreaseHandicap(boolean p0);
 public void setCanIncreaseHandicap(boolean p0);
 public void setDatePlayed(java.sql.Timestamp p0);
 public void setHandicapAfter(float p0);
 public void setHandicapBefore(float p0);
 public void setIsCorrection(boolean p0);
 public void setTee(is.idega.idegaweb.golf.course.data.Tee p0);
 public void setTeeID(Object p0);
 public void setTotalPoints(int p0);
 public void setTotalStrokes(int p0);
 public void setUser(com.idega.user.data.User p0);
 public void setUserID(Object p0);
}
