package com.idega.jmodule.file.data;

import java.sql.*;
import com.idega.data.*;


public class FileEntity extends GenericEntity{

	public FileEntity(){
		super();
	}

	public FileEntity(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("content_type","týpa",true,true,"java.lang.String");
		addAttribute("file_value","Skra",false,false,"java.sql.Blob");
		addAttribute("file_name","nafn skjals",true,true,"java.lang.String");
		addAttribute("date_added","sett inn",true,true,"java.sql.Timestamp");
		addAttribute("from_file","frá skrá",true,true,"java.lang.Boolean");
	}

	public String getEntityName(){
		return "file_";
	}

	public String getContentType(){
		return (String) getColumnValue("content_type");
	}

	public void setContentType(String contentType){
		setColumn("content_type", contentType);
	}

	public Blob getFileValue(){
		return (Blob) getColumnValue("file_value");
	}

	public void setFileValue(Blob fileValue){
		setColumn("file_value", fileValue);
	}

	public String getName(){
		return (String) getColumnValue("file_name");
	}

	public void setName(String name){
		setColumn("file_name", name);
	}

	public Date getDateAdded(){
		return (Date) getColumnValue("date_added");
	}

	public void setDateAdded(Date dateAdded){
		setColumn("date_added", dateAdded);
	}



}
