/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.block.forum.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import se.idega.idegaweb.commune.block.forum.business.CommuneForumBusiness;

import com.idega.block.forum.business.ForumBusiness;
import com.idega.block.forum.data.ForumData;
import com.idega.block.forum.presentation.Forum;
import com.idega.block.media.business.MediaBusiness;
import com.idega.block.presentation.CategoryWindow;
import com.idega.business.IBOLookup;
import com.idega.core.data.ICCategory;
import com.idega.core.data.ICFile;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.User;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CommuneForum extends Forum {
	public CommuneForum() {
		super();
	}

	protected void getForumTopics(IWContext iwc, Table table) {
		iwc.removeSessionAttribute(ForumBusiness.PARAMETER_FIRST_THREAD + "_" + _objectID);
		iwc.removeSessionAttribute(ForumBusiness.PARAMETER_LAST_THREAD + "_" + _objectID);

		Text topicText = new Text(_iwrb.getLocalizedString("topics", "Topics"));
		topicText.setFontStyle(_headingStyle);
		Text threadsText = new Text(_iwrb.getLocalizedString("threads", "Threads"));
		threadsText.setFontStyle(_headingStyle);
		Text updatedText = new Text(_iwrb.getLocalizedString("last_updated", "Last updated"));
		updatedText.setFontStyle(_headingStyle);
		Text moderatorText = new Text(_iwrb.getLocalizedString("moderator","Moderator"));
		moderatorText.setFontStyle(_headingStyle);
		Text filesText = new Text(_iwrb.getLocalizedString("files","Files"));
		filesText.setFontStyle(_headingStyle);

		table.setWidth(2, "60");
		table.setWidth(3, "90");

		table.add(topicText, 1, 1);
		table.add(threadsText, 2, 1);
		table.add(updatedText, 3, 1);
		table.add(moderatorText, 4, 1);
		table.add(filesText, 5, 1);

		Vector list = new Vector();
		list.addAll(this.getCategories());

		if (list != null) {
			Link topicLink = null;
			Text numberOfThreadsText = null;
			Text lastUpdatedText = null;
			ICCategory topic;
			User user;
			int row = 2;

			for (int a = 0; a < list.size(); a++) {
				topic = (ICCategory) list.get(a);
				try {
				user = getCommuneForumBusiness(iwc).getModerator(topic);
				}catch (RemoteException r) {
					throw new RuntimeException(r.getMessage());
				}
				if (topic != null) {
					topicLink = new Link(topic.getName());
					topicLink.setStyle(_topicName);
					topicLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, topic.getID());
					topicLink.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
					topicLink.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID, _objectID);

					int numberOfThreads = forumBusiness.getNumberOfThreads(topic);
					numberOfThreadsText = formatText(String.valueOf(numberOfThreads), _textStyle);

					ForumData newestThread = forumBusiness.getNewestThreads(topic);
					if (newestThread != null) {
						lastUpdatedText = getThreadDate(iwc, newestThread);
					}
					else
						lastUpdatedText = null;

					table.add(topicLink, 1, row);
					table.add(numberOfThreadsText, 2, row);
					if (lastUpdatedText != null)
						table.add(lastUpdatedText, 3, row);

					if (user != null) {
						table.add(formatText(user.getName(), _textStyle), 4, row);	
					}
					//table.add(new Text("Jón Jónsson"),4,row);

					Link file;
					try {
						file = new Link(formatText(Integer.toString(getCommuneForumBusiness(iwc).getFileCount(topic)), _textStyle));
						file.setWindowToOpen(CommuneForumTopicFiles.class);
						file.addParameter(CommuneForumTopicFiles.prmTopicId, topic.getID());
						table.add(file,5,row);
					} catch (RemoteException e) {
						e.printStackTrace(System.err);
						throw new RuntimeException(e.getMessage());
					}
					row++;
				}
			}
		}

		table.setColumnAlignment(2, "center");
		table.setColumnAlignment(3, "center");
	}
	
	protected Table getAdminPart(IWContext iwc) {
		//System.out.println("Commune business getAdminPart()");
		Table adminTable = new Table(2, 1);
		adminTable.setCellpadding(0);
		adminTable.setCellspacing(0);

		Link categoryLink = this.getCategoryLink();
		categoryLink.setPresentationObject(_iwcb.getImage("shared/edit.gif"));
				
		adminTable.add(categoryLink);

		return adminTable;
	}	
	
	/**
	 *  Returns a Link to CategoryWindow with specified type
	 *  for this intance
	 */
	public Link getCategoryLink(String type){
		//System.out.println("Getting category link in commune forum");
		
		Link L = new Link();
		L.addParameter(CategoryWindow.prmCategoryId,getCategoryId());
		L.addParameter(CategoryWindow.prmObjInstId,getICObjectInstanceID());
		L.addParameter(CategoryWindow.prmCategoryType,type);
		if(getMultible()) {
			L.addParameter(CategoryWindow.prmMulti,"true");
		}
		if (orderManually) {
			L.addParameter(CategoryWindow.prmOrder, "true");
		}
		if(invalidateBlockCache && !getCacheKey().equals(IW_BLOCK_CACHE_KEY)){
//			L.addParameter(CategoryWindow.prmCategoryId,getCacheKey());
//			L.addParameter(CategoryWindow.prmCacheClearKey ,getCacheKey());
			L.addParameter(CategoryWindow.prmCacheClearKey ,super.getDerivedCacheKey());
		}

		L.setWindowToOpen(CommuneCategoryWindow.class);
		return L;
	}
	
	protected int addBelowTopic(IWContext iwc, ICCategory cat, Table table, int row) {
		//System.out.println("adding below topic in commune forum");
		try {
			Collection pkColl = cat.getFiles();
			Collection coll = getCommuneForumBusiness(iwc).convertFilePKsToFileCollection(pkColl);
			
			Table t = new Table();
			t.setCellpaddingAndCellspacing(0);
			t.setWidth("100%");
			int r = 1;
			
			ICFile file;
			if (coll != null && !coll.isEmpty()) {
				t.add(formatText(_iwrb.getLocalizedString("attached_documents","Attached documents")+" :", _headingStyle), 1, r);
				Iterator iter = coll.iterator();
				while (iter.hasNext()) {
					file = (ICFile) iter.next();
					Link preview = new Link(formatText(file.getName(), _textStyle));
					preview.setURL(MediaBusiness.getMediaURL(file,iwc.getApplication()));
					preview.setTarget(Link.TARGET_NEW_WINDOW);
					t.add(preview, 2, r++);
				}
				t.add(formatText(Text.BREAK, _headingStyle), 1, row);
			}
			table.add(t, 1, row++);
			return ++row;
		} catch (Exception e) {
			e.printStackTrace(System.err);
		} 
		return row;
	}
	
	protected CommuneForumBusiness getCommuneForumBusiness(IWContext iwc) throws RemoteException {
		return (CommuneForumBusiness) IBOLookup.getServiceInstance(iwc, CommuneForumBusiness.class);
	}	
	
}