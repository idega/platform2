//idega 2000 - eiki
package com.idega.block.news.data;


//import java.util.*;
import java.sql.*;
import com.idega.data.*;
// Changed By Aron

public class NewsCategoryAttribute extends GenericEntity{

  public NewsCategoryAttribute(){
    super();
  }

  public NewsCategoryAttribute(int id)throws SQLException{
    super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getNewsCategoryIdColumnName(),"Category",true,true, "java.lang.Integer","many-to-one","com.idega.jmodule.image.data.ImageCatagory");
    addAttribute(getAttributeNameColumnName(),"Attribute Name",true,true, "java.lang.String");
    addAttribute(getAttributeIdColumnName(),"Attribute Id",true,true, "java.lang.Integer");
  }

  public String getIDColumnName(){
    return "news_category_attribute_id";
  }
  public String getEntityName(){
    return "news_category_attribute";
  }

  public static String getNewsCategoryIdColumnName(){return "NEWS_CATAGORY_ID";}
  public static String getAttributeNameColumnName(){return "ATTRIBUTE_NAME";}
  public static String getAttributeIdColumnName(){return "ATTRIBUTE_ID";}

  public void setName(String name) {
    setAttributeName(name);
  }
  public String getName() {
    return getAttributeName();
  }
  public void setAttributeName(String name) {
    setColumn(getAttributeNameColumnName(),name);
  }
  public String getAttributeName() {
    return (String) getStringColumnValue(getAttributeNameColumnName());
  }
  public void setAttributeId(int id) {
    setColumn(getAttributeIdColumnName(),new Integer(id));
  }
  public int getAttributeId() {
    return getIntColumnValue(getAttributeIdColumnName());
  }
  public void setNewsCategoryId(int id) {
    setColumn(getNewsCategoryIdColumnName(),new Integer(id));
  }
  public int getNewsCategoryId() {
    return getIntColumnValue(getNewsCategoryIdColumnName());
  }


}
