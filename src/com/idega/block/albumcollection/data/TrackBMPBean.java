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

public class TrackBMPBean extends com.idega.data.GenericEntity implements com.idega.block.albumcollection.data.Track {

  public static final String _COLUMNNAME_ALBUM_ID = "album_id";
  public static final String _COLUMNNAME_DESCRIPTION = "description";
  public static final String _COLUMNNAME_NAME = "name";
  public static final String _COLUMNNAME_NUMBER = "number";
  public static final String _COLUMNNAME_LENGTH = "track_length";
  public static final String _COLUMNNAME_LYRIC_ID = "lyric_id";

  public TrackBMPBean() {
    super();
  }

  public TrackBMPBean(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    this.addAttribute(this.getIDColumnName());
    this.addAttribute(_COLUMNNAME_NAME,"Nafn",true,true,String.class,255);
    this.addAttribute(_COLUMNNAME_DESCRIPTION,"Um lagið",true,true,String.class,1000);
    this.addAttribute(_COLUMNNAME_NUMBER,"Númer",true,true,Integer.class);
    this.addAttribute(_COLUMNNAME_ALBUM_ID,"Hljómplata",true,true,Integer.class,"one_to_many",Album.class);
    this.addAttribute(_COLUMNNAME_LYRIC_ID,"Söngtexti",true,true,Integer.class,"one_to_many",Lyric.class);
    this.addAttribute(_COLUMNNAME_LENGTH,"Lengd lags(sek)",true,true,Integer.class);
    this.addManyToManyRelationShip(Category.class,"ac_track_category");
    this.addManyToManyRelationShip(Author.class,"ac_track_author");
    this.addManyToManyRelationShip(Performer.class, "ac_track_performer");
  }

  public String getEntityName() {
    return "ac_track";
  }

  public String getName(){
    return getStringColumnValue(_COLUMNNAME_NAME);
  }

  public void setName(String name){
    setColumn(_COLUMNNAME_NAME, name);
  }

  public String getDescription(){
    return (String) getColumnValue(_COLUMNNAME_DESCRIPTION);
  }

  public void setDescription(String description){
    setColumn(_COLUMNNAME_DESCRIPTION,description);
  }

  public int getNumber(){
    return getIntColumnValue(_COLUMNNAME_NUMBER);
  }

  public void setNumber(int number){
    setColumn(_COLUMNNAME_NUMBER, number);
  }

  public int getAlbumId(){
    return this.getIntColumnValue(_COLUMNNAME_ALBUM_ID);
  }

  public void setAlbumId(int id){
    this.setColumn(_COLUMNNAME_ALBUM_ID,id);
  }

  public int getLyricId(){
    return this.getIntColumnValue(_COLUMNNAME_LYRIC_ID);
  }

  public void setLyricId(int id){
    this.setColumn(_COLUMNNAME_LYRIC_ID,id);
  }

  public void setLyricIdAsNull() throws SQLException{
    this.setColumnAsNull(_COLUMNNAME_LYRIC_ID);
  }

  public int getLength(){
    return this.getIntColumnValue(_COLUMNNAME_LENGTH);
  }

  public void setLength(int sek){
    this.setColumn(_COLUMNNAME_LENGTH,sek);
  }

}
