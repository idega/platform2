package se.idega.idegaweb.commune.message.presentation;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.business.MessageComparator;
import se.idega.idegaweb.commune.message.business.MessageSession;
import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.message.event.MessageListener;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.CustomDateFormat;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class MessageBox extends CommuneBlock {

  private final static String PARAM_MESSAGE_ID = MessageListener.PARAM_MESSAGE_ID;
  private final static String PARAM_TO_MSG_BOX = MessageListener.PARAM_TO_MSG_BOX;
  private final static String PARAM_TO_EMAIL = MessageListener.PARAM_TO_EMAIL;
	private final static String PARAM_DELETE_MESSAGE = MessageListener.PARAM_DELETE_MESSAGE;
	private final static String PARAM_SAVE_SETTINGS = MessageListener.PARAM_SAVE_SETTINGS;
	
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";

	private boolean showSettings = false;
	
	public MessageBox() {
	}

	public boolean getShowSetting() {
		return showSettings;
	}
	
	public void setShowSettings(boolean showSettings) {
		this.showSettings = showSettings;
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void main(IWContext iwc) {
		this.setResourceBundle(getResourceBundle(iwc));

		try {
			viewMessageList(iwc);
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	private void viewMessageList(IWContext iwc) throws Exception {
		Form f = new Form();
		f.setEventListener(MessageListener.class);
		
		int row = 1;
		Table messageTable = new Table();
		messageTable.setWidth(getWidth());
		messageTable.setCellpadding(getCellpadding());
		messageTable.setCellspacing(getCellspacing());
		f.add(messageTable);

		messageTable.add(getSmallHeader(localize("message.subject", "Subject")), 1, row);
		messageTable.add(getSmallHeader(localize("message.date", "Date")), 2, row);
		messageTable.setWidth(3, "12");
		messageTable.setRowColor(row++, getHeaderColor());
		boolean hasMessages = false;

		if (iwc.isLoggedOn()) {
			MessageBusiness messageBusiness = getMessageBusiness(iwc);
			MessageSession messageSession = getMessageSession(iwc);
			
			User user = iwc.getCurrentUser();
			Collection messages = messageBusiness.findMessages(user);
			Link subject = null;
			Text date = null;
			CheckBox deleteCheck = null;
			boolean isRead = false;
			DateFormat dateFormat = CustomDateFormat.getDateTimeInstance(iwc.getCurrentLocale());

			if (messages != null && !messages.isEmpty()) {
				hasMessages = true;
				Vector messageVector = new Vector(messages);
				Collections.sort(messageVector, new MessageComparator());
				Iterator iter = messageVector.iterator();
				while (iter.hasNext()) {
					Message msg = (Message) iter.next();
					Date msgDate = new Date(msg.getCreated().getTime());

					isRead = getMessageBusiness(iwc).isMessageRead(msg);
					
					subject = getSmallLink(msg.getSubject());
					subject.setWindowToOpen(MessageViewerWindow.class);
					subject.addParameter(PARAM_MESSAGE_ID, msg.getPrimaryKey().toString());
					subject.addParameter(MessageViewer.PARAMETER_METHOD, MessageViewer.METHOD_VIEW_MESSAGE);
					subject.addParameter(MessageViewer.PARAMETER_PAGE_ID, this.getParentPageID());
					if (!isRead)
						subject.setBold();
					
					date = this.getSmallText(dateFormat.format(msgDate));
					if (!isRead)
						date.setBold();
					
					deleteCheck = getCheckBox(PARAM_MESSAGE_ID, msg.getPrimaryKey().toString());

					messageTable.add(subject, 1, row);
					messageTable.add(date, 2, row);
					messageTable.add(deleteCheck, 3, row);
					if (row % 2 == 0)
						messageTable.setRowColor(row++, getZebraColor1());
					else
						messageTable.setRowColor(row++, getZebraColor2());
				}
			}

			messageTable.setHeight(row++,5);
			
			if (showSettings) {
				Table settingsTable = new Table(3, 3);
				settingsTable.setCellpaddingAndCellspacing(0);
				settingsTable.setWidth(2, "4");
				settingsTable.setHeight(2,"4");
				messageTable.mergeCells(1, row, messageTable.getColumns(), row);
				messageTable.add(settingsTable, 1, row++);
	
				boolean toMessageBox = messageSession.getIfUserPreferesMessageInMessageBox();
				CheckBox msgBox = getCheckBox(PARAM_TO_MSG_BOX,"true");
				msgBox.setChecked(toMessageBox);
				
				boolean toEmail = messageSession.getIfUserPreferesMessageByEmail();
				CheckBox email = getCheckBox(PARAM_TO_EMAIL,"true");
				email.setChecked(toEmail);
				
				settingsTable.add(msgBox, 1, 1);
				settingsTable.add(email, 1, 3);
				settingsTable.add(getSmallText(getLocalizedString("message.send_to_message_box", "Send to message box", iwc)), 3, 1);
				settingsTable.add(getSmallText(getLocalizedString("message.send_to_email", "Send to email", iwc)), 3, 3);
			}
			
			messageTable.setHeight(row++,5);
			
			Table submitTable = new Table(2, 1);
			submitTable.setCellpaddingAndCellspacing(0);
			submitTable.setWidth(Table.HUNDRED_PERCENT);
			submitTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
			messageTable.mergeCells(1, row, messageTable.getColumns(), row);
			messageTable.add(submitTable, 1, row);

			SubmitButton deleteButton = (SubmitButton) getButton(new SubmitButton(localize("delete", "Delete"), PARAM_DELETE_MESSAGE, "true"));
			deleteButton.setToEnableWhenChecked(PARAM_MESSAGE_ID);
			deleteButton.setDescription(localize("message.delete", "Delete"));
			deleteButton.setSubmitConfirm(localize("message.messages_to_delete", "Do you really want to delete the selected messages?"));
			if (hasMessages) {
				submitTable.add(deleteButton, 2, 1);
			}
			
			if (showSettings) {
				SubmitButton settings = (SubmitButton) getButton(new SubmitButton(localize("save", "Save"), PARAM_SAVE_SETTINGS, "true"));
				settings.setDescription(localize("message.settings", "Save settings"));
				submitTable.add(settings, 1, 1);
			}
		}

		add(f);
	}

	private MessageBusiness getMessageBusiness(IWContext iwc) throws Exception {
		return (MessageBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, MessageBusiness.class);
	}

  private MessageSession getMessageSession(IWContext iwc) throws Exception {
    return (MessageSession)com.idega.business.IBOLookup.getSessionInstance(iwc,MessageSession.class);
  }
}