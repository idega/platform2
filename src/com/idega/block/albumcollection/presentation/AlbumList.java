package com.idega.block.albumcollection.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.Table;
import com.idega.block.albumcollection.business.AlbumCollectionBusiness;
import com.idega.block.albumcollection.data.Album;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.IWContext;

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

public class AlbumList extends Block {

  Link albumLinkTemplate;
  Link updateAlbumLinkTemplate;
  Link deleteAlbumLinkTemplate;

  public AlbumList() {
    albumLinkTemplate = new Link();
    //albumLinkTemplate.setFontColor("#FFFFFF");
    updateAlbumLinkTemplate = new Link("Breyta");
    updateAlbumLinkTemplate.setWindowToOpen(com.idega.block.albumcollection.presentation.CreateAlbum.class);
    updateAlbumLinkTemplate.addParameter(AlbumCollectionBusiness._PRM_UPDATE,"true");

    deleteAlbumLinkTemplate = new Link("Eyða");
    deleteAlbumLinkTemplate.setWindowToOpen(DeleteConfirmWindow.class);
    deleteAlbumLinkTemplate.addParameter(AlbumCollectionBusiness._PRM_DELETE,AlbumCollectionBusiness._CONST_ALBUM);
  }


  public void main(IWContext iwc) throws Exception {
    List albumList = AlbumCollectionBusiness.getAlbums();
    this.empty();
    if(albumList != null){
      Table contentTable = new Table(3,albumList.size()+1);
      int index = 1;
      Text t = new Text("Titill");
      //t.setFontColor("#FFFFFF");
      contentTable.add(t,1,index++);
      Iterator iter = albumList.iterator();
      while (iter.hasNext()) {
        Album item = (Album)iter.next();
        Link link = (Link)albumLinkTemplate.clone();
        link.setText(item.getName());
        link.addParameter(AlbumCollectionBusiness._PRM_ALBUM_ID,item.getID());
        link.addParameter(AlbumCollection._PRM_STATE,AlbumCollection._STATE_ALBUMINFO);
        contentTable.add(link,1,index);

        if(hasEditPermission()){
          Link update = (Link)updateAlbumLinkTemplate.clone();
          update.addParameter(AlbumCollectionBusiness._PRM_ALBUM_ID,item.getID());
          contentTable.add(update,2,index);

          Link delete = (Link)deleteAlbumLinkTemplate.clone();
          delete.addParameter(DeleteConfirmWindow._PRM_ID,item.getID());
          contentTable.add(delete,3,index);
        }

        index++;
      }
      this.add(contentTable);
    }

    if(hasEditPermission()){
      Link createAlbumLink = new Link("create album");
      //createAlbumLink.setFontColor("#EEEEEE");
      createAlbumLink.setBold();
      createAlbumLink.setWindowToOpen(com.idega.block.albumcollection.presentation.CreateAlbum.class);
      this.add(createAlbumLink);
    }

  }
}