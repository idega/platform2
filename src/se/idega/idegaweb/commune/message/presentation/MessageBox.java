package se.idega.idegaweb.commune.message.presentation;

import java.text.DateFormat;
import java.util.*;

import javax.ejb.EJBException;

import se.idega.idegaweb.commune.presentation.*;
import se.idega.idegaweb.commune.message.data.*;
import se.idega.idegaweb.commune.message.business.*;

import com.idega.block.process.data.CaseStatus;
import com.idega.core.user.data.User;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.user.Converter;
import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class MessageBox extends CommuneBlock {

  private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";

  private final static int ACTION_VIEW_MESSAGE_LIST = 1;
  private final static int ACTION_VIEW_MESSAGE = 2;
  private final static int ACTION_SHOW_DELETE_INFO = 3;
  private final static int ACTION_DELETE_MESSAGE = 4;

  private final static String PARAM_VIEW_MESSAGE = "msg_view_msg";
  private final static String PARAM_VIEW_MESSAGE_LIST = "msg_view_msg_list";
  private final static String PARAM_MESSAGE_ID = "msg_id";
  private final static String PARAM_SAVE_SETTINGS = "msg_save_settings";
  private final static String PARAM_SHOW_DELETE_INFO = "msg_s_delete_i";
  private final static String PARAM_DELETE_MESSAGE = "msg_delete_message";
  
  private final static String PARAM_TO_MSG_BOX = "msg_to_msg_box";
  private final static String PARAM_TO_EMAIL = "msg_to_email";
  
  public static final String SEND_TO_MESSAGE_BOX = "msg_send_box";
  public static final String SEND_TO_EMAIL = "msg_send_email";
  
  private IWPropertyList userProperties;

  private Table mainTable = null;

  public MessageBox() {
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    this.setResourceBundle(getResourceBundle(iwc));
    userProperties = getUserProperties(iwc);
    
    try{
      int action = parseAction(iwc);
      switch(action){
        case ACTION_VIEW_MESSAGE_LIST:
          viewMessageList(iwc);
          break;
        case ACTION_VIEW_MESSAGE:
          viewMessage(iwc);
          break;
        case ACTION_SHOW_DELETE_INFO:
          showDeleteInfo(iwc);
          break;
        case ACTION_DELETE_MESSAGE:
          deleteMessage(iwc);
          viewMessageList(iwc);
        	break;
        default:
          break;
      }
      super.add(mainTable);
    } catch (Exception e) {
      super.add(new ExceptionWrapper(e,this));
    }
  }

  public void add(PresentationObject po){
    if(mainTable==null){
      mainTable = new Table();
      mainTable.setCellpadding(14);
      mainTable.setCellspacing(0);
      mainTable.setColor(getBackgroundColor());
      mainTable.setWidth(600);
    }
    mainTable.add(po);
  }

  private int parseAction(IWContext iwc){
    int action = ACTION_VIEW_MESSAGE_LIST;

    if(iwc.isParameterSet(PARAM_VIEW_MESSAGE)){
      action = ACTION_VIEW_MESSAGE;
    }
    if(iwc.isParameterSet(PARAM_SHOW_DELETE_INFO)){
      action = ACTION_SHOW_DELETE_INFO;
    }
    if(iwc.isParameterSet(PARAM_DELETE_MESSAGE)){
      action = ACTION_DELETE_MESSAGE;
    }
    if (iwc.isParameterSet(PARAM_SAVE_SETTINGS)){
    	saveSettings(iwc);
    }

    return action;
  }

  private void viewMessageList(IWContext iwc)throws Exception{
    add(getLocalizedHeader("message.my_messages", "My messages"));
    add(new Break(2));

    Form f = new Form();
    Table table = new Table(1,3);
    f.add(table);
    
    ColumnList messageList = new ColumnList(3);
    table.add(messageList,1,1);
    messageList.setBackroundColor("#e0e0e0");
    messageList.setHeader(localize("message.subject","Subject"),1);
    messageList.setHeader(localize("message.date","Date"),2);
    
    if ( iwc.isLoggedOn() ) {
	    Collection messages = getMessageBusiness(iwc).findMessages(Converter.convertToNewUser(iwc.getUser()));
	    Link subject = null;
	    Text date = null;
	    CheckBox deleteCheck = null;
	    boolean isRead = false;
	    DateFormat dateFormat = com.idega.util.CustomDateFormat.getDateTimeInstance(iwc.getCurrentLocale());
	
	    if ( messages != null ) {
	    	Vector messageVector = new Vector(messages);
	    	Collections.sort(messageVector,new MessageComparator());
		    Iterator iter = messageVector.iterator();
		    while (iter.hasNext()) {
		      Message msg = (Message)iter.next();
		      Date msgDate = new Date(msg.getCreated().getTime());
		      
		      isRead = getMessageBusiness(iwc).isMessageRead(msg);
		      subject = new Link(msg.getSubject());
		      subject.addParameter(PARAM_VIEW_MESSAGE,"true");
		      subject.addParameter(PARAM_MESSAGE_ID,msg.getPrimaryKey().toString());
		      if ( !isRead )
		      	subject.setBold();
		      date = this.getSmallText(dateFormat.format(msgDate));
		      if ( !isRead )
		      	date.setBold();
		      deleteCheck = new CheckBox(PARAM_MESSAGE_ID,msg.getPrimaryKey().toString());
		      
		      messageList.add(subject);
		      messageList.add(date);
		      messageList.add(deleteCheck);
		    }
	    }
	
	  	Table settingsTable = new Table(2,2);
	  	table.add(settingsTable,1,2);
	  	
	  	CheckBox msgBox = new CheckBox(PARAM_TO_MSG_BOX);
	  		msgBox.setChecked(true);
	  	CheckBox email = new CheckBox(PARAM_TO_EMAIL);
	  		email.setChecked(true);
	  	
	  	if ( userProperties.getProperty(SEND_TO_MESSAGE_BOX) != null )
	  		msgBox.setChecked(new Boolean(userProperties.getProperty(SEND_TO_MESSAGE_BOX)).booleanValue());
	  	if ( userProperties.getProperty(SEND_TO_EMAIL) != null )
	  		email.setChecked(new Boolean(userProperties.getProperty(SEND_TO_EMAIL)).booleanValue());
	  		
	  	settingsTable.add(msgBox,1,1);
	  	settingsTable.add(email,1,2);
	  	settingsTable.add(getText(getLocalizedString("message.send_to_message_box", "Send to message box", iwc)),2,1);
	  	settingsTable.add(getText(getLocalizedString("message.send_to_email", "Send to email", iwc)),2,2);
	    
	    Table submitTable = new Table(2,1);
	    submitTable.setCellpaddingAndCellspacing(0);
	    submitTable.setWidth(Table.HUNDRED_PERCENT);
	    submitTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
	    table.add(submitTable,1,3);
	    
	    SubmitButton deleteButton = new SubmitButton(this.getLocalizedString("message.delete", "Delete", iwc),PARAM_SHOW_DELETE_INFO,"true");
	    deleteButton.setAsImageButton(true);
	    submitTable.add(deleteButton,2,1);
	    
	    SubmitButton settings = new SubmitButton(this.getLocalizedString("message.settings", "Settings", iwc),PARAM_SAVE_SETTINGS,"true");
	    settings.setAsImageButton(true);
			submitTable.add(settings,1,1);	
    }

    add(f);
  }

  private void viewMessage(IWContext iwc)throws Exception{
    Message msg = getMessage(iwc.getParameter(PARAM_MESSAGE_ID),iwc);
    getMessageBusiness(iwc).markMessageAsRead(msg);

    add(getLocalizedHeader("message.message","Message"));
    add(new Break(2));
    add(getLocalizedText("message.from","From"));
    add(getText(": "));
    //add(getLink(msg.getSenderName()));
    add(new Break(2));
    add(getLocalizedText("message.date","Date"));
    add(getText(": "+(new IWTimestamp(msg.getCreated())).getLocaleDate(iwc)));
    add(new Break(2));
    add(getLocalizedText("message.subject","Subject"));
    add(getText(": "+msg.getSubject()));
    add(new Break(2));
    add(getText(msg.getBody()));

    add(new Break(2));
    Table t = new Table();
    t.setWidth("100%");
    t.setAlignment(1,1,"right");
    Link l = getLocalizedLink("message.back", "Back");
    l.addParameter(PARAM_VIEW_MESSAGE_LIST,"true");
    l.setAsImageButton(true);
    t.add(l,1,1);
    add(t);
  }

  private void showDeleteInfo(IWContext iwc)throws Exception{
    String[] ids = iwc.getParameterValues(PARAM_MESSAGE_ID);
    int msgId = 0;
    int nrOfMessagesToDelete = 0;
    if(ids!=null){
      nrOfMessagesToDelete = ids.length;
      msgId = Integer.parseInt(ids[0]);
    }

    if(nrOfMessagesToDelete==1){
      add(getLocalizedHeader("message.delete_message","Delete message"));
    }else{
      add(getLocalizedHeader("message.delete_messages","Delete messages"));
    }
    add(new Break(2));

    String s = null;
    if(nrOfMessagesToDelete==0){
      s = localize("message.no_messages_to_delete","No messages selected. You have to mark the message(s) to delete.");
    }else if(nrOfMessagesToDelete==1){
      Message msg = getMessageBusiness(iwc).getUserMessage(msgId);
      s = localize("message.one_message_to_delete","Do you really want to delete the message with subject: ")+msg.getSubject()+"?";
    }else{
      s = localize("message.messages_to_delete","Do you really want to delete the selected messages?");
    }

    Table t = new Table(1,5);
    t.setWidth("100%");
    t.add(getText(s),1,1);
    t.setAlignment(1,1,"center");
    if(nrOfMessagesToDelete==0){
      Link l = getLocalizedLink("message.back","back");
      l.addParameter(PARAM_VIEW_MESSAGE_LIST,"true");
      l.setAsImageButton(true);
      t.add(l,1,4);
    }else{
      Link l = getLocalizedLink("message.ok","OK");
      l.addParameter(PARAM_DELETE_MESSAGE,"true");
      for(int i=0; i<ids.length; i++){
        l.addParameter(PARAM_MESSAGE_ID,ids[i]);
      }
      l.setAsImageButton(true);
      t.add(l,1,4);
      t.add(getText(" "),1,4);
      l = getLocalizedLink("message.cancel","Cancel");
      l.addParameter(PARAM_VIEW_MESSAGE_LIST,"true");
      l.setAsImageButton(true);
      t.add(l,1,4);
    }
    t.setAlignment(1,4,"center");
    add(t);
  }

  private void deleteMessage(IWContext iwc)throws Exception{
    String[] ids = iwc.getParameterValues(PARAM_MESSAGE_ID);
    for(int i=0; i<ids.length; i++){
      getMessageBusiness(iwc).deleteUserMessage(Integer.parseInt(ids[i]));
    }
  }

  private void saveSettings(IWContext iwc) {
  	String toMsgBox = iwc.getParameter(PARAM_TO_MSG_BOX);
  	if ( toMsgBox == null )
  		userProperties.setProperty(SEND_TO_MESSAGE_BOX, new Boolean(false));
  	else
  		userProperties.setProperty(SEND_TO_MESSAGE_BOX, new Boolean(true));
  		
  	String toEmail = iwc.getParameter(PARAM_TO_EMAIL);
  	if ( toEmail == null )
  		userProperties.setProperty(SEND_TO_EMAIL, new Boolean(false));
  	else
  		userProperties.setProperty(SEND_TO_EMAIL, new Boolean(true));
  }
  
  private MessageBusiness getMessageBusiness(IWContext iwc) throws Exception {
    return (MessageBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,MessageBusiness.class);
  }

  private Message getMessage(String id, IWContext iwc)throws Exception{
    int msgId = Integer.parseInt(id);
    Message msg = getMessageBusiness(iwc).getUserMessage(msgId);
    return msg;
  }
}
