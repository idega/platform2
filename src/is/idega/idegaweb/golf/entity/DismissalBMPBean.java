//idega 2000 - Thorhallur Helgason

/*

*Copyright 2000 idega.is All Rights Reserved.

*/



package is.idega.idegaweb.golf.entity;



import java.sql.*;





/**

*@author <a href="mailto:laddi@idega.is">Thorhallur Helgason</a>

*@version 1.2

*/

public class DismissalBMPBean extends is.idega.idegaweb.golf.entity.GolfEntityBMPBean implements is.idega.idegaweb.golf.entity.Dismissal {



	public DismissalBMPBean(){

		super();

	}



	public DismissalBMPBean(int id)throws SQLException{

		super(id);

	}



	public void initializeAttributes(){

		addAttribute(getIDColumnName());

		addAttribute("name","Frávísun",true,true,"java.lang.String");

	}





	public void setName(String name){

		setColumn("name",name);

	}



	public String getName(){

		return (String) getColumnValue("name");

	}



	public String getEntityName(){

		return "dismissal";

	}



}

