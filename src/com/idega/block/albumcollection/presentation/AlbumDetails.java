package com.idega.block.albumcollection.presentation;

import java.util.Iterator;
import java.util.List;

import com.idega.block.albumcollection.business.AlbumCollectionBusiness;
import com.idega.block.albumcollection.data.Album;
import com.idega.block.albumcollection.data.AlbumType;
import com.idega.block.albumcollection.data.Author;
import com.idega.block.albumcollection.data.Lyric;
import com.idega.block.albumcollection.data.Performer;
import com.idega.block.albumcollection.data.Track;
import com.idega.data.EntityFinder;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class AlbumDetails extends Block {

  private final static String IW_BUNDLE_IDENTIFIER = AlbumCollection.IW_BUNDLE_IDENTIFIER;

  public final static String _PRM_ALBUM_ID = AlbumCollectionBusiness._PRM_ALBUM_ID;
  private Text trackNameTemplate;
  private Text trackNumberTemplate;
  private Link setLyricLinkTemplate;
  private Link lyricViewerLinkTemplate;
  private Link updateTrackLinkTemplate;
  private Link deleteTrackLinkTemplate;

  public AlbumDetails() {
    trackNameTemplate = AlbumCollectionBusiness.getMainTextBoldClone();
    trackNumberTemplate= AlbumCollectionBusiness.getMainTextBoldClone();

    setLyricLinkTemplate = AlbumCollectionBusiness.getMainLinkClone();
    setLyricLinkTemplate.setWindowToOpen(InsertLyric.class);

    lyricViewerLinkTemplate = AlbumCollectionBusiness.getMainLinkClone();
    lyricViewerLinkTemplate.addParameter(AlbumCollection._PRM_STATE,AlbumCollection._STATE_LYRIC);

    updateTrackLinkTemplate = AlbumCollectionBusiness.getMainLinkClone("U");
    updateTrackLinkTemplate.setWindowToOpen(AddTrack.class);
    updateTrackLinkTemplate.addParameter(AlbumCollectionBusiness._PRM_UPDATE,"true");

    deleteTrackLinkTemplate = AlbumCollectionBusiness.getMainLinkClone("D");
    deleteTrackLinkTemplate.setWindowToOpen(DeleteConfirmWindow.class);
    deleteTrackLinkTemplate.addParameter(AlbumCollectionBusiness._PRM_DELETE,AlbumCollectionBusiness._CONST_TRACK);


  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void lineUpElements(IWContext iwc)throws Exception {

    IWResourceBundle iwrb = this.getResourceBundle(iwc);

    Table frameTable = new Table(1,1);
    frameTable.setCellspacing(1);
    frameTable.setCellpadding(0);
    frameTable.setColor(AlbumCollection._COLOR_BRIGHTEST);
    frameTable.setColor(1,1,AlbumCollection._COLOR_BRIGHT);
    frameTable.setWidth("550");
    frameTable.setAlignment("center");


    Table contentTable = new Table(1,6);
    contentTable.setCellpadding(0);
    contentTable.setCellspacing(0);
    contentTable.setWidth("550");
    contentTable.setHeight(2,"10");
    contentTable.setHeight(3,"1");
    contentTable.setHeight(4,"10");
    contentTable.setRowAlignment(1,"center");
    contentTable.setVerticalAlignment(1,1,"bottom");
    contentTable.setHeight(1,"145");
    contentTable.setRowAlignment(3,"center");

    String albumId = iwc.getParameter(this._PRM_ALBUM_ID);
    if(albumId != null && !"".equals(albumId)){
      Album album = AlbumCollectionBusiness.getAlbum(Integer.parseInt(albumId));

      Table infoTable = new Table(2,1);
      infoTable.setVerticalAlignment(2,1,"bottom");
      infoTable.setWidth(1,1,"120");
      infoTable.setHeight(1,1,"120");
      //infoTable.setColor(1,1,AlbumCollection._COLOR_BRIGHTEST);


      if(album != null){
        int imageId = album.getFrontCoverFileId();
        if(imageId > 0){
          Image fCover = new Image(imageId,album.getName());
          fCover.setAlignment("left");
          fCover.setHeight(120);
          fCover.setWidth(120);
          infoTable.add(fCover,1,1);
        }

        Table albumInfoTable = new Table(2,4);
        albumInfoTable.setCellpadding(0);
        albumInfoTable.setCellspacing(3);
        albumInfoTable.setWidth(340);
        albumInfoTable.setWidth(1,1,"100");
        albumInfoTable.setWidth(2,1,"240");
        albumInfoTable.setHeight(90);
        albumInfoTable.setColumnAlignment(1,"right");
        albumInfoTable.setColumnAlignment(2,"left");
        albumInfoTable.setColumnColor(2,AlbumCollection._COLOR_DARK);

        albumInfoTable.add(AlbumCollectionBusiness.getMainTextClone("titill:"),1,1);
        albumInfoTable.add(AlbumCollectionBusiness.getMainTextClone("flytjendur:"),1,2);
        albumInfoTable.add(AlbumCollectionBusiness.getMainTextClone("útgáfuár:"),1,3);
        albumInfoTable.add(AlbumCollectionBusiness.getMainTextClone("tegund:"),1,4);

        for (int i = 0; i < 1; i++) {
          albumInfoTable.add(Text.getNonBrakingSpace(),2,1);
          albumInfoTable.add(Text.getNonBrakingSpace(),2,2);
          albumInfoTable.add(Text.getNonBrakingSpace(),2,3);
          albumInfoTable.add(Text.getNonBrakingSpace(),2,4);
        }

        albumInfoTable.add(AlbumCollectionBusiness.getMainTextBoldClone(album.getName()),2,1);

        List performers = EntityFinder.findRelated(album,com.idega.block.albumcollection.data.PerformerBMPBean.getStaticInstance(Performer.class));
        if(performers != null){
          Iterator iter2 = performers.iterator();
          boolean f = false;
          String name = "";
          while (iter2.hasNext()) {
            Performer performer = (Performer)iter2.next();
            if(f){
              name += ", ";
            }
            name += performer.getDisplayName();
            f=true;
          }
          albumInfoTable.add(AlbumCollectionBusiness.getMainTextBoldClone(name),2,2);
        }
        if(album.getPublishingDay() != null){
          albumInfoTable.add(AlbumCollectionBusiness.getMainTextBoldClone(Integer.toString(new IWTimestamp(album.getPublishingDay()).getYear())),2,3);
        }

        if(album.getAlbumTypeId() > 0){
          String type = null;
          try {
            if(album.getAlbumTypeId() > 0){
              type = ((com.idega.block.albumcollection.data.AlbumTypeHome)com.idega.data.IDOLookup.getHomeLegacy(AlbumType.class)).findByPrimaryKeyLegacy(album.getAlbumTypeId()).getName();
            }
          }
          catch (Exception ex) {
            ex.printStackTrace();
          }


          if(type != null){
            albumInfoTable.add(AlbumCollectionBusiness.getMainTextBoldClone(type),2,4);
          }
        }

        infoTable.add(albumInfoTable,2,1);

        contentTable.add(infoTable,1,1);

        String description = TextSoap.formatText(album.getDescription());
        if(description != null && !"".equals(description)){
          Table t = new Table();
          t.add(AlbumCollectionBusiness.getMainTextClone(description),1,1);
          t.setWidth(410);
          t.setCellpadding(8);
          contentTable.add(t,1,3);
        }
      }
    }

    contentTable.add(getTrackList(iwc),1,5);

    Table backTable = new Table(1,1);
    backTable.setCellpadding(4);
    backTable.setCellspacing(4);
    backTable.add(new BackButton(iwrb.getImage("back.gif","back")),1,1);
    contentTable.add(backTable,1,6);
    contentTable.setAlignment(1,6,"right");

    frameTable.add(contentTable);
    this.add(Text.getBreak());
    this.add(Text.getBreak());
    this.add(frameTable);
    this.add(Text.getBreak());
    this.add(Text.getBreak());
  }

  public Table getTrackList(IWContext iwc)throws Exception {

    IWBundle core = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);

    Table trackTable = null;
    int index=1;
    String albumId = iwc.getParameter(this._PRM_ALBUM_ID);
    if(albumId != null && !"".equals(albumId)){
      List tracks = AlbumCollectionBusiness.getTracks(Integer.parseInt(albumId));
      if(tracks != null && tracks.size() > 0){

        trackTable = new Table(1,tracks.size()+2);
        trackTable.setWidth("550");
        trackTable.setCellspacing(2);
        trackTable.setCellpadding(0);

        Table trackInfo = new Table(9,1);
        trackInfo.setCellpadding(1);
        trackInfo.setCellspacing(0);
        trackInfo.setWidth(1,"20");
        trackInfo.setWidth(2,"200");
        trackInfo.setWidth(3,"50");
        trackInfo.setWidth(4,"108");
        trackInfo.setWidth(5,"108");
        trackInfo.setWidth(6,"16");
        trackInfo.setWidth(7,"16");
        trackInfo.setWidth(8,"16");
        trackInfo.setWidth(9,"16");

        trackInfo.setColumnAlignment(1,"center");
        trackInfo.setColumnAlignment(2,"left");
        trackInfo.setColumnAlignment(3,"center");
        trackInfo.setColumnAlignment(4,"left");
        trackInfo.setColumnAlignment(5,"left");
        trackInfo.setColumnAlignment(6,"center");
        trackInfo.setColumnAlignment(7,"center");
        trackInfo.setColumnAlignment(8,"center");
        trackInfo.setColumnAlignment(9,"center");

        trackInfo.setRowVerticalAlignment(1,"top");


        Table info = (Table)trackInfo.clone();
        //info.add(AlbumCollectionBusiness.getMainTextClone("nr."),1,1);
        info.add(AlbumCollectionBusiness.getMainTextClone("heiti"),2,1);
        info.add(AlbumCollectionBusiness.getMainTextClone("lengd"),3,1);
        info.add(AlbumCollectionBusiness.getMainTextClone("lag"),4,1);
        info.add(AlbumCollectionBusiness.getMainTextClone("texti"),5,1);
        trackTable.add(info,1,index);
        index++;
        Iterator iter = tracks.iterator();
        while (iter.hasNext()) {
          Track item = (Track)iter.next();

          trackTable.setRowColor(index,AlbumCollection._COLOR_DARK);

          info = (Table)trackInfo.clone();

          if(item.getNumber() > 0){
            Text trackNumber = (Text)trackNumberTemplate.clone();
            trackNumber.setText(Integer.toString(item.getNumber()));
            info.add(trackNumber,1,1);
          }

          Text trackName = (Text)trackNameTemplate.clone();
          trackName.setText(item.getName());
          info.add(trackName,2,1);

          int length = item.getLength();
          if(length > 0){
            int m = length/60;
            int s = length%60;
            info.add(AlbumCollectionBusiness.getMainTextBoldClone(((m < 10)?TextSoap.addZero(m):Integer.toString(m))+":"+((s<10)?TextSoap.addZero(s):Integer.toString(s))),3,1);
          }



          List authors = EntityFinder.findRelated(item,com.idega.block.albumcollection.data.AuthorBMPBean.getStaticInstance(Author.class));
          if(authors != null){
            Iterator iter2 = authors.iterator();
            boolean f = false;
            String name = "";
            while (iter2.hasNext()) {
              Author author = (Author)iter2.next();
              if(f){
                name += ", ";
              }
              name += author.getDisplayName();
              f=true;
            }
            info.add(AlbumCollectionBusiness.getMainTextBoldClone(name),4,1);
          }





          if(hasEditPermission()){
            info.resize(info.getColumns(),2);
            Link update = (Link)updateTrackLinkTemplate.clone();
            update.setObject(core.getSharedImage("edit.gif","edit track"));
            update.addParameter(AlbumCollectionBusiness._PRM_TRACK_ID,item.getID());
            info.add(update,8,2);

            Link delete = (Link)deleteTrackLinkTemplate.clone();
            delete.setObject(core.getSharedImage("delete.gif","delete track"));
            delete.addParameter(DeleteConfirmWindow._PRM_ID,item.getID());
            info.add(delete,9,2);
          }


          if(item.getLyricId() < 0){
            if(hasEditPermission()){
              Link setLyric = (Link)setLyricLinkTemplate.clone();
              setLyric.setText("A");
              setLyric.addParameter(AlbumCollectionBusiness._PRM_TRACK_ID,item.getID());

              info.add(setLyric,8,1);
            }
          } else {
            Link setLyric = (Link)lyricViewerLinkTemplate.clone();
            setLyric.setText("T");
            setLyric.addParameter(AlbumCollectionBusiness._PRM_LYRIC_ID,item.getLyricId());
            info.add(setLyric,8,1);


            List T_authors = EntityFinder.findRelated(((com.idega.block.albumcollection.data.LyricHome)com.idega.data.IDOLookup.getHomeLegacy(Lyric.class)).findByPrimaryKeyLegacy(item.getLyricId()),com.idega.block.albumcollection.data.AuthorBMPBean.getStaticInstance(Author.class));
            if(T_authors != null){
              Iterator iter2 = T_authors.iterator();
              boolean f = false;
              String name2 = "";
              while (iter2.hasNext()) {
                Author T_author = (Author)iter2.next();
                if(f){
                  name2 += ", ";
                }
                name2 += T_author.getDisplayName();
                f=true;
              }
              info.add(AlbumCollectionBusiness.getMainTextBoldClone(name2),5,1);
            }
          }

          trackTable.add(info,1,index);
          index++;
        }
      }

    }


    if(hasEditPermission()){
      Link addTrackLink = AlbumCollectionBusiness.getMainLinkClone();
      addTrackLink.setObject(core.getSharedImage("create.gif", "add track"));
      //addTrackLink.setFontColor("#EEEEEE");
      addTrackLink.setBold();
      addTrackLink.setWindowToOpen(AddTrack.class);
      if(albumId != null && !albumId.equals("")){
        addTrackLink.addParameter(_PRM_ALBUM_ID,albumId);
      }

      if(trackTable == null){
        trackTable = new Table();
        trackTable.setWidth("550");
        trackTable.setCellspacing(2);
        trackTable.setCellpadding(0);
      }
      trackTable.add(addTrackLink,1,index);
    }


    return trackTable;
  }

  public void main(IWContext iwc) throws Exception {

    lineUpElements(iwc);


  }
}
