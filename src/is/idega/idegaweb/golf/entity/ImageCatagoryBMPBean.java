//idega 2000 - Gimmi



package is.idega.idegaweb.golf.entity;



//import java.util.*;

import java.sql.*;





public class ImageCatagoryBMPBean extends is.idega.idegaweb.golf.entity.GolfEntityBMPBean implements is.idega.idegaweb.golf.entity.ImageCatagory {



	public ImageCatagoryBMPBean(){

		super();	

	}

	

	public ImageCatagoryBMPBean(int id)throws SQLException{

		super(id);

	}



	public void initializeAttributes(){

		addAttribute(getIDColumnName());

		addAttribute("image_catagory_name","nafn flokks",true,true,"java.lang.String");

	}

		

	public String getEntityName(){

		return "image_catagory";

	}

	

	public String getImageCatagoryName() {

		return (String) getColumnValue("image_catagory_name");

	}





	

	

	

	

}

