/*
 * Created on Oct 8, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.forum.presentation;

import com.idega.block.forum.business.ForumBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;

/**
 * @author Anna
 */
public class TopicOverviewLink extends TopicLink {
	
	//if hasPermission is set to true then everyone is allowed to open an overview - must be true
	public boolean hasPermission() {
		return true;
	}

	public String getLinkName(IWResourceBundle resourceBundle) {
		return resourceBundle.getLocalizedString("topic_overview", "Topic overview");
	}
	
	public Image getLinkImage(IWBundle bundle) {
		return bundle.getImage("shared/forum.gif");
	}
	//efast um að þetta þurfi
	public Class getWindowClassToOpen() {
		return null;
	}
	//skil ekki að þetta þurfi heldur
	public boolean addTopicIDToLink() {
		return false;
	}

	public String getState() {
		return ForumBusiness.PARAMETER_STATE;
	}
}
