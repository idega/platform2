package is.idega.idegaweb.marathon.data;


public interface Run extends com.idega.data.IDOEntity{
    		public int getRunID();
    		public int getRunTypeGroupID();
    	  public int getRunYearGroupID();
    	  public int getRunDistanceGroupID();
    	  public int getRunGroupGroupID();
    	  public int getRunTime();
    	  public int getChipTime();
    	  public int getUserID();
    	  public String getChipNumber();
    	  public String getUserNationality();
    	  public String getTShirtSize();
    	  public String getRunGroupName();
    	  public int getBestTime();
    	  public int getGoalTime();

    	  public void setRunTypeGroupID(int runType);
    	  public void setRunYearGroupID(int runYear);
    	  public void setRunDistanceGroupID(int runDis);
    	  public void setRunGroupGroupID(int runGroup);
    	  public void setRunTime(double runTime);
    	  public void setChipTime(double chipTime);
    	  public void setUserID(int userID);
    	  public void setChipNumber(String chipNumber);
    	  public void setUserNationality(String nationality);
    	  public void setTShirtSize(String tShirtSize);
    	  public void setRunGroupName(String runGrName);
    	  public void setBestTime(double bestTime);
    	  public void setGoalTime(double goalTime);
}
