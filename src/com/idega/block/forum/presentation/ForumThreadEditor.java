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
		_isAdmin = true; //AccessControl.hasEditPermission(this,iwc);
		_iwb = iwc.getIWMainApplication().getBundle(Builderaware.IW_CORE_BUNDLE_IDENTIFIER);
		_iwrb = getResourceBundle(iwc);
		_localeID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());
		_isLoggedOn = iwc.isLoggedOn();
		if (_isLoggedOn) _userID = iwc.getUserId();

		addTitle(_iwrb.getLocalizedString("thread_editor", "Write new thread"));
		forumBusiness = new ForumBusiness();

		if (_isAdmin) {
			processForm(iwc);
		}
		else {
			noAccess();
		}
	}

	private void processForm(IWContext iwc) {
		if (iwc.getParameter(ForumBusiness.PARAMETER_TOPIC_ID) != null) {
			try {
				_topicID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_TOPIC_ID));
			}
			catch (NumberFormatException e) {
				_topicID = -1;
			}
		}

		if (iwc.getParameter(ForumBusiness.PARAMETER_PARENT_THREAD_ID) != null) {
			try {
				_parentThreadID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_PARENT_THREAD_ID));
			}
			catch (NumberFormatException e) {
				_threadID = -1;
			}
		}

		if (iwc.getParameter(ForumBusiness.PARAMETER_THREAD_ID) != null) {
			try {
				_threadID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_THREAD_ID));
			}
			catch (NumberFormatException e) {
				_threadID = -1;
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

		if (_threadID != -1) {
			if (iwc.getParameter(ForumBusiness.PARAMETER_MODE) != null) {
				if (iwc.getParameter(ForumBusiness.PARAMETER_MODE).equalsIgnoreCase(ForumBusiness.PARAMETER_DELETE)) {
					delete();
				}
			}
			else {
				_update = true;
			}
		}

		initializeFields();
	}

	private void initializeFields() {
		ForumData thread = null;
		ForumData parentThread = null;
		if (_threadID != -1) {
			thread = forumBusiness.getForumData(_threadID);
			if (thread != null) _update = true;
		}

		if (_parentThreadID != -1) parentThread = forumBusiness.getForumData(_parentThreadID);

		TextInput subject = new TextInput(ForumBusiness.PARAMETER_THREAD_HEADLINE);
		subject.setLength(24);
		if (_update && thread.getThreadSubject() != null) subject.setContent(thread.getThreadSubject());
		if (parentThread != null && parentThread.getThreadSubject() != null) {
			String subjectString = parentThread.getThreadSubject();
			if (subjectString.indexOf("RE:") == -1) subjectString = "RE: " + subjectString;
			subject.setContent(subjectString);
		}

		TextArea body = new TextArea(ForumBusiness.PARAMETER_THREAD_BODY);
		body.setHeight(4);
		if (_update && thread.getThreadBody() != null) body.setContent(thread.getThreadBody());
		if (_isLoggedOn) body.setHeight(10);
		body.setWidth(Table.HUNDRED_PERCENT);

		addLeft(_iwrb.getLocalizedString("thread_subject", "Title") + ":", subject, true);
		addLeft(_iwrb.getLocalizedString("thread_body", "Body") + ":", body, true);

		if (!_isLoggedOn) {
			TextInput userName = new TextInput(ForumBusiness.PARAMETER_USER_NAME);
			if (_update && thread.getUserName() != null) userName.setContent(thread.getUserName());

			TextInput userEmail = new TextInput(ForumBusiness.PARAMETER_USER_EMAIL);
			if (_update && thread.getUserEMail() != null) userEmail.setContent(thread.getUserEMail());

			addLeft(_iwrb.getLocalizedString("thread_username", "Name") + ":", userName, true);
			addLeft(_iwrb.getLocalizedString("thread_email", "E-mail") + ":", userEmail, true);
		}

		addHiddenInput(new HiddenInput(ForumBusiness.PARAMETER_TOPIC_ID, Integer.toString(_topicID)));
		addHiddenInput(new HiddenInput(ForumBusiness.PARAMETER_PARENT_THREAD_ID, Integer.toString(_parentThreadID)));
		addHiddenInput(new HiddenInput(ForumBusiness.PARAMETER_THREAD_ID, Integer.toString(_threadID)));

		addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("close", "CLOSE"), ForumBusiness.PARAMETER_MODE, ForumBusiness.PARAMETER_CLOSE));
		addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("save", "SAVE"), ForumBusiness.PARAMETER_MODE, ForumBusiness.PARAMETER_SAVE));
	}

	private void delete() {
		forumBusiness.deleteThread(_threadID);
		setParentToReload();
		close();
	}

	private void save(IWContext iwc) {
		String headline = iwc.getParameter(ForumBusiness.PARAMETER_THREAD_HEADLINE);
		String body = iwc.getParameter(ForumBusiness.PARAMETER_THREAD_BODY);
		String name = iwc.getParameter(ForumBusiness.PARAMETER_USER_NAME);
		String email = iwc.getParameter(ForumBusiness.PARAMETER_USER_EMAIL);

		forumBusiness.saveThread(_topicID, _threadID, _parentThreadID, _userID, name, email, headline, body);
		setParentToReload();
		close();
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