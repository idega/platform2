//idega 2000 - eiki
package is.idega.idegaweb.golf.block.news.data;


//import java.util.*;
import com.idega.data.GenericEntity;

public class NewsCategoryAttributesBMPBean extends GenericEntity implements NewsCategoryAttributes {

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
    addManyToOneRelationship("news_category_id", NewsCategory.class);
                addAttribute("attribute_name","Attribute Name",true,true, "java.lang.String");
                addAttribute("attribute_id","Attribute Id",true,true, "java.lang.Integer");
	}

	public String getEntityName(){
		return "news_category_attributes";
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

        public void setAttributeId(int id) {
          setColumn("attribute_id",new Integer(id));
        }

        public int getAttributeId() {
          return getIntColumnValue("attribute_id");
        }

        public void setNewsCategoryId(int id) {
          setColumn("news_category_id",new Integer(id));
        }

        public int getNewsCategoryId() {
          return getIntColumnValue("news_category_id");
        }


}
