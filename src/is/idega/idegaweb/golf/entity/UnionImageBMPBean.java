//idega 2000 - Laddi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import is.idega.idegaweb.golf.block.image.data.ImageEntity;

import com.idega.data.GenericEntity;

public class UnionImageBMPBean extends GenericEntity implements UnionImage{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("union_id", "Klúbbur", true, true, "java.lang.Integer");
		//addAttribute("image_id", "Mynd", true, true, "java.lang.Integer");
                      addAttribute("image_id","Image",false,false,Integer.class,"one-to-many",ImageEntity.class);

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
