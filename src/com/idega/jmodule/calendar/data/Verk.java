//idega 2000 - Gimmi

package com.idega.jmodule.calendar.data;

import java.sql.*;
import com.idega.data.*;

public class Verk extends GenericEntity{

	public Verk(){
		super();
	}

	public Verk(int id)throws SQLException{
		super(id);
	}


	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("verk_nafn_stutt","verk_nafn_stutt",true,true,"java.lang.String");
		addAttribute("verk_nafn","verk_nafn",true,true,"java.lang.String");
		addAttribute("project_id","project_id",true,true,"java.lang.String");
		addAttribute("status","status",true,true,"java.lang.String");
	}

	public String getIDColumnName() {
		return "verk_id";
	}

	public String getEntityName(){
		return "verk";
	}
	
	public String getName() {
		return getVerkNafn();
	}
	
	public String getVerkNafn() {
		return getStringColumnValue("verk_nafn");
	}

	public void setVerkNafnStutt(String verk_nafn_stutt) {
		setColumn("verk_nafn_stutt",verk_nafn_stutt);
	}

	public String getVerkNafnStutt() {
		return getStringColumnValue("verk_nafn_stutt");
	}

	public void setVerkNafn(String verk_nafn) {
		setColumn("verk_nafn",verk_nafn);
	}

	public String getProjectId() {
		return getStringColumnValue("project_id");
	}

	public void setProjectId(String project_id) {
		setColumn("project_id",project_id);
	}

	public String getStatus() {
		return getStringColumnValue("status");
	}

	public void setStatus(String status) {
		setColumn("status",status);
	}

}
