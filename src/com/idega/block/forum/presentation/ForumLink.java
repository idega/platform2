/*
 * Created on Oct 8, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.forum.presentation;

import javax.faces.component.UIComponent;
import com.idega.block.forum.business.ForumBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;

/**
 * @author Anna
 */
public abstract class ForumLink extends Block {

	private IWBundle iwb;
	private IWResourceBundle iwrb;

	private int imagePadding = 2;
	private String imageAlignment = Image.ALIGNMENT_ABSOLUTE_MIDDLE;
	
	private boolean showImage = true;
	private boolean iHasAddPermission = false;
	private boolean iHasReplyPermission = false;
	
	private Boolean hasViewPermission = null;
	

	public void main(IWContext iwc) throws Exception {
		this.iwrb = getResourceBundle(iwc);
		this.iwb = getBundle(iwc);
		this.iHasAddPermission = hasAddPermission(iwc);
		this.iHasReplyPermission = hasReplyPermission(iwc);

		int topicID = -1;
		try {
			topicID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_TOPIC_ID));
			showLink(iwc, topicID);
		}
		catch (NumberFormatException e) {
			//topic is not set...
		}
	}

	/**
	 * If this link is added into forum it asks about the forums permissions but not the links.
	 * So this method looks for the parent and uses that.
	 * @param permissionType
	 * @param iwc
	 * @return
	 * @throws Exception
	 */
	public boolean hasPermission(String permissionType, IWContext iwc) throws Exception {
		PresentationObject obj = this;
		UIComponent tmp = obj;
		while (tmp != null)
		{
			if (tmp instanceof Forum)
			{
				obj = (Forum) tmp;
				break;
			}
			tmp = tmp.getParent();
		}
		return hasPermission(permissionType, obj, iwc);
	}

	
	
	private boolean hasAddPermission(IWContext iwc) throws Exception {
		return hasPermission(Forum.AddPermission, iwc);
	}
	
	private boolean hasReplyPermission(IWContext iwc) throws Exception {
		return hasPermission(Forum.AddPermission, iwc);
	}
	
	protected boolean hasReplyPermission() {
		return this.iHasReplyPermission;
	}
	
	protected boolean hasAddPermission() {
		return this.iHasAddPermission;
	}
	
	public String getBundleIdentifier() {
		return Forum.IW_BUNDLE_IDENTIFIER;
	}

	public void registerPermissionKeys() {
		registerPermissionKey(Forum.AddPermission);
	}
	
	protected IWBundle getBundle() {
		return this.iwb;
	}
	
	protected IWResourceBundle getResourceBundle() {
		return this.iwrb;
	}
	
	protected int getImagePadding() {
		return this.imagePadding;
	}
	
	protected String getImageAlignment() {
		return this.imageAlignment;
	}
	
	protected boolean showImage() {
		return this.showImage;
	}

	public abstract void showLink(IWContext iwc, int topicID);

	public void setImageAlignment(String imageAlignment) {
		this.imageAlignment = imageAlignment;
	}
	
	public void setImagePadding(int imagePadding) {
		this.imagePadding = imagePadding;
	}
	
	public void setShowImage(boolean showImage) {
		this.showImage = showImage;
	}
}