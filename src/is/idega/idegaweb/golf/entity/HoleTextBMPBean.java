//idega 2000 - Gimmi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import com.idega.block.text.data.TxText;
import com.idega.data.GenericEntity;

public class HoleTextBMPBean extends GenericEntity implements HoleText{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
                addAttribute("field_id","Númer vallar",true,true,"java.lang.Integer","many-to-one","is.idega.idegaweb.golf.entity.Field");
                addAttribute("hole_number","Númer holu",true,true,"java.lang.Integer");
		addManyToOneRelationship("text", TxText.class);
	}

        public String getEntityName(){
		return "hole_text";
	}

        public void setFieldId(int field_id) {
            setColumn("field_id",new Integer(field_id));
        }

        public int getFieldId() {
            return getIntColumnValue("field_id");
        }

        public void setHoleNumber(int hole_number) {
            setColumn("hole_number",new Integer(hole_number));
        }

        public int getHoleNumber() {
            return getIntColumnValue("hole_number");
        }

	public void setTextID(int textID) {
            setColumn("text",textID);
        }

        public int getTextID() {
            return getIntColumnValue("text");
        }
}
