/*
 * Created on Oct 8, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.forum.presentation;

import com.idega.block.forum.data.ForumData;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Image;

/**
 * @author Anna
 */
public class ThreadReplyLink extends ThreadLink {

	public ThreadReplyLink(ForumData data) {
		super(data);
	}

	public String getLinkName(IWResourceBundle resourceBundle) {
		return resourceBundle.getLocalizedString("reply", "Reply");
	}

	public Image getLinkImage(IWBundle bundle) {
		return bundle.getImage("shared/reply.gif");
	}

	public boolean addThreadIDToLink() {
		return true;
	}

	public boolean addParentThreadIDToLink() {
		return false;
	}

	public String getMode() {
		return null;
	}
}