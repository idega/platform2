//idega 2000 - Laddi

package com.idega.projects.golf.entity;

//import java.util.*;
import java.sql.*;

public class TeeImage extends GolfEntity{

	public TeeImage(){
		super();
	}

	public TeeImage(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("field_id", "Völlur", true, true, "java.lang.Integer");
		addAttribute("hole_number", "Holunúmer", true, true, "java.lang.Integer");
		addAttribute("image_id", "Mynd", true, true, "java.lang.Integer");
	}

	public String getEntityName(){
		return "tee_image";
	}

	public int getFieldId(){
		return getIntColumnValue("field_id");
	}

	public void setFieldId(int field_id){
		setColumn("field_id", field_id);
	}

	public int getHoleNumber(){
		return getIntColumnValue("hole_number");
	}

	public void setHoleNumber(int hole_number){
		setColumn("hole_number", hole_number);
	}

	public int getImageId(){
		return getIntColumnValue("image_id");
	}

	public void setImageId(int image_id){
		setColumn("image_id", image_id);
	}

}
