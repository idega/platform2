//idega 2000 - Laddi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.util.Collection;

import javax.ejb.FinderException;

import is.idega.idegaweb.golf.block.image.data.ImageEntity;

import com.idega.core.file.data.ICFile;
import com.idega.data.GenericEntity;

public class TeeImageBMPBean extends GenericEntity implements TeeImage{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("field_id", "Völlur", true, true, "java.lang.Integer");
		addAttribute("hole_number", "Holunúmer", true, true, "java.lang.Integer");
		addManyToOneRelationship("image_id",ImageEntity.class); //Old Image
    addManyToOneRelationship("image", ICFile.class);
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

	public int getImageID(){
		return getIntColumnValue("image");
	}

	public void setImageID(int imageID){
		setColumn("image", imageID);
	}

	public void setImageID(Integer imageID){
		setColumn("image", imageID);
	}
	
	public ImageEntity getOldImage() {
		return (ImageEntity)getColumnValue("image_id");
	}
	
    public Collection ejbFindAll() throws FinderException {
		return idoFindPKsByQuery(idoQueryGetSelect());
    }
}