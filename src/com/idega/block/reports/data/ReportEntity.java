package com.idega.block.reports.data;

import java.sql.*;
import java.util.StringTokenizer;
import com.idega.data.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class ReportEntity extends GenericEntity {

  public ReportEntity() {
  }
  public ReportEntity(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute("entity", "Entity", true, true, "java.lang.String");
    addAttribute("maintable", "Table", true, true, "java.lang.String");
    addAttribute("joins","Join",true,true,"java.lang.String");
    addAttribute("jointables", "Join Tables", true, true, "java.lang.String");
  }
  public String getEntityName() {
    return "report_entity";
  }
  public String getEntity(){
    return getStringColumnValue("entity");
  }
  public void setCategory(String entity){
    setColumn("entity", entity);
  }
   public String getMainTable(){
    return getStringColumnValue("maintable");
  }
  public void setMainTable(String main_table){
    setColumn("maintable", main_table);
  }
  public String getJoin(){
    return getStringColumnValue("joins");
  }
  public void setJoin(String joins){
    setColumn("joins", joins);
  }
  public String getJoinTables(){
    return getStringColumnValue("jointables");
  }
  public void setJoinTables(String jointables){
    setColumn("jointables", jointables);
  }


}