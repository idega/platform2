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

public class Performer extends GenericEntity {

  public static final String _COLUMNNAME_DISPLAY_NAME = "display_name";
  public static final String _COLUMNNAME_NAME = "name";

  public Performer() {
    super();
  }

  public Performer(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    this.addAttribute(this.getIDColumnName());
    this.addAttribute(_COLUMNNAME_DISPLAY_NAME,"Nafn á vef",true,true,String.class,255);
    this.addAttribute(_COLUMNNAME_NAME,"Nafn",true,true,String.class,255);
    this.addManyToManyRelationShip(Album.class, "ac_album_performer");
    this.addManyToManyRelationShip(Track.class, "ac_track_performer");
  }

  public String getEntityName() {
    return "ac_performer";
  }

  public String getName(){
    return getStringColumnValue(_COLUMNNAME_NAME);
  }

  public void setName(String name){
    setColumn(_COLUMNNAME_NAME, name);
  }
}