//idega 2000 - Gimmi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.*;


public class ImageCatagory extends GolfEntity{

	public ImageCatagory(){
		super();	
	}
	
	public ImageCatagory(int id)throws SQLException{
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
