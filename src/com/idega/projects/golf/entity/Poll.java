//idega 2000 - Eiki

package com.idega.projects.golf.entity;

//import java.util.*;
import java.sql.*;

public class Poll extends GolfEntity{

	public Poll(){
		super();	
	}
	
	public Poll(int id)throws SQLException{
		super(id);
	}
	
	public void setDefaultValues(){
		setColumn("in_use",new Boolean(false));
	}
	
	public void setUnionID(int ID){
		setColumn("union_id",ID);
	}

	public void setUnionID(String ID){
		setUnionID(Integer.parseInt(ID));
	}
	
	
	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("question", "Spurning", true, true, "java.lang.String");
		addAttribute("start_time", "Hefst", true, true, "java.sql.Timestamp");
		addAttribute("end_time", "Lýkur", true, true, "java.sql.Timestamp");
		addAttribute("union_id", "Félag", true, true, "java.lang.Integer","many-to-one","com.idega.projects.golf.entity.Union");
		addAttribute("in_use","í Notkun", false, false, "java.lang.Boolean");
	}
	
	public String getQuestion(){
		return getStringColumnValue("question");
	}

	public Timestamp getStartTime(){
		return (Timestamp)getColumnValue("start_time");
	}	
	
	public Timestamp getEndTime() {
		return (Timestamp)getColumnValue("end_time");
	}	

	public Union getUnion() {
		return (Union)getColumnValue("union_id");
	}

	public int getUnionID() {
		return getIntColumnValue("union_id");
	}
	
	
	public String getEntityName(){
		return "poll";
	}
	
	public String getName(){
		return getQuestion();
	}
		
	public Poll_option[] findOptions()throws SQLException{
		return (Poll_option[]) findAssociated(new Poll_option());
	}
	
	public void delete() throws SQLException{
		Poll_option[] options = (Poll_option[])(new Poll_option()).findAllByColumn("poll_id",Integer.toString(this.getID()));
		for (int i = 0; i < options.length; i++){
			options[i].delete();
		}		
		super.delete();
	}

	public boolean getIfInUse(){
		return getBooleanColumnValue("in_use");
	}

	public int getID() {
		return  getIntColumnValue("poll_id");
	}	

	public void setInUse(boolean inUse){
		setColumn("in_use",new Boolean(inUse));
	}
	
}
