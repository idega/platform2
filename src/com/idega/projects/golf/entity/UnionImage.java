//idega 2000 - Laddi

package com.idega.projects.golf.entity;

//import java.util.*;
import java.sql.*;

public class UnionImage extends GolfEntity{

	public UnionImage(){
		super();
	}

	public UnionImage(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("union_id", "Klúbbur", true, true, "java.lang.Integer");
		addAttribute("image_id", "Mynd", true, true, "java.lang.Integer");
	}

	public String getEntityName(){
		return "union_image";
	}

	public int getUnionId(){
		return getIntColumnValue("union_id");
	}

	public void setUnionId(int union_id){
		setColumn("union_id", union_id);
	}

	public int getImageId(){
		return getIntColumnValue("image_id");
	}

	public void setImageId(int image_id){
		setColumn("image_id", image_id);
	}

}
