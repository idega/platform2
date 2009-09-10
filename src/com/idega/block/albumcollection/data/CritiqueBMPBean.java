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

public class CritiqueBMPBean extends com.idega.data.GenericEntity implements com.idega.block.albumcollection.data.Critique {

  public static final String _COLUMNNAME_TRACK_ID = "track_id";
  public static final String _COLUMNNAME_ALBUM_ID = "album_id";
  public static final String _COLUMNNAME_CRITIC_ID = "critic_id";
  public static final String _COLUMNNAME_HEADLINE = "headline";
  public static final String _COLUMNNAME_CRITIQUE = "critique";
  public static final String _COLUMNNAME_RECORD_TIME = "record_time";


  public CritiqueBMPBean() {
    super();
  }

  public CritiqueBMPBean(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    this.addAttribute(this.getIDColumnName());
    this.addAttribute(_COLUMNNAME_TRACK_ID,"Lag",true,true,Integer.class,"one_to_many",Track.class);
    this.addAttribute(_COLUMNNAME_ALBUM_ID,"Hljómplata",true,true,Integer.class,"one_to_many",Album.class);
    this.addAttribute(_COLUMNNAME_CRITIC_ID,"Gagnrýnandi",true,true,Integer.class,"one_to_many",Critic.class);
    this.addAttribute(_COLUMNNAME_HEADLINE,"Fyrirsögn",true,true,String.class,255);
    this.addAttribute(_COLUMNNAME_CRITIQUE,"Gagnrýni",true,true,String.class,255);
    this.addAttribute(_COLUMNNAME_RECORD_TIME,"Tími",true,true,java.sql.Timestamp.class);
  }

  public String getEntityName() {
    return "ac_critique";
  }
}
