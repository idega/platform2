package com.idega.block.albumcollection.business;

import com.idega.data.EntityFinder;
import com.idega.block.albumcollection.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.util.text.TextSoap;

import java.util.List;
import java.sql.SQLException;


/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class AlbumCollectionBusiness {

  public final static String _PRM_ALBUM_ID = "ac_album_id";
  public final static String _PRM_TRACK_ID = "ac_track_id";
  public final static String _PRM_LYRIC_ID = "ac_lyric_id";

  public AlbumCollectionBusiness() {
  }


  public static List getAuthors() throws SQLException{
    return EntityFinder.findAll(Author.getStaticInstance(Author.class));
  }

  public static List getPerformers() throws SQLException{
    return EntityFinder.findAll(Performer.getStaticInstance(Performer.class));
  }

  public static List getCategories() throws SQLException{
    return EntityFinder.findAll(Category.getStaticInstance(Category.class));
  }

  public static List getAlbumTypes() throws SQLException{
    return EntityFinder.findAll(AlbumType.getStaticInstance(AlbumType.class));
  }

  public static List getAlbums() throws SQLException{
    return EntityFinder.findAll(Album.getStaticInstance(Album.class));
  }

  public static void createAlbum(String name, String description,Integer albumType, idegaTimestamp publishingDay, int[] authors, int[] performers, int[] categories, Integer frontCoverId) throws SQLException {
    Album album = new Album();

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

  public static void addTrack(String name, String description, Integer number, Integer albumId, Integer lyricId, Integer lengthInSek, int[] authors, int[] performers, int[] categories) throws SQLException {
    Track track = new Track();

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


  public static void addLyric(String name, String description, String lyric, Integer trackId, int[] authors) throws SQLException {
    Lyric acLyric = new Lyric();

    if( name != null){
      acLyric.setName(name);
    }

    if(description != null){
      acLyric.setDescription(description);
    }

    if(lyric != null){
      acLyric.setLyric(TextSoap.formatText(lyric));
    }

    acLyric.insert();

    if( trackId != null){
      Track track = new Track(trackId.intValue());
      track.setLyricId(acLyric.getID());
      track.update();
    }

    if( authors != null){
      for (int i = 0; i < authors.length; i++) {
        acLyric.addTo(Author.class,authors[i]);
      }
    }


  }

  public static void addAuthor(String name, String displayName) throws SQLException {
    Author author = new Author();

    if( name != null){
      author.setName(name);
    }

    if(displayName != null){
      author.setDisplayName(displayName);
    }


    author.insert();

  }


  public static List getTracks(int albumId) throws SQLException {
    return EntityFinder.findAllByColumnOrdered(Track.getStaticInstance(Track.class),Track._COLUMNNAME_ALBUM_ID,Integer.toString(albumId), Track._COLUMNNAME_NUMBER);
  }

  public static List getLyrics() throws SQLException {
    return EntityFinder.findAll(Lyric.getStaticInstance(Lyric.class));
  }

  public static Album getAlbum(int albumId) {
    try {
      return new Album(albumId);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static Lyric getLyric(int lyricId) {
    try {
      return new Lyric(lyricId);
    }
    catch (SQLException ex) {
      return null;
    }
  }

}