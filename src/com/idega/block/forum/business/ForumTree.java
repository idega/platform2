package com.idega.block.forum.business;

import java.text.DateFormat;
import java.util.Date;

import com.idega.block.forum.data.ForumData;
import com.idega.core.builder.data.ICPage;
import com.idega.core.contact.data.Email;
import com.idega.core.data.ICTreeNode;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.AbstractTreeViewer;
import com.idega.util.IWTimestamp;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author @version 1.0
 */

public class ForumTree extends AbstractTreeViewer {

	private String _name;

	private String _style;

	private ICPage _page;

	private Image _threadImage;

	private int _id = -1;

	private IWResourceBundle _iwrb;

	public ForumTree() {
		super();
	}

	public PresentationObject getObjectToAddToColumn(int colIndex, ICTreeNode node, IWContext iwc, boolean nodeIsOpen, boolean nodeHasChild, boolean isRootNode) {
		PresentationObject obj = null;

		switch (colIndex) {
			case 1:
				obj = getThreadImage(iwc, node, nodeIsOpen);
				break;
			case 2:
				obj = getThreadLink(node, nodeIsOpen);
				break;
		}

		return obj;
	}

	public PresentationObject getObjectToAddToParallelExtraColumn(int colIndex, ICTreeNode node, IWContext iwc, boolean nodeIsOpen, boolean nodeHasChild, boolean isRootNode) {
		PresentationObject obj = null;

		switch (colIndex) {
			case 1:
				obj = getUserLink(iwc, node);
				break;
			case 2:
				obj = getNumberOfResponses(node);
				break;
			case 3:
				obj = getLastUpdated(node, iwc);
				break;
		}

		return obj;
	}

	private PresentationObject getThreadImage(IWContext iwc, ICTreeNode node, boolean isOpen) {
		ForumData thread = (ForumData) node;
		if (_threadImage == null) _threadImage = iwc.getIWMainApplication().getBundle(ForumBusiness.IW_BUNDLE_IDENTIFIER).getImage("shared/thread.gif");
		Link link = new Link(_threadImage);
		link.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, thread.getTopicID());
		link.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
		link.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID, _id);
		if (_page != null) link.setPage(_page);
		return setLinkToOpenOrCloseNode(link, node, isOpen);
	}

	private PresentationObject getThreadLink(ICTreeNode node, boolean isOpen) {
		ForumData thread = (ForumData) node;
		Link link = formatLink(thread.getThreadSubject());
		link.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, thread.getTopicID());
		link.addParameter(ForumBusiness.PARAMETER_STATE, ForumBusiness.FORUM_THREADS);
		link.addParameter(ForumBusiness.PARAMETER_THREAD_ID, thread.getID());
		link.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID, _id);
		if (_page != null) link.setPage(_page);
		return setLinkToOpenOrCloseNode(link, node, isOpen);
	}

	private PresentationObject getUserLink(IWContext iwc, ICTreeNode node) {
		ForumData thread = (ForumData) node;
		Text text = formatText(_iwrb.getLocalizedString("unknown", "Unknown"));
		if (thread.getUserID() != -1) {
			User user = UserBusiness.getUser(thread.getUserID());
			Email mail = UserBusiness.getUserMail(thread.getUserID());
			if (user != null && mail != null) {
				String name = user.getName();
				if (user.getDisplayName() != null && user.getDisplayName().length() > 0) name = user.getDisplayName();
				Link link = formatLink(name);
				link.setURL("mailto:" + mail.getEmailAddress());
				return link;
			}
			else if (user != null) {
				Text userText = formatText(user.getName());
				return userText;
			}
		}
		else if (thread.getUserName() != null && thread.getUserEMail() != null) {
			Link link = formatLink(thread.getUserName());
			if (thread.getUserEMail() != null) link.setURL("mailto:" + thread.getUserEMail());
			return link;
		}
		else if (thread.getUserName() != null) {
			Text userText = formatText(thread.getUserName());
			return userText;
		}
		return text;
	}

	private PresentationObject getNumberOfResponses(ICTreeNode node) {
		ForumData thread = (ForumData) node;
		Text text = formatText(String.valueOf(thread.getNumberOfResponses()));
		return text;
	}

	private PresentationObject getLastUpdated(ICTreeNode node, IWContext iwc) {
		ForumData thread = (ForumData) node;
		IWTimestamp stamp = new IWTimestamp(thread.getThreadDate());
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, iwc.getCurrentLocale());
		Date date = new Date(stamp.getTimestamp().getTime());
		Text text = formatText(format.format(date));
		return text;
	}

	private Link formatLink(String string) {
		Link link = new Link(string);
		if (_name != null) link.setStyleClass(_name);
		if (_page != null) link.setPage(_page);
		return link;
	}

	private Text formatText(String string) {
		Text text = new Text(string);
		if (_style != null) text.setStyleClass(_style);
		return text;
	}

	public void setLinkStyleName(String name) {
		_name = name;
	}

	public void setTextStyleName(String style) {
		_style = style;
	}

	public void setLinkPage(ICPage page) {
		_page = page;
	}

	public void setObjectInstanceID(int id) {
		_id = id;
	}

	public void setThreadImage(Image image) {
		_threadImage = image;
	}

	public void setResourceBundle(IWResourceBundle iwrb) {
		_iwrb = iwrb;
	}
	
	public void setLightRowStyle(String styleName) {
		lightRowStyle = styleName;
	}

	public void setDarkRowStyle(String styleName) {
		darkRowStyle = styleName;
	}
}