package com.idega.block.finance.data;



import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;



/**

 * Title:   idegaclasses

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author  <a href="mailto:aron@idega.is">aron@idega.is

 * @version 1.0

 */





public class FinanceHandlerInfoBMPBean extends com.idega.data.GenericEntity implements com.idega.block.finance.data.FinanceHandlerInfo {



  public FinanceHandlerInfoBMPBean(){

          super();

  }

  public FinanceHandlerInfoBMPBean(int id)throws SQLException{

          super(id);

  }

  public void initializeAttributes(){

    addAttribute(getIDColumnName());

    addAttribute(getColumnName(), "Name", true, true, String.class);

    addAttribute(getColumnInfo(), "Info", true, true, String.class);

    addAttribute(getColumnType(), "Type", true, true, String.class,20);

    addAttribute(getColumnClass(), "Class", true, true, String.class);

  }



  public static String getEntityTableName(){return "FIN_HANDLER_INFO";}

  public static String getColumnName(){return "NAME";}

  public static String getColumnInfo(){return "INFO";}

  public static String getColumnType(){return "HANDLER_TYPE";}

  public static String getColumnClass(){return "CLASS";}



  public String getEntityName(){

    return getEntityTableName();

  }

  public String getName(){

    return getHandlerName();

  }

  public String getHandlerName(){

    return getStringColumnValue(getColumnName());

  }

  public void setName(String name){

    setHandlerName(name);

  }

  public void setHandlerName(String name){

    setColumn(getColumnName(), name);

  }

  public String getInfo(){

    return getStringColumnValue(getColumnInfo());

  }

  public void setInfo(String info){

    setColumn(getColumnInfo(), info);

  }

  public String getType(){

    return getStringColumnValue(getColumnType());

  }

  public void setType(String type){

    setColumn(getColumnInfo(), type);

  }

  public String getClassName(){

    return getStringColumnValue(getColumnClass());

  }

  public void setClassName(String ClassName){

    setColumn(getColumnInfo(), ClassName);

  }
  
  public Collection ejbFindAll()throws FinderException{
  	return super.idoFindAllIDsBySQL();
  }

}

