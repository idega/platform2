package com.idega.block.albumcollection.presentation;

import com.idega.presentation.Block;
import com.idega.block.albumcollection.business.AlbumCollectionBusiness;
import com.idega.block.albumcollection.data.Lyric;
import com.idega.presentation.text.Link;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;

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

public class LyricList extends Block {

  private Link lyricViewerLinkTemplate;

  public LyricList() {
    lyricViewerLinkTemplate = AlbumCollectionBusiness.getMainLinkClone();
    lyricViewerLinkTemplate.addParameter(AlbumCollection._PRM_STATE,AlbumCollection._STATE_LYRIC);
  }

  public void main(IWContext iwc) throws Exception {
    List lyrics = AlbumCollectionBusiness.getLyrics();
    if(lyrics != null && lyrics.size() > 0){
      Table contentTable = new Table(1,lyrics.size()+1);
      int index = 1;
      Text t = AlbumCollectionBusiness.getMainTextClone("Titill");
      //t.setFontColor("#FFFFFF");
      contentTable.add(t,1,index++);
      Iterator iter = lyrics.iterator();
      while (iter.hasNext()) {
        Lyric item = (Lyric)iter.next();
        Link link = (Link)lyricViewerLinkTemplate.clone();
        link.setText(item.getName());
        link.addParameter(AlbumCollectionBusiness._PRM_LYRIC_ID,item.getID());
        contentTable.add(link,1,index++);
      }
      this.add(contentTable);
    } else {
      this.add(AlbumCollectionBusiness.getMainTextClone("Engir textar fundust"));
    }

  }

}