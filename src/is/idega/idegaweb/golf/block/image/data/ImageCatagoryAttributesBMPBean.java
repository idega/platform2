//idega 2000 - eiki
package is.idega.idegaweb.golf.block.image.data;


//import java.util.*;
import java.sql.SQLException;

import com.idega.data.GenericEntity;

public class ImageCatagoryAttributesBMPBean extends GenericEntity implements ImageCatagoryAttributes {

	public ImageCatagoryAttributesBMPBean(){
		super();
	}

	public ImageCatagoryAttributesBMPBean(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("image_catagory_id","Catagory",true,true, Integer.class,"many-to-one",ImageCatagory.class);
                addAttribute("attribute_name","Attribute Name",true,true, "java.lang.String");
                addAttribute("attribute_value","Attribute Value",true,true, "java.lang.String");
	}

	public String getEntityName(){
		return "image_catagory_attributes";
	}

        public void setName(String name) {
          setAttributeName(name);
        }

        public String getName() {
          return getAttributeName();
        }

        public void setAttributeName(String name) {
          setColumn("attribute_name",name);
        }

        public String getAttributeName() {
          return (String) getStringColumnValue("attribute_name");
        }

        public void setAttributeValue(String value) {
          setColumn("attribute_value",value);
        }

        public String getAttributeValue() {
          return getStringColumnValue("attribute_value");
        }

        public void setImageCatagoryId(int id) {
          setColumn("image_catagory_id",new Integer(id));
        }

        public int getImageCatagoryId() {
          return getIntColumnValue("image_catagory_id");
        }


}
