//idega 2000 - Laddi

package com.idega.jmodule.server;

import java.sql.*;
import com.idega.data.*;

public class Headlines extends GenericEntity{

	public Headlines(){
		super();
	}

	public Headlines(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("group_id", "ID", true, true, "java.lang.Integer");
		addAttribute("clip", "clip", true, true, "java.lang.String");
		addAttribute("site", "site", true, true, "java.lang.String");
		addAttribute("headline", "headline", true, true, "java.lang.String");
		addAttribute("theurl", "theurl", true, true, "java.lang.String");
		addAttribute("headline_time", "headline_time", true, true, "java.sql.Timestamp");

	}

	public String getIDColumnName(){
		return "headlines_id";
	}

	public String getEntityName(){
		return "headlines";
	}

	public int getGroupID(){
		return getIntColumnValue("group_id");
	}

	public void setGroupID(int group_id){
		setColumn("group_id", group_id);
	}

	public String getClip(){
		return getStringColumnValue("clip");
	}

	public void setClip(String clip){
		setColumn("clip", clip);
	}

	public String getSite(){
		return getStringColumnValue("site");
	}

	public void setSite(String site){
		setColumn("site", site);
	}

	public String getHeadline(){
		return getStringColumnValue("headline");
	}

	public void setHeadline(String headline){
		setColumn("headline", headline);
	}

	public String getTheURL(){
		return getStringColumnValue("theurl");
	}

	public void setTheURL(String theurl){
		setColumn("theurl", theurl);
	}

	public java.sql.Timestamp getTimestamp(){
		return (java.sql.Timestamp) getColumnValue("headline_time");
	}

	public void setTimestamp(java.sql.Timestamp headline_time){
		setColumn("headline_time", headline_time);
	}

}
