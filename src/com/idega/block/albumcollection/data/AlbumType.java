package com.idega.block.albumcollection.data;

import com.idega.data.*;
import java.sql.SQLException;

/**
 * Title:        AlbumCollection
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class AlbumType extends GenericEntity {

  public static final String _COLUMNNAME_NAME = "name";

  public AlbumType() {
    super();
  }

  public AlbumType(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    this.addAttribute(this.getIDColumnName());
    this.addAttribute(_COLUMNNAME_NAME,"Nafn",true,true,String.class,255);
  }

  public String getEntityName() {
    return "ac_album_type";
  }

  public String getName(){
    return getStringColumnValue(_COLUMNNAME_NAME);
  }

  public void setName(String name){
    setColumn(_COLUMNNAME_NAME, name);
  }

  public void insertStartData(){
    try {
      AlbumType type = new AlbumType();
      type.setName("Geisladiskur");
      type.insert();

      type = new AlbumType();
      type.setName("Hljómplata");
      type.insert();

      type = new AlbumType();
      type.setName("Segulband");
      type.insert();
    }
    catch (SQLException ex) {
      System.err.println(this.getClass().getName()+": insertStartData() failed");
      ex.printStackTrace();
    }
  }

}