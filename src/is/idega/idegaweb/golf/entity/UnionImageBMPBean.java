//idega 2000 - Laddi



package is.idega.idegaweb.golf.entity;



//import java.util.*;

import java.sql.*;



public class UnionImageBMPBean extends is.idega.idegaweb.golf.entity.GolfEntityBMPBean implements is.idega.idegaweb.golf.entity.UnionImage {



	public UnionImageBMPBean(){

		super();

	}



	public UnionImageBMPBean(int id)throws SQLException{

		super(id);

	}



	public void initializeAttributes(){

		addAttribute(getIDColumnName());

		addAttribute("union_id", "Klúbbur", true, true, "java.lang.Integer");

		//addAttribute("image_id", "Mynd", true, true, "java.lang.Integer");

                      addAttribute("image_id","Image",false,false,"java.lang.Integer","one-to-many","com.idega.jmodule.image.data.ImageEntity");



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

