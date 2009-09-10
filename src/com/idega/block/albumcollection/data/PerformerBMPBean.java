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

public class PerformerBMPBean extends com.idega.data.GenericEntity implements com.idega.block.albumcollection.data.Performer {

  public static final String _COLUMNNAME_DISPLAY_NAME = "display_name";
  public static final String _COLUMNNAME_NAME = "name";

  public PerformerBMPBean() {
    super();
  }

  public PerformerBMPBean(int id) throws SQLException {
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

  public String getDisplayName(){
    return getStringColumnValue(_COLUMNNAME_DISPLAY_NAME);
  }

  public void setDisplayName(String displayName){
    setColumn(_COLUMNNAME_DISPLAY_NAME, displayName);
  }

}
