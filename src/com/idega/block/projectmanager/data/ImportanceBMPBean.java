//idega 2000 - Gimmi



package com.idega.block.projectmanager.data;



//import java.util.*;

import java.sql.SQLException;



public class ImportanceBMPBean extends com.idega.data.GenericEntity implements com.idega.block.projectmanager.data.Importance {



	public ImportanceBMPBean(){

		super();

	}



	public ImportanceBMPBean(int id)throws SQLException{

		super(id);

	}





	public void initializeAttributes(){

		addAttribute(getIDColumnName());

		addAttribute("name","Mikilvægi",true,true,"java.lang.String");

	}





        public String getIDColumnName(){

          return "pm_importance_id";

        }





        public String getEntityName(){

		return "i_pm_importance";

	}





	public String getName() {

		return (String) getColumnValue("name");

	}



	public void setName(String name) {

		setColumn("name",name);

	}



}

