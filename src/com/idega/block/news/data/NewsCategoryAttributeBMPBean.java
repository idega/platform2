//idega 2000 - eiki

package com.idega.block.news.data;





//import java.util.*;

import java.sql.SQLException;

// Changed By Aron



public class NewsCategoryAttributeBMPBean extends com.idega.data.GenericEntity implements com.idega.block.news.data.NewsCategoryAttribute {



  public NewsCategoryAttributeBMPBean(){

    super();

  }



  public NewsCategoryAttributeBMPBean(int id)throws SQLException{

    super(id);

  }

  public void initializeAttributes(){

    addAttribute(getIDColumnName());

    addAttribute(getNewsCategoryIdColumnName(),"Category",true,true, "java.lang.Integer","many-to-one","com.idega.jmodule.image.data.ImageCatagory");

    addAttribute(getAttributeNameColumnName(),"Attribute Name",true,true, "java.lang.String");

    addAttribute(getAttributeIdColumnName(),"Attribute Id",true,true, "java.lang.Integer");

  }



  public String getEntityName(){

    return "nw_cat_att";

  }



  public static String getNewsCategoryIdColumnName(){return "NW_NEWS_CAT_ID";}

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

    return getStringColumnValue(getAttributeNameColumnName());

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

