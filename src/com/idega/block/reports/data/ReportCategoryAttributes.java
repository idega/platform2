package com.idega.block.reports.data;

import java.sql.*;
import com.idega.data.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class ReportCategoryAttributes extends GenericEntity{

  public ReportCategoryAttributes(){
    super();
  }

  public ReportCategoryAttributes(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute("category_id","Category",true,true, "java.lang.Integer","many-to-one","com.idega.block.reports.data.ReportCategory");
    addAttribute("attribute_name","Attribute Name",true,true, "java.lang.String");
    addAttribute("attribute_id","Attribute Id",true,true, "java.lang.Integer");
  }
  public String getEntityName(){
    return "report_category_attributes";
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

  public void setReportCategoryId(int id) {
    setColumn("category_id",new Integer(id));
  }

  public int getReportCategoryId() {
    return getIntColumnValue("category_id");
  }


}
