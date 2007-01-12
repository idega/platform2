package com.idega.block.albumcollection.business;

import com.idega.data.EntityFinder;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLegacyEntityComparator;
import com.idega.block.albumcollection.data.*;
import com.idega.util.IWTimestamp;
import com.idega.presentation.text.Text;
import com.idega.presentation.text.Link;

import java.util.List;
import java.util.Iterator;
import java.sql.SQLException;
import java.util.Map;
import java.util.Hashtable;
import java.util.Collections;


/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class AlbumCollectionBusiness {

  public final static String _PRM_ALBUM_ID = "ac_album_id";
  public final static String _PRM_TRACK_ID = "ac_track_id";
  public final static String _PRM_LYRIC_ID = "ac_lyric_id";
  public final static String _PRM_UPDATE = "ac_update";
  public final static String _PRM_DELETE = "ac_delete";
  public final static int _CONST_ALBUM = 0;
  public final static int _CONST_TRACK = 1;
  public final static int _CONST_LYRIC = 2;


  private static Text header;
  private static Text text;
  private static Text boldText;
  private static Link link;

  static{
    header = new Text();
    header.setFontColor("#FFFFFF");
    header.setFontFace(Text.FONT_FACE_ARIAL);
    header.setFontSize(Text.FONT_SIZE_14_HTML_4);
    text = new Text();
    text.setFontColor("#FFFFFF");
    text.setFontFace(Text.FONT_FACE_ARIAL);
    text.setFontSize(Text.FONT_SIZE_10_HTML_2);
    boldText = new Text();
    boldText.setFontColor("#FFFFFF");
    boldText.setFontFace(Text.FONT_FACE_ARIAL);
    boldText.setFontSize(Text.FONT_SIZE_10_HTML_2);
    boldText.setBold();
    link = new Link();
    link.setFontColor("#FFFFFF");
    link.setFontFace(Text.FONT_FACE_ARIAL);
    link.setFontSize(Text.FONT_SIZE_10_HTML_2);


  }

  public AlbumCollectionBusiness() {
  }

  public static Text getHeaderTextClone(){
    if(header == null){
      header = new Text();
      header.setFontColor("#FFFFFF");
      header.setFontFace(Text.FONT_FACE_ARIAL);
      header.setFontSize(Text.FONT_SIZE_14_HTML_4);
    }
    return (Text)header.clone();
  }

  public static Text getMainTextClone(){
    if(text == null){
      text = new Text();
      text.setFontColor("#FFFFFF");
      text.setFontFace(Text.FONT_FACE_ARIAL);
      text.setFontSize(Text.FONT_SIZE_10_HTML_2);
    }
    return (Text)text.clone();
  }

  public static Text getMainTextBoldClone(){
    if(boldText == null){
      boldText = new Text();
      boldText.setFontColor("#FFFFFF");
      boldText.setFontFace(Text.FONT_FACE_ARIAL);
      boldText.setFontSize(Text.FONT_SIZE_10_HTML_2);
      boldText.setBold();
    }
    return (Text)boldText.clone();
  }

  public static Link getMainLinkClone(){
    if(link == null){
      link = new Link();
      link.setFontColor("#FFFFFF");
      link.setFontFace(Text.FONT_FACE_ARIAL);
      link.setFontSize(Text.FONT_SIZE_10_HTML_2);
    }
    return (Link)link.clone();
  }



  public static Text getHeaderTextClone(String text){
    Text t = getHeaderTextClone();
    t.setText(text);
    return t;
  }

  public static Text getMainTextClone(String text){
    Text t = getMainTextClone();
    t.setText(text);
    return t;
  }

  public static Text getMainTextBoldClone(String text){
    Text t = getMainTextBoldClone();
    t.setText(text);
    return t;
  }

  public static Link getMainLinkClone(String text){
    Link l = getMainLinkClone();
    l.setText(text);
    return l;
  }






  public static List getAuthors() throws SQLException{
    return EntityFinder.findAll(GenericEntity.getStaticInstance(Author.class));
  }

  public static List getPerformers() throws SQLException{
    return EntityFinder.findAll(GenericEntity.getStaticInstance(Performer.class));
  }

  public static List getCategories() throws SQLException{
    return EntityFinder.findAll(GenericEntity.getStaticInstance(Category.class));
  }

  public static List getAlbumTypes() throws SQLException{
    return EntityFinder.findAll(GenericEntity.getStaticInstance(AlbumType.class));
  }

  public static Map getAlbumTypeNames() throws SQLException {
    List types = getAlbumTypes();
    if(types != null){
      Map m = new Hashtable();
      Iterator iter = types.iterator();
      while (iter.hasNext()) {
        AlbumType item = (AlbumType)iter.next();
        m.put(new Integer(item.getID()),item.getName());
      }
      return m;
    } else{
      return null;
    }
  }

  public static List getAlbums() throws SQLException{
    return EntityFinder.findAllDescendingOrdered(GenericEntity.getStaticInstance(Album.class),com.idega.block.albumcollection.data.AlbumBMPBean._COLUMNNAME_PUBLISHINGDAY);
  }

  public static void createAlbum(String name, String description,Integer albumType, IWTimestamp publishingDay, int[] authors, int[] performers, int[] categories, Integer frontCoverId) throws SQLException {
    Album album = ((com.idega.block.albumcollection.data.AlbumHome)com.idega.data.IDOLookup.getHomeLegacy(Album.class)).createLegacy();

    if( name != null){
      album.setName(name);
    }

    if(description != null){
      album.setDescription(description);
    }

    if( albumType != null){
      album.setAlbumTypeId(albumType);
    }

    if( publishingDay != null){
      album.setPublishingDay(publishingDay.getSQLDate());
    }

    if(frontCoverId != null){
      album.setFrontCoverFileId(frontCoverId.intValue());
    }

    album.insert();

    if( authors != null){
      for (int i = 0; i < authors.length; i++) {
        album.addTo(Author.class,authors[i]);
      }
    }

    if( performers != null){
      for (int i = 0; i < performers.length; i++) {
        album.addTo(Performer.class,performers[i]);
      }
    }

    if( categories != null){
      for (int i = 0; i < categories.length; i++) {
        album.addTo(Category.class,categories[i]);
      }
    }

  }

  public static void updateAlbum(int albumId,String name, String description,Integer albumType, IWTimestamp publishingDay, int[] authors, int[] performers, int[] categories, Integer frontCoverId) throws SQLException {
    Album album = ((com.idega.block.albumcollection.data.AlbumHome)com.idega.data.IDOLookup.getHomeLegacy(Album.class)).findByPrimaryKeyLegacy(albumId);

    if( name != null){
      album.setName(name);
    }

    if(description != null){
      album.setDescription(description);
    }

    if( albumType != null){
      album.setAlbumTypeId(albumType);
    }

    if( publishingDay != null){
      album.setPublishingDay(publishingDay.getSQLDate());
    }

    if(frontCoverId != null){
      album.setFrontCoverFileId(frontCoverId.intValue());
    } else {
      album.setFrontCoverFileIdAsNull();
    }

    album.update();

    album.removeFrom(GenericEntity.getStaticInstance(Author.class));

    if( authors != null){
      for (int i = 0; i < authors.length; i++) {
        album.addTo(Author.class,authors[i]);
      }
    }

    album.removeFrom(GenericEntity.getStaticInstance(Performer.class));
    if( performers != null){
      for (int i = 0; i < performers.length; i++) {
        album.addTo(Performer.class,performers[i]);
      }
    }

    album.removeFrom(GenericEntity.getStaticInstance(Category.class));
    if( categories != null){
      for (int i = 0; i < categories.length; i++) {
        album.addTo(Category.class,categories[i]);
      }
    }

  }

  public static void addTrack(String name, String description, Integer number, Integer albumId, Integer lyricId, Integer lengthInSek, Integer audioID, boolean audioIsHidden, int[] authors, int[] performers, int[] categories) throws SQLException {
    Track track = ((com.idega.block.albumcollection.data.TrackHome)com.idega.data.IDOLookup.getHomeLegacy(Track.class)).createLegacy();

    if( name != null){
      track.setName(name);
    }

    if(description != null){
      track.setDescription(description);
    }

    if( number != null){
      track.setNumber(number.intValue());
    }

    if( albumId != null){
      track.setAlbumId(albumId.intValue());
    }

    if( lyricId != null){
      track.setLyricId(lyricId.intValue());
    }

    if(lengthInSek != null){
      track.setLength(lengthInSek.intValue());
    }
    
    if(audioID!=null){
    	track.setTrackID(audioID);
    }
    
    track.setAudoTrackHidden(audioIsHidden);

    track.insert();

    if( authors != null){
      for (int i = 0; i < authors.length; i++) {
        track.addTo(Author.class,authors[i]);
      }
    }

    if( performers != null){
      for (int i = 0; i < performers.length; i++) {
        track.addTo(Performer.class,performers[i]);
      }
    }

    if( categories != null){
      for (int i = 0; i < categories.length; i++) {
        track.addTo(Category.class,categories[i]);
      }
    }

  }

  public static void updateTrack(int trackId, String name, String description, Integer number, Integer albumId, Integer lyricId, Integer lengthInSek, Integer audioID, boolean audioIsHidden, int[] authors, int[] performers, int[] categories) throws SQLException {
    Track track = ((com.idega.block.albumcollection.data.TrackHome)com.idega.data.IDOLookup.getHomeLegacy(Track.class)).findByPrimaryKeyLegacy(trackId);

    if( name != null){
      track.setName(name);
    }

    if(description != null){
      track.setDescription(description);
    }

    if( number != null){
      track.setNumber(number.intValue());
    }

    if( albumId != null){
      track.setAlbumId(albumId.intValue());
    }

    if( lyricId != null){
      track.setLyricId(lyricId.intValue());
    }

    if(lengthInSek != null){
      track.setLength(lengthInSek.intValue());
    }
    
	if(audioID!=null){
		track.setTrackID(audioID);
	}
	
	track.setAudoTrackHidden(audioIsHidden);

    track.update();


    track.removeFrom(GenericEntity.getStaticInstance(Author.class));
    if( authors != null){
      for (int i = 0; i < authors.length; i++) {
        track.addTo(Author.class,authors[i]);
      }
    }

    track.removeFrom(GenericEntity.getStaticInstance(Performer.class));
    if( performers != null){
      for (int i = 0; i < performers.length; i++) {
        track.addTo(Performer.class,performers[i]);
      }
    }

    track.removeFrom(GenericEntity.getStaticInstance(Category.class));
    if( categories != null){
      for (int i = 0; i < categories.length; i++) {
        track.addTo(Category.class,categories[i]);
      }
    }

  }

  public static void addLyric(String name, String description, String lyric, Integer trackId, int[] authors) throws SQLException {
    Lyric acLyric = ((com.idega.block.albumcollection.data.LyricHome)com.idega.data.IDOLookup.getHomeLegacy(Lyric.class)).createLegacy();

    if( name != null){
      acLyric.setName(name);
    }

    if(description != null){
      acLyric.setDescription(description);
    }

    if(lyric != null){
      acLyric.setLyric(lyric);
    }

    acLyric.insert();

    if( trackId != null){
      Track track = ((com.idega.block.albumcollection.data.TrackHome)com.idega.data.IDOLookup.getHomeLegacy(Track.class)).findByPrimaryKeyLegacy(trackId.intValue());
      track.setLyricId(acLyric.getID());
      track.update();
    }

    if( authors != null){
      for (int i = 0; i < authors.length; i++) {
        acLyric.addTo(Author.class,authors[i]);
      }
    }


  }

  public static void updateLyric(int lyricId, String name, String description, String lyric, int[] authors) throws SQLException {
    Lyric acLyric = ((com.idega.block.albumcollection.data.LyricHome)com.idega.data.IDOLookup.getHomeLegacy(Lyric.class)).findByPrimaryKeyLegacy(lyricId);

    if( name != null){
      acLyric.setName(name);
    }

    if(description != null){
      acLyric.setDescription(description);
    }

    if(lyric != null){
      acLyric.setLyric(lyric);
    }

    acLyric.update();

    acLyric.removeFrom(GenericEntity.getStaticInstance(Author.class));
    if( authors != null){
      for (int i = 0; i < authors.length; i++) {
        acLyric.addTo(Author.class,authors[i]);
      }
    }


  }


  public static void addAuthor(String name, String displayName) throws SQLException {
    Author author = ((com.idega.block.albumcollection.data.AuthorHome)com.idega.data.IDOLookup.getHomeLegacy(Author.class)).createLegacy();

    if( name != null){
      author.setName(name);
    }

    if(displayName != null){
      author.setDisplayName(displayName);
    }


    author.insert();

  }

  public static void addPerformer(String name, String displayName) throws SQLException {
    Performer performer = ((com.idega.block.albumcollection.data.PerformerHome)com.idega.data.IDOLookup.getHomeLegacy(Performer.class)).createLegacy();

    if( name != null){
      performer.setName(name);
    }

    if(displayName != null){
      performer.setDisplayName(displayName);
    }


    performer.insert();

  }

  public static Track getTrack(int trackId) {
    try {
	  return ((com.idega.block.albumcollection.data.TrackHome)com.idega.data.IDOLookup.getHomeLegacy(Track.class)).findByPrimaryKeyLegacy(trackId);
    }
    catch (SQLException ex) {
	  return null;
    }
  }

  public static List getTracks(int albumId) throws SQLException {
    return EntityFinder.findAllByColumnOrdered(GenericEntity.getStaticInstance(Track.class),com.idega.block.albumcollection.data.TrackBMPBean._COLUMNNAME_ALBUM_ID,Integer.toString(albumId), com.idega.block.albumcollection.data.TrackBMPBean._COLUMNNAME_NUMBER);
  }

  public static List getLyrics() throws SQLException {
    List l = EntityFinder.findAllOrdered(GenericEntity.getStaticInstance(Lyric.class),com.idega.block.albumcollection.data.LyricBMPBean._COLUMNNAME_NAME);
    if(l != null){
      Collections.sort(l,new IDOLegacyEntityComparator(com.idega.block.albumcollection.data.LyricBMPBean._COLUMNNAME_NAME));
    }
    return l;
  }

  public static Album getAlbum(int albumId) {
    try {
      return ((com.idega.block.albumcollection.data.AlbumHome)com.idega.data.IDOLookup.getHomeLegacy(Album.class)).findByPrimaryKeyLegacy(albumId);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static Lyric getLyric(int lyricId) {
    try {
      return ((com.idega.block.albumcollection.data.LyricHome)com.idega.data.IDOLookup.getHomeLegacy(Lyric.class)).findByPrimaryKeyLegacy(lyricId);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List getTracksRelatedToLyric(int lyricId){
    try {
      return EntityFinder.findAllByColumn(GenericEntity.getStaticInstance(Track.class),com.idega.block.albumcollection.data.TrackBMPBean._COLUMNNAME_LYRIC_ID,lyricId);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static void deleteAlbum(int albumId) {
    try {
      Album album = ((com.idega.block.albumcollection.data.AlbumHome)com.idega.data.IDOLookup.getHomeLegacy(Album.class)).findByPrimaryKeyLegacy(albumId);

      album.removeFrom(GenericEntity.getStaticInstance(Author.class));
      album.removeFrom(GenericEntity.getStaticInstance(Performer.class));
      album.removeFrom(GenericEntity.getStaticInstance(Category.class));

      List tracks = getTracks(album.getID());
      if(tracks != null){
        Iterator iter = tracks.iterator();
        while (iter.hasNext()) {
          Track item = (Track)iter.next();
          deleteTrack(item);
          /**
           * or
           * item.setAlbumIdAsNull();
           * item.update();
           */
        }
      }

      album.delete();

    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public static void deleteLyric(int lyricId) {
    try {
      Lyric lyric = ((com.idega.block.albumcollection.data.LyricHome)com.idega.data.IDOLookup.getHomeLegacy(Lyric.class)).findByPrimaryKeyLegacy(lyricId);

      lyric.removeFrom(GenericEntity.getStaticInstance(Author.class));

      List tracks = getTracksRelatedToLyric(lyric.getID());
      if(tracks != null){
        Iterator iter = tracks.iterator();
        while (iter.hasNext()) {
          Track item = (Track)iter.next();
          item.setLyricIdAsNull();
        }
      }

      lyric.delete();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public static void deleteTrack(int trackId) {
    try {
      Track track = ((com.idega.block.albumcollection.data.TrackHome)com.idega.data.IDOLookup.getHomeLegacy(Track.class)).findByPrimaryKeyLegacy(trackId);
      deleteTrack(track);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public static void deleteTrack(Track track) {
    try {

      track.removeFrom(GenericEntity.getStaticInstance(Author.class));
      track.removeFrom(GenericEntity.getStaticInstance(Performer.class));
      track.removeFrom(GenericEntity.getStaticInstance(Category.class));

      track.delete();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
  }


}
