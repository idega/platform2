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
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
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
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
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
  private final static int ACTION_PRINT_MESSAGE=6;

  private final static String PARAM_VIEW_MESSAGE = "prv_view_msg";
  private final static String PARAM_VIEW_MESSAGE_LIST = "prv_view_msg_list";
  private final static String PARAM_MESSAGE_ID = "prv_id";
  private final static String PARAM_SHOW_DELETE_INFO = "prv_s_delete_i";
  private final static String PARAM_DELETE_MESSAGE = "prv_delete_message";
  private final static String PARAM_PRINT_UNPRINTED = "prv_unprinted";
  private final static String PARAM_PRINT_MSG = "prv_pr_msg";
  private final static String PARAM_LETTER_TYPE = "prv_let_tp";
  
  private final static String PRM_STAMP_P_FROM = "prv_pfrm";;
  private final static String PRM_STAMP_U_FROM= "prv_ufrm";
  private final static String PRM_STAMP_P_TO= "prv_pto";
  private final static String PRM_STAMP_U_TO= "prv_uto";
  
  private final static String PRM_CURSOR_P = "prv_crs_p";
  private final static String PRM_CURSOR_U = "prv_crs_u";
  
  
  private boolean isBulkType = false;
  private boolean showTypesAsDropdown = false;
  private String currentType = "";
  private int msgID = -1;
  private int fileID = -1;
  private IWTimestamp today = IWTimestamp.RightNow(), pFrom=null,pTo=null,uFrom=null,uTo=null;
  private int defaultDays = 7;
  private int defaultShown = 25;
  private int cursor_p = 0;
  private int cursor_u = 0;

  private Table mainTable = null;

  public PrintDocumentsViewer() {
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    this.setResourceBundle(getResourceBundle(iwc));
	if ( iwc.isLoggedOn() ) {
    try{
      initDates(iwc);
      initCursors(iwc);
      int action = parseAction(iwc);
      switch(action){
      	case ACTION_VIEW_MESSAGE_OVERVIEW:
      		//viewMessageOverview(iwc);
      		viewMessages(iwc);
      		break;
        case ACTION_VIEW_MESSAGE_LIST:
          viewMessages(iwc);
          break;
        case ACTION_VIEW_MESSAGE:
          viewMessage(iwc);
          break;
        case ACTION_SHOW_DELETE_INFO:
          showDeleteInfo(iwc);
          break;
        case ACTION_DELETE_MESSAGE:
          deleteMessage(iwc);
          viewMessages(iwc);
        	break;
        case ACTION_PRINT_UNPRINTED_MESSAGES:
          printAllUnPrintedMessages(iwc);
          viewMessages(iwc);
        	break;
       	case ACTION_PRINT_MESSAGE:
          printMessage(iwc);
          viewMessages(iwc);
        	break;
        default:
          break;
      }
      super.add(mainTable);
    } catch (Exception e) {
      super.add(new ExceptionWrapper(e,this));
    }
	}
    else{
	    add(getLocalizedHeader("printdoc.not_logged_on", "You must be logged on to use this function"));
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
    else if(iwc.isParameterSet(PARAM_PRINT_MSG)){
      action = ACTION_PRINT_MESSAGE;
    }
    
    if(iwc.isParameterSet(PARAM_LETTER_TYPE )){
      currentType = iwc.getParameter(PARAM_LETTER_TYPE);
    }
    if("".equals(currentType))
      currentType = getDocumentBusiness(iwc).getPrintedLetterTypes()[0];
     
    isBulkType=getDocumentBusiness(iwc).isBulkLetterType(currentType);
     
    if(iwc.isParameterSet(PARAM_MESSAGE_ID)){
    	msgID = Integer.parseInt(iwc.getParameter(PARAM_MESSAGE_ID));
    }
    return action;
  }
  
  private void initDates(IWContext iwc)throws Exception{
  		pFrom = IWTimestamp.RightNow();
  		pFrom.addDays(-defaultDays);
  		pTo = IWTimestamp.RightNow();
  		uFrom = IWTimestamp.RightNow();
  		uFrom.addDays(-defaultDays);
  		uTo = IWTimestamp.RightNow();
  		if(iwc.isParameterSet(PRM_STAMP_P_FROM))
  			pFrom = new IWTimestamp(iwc.getParameter(PRM_STAMP_P_FROM));
  		if(iwc.isParameterSet(PRM_STAMP_U_FROM))
  			uFrom = new IWTimestamp(iwc.getParameter(PRM_STAMP_U_FROM));
  		if(iwc.isParameterSet(PRM_STAMP_P_TO))
  			pTo = new IWTimestamp(iwc.getParameter(PRM_STAMP_P_TO));
  		if(iwc.isParameterSet(PRM_STAMP_U_TO))
  			uTo = new IWTimestamp(iwc.getParameter(PRM_STAMP_U_TO));
  		
  }
  
  private void initCursors(IWContext iwc)throws Exception{
  		if(iwc.isParameterSet(PRM_CURSOR_P))
  			cursor_p = Integer.parseInt(iwc.getParameter(PRM_CURSOR_P));
  		if(iwc.isParameterSet(PRM_CURSOR_U))
  			cursor_p = Integer.parseInt(iwc.getParameter(PRM_CURSOR_U));
  }

  private void printAllUnPrintedMessages(IWContext iwc)throws Exception{
	int userID = ((Integer)iwc.getCurrentUser().getPrimaryKey()).intValue();
	//getDocumentBusiness(iwc).printAllUnPrintedLetters(userID,currentType);
	Collection unPrintedLetters = getDocumentBusiness(iwc).getPrintedLetterMessageHome().findUnPrintedLettersByType(currentType);
	getDocumentBusiness(iwc).writeBulkPDF(unPrintedLetters,iwc.getCurrentUser(),"BulkLetterPDF",iwc.getApplicationSettings().getDefaultLocale());
  }
  
  private void printMessage(IWContext iwc) throws Exception {
  	int userID = ((Integer)iwc.getCurrentUser().getPrimaryKey()).intValue();
  	if(msgID>0){
  		PrintedLetterMessage msg = (PrintedLetterMessage)getDocumentBusiness(iwc).getPrintedLetterMessageHome().findByPrimaryKey(new Integer(msgID));
  		fileID = getDocumentBusiness(iwc).writePDF(msg,iwc.getCurrentUser(),"LetterPDF",iwc.getApplicationSettings().getDefaultLocale());
  		//System.err.println("file id written :"+fileID);
  		//getDocumentBusiness(iwc).writePrintedLetterPDF(msgId,userID);
  	}
  	
  }
  
  private void viewMessages(IWContext iwc) throws Exception{
  
	    //add(getLocalizedHeader("printdoc.letters", "Letters for printing"));
	  	addTypeMenu(iwc);
	  	if(isBulkType)
	  		addDocumentsList(iwc);
	  	else
	  		addMessagesList(iwc);
	  	
	
  }
  
  private void addTypeMenu(IWContext iwc) throws Exception{
  	 
  	 String[] types = getDocumentBusiness(iwc).getPrintedLetterTypes();
  	 if(showTypesAsDropdown){
  	 	DropdownMenu drp = new DropdownMenu(PARAM_LETTER_TYPE);
  	 	for (int i = 0; i < types.length; i++) {
			drp.addMenuElement(types[i],localize("printdoc.letter_type_"+types[i],types[i]));
		}
		drp.setToSubmit();
		drp.setSelectedElement(currentType);
  	 	Form F = new Form();
  	 	F.add(drp);
  	 	F.add(new HiddenInput(PRM_STAMP_P_FROM,pFrom.toString()));
  	 	F.add(new HiddenInput(PRM_STAMP_P_TO,pTo.toString()));
  	 	F.add(new HiddenInput(PRM_STAMP_U_FROM,uFrom.toString()));
  	 	F.add(new HiddenInput(PRM_STAMP_U_TO,uTo.toString()));
  	 	add(F);
  	 }  	 
  	 else{
	  	 Table T = new Table();
	  	 T.setCellpadding(2);
	  	 int col = 1;
	  	 for (int i = 0; i < types.length; i++) {
			Link typeLink = new Link(getHeader(localize("printdoc.letter_type_"+types[i],types[i])));	
			typeLink.addParameter(PARAM_LETTER_TYPE,types[i]);
			typeLink.addParameter(PRM_STAMP_P_FROM,pFrom.toString());
			typeLink.addParameter(PRM_STAMP_U_FROM,uFrom.toString());
			typeLink.addParameter(PRM_STAMP_P_TO,pTo.toString());
			typeLink.addParameter(PRM_STAMP_U_TO,uTo.toString());
			T.add(typeLink,col++,1);
		}
  	 	add(T);
  	 }
  	
  }
  
  private void addPrintedDatesForm(IWContext iwc){
  	Form F = new Form();
  	Table T = new Table();
  	DateInput from = new DateInput(PRM_STAMP_P_FROM);
  	from =(DateInput) getStyledInterface(from);
  	DateInput to = new DateInput(PRM_STAMP_P_TO);
  	to = (DateInput)getStyledInterface(to);
  	
  	from.setDate(pFrom.getSQLDate());
  	to.setDate(pTo.getSQLDate());
  	SubmitButton search = new SubmitButton(getResourceBundle().getLocalizedString("printdoc.fetch","Fetch"));
  	search = (SubmitButton)getButton(search);
  	T.add(getHeader(getResourceBundle().getLocalizedString("printdoc.date_from","From:")),1,1);
  	T.add(from,2,1);
  	T.add(getHeader(getResourceBundle().getLocalizedString("printdoc.date_to","To:")),3,1);
  	T.add(to,4,1);
  	T.add(search,5,1);
  	T.add(new HiddenInput(PRM_STAMP_U_FROM,uFrom.toString()));
  	T.add(new HiddenInput(PRM_STAMP_U_TO,uTo.toString()));
  	T.add(new HiddenInput(PARAM_LETTER_TYPE,this.currentType));
  	F.add(T);
  	add(F);
  
  }
   private void addUnPrintedDatesForm(IWContext iwc){
  	Form F = new Form();
  	Table T = new Table();
  	DateInput from = new DateInput(PRM_STAMP_U_FROM);
  	from =(DateInput) getStyledInterface(from);
  	DateInput to = new DateInput(PRM_STAMP_U_TO);
  	to = (DateInput)getStyledInterface(to);
  	from.setDate(uFrom.getSQLDate());
  	to.setDate(uTo.getSQLDate());
  	SubmitButton search = new SubmitButton(getResourceBundle().getLocalizedString("printdoc.fetch","Fetch"));
  	search = (SubmitButton)getButton(search);
  	T.add(getHeader(getResourceBundle().getLocalizedString("printdoc.date_from","From:")),1,1);
  	T.add(from,2,1);
  	T.add(getHeader(getResourceBundle().getLocalizedString("printdoc.date_to","To:")),3,1);
  	T.add(to,4,1);
  	T.add(search,5,1);
  	T.add(new HiddenInput(PRM_STAMP_P_FROM,pFrom.toString()));
  	T.add(new HiddenInput(PRM_STAMP_P_TO,pTo.toString()));
  	T.add(new HiddenInput(PARAM_LETTER_TYPE,this.currentType));
  	F.add(T);
  	add(F);
  
  }
  
  private void addCursorLinks(IWContext iwc,int totalsize,int cursor,String cursorPrm){
  	Table T = new Table();
  	T.setAlignment(T.HORIZONTAL_ALIGN_RIGHT);
  	T.setCellpadding(2);
  	if(cursor>0){
  		Link prev  = new Link(localize("printdoc.last","last")+"  "+defaultShown);
  		prev.addParameter(PARAM_LETTER_TYPE,currentType);
  		prev.addParameter(cursorPrm,String.valueOf(cursor-defaultShown));
  		addDateParametersToLink(prev);
  		T.add(prev,1,1);
  	}
  	if(cursor<=(totalsize-defaultShown)){
  		Link next  = new Link(localize("printdoc.next","next")+"  "+defaultShown);
  		next.addParameter(PARAM_LETTER_TYPE,currentType);
  		next.addParameter(cursorPrm,String.valueOf(cursor+defaultShown));
  		addDateParametersToLink(next);
  		T.add(next,3,1);
  	}
  	
  	add(T);
  }
  
  private void addDateParametersToLink(Link link){
  		link.addParameter(PRM_STAMP_P_FROM,pFrom.toString());
		link.addParameter(PRM_STAMP_U_FROM,uFrom.toString());
		link.addParameter(PRM_STAMP_P_TO,pTo.toString());
		link.addParameter(PRM_STAMP_U_TO,uTo.toString());
  }
  
  private void addDocumentsList(IWContext iwc)throws Exception{
  	add(getLocalizedHeader("printdoc.unprinted_letters", "Letters for printing"));
  	
  	ColumnList unPrintedLetterDocs = new ColumnList(3);
		
		unPrintedLetterDocs.setWidth(Table.HUNDRED_PERCENT);
		unPrintedLetterDocs.setBackroundColor("#e0e0e0");
		//messageList.setHeader(localize("printdoc.name","Name"),1);
		//unPrintedLetterDocs.setHeader(localize("printdoc.date","Date"),1);
		//unPrintedLetterDocs.setHeader(localize("printdoc.n_o_docs","Number of documents"),2);

		unPrintedLetterDocs.add(localize("printdoc.unprinted","Unprinted"));
		//messageList.add("-");
		unPrintedLetterDocs.add(Integer.toString(getDocumentBusiness(iwc).getUnprintedLettersCountByType(currentType)));

		Link printLink = new Link(localize("printdoc.print","Print"));
		printLink.addParameter(PARAM_PRINT_UNPRINTED,"true");
		printLink.addParameter(PARAM_LETTER_TYPE,currentType);
		addDateParametersToLink(printLink);
		unPrintedLetterDocs.add(printLink);
		
		add(unPrintedLetterDocs);
		add(Text.getBreak());
	
		ColumnList printedLetterDocs = new ColumnList(3);
		
		Collection printDocs = getDocumentBusiness(iwc).getPrintedDocuments(currentType,pFrom,pTo);
		
			
			add(getLocalizedHeader("printdoc.printed_letters", "Printed letters"));
			addPrintedDatesForm(iwc);
			add(printedLetterDocs);
			//addCursorLinks(iwc,printDocs.size(),cursor_p,PRM_CURSOR_P);
			
			printedLetterDocs.setHeader(localize("printdoc.printed_date","Printing date"),1);
			printedLetterDocs.setHeader(localize("printdoc.n_o_docs","Number of documents"),2);
			printedLetterDocs.setWidth(Table.HUNDRED_PERCENT);
			
			Iterator iter = printDocs.iterator();
			int count = 0;
			if(cursor_p >0){
				while(iter.hasNext() && cursor_p>0){
					iter.next();
					cursor_p--;
				}
			}
			while (iter.hasNext() && count < defaultShown) {
				PrintDocuments doc = (PrintDocuments)iter.next();
				printedLetterDocs.add(doc.getCreated().toString());
				//messageList.add("-");
				printedLetterDocs.add(Integer.toString(doc.getNumberOfSubDocuments()));
				int fileID = doc.getDocumentFileID();
				Link viewLink = new Link(localize("printdoc.view","View"));
				viewLink.setFile(fileID);
				printedLetterDocs.add(viewLink);
				count++;
			}
			
		
  
  }
  
  private void addMessagesList(IWContext iwc)throws Exception{
  	
  	ColumnList unPrintedLetterDocs = new ColumnList(3);
  	
  	Collection unprintedLetters = getMessageBusiness(iwc).getUnPrintedLetterMessagesByType(currentType,uFrom,uTo);
  	add(getLocalizedHeader("printdoc.unprinted_letters", "Letters for printing"));
  	addUnPrintedDatesForm(iwc);
  	add(unPrintedLetterDocs);
  	//addCursorLinks(iwc,unprintedLetters.size(),cursor_u,PRM_CURSOR_U);
  	
  
		unPrintedLetterDocs.setWidth(Table.HUNDRED_PERCENT);
		unPrintedLetterDocs.setBackroundColor("#e0e0e0");
		unPrintedLetterDocs.setHeader(localize("printdoc.created_date","Message created"),1);
		unPrintedLetterDocs.setHeader(localize("printdoc.receiver","Receiver"),2);
		unPrintedLetterDocs.setHeader(localize("printdoc.file","File"),3);

		
		Iterator iter = unprintedLetters.iterator();
		int count = 0;
			if(cursor_u >0){
				while(iter.hasNext() && cursor_u>0){
					iter.next();
					cursor_u--;
				}
			}
			while (iter.hasNext() && count < defaultShown) {
			PrintedLetterMessage msg = (PrintedLetterMessage) iter.next();
			unPrintedLetterDocs.add(msg.getCreated().toString());
			//messageList.add("-");
			unPrintedLetterDocs.add(msg.getOwner().getName());
			
			
			Link printLink = new Link(localize("printdoc.print","Print"));
			printLink.addParameter(PARAM_PRINT_MSG,"true");
			printLink.addParameter(PARAM_LETTER_TYPE,currentType);
			printLink.addParameter(PARAM_MESSAGE_ID,msg.getPrimaryKey().toString());
			addDateParametersToLink(printLink);
			
			unPrintedLetterDocs.add(printLink);
			count++;
		}
		
		
	
		ColumnList printedLetterDocs = new ColumnList(3);
			printedLetterDocs.setWidth(Table.HUNDRED_PERCENT);
			printedLetterDocs.setBackroundColor("#e0e0e0");
			//messageList.setHeader(localize("printdoc.name","Name"),1);
			printedLetterDocs.setHeader(localize("printdoc.created_date","Message created"),1);
			printedLetterDocs.setHeader(localize("printdoc.receiver","Receiver"),2);
			printedLetterDocs.setHeader(localize("printdoc.file","File"),3);
		Collection printedLetters = getMessageBusiness(iwc).getPrintedLetterMessagesByType(currentType,pFrom,pTo);
		
		
		add(Text.getBreak());
		add(getLocalizedHeader("printdoc.printed_letters", "Printed letters"));
		addPrintedDatesForm(iwc);
		add(printedLetterDocs);
		//addCursorLinks(iwc,printedLetters.size(),cursor_p,PRM_CURSOR_P);
		
			iter = printedLetters.iterator();
			count = 0;
			if(cursor_u >0){
				while(iter.hasNext() && cursor_u>0){
					iter.next();
					cursor_u--;
				}
			}
			while (iter.hasNext()&& count < defaultShown) {
				PrintedLetterMessage msg = (PrintedLetterMessage) iter.next();
				
				printedLetterDocs.add(msg.getCreated().toString());
				//messageList.add("-");
				printedLetterDocs.add(msg.getOwner().getName());
			
				int fileID = msg.getMessageDataFileID();
				Link viewLink = new Link(localize("printdoc.view","View"));
				viewLink.setFile(fileID);
				printedLetterDocs.add(viewLink);
				count++;
			}
		
		
		
  	
  }

  private void viewMessageOverview(IWContext iwc)throws Exception{

	if ( iwc.isLoggedOn() ) {
	   add(getLocalizedHeader("printdoc.letters", "Letters for printing"));
		//messageList.skip();
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
  
  public void setShowTypesInDropdown(boolean showInDropdown){
  	this.showTypesAsDropdown = showInDropdown;
  }
  
  public void setDefaultNumberOfShownMessages(int number){
  	this.defaultShown = number;
  }
  
  public void setDefaultNumberOfShownDays(int number){
  	this.defaultDays = number;
  }
}
