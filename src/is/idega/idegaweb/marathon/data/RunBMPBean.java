/*
 * Created on Jul 6, 2004
 */
package is.idega.idegaweb.marathon.data;

import java.sql.SQLException;

import com.idega.data.GenericEntity;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author birna
 */
public class RunBMPBean extends GenericEntity implements Run{
    
  		public RunBMPBean(){
  			super();
  		}

  		public RunBMPBean(int id)throws SQLException{
  			super(id);
  		}
  		public void initializeAttributes(){
  			addAttribute(getIDColumnName());
  			addAttribute(getColumnNameRunTypeGroupID(),"Run Type",true,true,Integer.class);
  			addAttribute(getColumnNameRunYearGroupID(),"Run Year",true,true,Integer.class);
  			addAttribute(getColumnNameRunDistanceGroupID(),"Run Distance",true,true,Integer.class);
  			addAttribute(getColumnNameRunTime(),"Run Time",true,true,Integer.class);
  			addAttribute(getColumnNameChipTime(),"Chip Time", true, true, Integer.class);
  			addAttribute(getColumnNameUserID(),"User ID",true,true,Integer.class);
  			addAttribute(getColumnNameChipNumber(),"Chip Number",true,true,String.class);
  			addAttribute(getColumnNameTShirtSize(),"TShirt Size",true,true,String.class);
  			addAttribute(getColumnNameRunGroupName(),"Run Group Name",true,true,String.class);
  			addAttribute(getColumnNameBestTime(),"Best Time",true,true,String.class);
  			addAttribute(getColumnNameGoalTime(),"Goal Time",true,true,String.class);
  		}

  		public static String getEntityTableName() { return "run"; }
  		public static String getColumnNameRunID() { return "run_id"; }
  		public static String getColumnNameRunTypeGroupID() { return com.idega.user.data.GroupBMPBean.getColumnNameGroupID() + "_run"; }
  		public static String getColumnNameRunYearGroupID() { return com.idega.user.data.GroupBMPBean.getColumnNameGroupID() + "_year"; }
  		public static String getColumnNameRunDistanceGroupID() { return com.idega.user.data.GroupBMPBean.getColumnNameGroupID() + "_distance"; }
  		public static String getColumnNameRunTime() { return "run_time"; }
  		public static String getColumnNameChipTime() { return "chip_time"; }
  		public static String getColumnNameChipNumber() { return "run_chip_number"; }
  		public static String getColumnNameTShirtSize() { return "run_tShirt_size"; }
  		public static String getColumnNameRunGroupName() { return "run_group_name"; }
  		public static String getColumnNameBestTime() { return "run_best_time"; }
  		public static String getColumnNameGoalTime() { return "run_goal_time"; }
  	  public static String getColumnNameUserID(){ return com.idega.user.data.UserBMPBean.getColumnNameUserID();}

  		
  	  public String getIDColumnName(){
  			return getColumnNameRunID();
  		}
  	  
  		public String getEntityName(){
  			return getEntityTableName();
  		}
  		public int getRunID() {
  			return getIntColumnValue(getColumnNameRunID());
  		}

  		//GET
    public int getRunTypeGroupID() {
      return getIntColumnValue(getColumnNameRunTypeGroupID());
  	  }
  	  public int getRunYearGroupID() {
  	    return getIntColumnValue(getColumnNameRunYearGroupID());
  	  }
  	  public int getRunDistanceGroupID() {
  	    return getIntColumnValue(getColumnNameRunDistanceGroupID());
  	  }

  	  public int getRunTime(){
  			return getIntColumnValue(getColumnNameRunTime());
  		}
  	  public int getChipTime() {
  	    return getIntColumnValue(getColumnNameChipTime());
  	  }
  	  public int getUserID() {
  	    return getIntColumnValue(getColumnNameUserID());
  	  }
  	  public String getChipNumber() {
  	      return getStringColumnValue(getColumnNameChipNumber());
  	  }
  	  public String getTShirtSize() {
  	      return getStringColumnValue(getColumnNameTShirtSize());
  	  }
  	  public String getRunGroupName() {
  	      return getStringColumnValue(getColumnNameRunGroupName());
  	  }
  	  public int getBestTime() {
  	      return getIntColumnValue(getColumnNameBestTime());
  	  }
  	  public int getGoalTime() {
  	      return getIntColumnValue(getColumnNameGoalTime());
  	  }
  	  //SET
  	  public void setRunTypeGroupID(int runTypeGroupID) {
  	      setColumn(getColumnNameRunTypeGroupID(),runTypeGroupID);
  	  }
  	  public void setRunYearGroupID(int runYearGroupID) {
  	      setColumn(getColumnNameRunYearGroupID(),runYearGroupID);
  	  }
  	  public void setRunDistanceGroupID(int runDisGroupID) {
  	      setColumn(getColumnNameRunDistanceGroupID(),runDisGroupID);
  	  }
  	  public void setRunTime(double runTime) {
  	      setColumn(getColumnNameRunTime(),runTime);
  	  }
  	  public void setChipTime(double chipTime) {
  	    setColumn(getColumnNameChipTime(),chipTime);
  	  }
  	  public void setUserID(int userID) {
  	      setColumn(getColumnNameUserID(),userID);
  	  }
  	  public void setChipNumber(String chipNumber) {
  	      setColumn(getColumnNameChipNumber(),chipNumber);
  	  }
  	  public void setTShirtSize(String tShirtSize) {
  	      setColumn(getColumnNameTShirtSize(),tShirtSize);
  	  }
  	  public void setRunGroupName(String runGrName) {
  	      setColumn(getColumnNameRunGroupName(),runGrName);
  	  }
  	  public void setBestTime(double bestTime) {
  	      setColumn(getColumnNameBestTime(),bestTime);
  	  }
  	  public void setGoalTime(double goalTime) {
  	      setColumn(getColumnNameGoalTime(),goalTime);
  	  }



}
