/*
 * Created on Oct 8, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.forum.presentation;

import com.idega.block.forum.business.ForumBusiness;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * @author Anna
 */
public abstract class TopicLink extends ForumLink {
	
	private ICPage page;

	public void showLink(IWContext iwc, int topicID) {
		Image image = getLinkImage(getBundle());
		image.setPaddingRight(getImagePadding());
		image.setAlignment(getImageAlignment());

		if (hasPermission()) {
			Link imageLink = new Link(image);
			if (getWindowClassToOpen() != null) {
				imageLink.setWindowToOpen(getWindowClassToOpen());
			}
			if (addTopicIDToLink()) {
				imageLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, topicID);
			}
			if (getState() != null) {
				imageLink.addParameter(ForumBusiness.PARAMETER_STATE, getState());
			}
			if (page != null) {
				imageLink.setPage(page);
			}
			
			Link link = getStyleLink(getLinkName(getResourceBundle()), Forum.LINK_STYLE);
			if (getWindowClassToOpen() != null) {
				link.setWindowToOpen(getWindowClassToOpen());
			}
			if (addTopicIDToLink()) {
				link.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, topicID);
			}
			if (getState() != null) {
				link.addParameter(ForumBusiness.PARAMETER_STATE, getState());
			}
			if (page != null) {
				link.setPage(page);
			}

			if (showImage()) {
				add(imageLink);
			}
			add(link);
		}
		else {
			Text text = getStyleText(getLinkName(getResourceBundle()), Forum.SMALL_TEXT_STYLE);
			if (showImage()) {
				add(image);
			}
			add(text);
		}
	}
	
	public abstract boolean hasPermission();
	public abstract String getLinkName(IWResourceBundle resourceBundle);
	public abstract Image getLinkImage(IWBundle bundle);
	public abstract Class getWindowClassToOpen();
	public abstract boolean addTopicIDToLink();
	public abstract String getState();
	
	public void setPage(ICPage page) {
		this.page = page;
	}
}