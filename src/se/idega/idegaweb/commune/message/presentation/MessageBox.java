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

import com.idega.presentation.CollectionNavigator;
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
	private int _messageSize = 0;
	private int _numberPerPage = 10;
	private int _start = 0;
	
	private boolean useStyleNames = false;
	private int firstColumnPadding = 12;
	
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
		
		int row = 2;
		Table messageTable = new Table();
		messageTable.setWidth(getWidth());
		messageTable.setCellpadding(getCellpadding());
		messageTable.setCellspacing(getCellspacing());
		f.add(messageTable);


		addTableHeader(messageTable, row);
		if (useStyleNames) {
			messageTable.setCellpaddingLeft(getMessageNumberColumn(), row, firstColumnPadding);
			messageTable.setRowStyleClass(row++, getStyleName(STYLENAME_HEADER_ROW));
		}
		else {
			messageTable.setRowColor(row++, getHeaderColor());
		}
		boolean hasMessages = false;

		if (iwc.isLoggedOn()) {
			MessageSession messageSession = getMessageSession(iwc);
			User user = iwc.getCurrentUser();

			CollectionNavigator navigator = getNavigator(iwc, user);

			if (useStyleNames) {
				messageTable.setRowStyleClass(1, getHeadingRowClass());
				messageTable.mergeCells(1, 1, 2, 1);
				messageTable.add(localize("message.messages", "Messages"), 1, 1);
				messageTable.mergeCells(3, 1, 4, 1);
				messageTable.setAlignment(3, 1, Table.HORIZONTAL_ALIGN_RIGHT);
				
				navigator.setUseShortText(true);
				navigator.setWidth(50);
				navigator.setLinkStyle(getStyleName(STYLENAME_SMALL_HEADER_LINK));
				navigator.setTextStyle(getStyleName(STYLENAME_SMALL_HEADER));
				messageTable.add(navigator, 3, 1);
			}
			else {
				messageTable.mergeCells(1, 1, messageTable.getColumns(), 1);
				messageTable.add(navigator, 1, 1);
			}
			
			Collection messages = getMessages(iwc, user, _numberPerPage, _start); 
						
//			Link subject = null;
//			Text date = null;
//			CheckBox deleteCheck = null;
//			boolean isRead = false;
			DateFormat dateFormat = null;
			if (useStyleNames) {
				dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());
			}
			else {
				dateFormat = CustomDateFormat.getDateTimeInstance(iwc.getCurrentLocale());
			}

			if (messages != null && !messages.isEmpty()) {
				hasMessages = true;
				Vector messageVector = new Vector(messages);
				Collections.sort(messageVector, new MessageComparator());
				Iterator iter = messageVector.iterator();
				int messageNumber = _start + 1;
				while (iter.hasNext()) {
					Message msg = (Message) iter.next();
					
					addMessageToTable(iwc, messageTable, msg, row, dateFormat, messageNumber++);

					if (useStyleNames) {
						messageTable.setAlignment(getMessageNumberColumn(), row, Table.HORIZONTAL_ALIGN_CENTER);
						messageTable.setCellpaddingLeft(getMessageNumberColumn(), row, firstColumnPadding);
						if (row % 2 == 0) {
							messageTable.setRowStyleClass(row++, getStyleName(STYLENAME_LIGHT_ROW));
						}
						else {
							messageTable.setRowStyleClass(row++, getStyleName(STYLENAME_DARK_ROW));
						}
					}
					else {
						if (row % 2 == 0) {
							messageTable.setRowColor(row++, getZebraColor1());
						}
						else {
							messageTable.setRowColor(row++, getZebraColor2());
						}
					}
				}
			}

			messageTable.setColumnAlignment(getDeleteColumn(), Table.HORIZONTAL_ALIGN_CENTER);
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
			messageTable.setCellpaddingRight(1, row, 12);
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

	private CollectionNavigator getNavigator(IWContext iwc, User user) {
		_messageSize = getNumberOfMessages(iwc, user);

		CollectionNavigator navigator = new CollectionNavigator(_messageSize);
		navigator.setIdentifier("msgbx");
		navigator.setTextStyle(STYLENAME_SMALL_TEXT);
		navigator.setLinkStyle(STYLENAME_SMALL_LINK);
		navigator.setNumberOfEntriesPerPage(_numberPerPage);
		navigator.setPadding(getCellpadding());
		_start = navigator.getStart(iwc);
		
		return navigator;
	}
	
	int getNumberOfMessages(IWContext iwc, User user) {
		try {
			return getMessageBusiness(iwc).getNumberOfMessages(user);
		}
		catch (Exception e) {
			return 0;
		}
	}
	
/**
 * Adds headers to the table. 
 * @param messageTable
 */
	void addTableHeader(Table messageTable, int row) {
		messageTable.add(getSmallHeader(localize("message.#", "#")), getMessageNumberColumn(), row);
		messageTable.setAlignment(getMessageNumberColumn(), row, Table.HORIZONTAL_ALIGN_CENTER);
		messageTable.setWidth(getMessageNumberColumn(), row, 8);
		messageTable.add(getSmallHeader(localize("message.subject", "Subject")), getSubjectColumn(), row);
		messageTable.add(getSmallHeader(localize("message.date", "Date")), getDateColumn(), row);
		messageTable.add(getSmallHeader(localize("message.delete", "Delete")), getDeleteColumn(), row);
		//messageTable.setWidth(getDeleteColumn(), "12");
	}
	
	/**
	 * Returns the messages for the user specified. This method is called from subclass AdminMessageBox.
	 * @param iwc Used in subclass implemetation
	 * @param user
	 * @param messageBusiness
	 * @return
	 * @throws Exception
	 */
	Collection getMessages(IWContext iwc, User user, int numberOfEntries, int startingEntry) throws Exception{
		return getMessageBusiness(iwc).findMessages(user, numberOfEntries, startingEntry);		
	}
	
	/**
	 * Adds the messages to the Table. . This method is called from subclass AdminMessageBox
	 * @param iwc
	 * @param messageTable
	 * @param msg
	 * @param row
	 * @param dateFormat
	 * @throws Exception
	 */
	void addMessageToTable(IWContext iwc, Table messageTable, Message msg, int row, DateFormat dateFormat, int messageNumber) throws Exception{
		Date msgDate = new Date(msg.getCreated().getTime());

		boolean isRead = getMessageBusiness(iwc).isMessageRead(msg);
					
		Link subject = getSmallLink(msg.getSubject());
		subject.setWindowToOpen(MessageViewerWindow.class);
		subject.addParameter(PARAM_MESSAGE_ID, msg.getPrimaryKey().toString());
		subject.addParameter(MessageViewer.PARAMETER_METHOD, MessageViewer.METHOD_VIEW_MESSAGE);
		subject.addParameter(MessageViewer.PARAMETER_PAGE_ID, this.getParentPageID());
		if (!isRead)
			subject.setBold();
					
		Text date = this.getSmallText(dateFormat.format(msgDate));
		if (!isRead)
			date.setBold();
					
		CheckBox deleteCheck = getCheckBox(PARAM_MESSAGE_ID, msg.getPrimaryKey().toString());

		messageTable.add(getSmallHeader(String.valueOf(messageNumber)), getMessageNumberColumn(), row);
		messageTable.setAlignment(getMessageNumberColumn(), row, Table.HORIZONTAL_ALIGN_CENTER);
		messageTable.setWidth(getMessageNumberColumn(), row, 8);
		messageTable.add(subject, getSubjectColumn(), row);
		messageTable.add(date, getDateColumn(), row);
		messageTable.add(deleteCheck, getDeleteColumn(), row);	
	}
	
	int getMessageNumberColumn(){
		return 1;
	}
	
	int getSubjectColumn(){
		return 2;
	}
	
	int getDateColumn(){
		return 3;
	}
	
	int getDeleteColumn(){
		return 4;
	}
	

	protected MessageBusiness getMessageBusiness(IWContext iwc) throws Exception {
		return (MessageBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, MessageBusiness.class);
	}

  private MessageSession getMessageSession(IWContext iwc) throws Exception {
    return (MessageSession)com.idega.business.IBOLookup.getSessionInstance(iwc,MessageSession.class);
  }
	/**
	 * @param numberPerPage The numberPerPage to set.
	 */
	public void setNumberPerPage(int numberPerPage) {
		this._numberPerPage = numberPerPage;
	}
	/**
	 * @param useStyleNames The useStyleNames to set.
	 */
	public void setUseStyleNames(boolean useStyleNames) {
		this.useStyleNames = useStyleNames;
	}
	/**
	 * @param firstColumnPadding The firstColumnPadding to set.
	 */
	public void setFirstColumnPadding(int firstColumnPadding) {
		this.firstColumnPadding = firstColumnPadding;
	}
}