//idega 2000 - Tryggvi Larusson

package com.idega.projects.golf.entity;

//import java.util.*;
import java.sql.*;

public class Statistic extends GolfEntity{

	public Statistic(){
		super();
	}

	public Statistic(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("scorecard_id", "Skorkort", true, true, "java.lang.Integer","one-to-many","com.idega.projects.golf.entity.Scorecard");
		addAttribute("tee_id", "Teigur", true, true, "java.lang.Integer","one-to-many","com.idega.projects.golf.entity.Tee");
		addAttribute("fairway", "Á braut", true, true, "java.lang.Integer");
		addAttribute("greens", "Á flöt", true, true, "java.lang.Integer");
		addAttribute("putts", "Pútt", true, true, "java.lang.Integer");

	}

	public String getEntityName(){
		return "statistic";
	}

	public int getScorecardID() {
		return getIntColumnValue("scorecard_id");
	}

	public int getTeeID() {
		return getIntColumnValue("tee_id");
	}

	public int getFairway(){
		return getIntColumnValue("fairway");
	}

	public int getGreens(){
		return getIntColumnValue("greens");
	}

	public int getPutts(){
		return getIntColumnValue("putts");
	}

	public void setScorecardID(int scorecard_id) {
		setColumn("scorecard_id",scorecard_id);
	}

	public void setTeeID(int tee_id) {
		setColumn("tee_id",tee_id);
	}

	public void setFairway(int fairway) {
		setColumn("fairway",fairway);
	}

	public void setGreens(int greens) {
		setColumn("greens",greens);
	}

	public void setPutts(int putts) {
		setColumn("putts",putts);
	}


}
