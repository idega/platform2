package com.idega.block.albumcollection.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.block.albumcollection.business.AlbumCollectionBusiness;
import com.idega.presentation.text.Text;
import com.idega.block.albumcollection.data.Lyric;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class LyricViewer extends Block {

  private Text headingText;
  private Text mainText;

  public LyricViewer() {
    headingText = new Text();
    headingText.setFontSize(3);
    headingText.setBold();

    mainText = new Text();

  }

  public void main(IWContext iwc) throws Exception {
    //this.add("lyric - "+iwc.getParameter(AlbumCollectionBusiness._PRM_LYRIC_ID));
    String lyricId = iwc.getParameter(AlbumCollectionBusiness._PRM_LYRIC_ID);
    if(lyricId != null){
      Lyric lyric = AlbumCollectionBusiness.getLyric(Integer.parseInt(lyricId));
      if(lyric != null){
        Text heading = (Text)headingText.clone();
        heading.setText(lyric.getName());

        Text text = (Text)mainText.clone();
        text.setText(lyric.getLyric());

        this.add(heading);
        this.add(Text.getBreak());
        this.add(text);
      } else {
        this.add("Texti finnst ekki");
      }
    } else {
      this.add("Texti finnst ekki");
    }

  }
}