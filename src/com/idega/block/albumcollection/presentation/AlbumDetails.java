package com.idega.block.albumcollection.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.text.Text;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.presentation.Table;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.block.albumcollection.business.AlbumCollectionBusiness;
import com.idega.block.albumcollection.data.Album;
import com.idega.block.albumcollection.data.Track;
import com.idega.block.albumcollection.data.Author;
import com.idega.block.albumcollection.data.Performer;

import java.util.List;
import java.util.Iterator;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class AlbumDetails extends Block {

  public final static String _PRM_ALBUM_ID = AlbumCollectionBusiness._PRM_ALBUM_ID;
  private Text trackNameTemplate;
  private Text trackNumberTemplate;

  public AlbumDetails() {
    trackNameTemplate = new Text();
    trackNumberTemplate= new Text();

  }

  public void lineUpElements(IWContext iwc)throws Exception {
    Table contentTable = new Table(1,2);
    String albumId = iwc.getParameter(this._PRM_ALBUM_ID);
    if(albumId != null && !"".equals(albumId)){
      Album album = AlbumCollectionBusiness.getAlbum(Integer.parseInt(albumId));
      if(album != null){
        contentTable.add(album.getName(),1,1);
        contentTable.add(Text.getBreak(),1,1);
        int imageId = album.getFrontCoverFileId();
        if(imageId > 0){
          Image fCover = new Image(imageId,album.getName());
          contentTable.add(fCover,1,1);
        }
        contentTable.add(Text.getBreak(),1,1);
        contentTable.add(album.getDescription(),1,1);
      }
    }

    contentTable.add(getTrackList(iwc),1,2);

    this.add(contentTable);
  }

  public Table getTrackList(IWContext iwc)throws Exception {
    Table trackTable = null;
    String albumId = iwc.getParameter(this._PRM_ALBUM_ID);
    if(albumId != null && !"".equals(albumId)){
      List tracks = AlbumCollectionBusiness.getTracks(Integer.parseInt(albumId));
      if(tracks != null && tracks.size() > 0){
        trackTable = new Table(2,tracks.size()+1);
        int index=1;
        trackTable.add(new Text("Númer"),1,index);
        trackTable.add(new Text("Heiti"),2,index++);
        Iterator iter = tracks.iterator();
        while (iter.hasNext()) {
          Track item = (Track)iter.next();

          if(item.getNumber() > 0){
            Text trackNumber = (Text)trackNumberTemplate.clone();
            trackNumber.setText(Integer.toString(item.getNumber()));
            trackTable.add(trackNumber,1,index);
          }

          Text trackName = (Text)trackNameTemplate.clone();
          trackName.setText(item.getName());
          trackTable.add(trackName,2,index);

          index++;
        }
      }

    }


    return trackTable;
  }

  public void main(IWContext iwc) throws Exception {

    lineUpElements(iwc);

    Link addTrackLink = new Link("add track");
    //addTrackLink.setFontColor("#EEEEEE");
    addTrackLink.setBold();
    addTrackLink.setWindowToOpen(com.idega.block.albumcollection.presentation.AddTrack.class);
    String albumId = iwc.getParameter(_PRM_ALBUM_ID);
    if(albumId != null && !albumId.equals("")){
      addTrackLink.addParameter(_PRM_ALBUM_ID,albumId);
    }

    this.add(addTrackLink);

  }
}