//idega 2000 - eiki
package com.idega.block.news.data;


//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class NewsCategoryAttributes extends GenericEntity{

	public NewsCategoryAttributes(){
		super();
	}

	public NewsCategoryAttributes(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("news_category_id","Category",true,true, "java.lang.Integer","many-to-one","com.idega.jmodule.image.data.ImageCatagory");
                addAttribute("attribute_name","Attribute Name",true,true, "java.lang.String");
                addAttribute("attribute_id","Attribute Id",true,true, "java.lang.Integer");
	}

        public String getIDColumnName(){
          return "news_category_attributes_id";
        }

	public String getEntityName(){
		return "i_news_category_attributes";
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
