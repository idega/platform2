package com.idega.block.reports.data;



import java.sql.SQLException;



/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega multimedia

 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>

 * @version 1.0

 */



public class ReportEntityBMPBean extends com.idega.data.GenericEntity implements com.idega.block.reports.data.ReportEntity {



  public ReportEntityBMPBean() {

  }

  public ReportEntityBMPBean(int id) throws SQLException {

    super(id);

  }

  public void initializeAttributes() {

    addAttribute(getIDColumnName());

    addAttribute(getColumnNameEntity(), "Entity", true, true, java.lang.String.class);

    addAttribute(getColumnNameMaintable(), "Table", true, true, java.lang.String.class);

    addAttribute(getColumnNameJoins(),"Join",true,true,java.lang.String.class);

    addAttribute(getColumnNameJoinTables(), "Join Tables", true, true, java.lang.String.class);

  }



  public static String getEntityTableName(){return "REP_ENTITY";}

  public static String getColumnNameMaintable(){return "MAINTABLE";}

  public static String getColumnNameJoins(){return "JOINS";}

  public static String getColumnNameEntity(){return "ENTITY";}

  public static String getColumnNameJoinTables(){return "JOINTABLES";}



  public String getEntityName() {

    return getEntityTableName();

  }

  public String getEntity(){

    return getStringColumnValue(getColumnNameEntity());

  }

  public void setCategory(String entity){

    setColumn(getColumnNameEntity(), entity);

  }

   public String getMainTable(){

    return getStringColumnValue(getColumnNameMaintable());

  }

  public void setMainTable(String main_table){

    setColumn(getColumnNameMaintable(), main_table);

  }

  public String getJoin(){

    return getStringColumnValue(getColumnNameJoins());

  }

  public void setJoin(String joins){

    setColumn(getColumnNameJoins(), joins);

  }

  public String getJoinTables(){

    return getStringColumnValue(getColumnNameJoinTables());

  }

  public void setJoinTables(String jointables){

    setColumn(getColumnNameJoinTables(), jointables);

  }





}
