package com.idega.block.reports.data;



import java.sql.SQLException;



/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega multimedia

 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>

 * @version 1.0

 */



public class ReportCategoryAttributeBMPBean extends com.idega.data.GenericEntity implements com.idega.block.reports.data.ReportCategoryAttribute {



  public ReportCategoryAttributeBMPBean(){

    super();

  }



  public ReportCategoryAttributeBMPBean(int id)throws SQLException{

    super(id);

  }



  public void initializeAttributes(){

    addAttribute(getIDColumnName());

    addAttribute(getColumnNameCategoryId(),"Category",true,true, java.lang.Integer.class,"many-to-one",com.idega.block.reports.data.ReportCategory.class);

    addAttribute(getColumnNameAttributeName(),"Attribute Name",true,true, java.lang.String.class);

    addAttribute(getColumnNameAttributeId(),"Attribute Id",true,true, java.lang.Integer.class);

  }



  public static String getEntityTableName(){return "REP_CAT_ATTRIBUTE";}

  public static String getColumnNameCategoryId(){return "REP_CATEGORY_ID";}

  public static String getColumnNameAttributeName(){return "ATTRIBUTE_NAME";}

  public static String getColumnNameAttributeId(){return "ATTRIBUTE_ID";}





  public String getEntityName(){

    return getEntityTableName();

  }



  public void setName(String name) {

    setAttributeName(name);

  }

  public String getName() {

    return getAttributeName();

  }



  public void setAttributeName(String name) {

    setColumn(getColumnNameAttributeName(),name);

  }



  public String getAttributeName() {

    return getStringColumnValue(getColumnNameAttributeName());

  }



  public void setAttributeId(int id) {

    setColumn(getColumnNameAttributeId(),new Integer(id));

  }



  public int getAttributeId() {

    return getIntColumnValue(getColumnNameAttributeId());

  }



  public void setReportCategoryId(int id) {

    setColumn(getColumnNameCategoryId(),new Integer(id));

  }



  public int getReportCategoryId() {

    return getIntColumnValue(getColumnNameCategoryId());

  }





}

