package com.idega.block.albumcollection.presentation;

import java.util.Iterator;
import java.util.List;

import com.idega.block.albumcollection.business.AlbumCollectionBusiness;
import com.idega.block.albumcollection.data.Lyric;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class LyricList extends Block {

  private final static String IW_BUNDLE_IDENTIFIER = AlbumCollection.IW_BUNDLE_IDENTIFIER;

  private Link lyricViewerLinkTemplate;
  private Link updateLyricLinkTemplate;
  private Link deleteLyricLinkTemplate;

  public LyricList() {
    this.lyricViewerLinkTemplate = AlbumCollectionBusiness.getMainLinkClone();
    this.lyricViewerLinkTemplate.addParameter(AlbumCollection._PRM_STATE,AlbumCollection._STATE_LYRIC);

    this.updateLyricLinkTemplate = AlbumCollectionBusiness.getMainLinkClone("U");
    this.updateLyricLinkTemplate.setWindowToOpen(InsertLyric.class);
    this.updateLyricLinkTemplate.addParameter(AlbumCollectionBusiness._PRM_UPDATE,"true");

    this.deleteLyricLinkTemplate = AlbumCollectionBusiness.getMainLinkClone("D");
    this.deleteLyricLinkTemplate.setWindowToOpen(DeleteConfirmWindow.class);
    this.deleteLyricLinkTemplate.addParameter(AlbumCollectionBusiness._PRM_DELETE,AlbumCollectionBusiness._CONST_LYRIC);

  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc) throws Exception {

    Table frameTable = new Table(1,1);
    frameTable.setCellspacing(1);
    frameTable.setCellpadding(0);
    frameTable.setColor(AlbumCollection._COLOR_BRIGHTEST);
    frameTable.setColor(1,1,AlbumCollection._COLOR_BRIGHT);
    frameTable.setWidth("550");
    frameTable.setAlignment(1,1,"center");


    frameTable.add(Text.getBreak(),1,1);
    frameTable.add(this.getLyricList(iwc),1,1);


    this.add(Text.getBreak());
    this.add(Text.getBreak());
    this.add(frameTable);
    this.add(Text.getBreak());
    this.add(Text.getBreak());

  }


  public Table getLyricList(IWContext iwc)throws Exception {

    IWBundle core = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);

    Table lyricTable = null;
    int index=1;
    List lyrics = AlbumCollectionBusiness.getLyrics();
    if(lyrics != null && lyrics.size() > 0){

      lyricTable = new Table(1,lyrics.size()+2);
      lyricTable.setWidth("550");
      lyricTable.setCellspacing(2);
      lyricTable.setCellpadding(0);

      Table lyricInfo = new Table(7,1);
      lyricInfo.setCellpadding(1);
      lyricInfo.setCellspacing(0);
      lyricInfo.setWidth(1,"5");
      lyricInfo.setWidth(2,"250");
      lyricInfo.setWidth(3,"120");
      lyricInfo.setWidth(4,"120");
      lyricInfo.setWidth(5,"16");
      lyricInfo.setWidth(6,"16");
      lyricInfo.setWidth(7,"16");


      lyricInfo.setColumnAlignment(1,"center");
      lyricInfo.setColumnAlignment(2,"left");
      lyricInfo.setColumnAlignment(3,"left");
      lyricInfo.setColumnAlignment(4,"left");
      lyricInfo.setColumnAlignment(5,"center");
      lyricInfo.setColumnAlignment(6,"center");
      lyricInfo.setColumnAlignment(7,"center");

      lyricInfo.setRowVerticalAlignment(1,"top");


      Table info = (Table)lyricInfo.clone();
      //info.add(AlbumCollectionBusiness.getMainTextClone("nr."),1,1);
      info.add(AlbumCollectionBusiness.getMainTextClone("heiti"),2,1);
      //info.add(AlbumCollectionBusiness.getMainTextClone("h�fundar"),3,1);
      //info.add(AlbumCollectionBusiness.getMainTextClone("flutt �"),4,1);
      lyricTable.add(info,1,index);
      index++;
      Iterator iter = lyrics.iterator();
      while (iter.hasNext()) {
        Lyric item = (Lyric)iter.next();

        lyricTable.setRowColor(index,AlbumCollection._COLOR_DARK);

        info = (Table)lyricInfo.clone();


        Link link = (Link)this.lyricViewerLinkTemplate.clone();
        link.setText(item.getName());
        link.addParameter(AlbumCollectionBusiness._PRM_LYRIC_ID,item.getID());
        link.setBold();

        info.add(link,2,1);


/*
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
          info.add(AlbumCollectionBusiness.getMainTextBoldClone(name),3,1);
        }
*/




        if(hasEditPermission()){
          Link update = (Link)this.updateLyricLinkTemplate.clone();
          update.setObject(core.getSharedImage("edit.gif","edit lyric"));
          update.addParameter(AlbumCollectionBusiness._PRM_LYRIC_ID,item.getID());
          info.add(update,6,1);

          Link delete = (Link)this.deleteLyricLinkTemplate.clone();
          delete.setObject(core.getSharedImage("delete.gif","delete lyric"));
          delete.addParameter(DeleteConfirmWindow._PRM_ID,item.getID());
          info.add(delete,7,1);
        }


        lyricTable.add(info,1,index);
        index++;
      }
    } else {
      lyricTable = new Table(1,2);
      lyricTable.add(AlbumCollectionBusiness.getMainTextClone("Engir textar fundust"),1,index++);
    }


    if(hasEditPermission()){
      /*Link addTrackLink = AlbumCollectionBusiness.getMainLinkClone("add track");
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

      */
      //lyricTable.add(AlbumCollectionBusiness.getMainTextClone("Engir textar fundust"),1,index);
    }


    return lyricTable;
  }


}
