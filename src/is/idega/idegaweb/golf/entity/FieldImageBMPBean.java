//idega 2000 - Laddi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import is.idega.idegaweb.golf.block.image.data.ImageEntity;

import com.idega.data.GenericEntity;

public class FieldImageBMPBean extends GenericEntity implements FieldImage{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("field_id", "Völlur", true, true, "java.lang.Integer");
		//addAttribute("image_id", "Mynd", true, true, "java.lang.Integer");
                      addAttribute("image_id","Image",false,false,Integer.class,"one-to-many",ImageEntity.class);
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
