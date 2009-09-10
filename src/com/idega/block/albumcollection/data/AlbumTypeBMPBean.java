package com.idega.block.albumcollection.data;

import java.sql.SQLException;

/**
 * Title:        AlbumCollection
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class AlbumTypeBMPBean extends com.idega.data.GenericEntity implements com.idega.block.albumcollection.data.AlbumType {

  public static final String _COLUMNNAME_NAME = "name";

  public AlbumTypeBMPBean() {
    super();
  }

  public AlbumTypeBMPBean(int id) throws SQLException {
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
      AlbumType type = ((com.idega.block.albumcollection.data.AlbumTypeHome)com.idega.data.IDOLookup.getHomeLegacy(AlbumType.class)).createLegacy();
      type.setName("Geisladiskur");
      type.insert();

      type = ((com.idega.block.albumcollection.data.AlbumTypeHome)com.idega.data.IDOLookup.getHomeLegacy(AlbumType.class)).createLegacy();
      type.setName("Hljómplata");
      type.insert();

      type = ((com.idega.block.albumcollection.data.AlbumTypeHome)com.idega.data.IDOLookup.getHomeLegacy(AlbumType.class)).createLegacy();
      type.setName("Segulband");
      type.insert();
    }
    catch (SQLException ex) {
      System.err.println(this.getClass().getName()+": insertStartData() failed");
      ex.printStackTrace();
    }
  }

}
