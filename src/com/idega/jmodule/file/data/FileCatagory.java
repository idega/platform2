package com.idega.jmodule.file.data;

import java.sql.*;
import com.idega.data.*;


public class FileCatagory extends GenericEntity{

	public FileCatagory(){
		super();
	}

	public FileCatagory(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("file_catagory_name","Skjalaflokkur",true,true,"java.lang.String");
	}

	public String getEntityName(){
		return "file_catagory";
	}

	public String getFileCatagoryName() {
		return (String) getColumnValue("file_catagory_name");
	}






}
