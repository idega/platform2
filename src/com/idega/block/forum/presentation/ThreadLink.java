/*
 * Created on Oct 8, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.forum.presentation;

import com.idega.block.forum.business.ForumBusiness;
import com.idega.block.forum.data.ForumData;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.text.Link;

/**
 * @author Anna
 */
public abstract class ThreadLink extends ForumLink {
	
	private ForumData thread;
	
	public ThreadLink(ForumData data) {
		thread = data;
	}

	public void showLink(IWContext iwc, int topicID) {
		Image image = getLinkImage(getBundle());
		if (showImage() && image != null) {
			image.setPaddingRight(getImagePadding());
			image.setAlignment(getImageAlignment());
	
			Link imageLink = new Link(image);
			imageLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, topicID);
			if (addThreadIDToLink()) {
				imageLink.addParameter(ForumBusiness.PARAMETER_THREAD_ID, thread.getID());
			}
			if (addParentThreadIDToLink()) {
				imageLink.addParameter(ForumBusiness.PARAMETER_PARENT_THREAD_ID, thread.getParentThreadID());
			}
			if (getMode() != null) {
				imageLink.addParameter(ForumBusiness.PARAMETER_MODE, getMode());
			}
			imageLink.setWindowToOpen(ForumThreadEditor.class);
			add(imageLink);
		}
		
		Link link = getStyleLink(getLinkName(getResourceBundle()), Forum.LINK_STYLE);
		link.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, topicID);
		if (addThreadIDToLink()) {
			link.addParameter(ForumBusiness.PARAMETER_THREAD_ID, thread.getID());
		}
		if (addParentThreadIDToLink()) {
			link.addParameter(ForumBusiness.PARAMETER_PARENT_THREAD_ID, thread.getParentThreadID());
		}
		if (getMode() != null) {
			link.addParameter(ForumBusiness.PARAMETER_MODE, getMode());
		}
		link.setWindowToOpen(ForumThreadEditor.class);
		add(link);
	}
	
	public abstract String getLinkName(IWResourceBundle resourceBundle);
	public abstract Image getLinkImage(IWBundle bundle);
	public abstract boolean addThreadIDToLink();
	public abstract boolean addParentThreadIDToLink();
	public abstract String getMode();
}