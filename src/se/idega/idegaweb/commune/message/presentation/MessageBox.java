package se.idega.idegaweb.commune.message.presentation;

import java.util.*;

import se.idega.idegaweb.commune.presentation.*;
import se.idega.idegaweb.commune.message.data.*;
import se.idega.idegaweb.commune.message.business.*;

import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class MessageBox extends CommuneBlock {

  private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune.message";

  private final static int ACTION_VIEW_MESSAGE_LIST = 1;
  private final static int ACTION_VIEW_MESSAGE = 2;
  private final static int ACTION_SHOW_DELETE_INFO = 3;
  private final static int ACTION_DELETE_MESSAGE = 4;

  private final static String PARAM_VIEW_MESSAGE = "msg_view_msg";
  private final static String PARAM_VIEW_MESSAGE_LIST = "msg_view_msg_list";
  private final static String PARAM_MESSAGE_ID = "msg_id";
  private final static String PARAM_SHOW_DELETE_INFO = "msg_s_delete_i";
  private final static String PARAM_DELETE_MESSAGE = "msg_delete_message";

  private Table mainTable = null;

  public MessageBox() {
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    this.setResourceBundle(getResourceBundle(iwc));

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

    return action;
  }

  private void viewMessageList(IWContext iwc)throws Exception{
    add(getLocalizedHeader("message.my_messages", "My messages"));
    add(new Break(2));

    Form f = new Form();
    ColumnList messageList = new ColumnList(4);
    f.add(messageList);
    messageList.setBackroundColor("#e0e0e0");
    messageList.setHeader(localize("message.date","Date"),1);
    messageList.setHeader(localize("message.from","From"),2);
    messageList.setHeader(localize("message.subject","Subject"),3);

    int userId = iwc.getUserId();
    Collection messages = getMessageBusiness(iwc).findMessages(userId);

    Iterator iter = messages.iterator();
    while (iter.hasNext()) {
      Message msg = (Message)iter.next();
      Link l = new Link(msg.getDateString());
      l.addParameter(PARAM_VIEW_MESSAGE,"true");
      l.addParameter(PARAM_MESSAGE_ID,msg.getPrimaryKey().toString());
      messageList.add(l);
      messageList.add(msg.getSenderName());
      messageList.add(msg.getSubject());
      CheckBox deleteCheckbox = new CheckBox(PARAM_MESSAGE_ID,msg.getPrimaryKey().toString());
      messageList.add(deleteCheckbox);
    }
    Link deleteButton = getLocalizedLink("message.delete","Delete");
    deleteButton.setAsImageButton(true);
    deleteButton.setToFormSubmit(f);

    messageList.skip(3);
    PresentationObject[] bottomRow = new PresentationObject[4];
    bottomRow[3] = deleteButton;
    messageList.addBottomRow(bottomRow);

    f.addParameter(PARAM_SHOW_DELETE_INFO,"true");
    add(f);
  }

  private void viewMessage(IWContext iwc)throws Exception{
    Message msg = getMessage(iwc.getParameter(PARAM_MESSAGE_ID),iwc);

    add(getLocalizedHeader("message.message","Message"));
    add(new Break(2));
    add(getLocalizedText("message.from","From"));
    add(getText(": "));
    add(getLink(msg.getSenderName()));
    add(new Break(2));
    add(getLocalizedText("message.date","Date"));
    add(getText(": "+msg.getDateString()));
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
      Message msg = getMessage(ids[i],iwc);
      add(getText(msg.getSubject()));
      add(new Break(2));
    }
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
