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
public class ThreadEditLink extends ThreadLink {

	public ThreadEditLink(ForumData data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	public String getLinkName(IWResourceBundle resourceBundle) {
		return resourceBundle.getLocalizedString("edit", "Edit");
	}

	public Image getLinkImage(IWBundle bundle) {
		return bundle.getImage("shared/edit.gif");
	}

	public boolean addThreadIDToLink() {
		return true;
	}

	public boolean addParentThreadIDToLink() {
		return true;
	}

	public String getMode() {
		return null;
	}

}
