package is.idega.idegaweb.marathon.data;


public interface Run extends com.idega.data.IDOEntity
{
 public int getBestTime();
 public java.lang.String getChipNumber();
 public int getChipTime();
 public int getGoalTime();
 public int getParticipantNumber();
 public int getRunDistanceGroupID();
 public int getRunGroupGroupID();
 public java.lang.String getRunGroupName();
 public int getRunID();
 public int getRunTime();
 public int getRunTypeGroupID();
 public int getRunYearGroupID();
 public java.lang.String getTShirtSize();
 public int getUserID();
 public java.lang.String getUserNationality();
 public void setBestTime(double p0);
 public void setChipNumber(java.lang.String p0);
 public void setChipTime(double p0);
 public void setGoalTime(double p0);
 public void setParticipantNumber(int p0);
 public void setRunDistanceGroupID(int p0);
 public void setRunGroupGroupID(int p0);
 public void setRunGroupName(java.lang.String p0);
 public void setRunTime(double p0);
 public void setRunTypeGroupID(int p0);
 public void setRunYearGroupID(int p0);
 public void setTShirtSize(java.lang.String p0);
 public void setUserID(int p0);
 public void setUserNationality(java.lang.String p0);
}
