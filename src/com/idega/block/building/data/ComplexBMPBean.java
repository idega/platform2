package com.idega.block.building.data;



import com.idega.data.IDOLegacyEntity;

import java.sql.SQLException;

/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega multimedia

 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>

 * @version 1.0

 */



public class ComplexBMPBean extends com.idega.data.GenericEntity implements com.idega.block.building.data.Complex {



  public ComplexBMPBean() {

    super();

  }



  public ComplexBMPBean(int id)throws SQLException{

    super(id);

  }



  public void initializeAttributes() {

    addAttribute(getIDColumnName());

    addAttribute(getNameColumnName(),"Name",true,true,java.lang.String.class);

    addAttribute(getInfoColumnName(),"Info",true,true,java.lang.String.class,4000);

    addAttribute(getImageIdColumnName(),"Map",true,true,java.lang.Integer.class);

  }



  public String getEntityName() {

    return getNameTableName();

  }

  public static String getNameTableName(){return "bu_complex";}

  public static String getNameColumnName(){return "name";}

  public static String getInfoColumnName(){return "info";}

  public static String getImageIdColumnName(){return "ic_image_id";}



  public String getName(){

    return getStringColumnValue(getNameColumnName());

  }

  public void setName(String name){

    setColumn(getNameColumnName(),name);

  }

  public String getInfo(){

    return getStringColumnValue(getInfoColumnName());

  }

  public void setInfo(String info){

    setColumn(getInfoColumnName(),info);

  }

   public int getImageId(){

    return getIntColumnValue(getImageIdColumnName());

  }

  public void setImageId(int image_id){

    setColumn(getImageIdColumnName(),image_id);

  }

  public void setImageId(Integer image_id){

    setColumn(getImageIdColumnName(),image_id);

  }

}
