//idega 2000 - Ægir

package com.idega.projects.projectscene.entity;

//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class ImageEntity extends GenericEntity{

	public ImageEntity(){
		super();
	}

	public ImageEntity(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("content_type","týpa",true,true,"java.lang.String");
		addAttribute("image_value","myndin sjalf",false,false,"java.sql.Blob");
		addAttribute("image_name","nafn myndar",true,true,"java.lang.String");
		addAttribute("date_added","sett inn",true,true,"java.sql.Timestamp");
		addAttribute("from_file","frá skrá",true,true,"java.lang.Boolean");
	}

	public String getEntityName(){
		return "image";
	}

	public String getContentType(){
		return (String) getColumnValue("content_type");
	}

	public void setContentType(String contentType){
		setColumn("content_type", contentType);
	}

	public Blob getImageValue(){
		return (Blob) getColumnValue("image_value");
	}

	public void setImageValue(Blob imageValue){
		setColumn("image_value", imageValue);
	}

	public String getName(){
		return (String) getColumnValue("name");
	}

	public void setName(String name){
		setColumn("name", name);
	}

	public Date getDateAdded(){
		return (Date) getColumnValue("date_added");
	}

	public void setDateAdded(Date dateAdded){
		setColumn("date_added", dateAdded);
	}



}
