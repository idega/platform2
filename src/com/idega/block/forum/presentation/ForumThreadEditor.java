package com.idega.block.forum.presentation;

import java.io.IOException;
import java.sql.SQLException;

import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.block.forum.business.ForumBusiness;
import com.idega.block.forum.data.ForumData;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

public class ForumThreadEditor extends IWAdminWindow {

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.forum";
	private boolean _isAdmin = false;
	private boolean _save = false;
	private boolean _update = false;
	private int _forumID = -1;
	private int _topicID = -1;
	private int _parentThreadID = -1;
	private int _threadID = -1;
	private int _localeID = -1;
	private int _userID = -1;
	private boolean _isLoggedOn = false;
	
	private String someErrorMessage = null;
	private String errorDetail = null;

	private IWBundle _iwb;
	private IWResourceBundle _iwrb;
	private ForumBusiness forumBusiness;

	public ForumThreadEditor() {
		setWidth(420);
		setHeight(280);
		setUnMerged();
		setResizable(true);
		setScrollbar(true);
	}

	public void main(IWContext iwc) throws Exception {
		/**
		 * @todo permission
		 */
		this._isAdmin = true; //AccessControl.hasEditPermission(this,iwc);
		this._iwb = iwc.getIWMainApplication().getBundle(Builderaware.IW_CORE_BUNDLE_IDENTIFIER);
		this._iwrb = getResourceBundle(iwc);
		this._localeID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());
		this._isLoggedOn = iwc.isLoggedOn();
		if (this._isLoggedOn) {
			this._userID = iwc.getUserId();
		}

		addTitle(this._iwrb.getLocalizedString("thread_editor", "Write new thread"));
		this.forumBusiness = new ForumBusiness();

		if (this._isAdmin) {
			processForm(iwc);
		}
		else {
			noAccess();
		}
	}

	private void processForm(IWContext iwc) {
		this.someErrorMessage = null;
		this.errorDetail = null;
		if (iwc.getParameter(ForumBusiness.PARAMETER_TOPIC_ID) != null) {
			try {
				this._topicID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_TOPIC_ID));
			}
			catch (NumberFormatException e) {
				this._topicID = -1;
			}
		}

		if (iwc.getParameter(ForumBusiness.PARAMETER_PARENT_THREAD_ID) != null) {
			try {
				this._parentThreadID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_PARENT_THREAD_ID));
			}
			catch (NumberFormatException e) {
				this._threadID = -1;
			}
		}

		if (iwc.getParameter(ForumBusiness.PARAMETER_THREAD_ID) != null) {
			try {
				this._threadID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_THREAD_ID));
			}
			catch (NumberFormatException e) {
				this._threadID = -1;
			}
		}

		if (iwc.getParameter(ForumBusiness.PARAMETER_MODE) != null) {
			if (iwc.getParameter(ForumBusiness.PARAMETER_MODE).equalsIgnoreCase(ForumBusiness.PARAMETER_CLOSE)) {
				close(iwc);
			}
			else if (iwc.getParameter(ForumBusiness.PARAMETER_MODE).equalsIgnoreCase(ForumBusiness.PARAMETER_SAVE)) {
				save(iwc);
			}
		}

		if (this._threadID != -1) {
			if (iwc.getParameter(ForumBusiness.PARAMETER_MODE) != null) {
				if (iwc.getParameter(ForumBusiness.PARAMETER_MODE).equalsIgnoreCase(ForumBusiness.PARAMETER_DELETE)) {
					delete();
				}
			}
			else {
				this._update = true;
			}
		}
		
		if (this._update && this._parentThreadID == -1) {
			this._parentThreadID = this._threadID;
			this._threadID = -1;
			this._update = false;
		}

		initializeFields();
	}

	private void initializeFields() {
		ForumData thread = null;
		ForumData parentThread = null;
		if (this._threadID != -1) {
			thread = this.forumBusiness.getForumData(this._threadID);
			if (thread != null) {
				this._update = true;
			}
		}

		if (this._parentThreadID != -1) {
			parentThread = this.forumBusiness.getForumData(this._parentThreadID);
		}
		
		if(this.someErrorMessage!=null){
			addRight(this.someErrorMessage);
			if(this.errorDetail!=null){
				addRight(this.errorDetail);
			}
		}
		

		TextInput subject = new TextInput(ForumBusiness.PARAMETER_THREAD_HEADLINE);
		subject.setLength(24);
		if (this._update && thread.getThreadSubject() != null) {
			subject.setContent(thread.getThreadSubject());
		}
		if (parentThread != null && parentThread.getThreadSubject() != null) {
			String subjectString = parentThread.getThreadSubject();
			if (subjectString.indexOf("RE:") == -1) {
				subjectString = "RE: " + subjectString;
			}
			subject.setContent(subjectString);
		}

		TextArea body = new TextArea(ForumBusiness.PARAMETER_THREAD_BODY);
		body.setHeight(4);
		if (this._update && thread.getThreadBody() != null) {
			body.setContent(thread.getThreadBody());
		}
		if (this._isLoggedOn) {
			body.setHeight(10);
		}
		body.setWidth(Table.HUNDRED_PERCENT);

		addLeft(this._iwrb.getLocalizedString("thread_subject", "Title") + ":", subject, true);
		addLeft(this._iwrb.getLocalizedString("thread_body", "Body") + ":", body, true);

		if (!this._isLoggedOn) {
			TextInput userName = new TextInput(ForumBusiness.PARAMETER_USER_NAME);
			if (this._update && thread.getUserName() != null) {
				userName.setContent(thread.getUserName());
			}

			TextInput userEmail = new TextInput(ForumBusiness.PARAMETER_USER_EMAIL);
			if (this._update && thread.getUserEMail() != null) {
				userEmail.setContent(thread.getUserEMail());
			}

			addLeft(this._iwrb.getLocalizedString("thread_username", "Name") + ":", userName, true);
			addLeft(this._iwrb.getLocalizedString("thread_email", "E-mail") + ":", userEmail, true);
		}

		addHiddenInput(new HiddenInput(ForumBusiness.PARAMETER_TOPIC_ID, Integer.toString(this._topicID)));
		addHiddenInput(new HiddenInput(ForumBusiness.PARAMETER_PARENT_THREAD_ID, Integer.toString(this._parentThreadID)));
		addHiddenInput(new HiddenInput(ForumBusiness.PARAMETER_THREAD_ID, Integer.toString(this._threadID)));

		addSubmitButton(new SubmitButton(this._iwrb.getLocalizedImageButton("close", "CLOSE"), ForumBusiness.PARAMETER_MODE, ForumBusiness.PARAMETER_CLOSE));
		addSubmitButton(new SubmitButton(this._iwrb.getLocalizedImageButton("save", "SAVE"), ForumBusiness.PARAMETER_MODE, ForumBusiness.PARAMETER_SAVE));
	}

	private void delete() {
		this.forumBusiness.deleteThread(this._threadID);
		setParentToReload();
		close();
	}

	private void save(IWContext iwc) {
		String headline = iwc.getParameter(ForumBusiness.PARAMETER_THREAD_HEADLINE);
		String body = iwc.getParameter(ForumBusiness.PARAMETER_THREAD_BODY);
		String name = iwc.getParameter(ForumBusiness.PARAMETER_USER_NAME);
		String email = iwc.getParameter(ForumBusiness.PARAMETER_USER_EMAIL);
		
		if(headline == null || "".equals(headline)) {
			this.someErrorMessage = this._iwrb.getLocalizedString("cannot_save", "Cannot save");
			this.errorDetail = this._iwrb.getLocalizedString("headline_is_empty", "Threads headline is empty");
		} else if ( body == null || "".equals(body)) {
			this.someErrorMessage = this._iwrb.getLocalizedString("cannot_save", "Cannot save");
			this.errorDetail = this._iwrb.getLocalizedString("body_is_empty", "Threads body is empty");
		} else {
			this.forumBusiness.saveThread(this._topicID, this._threadID, this._parentThreadID, this._userID, name, email, headline, body);
			setParentToReload();
			close();
			
		}
	}

	private void close(IWContext iwc) {
		setParentToReload();
		close();
	}

	private void noAccess() throws IOException, SQLException {
		close();
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

}