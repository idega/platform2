//idega 2000 - Laddi

package com.idega.block.text.data;



import java.sql.SQLException;
import com.idega.core.component.data.ICObjectInstance;


/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved

 * Company:      idega

  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>

 * @version 1.1

 */



public class TxTextBMPBean extends com.idega.data.GenericEntity implements com.idega.block.text.data.TxText {



  public TxTextBMPBean(){

    super();

  }



  public TxTextBMPBean(int id)throws SQLException{

    super(id);

  }



  public void initializeAttributes(){

    addAttribute(getIDColumnName());

    addAttribute(getColumnNameAttribute(), "attribute", true, true, String.class);

    addAttribute(getColumnNameContentId(), "content", true, true, Integer.class,"many-to-one",Content.class);

    addManyToManyRelationShip(ICObjectInstance.class,"TX_TEXT_IC_OBJECT_INSTANCE");


  }



  public static String getEntityTableName(){ return "TX_TEXT";}

  public static String getColumnNameContentId(){ return "CONTENT";}

  public static String getColumnNameAttribute(){return "ATTRIBUTE";}



  public String getEntityName(){

    return getEntityTableName();

  }

  public int getContentId(){

    return getIntColumnValue(getColumnNameContentId());

  }

  public void setContentId(int iContentId){

    setColumn(getColumnNameContentId(),iContentId);

  }

  public void setContentId(Integer iContentId){

    setColumn(getColumnNameContentId(),iContentId);

  }

  public String getAttribute(){

    return getStringColumnValue(getColumnNameAttribute());

  }

  public void setAttribute(String attribute){

    setColumn(getColumnNameAttribute(), attribute);

  }

}

