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
import com.idega.presentation.Image;

/**
 * @author Anna
 */
public class ThreadDeleteLink extends ThreadLink {

	public ThreadDeleteLink(ForumData data) {
		super(data);
	}

	public String getLinkName(IWResourceBundle resourceBundle) {
		return resourceBundle.getLocalizedString("delete", "Delete");
	}

	public Image getLinkImage(IWBundle bundle) {
		return bundle.getImage("shared/delete.gif");
	}

	public boolean addThreadIDToLink() {
		return true;
	}
	
	public boolean addParentThreadIDToLink() {
		return false;
	}

	public String getMode() {
		return ForumBusiness.PARAMETER_DELETE;
	}
}