//idega 2000 - Laddi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.*;

public class FieldImage extends GolfEntity{

	public FieldImage(){
		super();
	}

	public FieldImage(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("field_id", "Völlur", true, true, "java.lang.Integer");
		//addAttribute("image_id", "Mynd", true, true, "java.lang.Integer");
                      addAttribute("image_id","Image",false,false,"java.lang.Integer","one-to-many","com.idega.jmodule.image.data.ImageEntity");
	}

	public String getEntityName(){
		return "field_image";
	}

	public int getFieldId(){
		return getIntColumnValue("field_id");
	}

	public void setFieldId(int field_id){
		setColumn("field_id", field_id);
	}

	public int getImageId(){
		return getIntColumnValue("image_id");
	}

	public void setImageId(int image_id){
		setColumn("image_id", image_id);
	}

}
