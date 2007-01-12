package com.idega.block.albumcollection.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.block.albumcollection.business.AlbumCollectionBusiness;
import com.idega.presentation.text.Text;
import com.idega.presentation.text.Link;
import com.idega.presentation.Table;
import com.idega.presentation.ui.BackButton;
import com.idega.block.albumcollection.data.Lyric;
import com.idega.block.albumcollection.data.Author;
import com.idega.block.albumcollection.data.Track;
import com.idega.data.EntityFinder;
import com.idega.data.GenericEntity;
import com.idega.util.text.TextSoap;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;


import java.util.Iterator;
import java.util.List;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class LyricViewer extends Block {

  private final static String IW_BUNDLE_IDENTIFIER = AlbumCollection.IW_BUNDLE_IDENTIFIER;

  private Text headingText;
  private Text mainText;

  public LyricViewer() {
    this.headingText = AlbumCollectionBusiness.getHeaderTextClone();

    this.mainText = AlbumCollectionBusiness.getMainTextClone();

  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc) throws Exception {


    IWBundle core = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
    IWResourceBundle iwrb = this.getResourceBundle(iwc);

    this.empty();

    Table frameTable = new Table(1,1);
    frameTable.setCellspacing(1);
    frameTable.setCellpadding(10);
    frameTable.setColor(AlbumCollection._COLOR_BRIGHTEST);
    frameTable.setColor(1,1,AlbumCollection._COLOR_BRIGHT);
    frameTable.setWidth("550");
    frameTable.setAlignment(1,1,"center");
    //frameTable.setBorder(1);

    Table contentTable = new Table(1,6);
    //contentTable.setAlignment(1,4,"right");
	contentTable.setRowHeight(4,"15");
	
    //contentTable.setBorder(1);


    frameTable.add(contentTable);

    //this.add("lyric - "+iwc.getParameter(AlbumCollectionBusiness._PRM_LYRIC_ID));
    String lyricId = iwc.getParameter(AlbumCollectionBusiness._PRM_LYRIC_ID);
    if(lyricId != null){
      Lyric lyric = AlbumCollectionBusiness.getLyric(Integer.parseInt(lyricId));
      if(lyric != null){
        Text heading = (Text)this.headingText.clone();
        heading.setText(lyric.getName());

        Text text = (Text)this.mainText.clone();
        text.setText(TextSoap.formatString(lyric.getLyric()));

        contentTable.add(heading,1,1);
        //contentTable.add(Text.getBreak());
        contentTable.add(text,1,3);
		
		// add authors - begins
		contentTable.add(new Text(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE+"( "),1,1);
        List T_authors = EntityFinder.findRelated(lyric,GenericEntity.getStaticInstance(Author.class));
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
          contentTable.add(new Text(" texti:"),1,1);
		  contentTable.add(AlbumCollectionBusiness.getMainTextClone(name2),1,1);
        }
        
		String trackId = iwc.getParameter(AlbumCollectionBusiness._PRM_TRACK_ID);
		if(trackId != null){
		  Track track = AlbumCollectionBusiness.getTrack(Integer.parseInt(trackId));
		  if(track != null){
			List songAuthors = EntityFinder.findRelated(track,GenericEntity.getStaticInstance(Author.class));
			if(songAuthors != null){
			  Iterator iter3 = songAuthors.iterator();
			  boolean f = false;
			  String name3 = "";
			  while (iter3.hasNext()) {
				Author songAuthor = (Author)iter3.next();
				if(f){
				  name3 += ", ";
				}
				name3 += songAuthor.getDisplayName();
				f=true;
			  }
			  contentTable.add(new Text(" lag:"),1,1);
			  contentTable.add(AlbumCollectionBusiness.getMainTextClone(name3),1,1);
			}
		  }
		}
		contentTable.add(new Text(" )"),1,1);
		// add authors - ends
		

        if(hasEditPermission()){

          Link updateLyricLink = AlbumCollectionBusiness.getMainLinkClone();
          updateLyricLink.setObject(core.getSharedImage("edit.gif","edit lyric"));
          updateLyricLink.setWindowToOpen(InsertLyric.class);
          updateLyricLink.addParameter(AlbumCollectionBusiness._PRM_UPDATE,"true");
          updateLyricLink.addParameter(AlbumCollectionBusiness._PRM_LYRIC_ID,lyric.getID());

          contentTable.add(updateLyricLink,1,6);

          Link deleteTrackLinkTemplate = AlbumCollectionBusiness.getMainLinkClone();
          deleteTrackLinkTemplate.setObject(core.getSharedImage("delete.gif","delete lyric"));
          deleteTrackLinkTemplate.setWindowToOpen(DeleteConfirmWindow.class);
          deleteTrackLinkTemplate.addParameter(AlbumCollectionBusiness._PRM_DELETE,AlbumCollectionBusiness._CONST_LYRIC);
          deleteTrackLinkTemplate.addParameter(DeleteConfirmWindow._PRM_ID,lyric.getID());

          contentTable.add(deleteTrackLinkTemplate,1,6);
          contentTable.add(Text.getBreak(),1,6);
          contentTable.add(Text.getBreak(),1,6);
        }

      } else {
        contentTable.add(AlbumCollectionBusiness.getMainTextClone("Texti finnst ekki"),1,2);
      }
    } else {
      contentTable.add(AlbumCollectionBusiness.getMainTextClone("Texti finnst ekki"),1,2);
    }

    contentTable.add(new BackButton(iwrb.getImage("back.gif","back")),1,6);

    this.add(Text.getBreak());
    this.add(Text.getBreak());
    this.add(frameTable);
    this.add(Text.getBreak());
    this.add(Text.getBreak());

  }
}
