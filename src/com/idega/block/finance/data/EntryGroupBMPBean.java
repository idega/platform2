package com.idega.block.finance.data;

import java.sql.*;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.category.data.CategoryEntityBMPBean;
import com.idega.data.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class EntryGroupBMPBean extends CategoryEntityBMPBean implements com.idega.block.finance.data.EntryGroup {

  public EntryGroupBMPBean() {
    super();
  }
  public EntryGroupBMPBean(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameGroupTypeId(),"Group type",true,true,Integer.class);
    addAttribute(getColumnNameGroupDate(),"Group date",true,true,java.sql.Date.class);
    addAttribute(getColumnNameEntryIdFrom(),"Entry from",true,true,Integer.class);
    addAttribute(getColumnNameEntryIdTo(),"Entry to",true,true,Integer.class);
    addAttribute(getColumnNameFileName(),"File name",true,true,String.class);
    addAttribute(getColumnNameInfo(),"Info",true,true,String.class);
  }

  public String getEntityName(){
    return getEntityTableName();
  }
  /*
  "FIN_ENTRY_TYPE_ID"	INTEGER NOT NULL,
  "FIN_GROUP_TYPE_ID"	INTEGER NOT NULL,
  "GROUP_DATE"	TIMESTAMP,
  "ENTRY_ID_FROM"	INTEGER,
  "ENTRY_ID_TO"	INTEGER,
  "FILENAME"	VARCHAR(4000),
  "INFO"	VARCHAR(4000)
  */
  public static String getEntityTableName(){return  "FIN_ENTRY_GROUP";}
  public static String getColumnNameGroupTypeId(){return "FIN_GROUP_TYPE_ID";}
  public static String getColumnNameGroupDate(){return "GROUP_DATE";}
  public static String getColumnNameEntryIdFrom(){return "ENTRY_ID_FROM";}
  public static String getColumnNameEntryIdTo(){return "ENTRY_ID_TO";}
  public static String getColumnNameFileName(){return "FILENAME";}
  public static String getColumnNameInfo(){return "INFO";}

  public String getName(){
    return String.valueOf(getID());
  }

  public int getGroupTypeId(){
    return getIntColumnValue( getColumnNameGroupTypeId() );
  }
  public void setGroupTypeId(int id){
    setColumn(getColumnNameGroupTypeId(),id);
  }
  public int getEntryIdFrom(){
    return getIntColumnValue(getColumnNameEntryIdFrom() );
  }
  public void setEntryIdFrom(int id){
    setColumn(getColumnNameEntryIdFrom(),id);
  }
  public int getEntryIdTo(){
    return getIntColumnValue(getColumnNameEntryIdTo() );
  }
  public void setEntryIdTo(int id){
    setColumn(getColumnNameEntryIdTo(),id);
  }
  public String getFileName(){
    return getStringColumnValue( getColumnNameFileName());
  }
  public void setFileName(String fileName){
    setColumn(getColumnNameFileName(),fileName);
  }
  public String getInfo(){
    return getStringColumnValue( getColumnNameInfo() );
  }
  public void setInfo(String info){
    setColumn(getColumnNameInfo(),info);
  }
  public java.sql.Date getGroupDate(){
    return (java.sql.Date) getColumnValue(getColumnNameGroupDate());
  }
  public void setGroupDate(java.sql.Date date){
    setColumn(getColumnNameGroupDate(),date);
  }
  
  public Collection ejbFindAll()throws FinderException{
  	return super.idoFindPKsByQuery(super.idoQueryGetSelect());
  }
  
 

}
