package se.idega.idegaweb.commune.printing.presentation;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.business.MessageComparator;
import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.presentation.ColumnList;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.printing.business.DocumentBusiness;
import se.idega.idegaweb.commune.printing.data.PrintDocuments;

import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Title: PrintedLetterViewer
 * Description: A class to view and manage PrintedLetterMessage in the idegaWeb Commune application
 * Copyright:    Copyright idega Software (c) 2002
 * Company:	idega Software
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class PrintDocumentsViewer extends CommuneBlock {

  private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";

  private final static int ACTION_VIEW_MESSAGE_OVERVIEW = 0;
  private final static int ACTION_VIEW_MESSAGE_LIST = 1;
  private final static int ACTION_VIEW_MESSAGE = 2;
  private final static int ACTION_SHOW_DELETE_INFO = 3;
  private final static int ACTION_DELETE_MESSAGE = 4;
  private final static int ACTION_PRINT_UNPRINTED_MESSAGES = 5;

  private final static String PARAM_VIEW_MESSAGE = "prv_view_msg";
  private final static String PARAM_VIEW_MESSAGE_LIST = "prv_view_msg_list";
  private final static String PARAM_MESSAGE_ID = "prv_id";
  private final static String PARAM_SHOW_DELETE_INFO = "prv_s_delete_i";
  private final static String PARAM_DELETE_MESSAGE = "prv_delete_message";
  private final static String PARAM_PRINT_UNPRINTED = "prv_unprinted";

  private Table mainTable = null;

  public PrintDocumentsViewer() {
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    this.setResourceBundle(getResourceBundle(iwc));

    try{
      int action = parseAction(iwc);
      switch(action){
      	case ACTION_VIEW_MESSAGE_OVERVIEW:
      		viewMessageOverview(iwc);
      		break;
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
        case ACTION_PRINT_UNPRINTED_MESSAGES:
          printAllUnPrintedMessages(iwc);
          viewMessageOverview(iwc);
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
      mainTable.setWidth(getWidth());
    }
    mainTable.add(po);
  }

  private int parseAction(IWContext iwc)throws Exception{
    int action = ACTION_VIEW_MESSAGE_OVERVIEW;

    if(iwc.isParameterSet(PARAM_VIEW_MESSAGE)){
      action = ACTION_VIEW_MESSAGE;
    }
    else if(iwc.isParameterSet(PARAM_SHOW_DELETE_INFO)){
      action = ACTION_SHOW_DELETE_INFO;
    }
    else if(iwc.isParameterSet(PARAM_DELETE_MESSAGE)){
      action = ACTION_DELETE_MESSAGE;
    }
    else if(iwc.isParameterSet(PARAM_PRINT_UNPRINTED)){
      action = ACTION_PRINT_UNPRINTED_MESSAGES;
    }

    return action;
  }

  private void printAllUnPrintedMessages(IWContext iwc)throws Exception{
		  int userID = ((Integer)iwc.getCurrentUser().getPrimaryKey()).intValue();
		  getDocumentBusiness(iwc).printAllUnPrintedLetters(userID);
  }

  private void viewMessageOverview(IWContext iwc)throws Exception{

	if ( iwc.isLoggedOn() ) {
	   add(getLocalizedHeader("printdoc.letters", "Letters for printing"));
		ColumnList messageList = new ColumnList(3);
		add(messageList);
		messageList.setWidth(Table.HUNDRED_PERCENT);
		messageList.setBackroundColor("#e0e0e0");
		//messageList.setHeader(localize("printdoc.name","Name"),1);
		messageList.setHeader(localize("printdoc.date","Date"),1);
		messageList.setHeader(localize("printdoc.n_o_docs","Number of documents"),2);

		messageList.add(localize("printdoc.unprinted","Unprinted"));
		//messageList.add("-");
		messageList.add(Integer.toString(getDocumentBusiness(iwc).getUnPrintedLettersCount()));

		Link printLink = new Link(localize("printdoc.print","Print"));
		printLink.addParameter(PARAM_PRINT_UNPRINTED,"true");
		messageList.add(printLink);

		//messageList.skip();

		Collection printDocs = getDocumentBusiness(iwc).getPrintedDocuments();
		Iterator iter = printDocs.iterator();
		while (iter.hasNext()) {
			PrintDocuments doc = (PrintDocuments)iter.next();
			messageList.add(doc.getCreated().toString());
			//messageList.add("-");
			messageList.add(Integer.toString(doc.getNumberOfSubDocuments()));
			int fileID = doc.getDocumentFileID();
			Link viewLink = new Link(localize("printdoc.view","View"));
			viewLink.setFile(fileID);
			messageList.add(viewLink);
		}

	}
	else{
	    add(getLocalizedHeader("printdoc.not_logged_on", "You must be logged on to use this function"));
	}
  }


  private void viewMessageList(IWContext iwc)throws Exception{
    add(getLocalizedHeader("printdoc.pending_letters", "Pending letters for printout"));
    add(new Break(2));

    Form f = new Form();
    Table table = new Table(1,3);
    table.setWidth(Table.HUNDRED_PERCENT);
    table.setCellpaddingAndCellspacing(0);
    f.add(table);

    ColumnList messageList = new ColumnList(4);
    table.add(messageList,1,1);
    messageList.setWidth(Table.HUNDRED_PERCENT);
    messageList.setBackroundColor("#e0e0e0");
    messageList.setHeader(localize("printdoc.subject","Subject"),1);
    messageList.setHeader(localize("printdoc.date","Date"),2);
    messageList.setHeader(localize("printdoc.for_user","For user"),3);

    if ( iwc.isLoggedOn() ) {
    	MessageBusiness messageBusiness = getMessageBusiness(iwc);
    	User user = iwc.getCurrentUser();
	    Collection messages = messageBusiness.getUnPrintedLetterMessages();
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
		      User owner = msg.getOwner();
		      Text tOwnerName = getSmallText(owner.getName());
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
		      messageList.add(tOwnerName);
		      messageList.add(deleteCheck);
		    }
	    }

	    Table submitTable = new Table(3,1);
	    submitTable.setCellpaddingAndCellspacing(0);
	    submitTable.setWidth(2,1,"6");
	    table.add(submitTable,1,3);

	    SubmitButton deleteButton = new SubmitButton(this.getLocalizedString("printdoc.delete", "Delete", iwc),PARAM_SHOW_DELETE_INFO,"true");
	    deleteButton.setAsImageButton(true);
	    submitTable.add(deleteButton,1,1);

    }

    add(f);
  }

  private void viewMessage(IWContext iwc)throws Exception{
    Message msg = getMessage(iwc.getParameter(PARAM_MESSAGE_ID),iwc);
    getMessageBusiness(iwc).markMessageAsRead(msg);

    add(getLocalizedHeader("printdoc.message","Message"));
    add(new Break(2));
    add(getLocalizedText("printdoc.from","From"));
    add(getText(": "));
    //add(getLink(msg.getSenderName()));
    add(new Break(2));
    add(getLocalizedText("printdoc.date","Date"));
    add(getText(": "+(new IWTimestamp(msg.getCreated())).getLocaleDate(iwc)));
    add(new Break(2));
    add(getLocalizedText("printdoc.subject","Subject"));
    add(getText(": "+msg.getSubject()));
    add(new Break(2));
    add(getText(msg.getBody()));

    add(new Break(2));
    Table t = new Table();
    t.setWidth(Table.HUNDRED_PERCENT);
    t.setAlignment(1,1,"right");
    Link l = getLocalizedLink("printdoc.back", "Back");
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
      add(getLocalizedHeader("printdoc.delete_message","Delete message"));
    }else{
      add(getLocalizedHeader("printdoc.delete_messages","Delete messages"));
    }
    add(new Break(2));

    String s = null;
    if(nrOfMessagesToDelete==0){
      s = localize("printdoc.no_messages_to_delete","No messages selected. You have to mark the message(s) to delete.");
    }else if(nrOfMessagesToDelete==1){
      Message msg = getMessageBusiness(iwc).getUserMessage(msgId);
      s = localize("printdoc.one_message_to_delete","Do you really want to delete the message with subject: ")+msg.getSubject()+"?";
    }else{
      s = localize("printdoc.messages_to_delete","Do you really want to delete the selected messages?");
    }

    Table t = new Table(1,5);
    t.setWidth(Table.HUNDRED_PERCENT);
    t.add(getText(s),1,1);
    t.setAlignment(1,1,"center");
    if(nrOfMessagesToDelete==0){
      Link l = getLocalizedLink("printdoc.back","back");
      l.addParameter(PARAM_VIEW_MESSAGE_LIST,"true");
      l.setAsImageButton(true);
      t.add(l,1,4);
    }else{
      Link l = getLocalizedLink("printdoc.ok","OK");
      l.addParameter(PARAM_DELETE_MESSAGE,"true");
      for(int i=0; i<ids.length; i++){
        l.addParameter(PARAM_MESSAGE_ID,ids[i]);
      }
      l.setAsImageButton(true);
      t.add(l,1,4);
      t.add(getText(" "),1,4);
      l = getLocalizedLink("printdoc.cancel","Cancel");
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
      //getMessageBusiness(iwc).deleteUserMessage(Integer.parseInt(ids[i]));
    }
  }

  private MessageBusiness getMessageBusiness(IWContext iwc) throws Exception {
    return (MessageBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,MessageBusiness.class);
  }

  private DocumentBusiness getDocumentBusiness(IWContext iwc) throws Exception {
    return (DocumentBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,DocumentBusiness.class);
  }

  private Message getMessage(String id, IWContext iwc)throws Exception{
    int msgId = Integer.parseInt(id);
    Message msg = getMessageBusiness(iwc).getUserMessage(msgId);
    return msg;
  }
}
