//idega 2000 - Laddi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import com.idega.core.file.data.ICFile;
import com.idega.data.GenericEntity;

public class FieldImageBMPBean extends GenericEntity implements FieldImage{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("field_id", "Völlur", true, true, "java.lang.Integer");
		addManyToOneRelationship("image",ICFile.class);
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

	public int getImageID(){
		return getIntColumnValue("image");
	}

	public void setImageID(int imageID){
		setColumn("image", imageID);
	}

	public void setImageID(Integer imageID){
		setColumn("image", imageID);
	}
}