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
		trackNameTemplate = AlbumCollectionBusiness.getMainTextClone();
		trackNumberTemplate = AlbumCollectionBusiness.getMainTextClone();

		setLyricLinkTemplate = AlbumCollectionBusiness.getMainLinkClone();
		setLyricLinkTemplate.setWindowToOpen(InsertLyric.class);

		lyricViewerLinkTemplate = AlbumCollectionBusiness.getMainLinkClone();
		lyricViewerLinkTemplate.addParameter(AlbumCollection._PRM_STATE, AlbumCollection._STATE_LYRIC);

		updateTrackLinkTemplate = AlbumCollectionBusiness.getMainLinkClone("U");
		updateTrackLinkTemplate.setWindowToOpen(AddTrack.class);
		updateTrackLinkTemplate.addParameter(AlbumCollectionBusiness._PRM_UPDATE, "true");

		deleteTrackLinkTemplate = AlbumCollectionBusiness.getMainLinkClone("D");
		deleteTrackLinkTemplate.setWindowToOpen(DeleteConfirmWindow.class);
		deleteTrackLinkTemplate.addParameter(AlbumCollectionBusiness._PRM_DELETE, AlbumCollectionBusiness._CONST_TRACK);

	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void lineUpElements(IWContext iwc) throws Exception {

		IWResourceBundle iwrb = this.getResourceBundle(iwc);

		Table frameTable = new Table(1, 1);
		frameTable.setCellspacing(1);
		frameTable.setCellpadding(0);
		frameTable.setColor(AlbumCollection._COLOR_BRIGHTEST);
		frameTable.setColor(1, 1, AlbumCollection._COLOR_BRIGHT);
		frameTable.setWidth("550");

		Table contentTable = new Table(1, 6);
		contentTable.setCellpadding(0);
		contentTable.setCellspacing(0);
		contentTable.setWidth("550");
		contentTable.setHeight(2, "10");
		contentTable.setHeight(3, "1");
		contentTable.setHeight(4, "10");
		contentTable.setRowAlignment(1, "center");
		contentTable.setVerticalAlignment(1, 1, "bottom");
		contentTable.setHeight(1, "145");
		contentTable.setRowAlignment(3, "center");

		String albumId = iwc.getParameter(_PRM_ALBUM_ID);
		if (albumId != null && !"".equals(albumId)) {
			Album album = AlbumCollectionBusiness.getAlbum(Integer.parseInt(albumId));

			Table infoTable = new Table(2, 1);
			infoTable.setVerticalAlignment(2, 1, "bottom");
			infoTable.setWidth(1, 1, "120");
			infoTable.setHeight(1, 1, "120");
			//infoTable.setColor(1,1,AlbumCollection._COLOR_BRIGHTEST);

			if (album != null) {
				int imageId = album.getFrontCoverFileId();
				if (imageId > 0) {
					Image fCover = new Image(imageId, album.getName());
					fCover.setAlignment("left");
					fCover.setHeight(110);
					fCover.setWidth(110);
					fCover.setBorder(5);
					fCover.setBorderColor("#FFFFFF");
					infoTable.add(fCover, 1, 1);
				}

				Table albumInfoTable = new Table(2, 4);
				albumInfoTable.setCellpadding(0);
				albumInfoTable.setCellspacing(3);
				albumInfoTable.setWidth(340);
				albumInfoTable.setWidth(1, 1, "100");
				albumInfoTable.setWidth(2, 1, "240");
				albumInfoTable.setHeight(90);
				albumInfoTable.setColumnAlignment(1, "right");
				albumInfoTable.setColumnAlignment(2, "left");
				albumInfoTable.setColumnColor(2, AlbumCollection._COLOR_DARK);

				albumInfoTable.add(AlbumCollectionBusiness.getMainTextClone("titill:"), 1, 1);
				albumInfoTable.add(AlbumCollectionBusiness.getMainTextClone("flytjendur:"), 1, 2);
				albumInfoTable.add(AlbumCollectionBusiness.getMainTextClone("útgáfuár:"), 1, 3);
				albumInfoTable.add(AlbumCollectionBusiness.getMainTextClone("tegund:"), 1, 4);

				for (int i = 0; i < 1; i++) {
					albumInfoTable.add(Text.getNonBrakingSpace(), 2, 1);
					albumInfoTable.add(Text.getNonBrakingSpace(), 2, 2);
					albumInfoTable.add(Text.getNonBrakingSpace(), 2, 3);
					albumInfoTable.add(Text.getNonBrakingSpace(), 2, 4);
				}

				albumInfoTable.add(AlbumCollectionBusiness.getMainTextBoldClone(album.getName()), 2, 1);

				List performers = EntityFinder.findRelated(album, com.idega.block.albumcollection.data.PerformerBMPBean.getStaticInstance(Performer.class));
				if (performers != null) {
					Iterator iter2 = performers.iterator();
					boolean f = false;
					String name = "";
					while (iter2.hasNext()) {
						Performer performer = (Performer)iter2.next();
						if (f) {
							name += ", ";
						}
						name += performer.getDisplayName();
						f = true;
					}
					albumInfoTable.add(AlbumCollectionBusiness.getMainTextBoldClone(name), 2, 2);
				}
				if (album.getPublishingDay() != null) {
					albumInfoTable.add(AlbumCollectionBusiness.getMainTextBoldClone(Integer.toString(new IWTimestamp(album.getPublishingDay()).getYear())), 2, 3);
				}

				if (album.getAlbumTypeId() > 0) {
					String type = null;
					try {
						if (album.getAlbumTypeId() > 0) {
							type = ((com.idega.block.albumcollection.data.AlbumTypeHome)com.idega.data.IDOLookup.getHomeLegacy(AlbumType.class)).findByPrimaryKeyLegacy(album.getAlbumTypeId()).getName();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					if (type != null) {
						albumInfoTable.add(AlbumCollectionBusiness.getMainTextBoldClone(type), 2, 4);
					}
				}

				infoTable.add(albumInfoTable, 2, 1);

				contentTable.add(infoTable, 1, 1);

				String description = TextSoap.formatTabsAndReturnsToHtml(album.getDescription());
				if (description != null && !"".equals(description)) {
					Table t = new Table();
					t.add(AlbumCollectionBusiness.getMainTextClone(description), 1, 1);
					t.setWidth(410);
					t.setCellpadding(8);
					contentTable.add(t, 1, 3);
				}
			}
		}

		contentTable.add(getTrackList(iwc), 1, 5);

		Table backTable = new Table(1, 1);
		backTable.setCellpadding(4);
		backTable.setCellspacing(4);
		backTable.add(new BackButton(iwrb.getImage("back.gif", "back")), 1, 1);
		contentTable.add(backTable, 1, 6);
		contentTable.setAlignment(1, 6, "right");

		frameTable.add(contentTable);
		this.add(Text.getBreak());
		this.add(Text.getBreak());
		this.add(frameTable);
		this.add(Text.getBreak());
		this.add(Text.getBreak());
	}

	public Table getTrackList(IWContext iwc) throws Exception {

		IWBundle core = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		IWBundle iwb = this.getBundle(iwc);
		IWResourceBundle iwrb = this.getResourceBundle(iwc);
		
		
		Table trackTable = null;
		int row = 1;
		int index = 1;
		String albumId = iwc.getParameter(_PRM_ALBUM_ID);
		if (albumId != null && !"".equals(albumId)) {
			List tracks = AlbumCollectionBusiness.getTracks(Integer.parseInt(albumId));
			if (tracks != null && tracks.size() > 0) {
				int trackTableRows;
				trackTableRows=tracks.size()+2;
				int editColumns = 0;
				int editSubtraction = 0;
				if (hasEditPermission()){
					editColumns=2;
					editSubtraction=16;
					//trackTableRows=(tracks.size()*2)+2;
				}
				//else{
					//trackTableRows=tracks.size()+2;
				//}
				int trackTableColumns = 7;
				trackTable = new Table(trackTableColumns+editColumns, trackTableRows);
				//trackTable.setBorder(1);
				trackTable.setWidth("550");
				trackTable.setCellspacing(0);
				//trackTable.setCellpadding(0);

				trackTable.removeLineColor(true);
				trackTable.setLinesBetween(true);
				trackTable.setLineHeight("2");

				trackTable.setWidth(1, "20");
				trackTable.setWidth(2, "200");
				trackTable.setWidth(3, "1"); 
//				trackTable.setWidth(3,"38");
				trackTable.setWidth(4, Integer.toString(143-editSubtraction));
				trackTable.setWidth(5, Integer.toString(144-editSubtraction));
				trackTable.setWidth(6, "16");
				trackTable.setWidth(7, "16");
				for(int i=1; i<=editColumns; i++){
					trackTable.setWidth(trackTableColumns+1, "16");
				}

				trackTable.setColumnAlignment(1, "center");
				trackTable.setColumnAlignment(2, "left");
				trackTable.setColumnAlignment(3, "center");
				trackTable.setColumnAlignment(4, "left");
				trackTable.setColumnAlignment(5, "left");
				trackTable.setColumnAlignment(6, "center");
				trackTable.setColumnAlignment(7, "center");
//				trackTable.setColumnAlignment(8, "center");
//				trackTable.setColumnAlignment(9, "center");

				//        Table trackInfo = new Table(9,1);
				//        trackInfo.setCellpadding(1);
				//        trackInfo.setCellspacing(0);
				//        trackInfo.setWidth(1,"20");
				//        trackInfo.setWidth(2,"200");
				//		trackInfo.setWidth(3,"1");//trackInfo.setWidth(3,"50");
				//        trackInfo.setWidth(4,"127");
				//        trackInfo.setWidth(5,"128");
				//        trackInfo.setWidth(6,"16");
				//        trackInfo.setWidth(7,"16");
				//        trackInfo.setWidth(8,"16");
				//        trackInfo.setWidth(9,"16");
				//
				//        trackInfo.setColumnAlignment(1,"center");
				//        trackInfo.setColumnAlignment(2,"left");
				//        trackInfo.setColumnAlignment(3,"center");
				//        trackInfo.setColumnAlignment(4,"left");
				//        trackInfo.setColumnAlignment(5,"left");
				//        trackInfo.setColumnAlignment(6,"center");
				//        trackInfo.setColumnAlignment(7,"center");
				//        trackInfo.setColumnAlignment(8,"center");
				//        trackInfo.setColumnAlignment(9,"center");
				//
				//        trackInfo.setRowVerticalAlignment(1,"top");

				//Table info = (Table)trackInfo.clone();
				//info.add(AlbumCollectionBusiness.getMainTextClone("nr."),1,1);
				trackTable.add(AlbumCollectionBusiness.getMainTextClone("heiti"), 2, row);
				//trackTable.add(AlbumCollectionBusiness.getMainTextClone("lengd"),3,row);
				trackTable.add(AlbumCollectionBusiness.getMainTextClone("lag"), 4, row);
				trackTable.add(AlbumCollectionBusiness.getMainTextClone("texti"), 5, row);
				//trackTable.add(iwb.getSharedImage("lyric_label.gif", iwrb.getLocalizedString("lyric_label","lyrics")),6,row);
				//trackTable.add(iwb.getSharedImage("audio_label.gif", iwrb.getLocalizedString("audio_label","audio")),7,row);
				//trackTable.add(info,1,index);
				index++;
				Iterator iter = tracks.iterator();
				//int tmp = 0;
				while (iter.hasNext()) {
					Track item = (Track)iter.next();

					//info = (Table)trackInfo.clone();
					row++;
					trackTable.setRowColor(row, AlbumCollection._COLOR_DARK);
					trackTable.setRowVerticalAlignment(row, "top");

					if (item.getNumber() > 0) {
						Text trackNumber = (Text)trackNumberTemplate.clone();
						trackNumber.setText(Integer.toString(item.getNumber()));
						trackTable.add(trackNumber, 1, row);
					}

					//Track length
//			          int length = item.getLength();
//			          if(length > 0){
//			            int m = length/60;
//			            int s = length%60;
//						trackTable.add(AlbumCollectionBusiness.getMainTextClone(((m < 10)?TextSoap.addZero(m):Integer.toString(m))+":"+((s<10)?TextSoap.addZero(s):Integer.toString(s))),3,row);
//			          }

					if (!item.isAudoTrackHidden() && item.getTrackID() > -1) {
						Link audioLink = new Link("S");
						Image audioIcon = null;
						audioIcon=iwb.getSharedImage("audio.gif", iwrb.getLocalizedString("audio","audio"));
						audioLink.setImage(audioIcon);
						audioLink.setFile(item.getTrackID());
						trackTable.add(audioLink, 7, row);
					}

					List authors = EntityFinder.findRelated(item, com.idega.block.albumcollection.data.AuthorBMPBean.getStaticInstance(Author.class));
					if (authors != null) {
						Iterator iter2 = authors.iterator();
						boolean f = false;
						String name = "";
						while (iter2.hasNext()) {
							Author author = (Author)iter2.next();
							if (f) {
								name += "<br>";
							}
							name += author.getDisplayName();
							f = true;
						}
						trackTable.add(AlbumCollectionBusiness.getMainTextClone(name), 4, row);
					}

					

					if (item.getLyricId() < 0) {

						Text trackName = (Text)trackNameTemplate.clone();
						trackName.setText(item.getName());
						trackTable.add(trackName, 2, row);

						if (hasEditPermission()) {

							Link setLyric = (Link)setLyricLinkTemplate.clone();
							Image setLyricIcon = iwb.getSharedImage("write.gif", iwrb.getLocalizedString("set_lyrics","add lyrics"));
							//setLyric.setText("A");
							setLyric.setImage(setLyricIcon);
							setLyric.addParameter(AlbumCollectionBusiness._PRM_TRACK_ID, item.getID());

							trackTable.add(setLyric, 6, row);
						}
					} else {

						Link trackName = (Link)lyricViewerLinkTemplate.clone();
						trackName.setText(item.getName());
						trackName.addParameter(AlbumCollectionBusiness._PRM_LYRIC_ID, item.getLyricId());
						trackName.addParameter(AlbumCollectionBusiness._PRM_TRACK_ID, item.getID());
						trackTable.add(trackName, 2, row);

						Link setLyric = (Link)lyricViewerLinkTemplate.clone();
						Image textIcon = iwb.getSharedImage("lyrics.gif", iwrb.getLocalizedString("lyrics","lyrics"));
						//setLyric.setText("T");
						setLyric.setImage(textIcon);
						setLyric.addParameter(AlbumCollectionBusiness._PRM_LYRIC_ID, item.getLyricId());
						setLyric.addParameter(AlbumCollectionBusiness._PRM_TRACK_ID, item.getID());
						trackTable.add(setLyric, 6, row);

						List T_authors = EntityFinder.findRelated(((com.idega.block.albumcollection.data.LyricHome)com.idega.data.IDOLookup.getHomeLegacy(Lyric.class)).findByPrimaryKeyLegacy(item.getLyricId()), com.idega.block.albumcollection.data.AuthorBMPBean.getStaticInstance(Author.class));
						if (T_authors != null) {
							Iterator iter2 = T_authors.iterator();
							boolean f = false;
							String name2 = "";
							while (iter2.hasNext()) {
								Author T_author = (Author)iter2.next();
								if (f) {
									name2 += "<br>";
								}
								name2 += T_author.getDisplayName();
								f = true;
							}
							trackTable.add(AlbumCollectionBusiness.getMainTextClone(name2), 5, row);
						}
					}
					
					if (hasEditPermission()) {
						//row++;
						
						//info.resize(info.getColumns(),2);
						Link update = (Link)updateTrackLinkTemplate.clone();
						update.setObject(core.getSharedImage("edit.gif", "edit track"));
						update.addParameter(AlbumCollectionBusiness._PRM_TRACK_ID, item.getID());
						trackTable.add(update, trackTableColumns+1, row);
	
						Link delete = (Link)deleteTrackLinkTemplate.clone();
						delete.setObject(core.getSharedImage("delete.gif", "delete track"));
						delete.addParameter(DeleteConfirmWindow._PRM_ID, item.getID());
						trackTable.add(delete, trackTableColumns+2, row);
					}

					//          trackTable.add(info,1,index);
					index++;
				}
			}

		}

		if (hasEditPermission()) {
			row++;
			Link addTrackLink = AlbumCollectionBusiness.getMainLinkClone();
			addTrackLink.setObject(core.getSharedImage("create.gif", iwrb.getLocalizedString("add_track","add track")));
			//addTrackLink.setFontColor("#EEEEEE");
			addTrackLink.setBold();
			addTrackLink.setWindowToOpen(AddTrack.class);
			if (albumId != null && !albumId.equals("")) {
				addTrackLink.addParameter(_PRM_ALBUM_ID, albumId);
			}

			if (trackTable == null) {
				trackTable = new Table();
				trackTable.setWidth("550");
				trackTable.setCellspacing(2);
				trackTable.setCellpadding(0);
			}
			trackTable.add(addTrackLink, 1, row);
		}

		return trackTable;
	}

	public void main(IWContext iwc) throws Exception {

		lineUpElements(iwc);

	}
}
