/*
 * Created on Oct 7, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.forum.presentation;

import com.idega.block.forum.business.ForumBusiness;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * @author Laddi
 */
public class ForumNavigation extends Block {

	private int _topicID = -1;
	
	private boolean _hasAddPermission = false;
	private boolean _showOverviewLink = true;
	
	private IWBundle _iwb;
	private IWResourceBundle _iwrb;
	
	private ICPage _page;
	
	public void main(IWContext iwc) {
		this._iwrb = getResourceBundle(iwc);
		this._iwb = getBundle(iwc);

		this._hasAddPermission = hasAddPermission(iwc);

		try {
			this._topicID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_TOPIC_ID));
		}
		catch (NumberFormatException e) {
			this._topicID = -1;
		}
		
		add(getNavigation(iwc));
	}
	
	public boolean hasAddPermission(IWContext iwc) {
		return iwc.hasPermission(Forum.AddPermission, this);
	}
	
	private Table getNavigation(IWContext iwc) {
		Table table = new Table();
		table.setCellspacing(0);
		table.setCellpadding(2);
		int column = 1;

		if (this._topicID != -1) {
			Image newImage = this._iwb.getImage("shared/new.gif");
			newImage.setPaddingRight(2);
			newImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);

			if (this._hasAddPermission) {
				Link newImageLink = new Link(newImage);
				newImageLink.setWindowToOpen(ForumThreadEditor.class);
				newImageLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, this._topicID);
				
				Link newLink = getStyleLink(this._iwrb.getLocalizedString("new_thread", "New thread"), Forum.LINK_STYLE);
				newLink.setWindowToOpen(ForumThreadEditor.class);
				newLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, this._topicID);

				table.add(newImageLink, column, 1);
				table.add(newLink, column++, 1);
			}
			else {
				Text newText = getStyleText(this._iwrb.getLocalizedString("new_thread", "New thread"), Forum.SMALL_TEXT_STYLE);
				table.add(newImage, column, 1);
				table.add(newText, column++, 1);
			}

			if (this._showOverviewLink) {
				Image overviewImage = this._iwb.getImage("shared/forum.gif");
				overviewImage.setPaddingRight(2);
				overviewImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
				
				Link overviewImageLink = new Link(overviewImage);
				overviewImageLink.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_TOPICS);
				if (this._page != null) {
					overviewImageLink.setPage(this._page);
				}
				
				table.add(overviewImageLink, column, 1);
				table.add(getOverviewLink(), column, 1);
			}
		}

		return table;
	}

	private Link getOverviewLink() {
		Link overView = getStyleLink(this._iwrb.getLocalizedString("topic_overview", "Topic overview"), Forum.LINK_STYLE);
		overView.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_TOPICS);
		if (this._page != null) {
			overView.setPage(this._page);
		}

		return overView;
	}
	
	public String getBundleIdentifier() {
		return Forum.IW_BUNDLE_IDENTIFIER;
	}

	public void registerPermissionKeys() {
		registerPermissionKey(Forum.AddPermission);
	}

	public void setPage(ICPage page) {
		this._page = page;
	}
	
	public void setShowOverviewLink(boolean overviewLink) {
		this._showOverviewLink = overviewLink;
	}
}