package se.idega.idegaweb.commune.childcare.presentation;


import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.business.ChildCareSession;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplicationHome;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplicationHomeImpl;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.business.MessageComparator;
import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.message.data.PrintMessage;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
import se.idega.idegaweb.commune.presentation.ColumnList;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.printing.business.DocumentBusiness;
import se.idega.idegaweb.commune.printing.data.PrintDocuments;


import com.idega.business.IBOLookup;
import com.idega.core.data.Address;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
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
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * Title: EventList
 * Description: A class to view and manage PrintedLetterMessage in the idegaWeb Commune application
 * Copyright:    Copyright idega Software (c) 2002
 * Company:	idega Software
 * @author <a href="mailto:roar@idega.is">roar</a>
 * @version $Id: EventList.java,v 1.2 2003/03/21 18:39:53 roar Exp $
 * @since 17.3.2003 
 */

public class EventList extends CommuneBlock {

  private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";

  private final static int ACTION_VIEW_MESSAGE_OVERVIEW = 0;
  private final static int ACTION_VIEW_MESSAGE_LIST = 1;
  private final static int ACTION_VIEW_UNPRINTED = 2;
  private final static int ACTION_SHOW_DELETE_INFO = 3;
  private final static int ACTION_DELETE_MESSAGE = 4;
  private final static int ACTION_PRINT_UNPRINTED_MESSAGES = 5;
  private final static int ACTION_PRINT_MESSAGE=6;
  private final static int ACTION_PRINT_SELECTED = 7;

  private final static String PARAM_VIEW_UNPRINTED = "prv_view_upr";
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
  private final static String PRM_P_COUNT = "prv_pcnt";
  private final static String PRM_U_COUNT = "prv_ucnt";
  private final static String PRM_U_CHK = "prv_uchk";
  
  private final static String PRM_CURSOR_P = "prv_crs_p";
  private final static String PRM_CURSOR_U = "prv_crs_u";
  
  private final static String PRM_PRINT_SELECTED = "prv_pr_sel";
  
  
  private boolean isBulkType = false;
  private boolean showTypesAsDropdown = false;
  private boolean useCheckBox = true;
  private String currentType = "";
  private int msgID = -1;
  private int fileID = -1;
  private IWTimestamp today = IWTimestamp.RightNow(), pFrom=null,pTo=null,uFrom=null,uTo=null;
  private int defaultDays = 7;
  private int defaultShown = 25;
  private int cursor_p = 0;
  private int cursor_u = 0;
  private int count_p = 25;
  private int count_u = 25;

  private Table mainTable = null;

  public EventList() {
  }

  public String getBundleIdentifier(){
	return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
	//this.debugParameters(iwc);
	this.setResourceBundle(getResourceBundle(iwc));
	if ( iwc.isLoggedOn() ) {
	try{
	  initDates(iwc);
	  initCursors(iwc);
	  int action = parseAction(iwc);
	  switch(action){
		case ACTION_VIEW_MESSAGE_OVERVIEW:
			viewMessages(iwc);
			break;
		case ACTION_VIEW_MESSAGE_LIST:
		  viewMessages(iwc);
		  break;
/*RS		  
		case ACTION_PRINT_UNPRINTED_MESSAGES:
		  printAllUnPrintedMessages(iwc);
		  viewMessages(iwc);
			break;
		case ACTION_PRINT_MESSAGE:
		  printMessage(iwc);
		  viewMessages(iwc);
			break;
		case ACTION_VIEW_UNPRINTED:    
			addUnPrintedNameList(iwc);
		break;
		case ACTION_PRINT_SELECTED:
			printSelected(iwc);
			viewMessages(iwc);
		break;
RS*/
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

	if(iwc.isParameterSet(PARAM_VIEW_UNPRINTED)){
	  action = ACTION_VIEW_UNPRINTED;
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
	else if(iwc.isParameterSet(PRM_PRINT_SELECTED)){
		action = ACTION_PRINT_SELECTED;
	}
    
	if(iwc.isParameterSet(PARAM_LETTER_TYPE )){
	  currentType = iwc.getParameter(PARAM_LETTER_TYPE);
	}
	if("".equals(currentType))
	  currentType = getDocumentBusiness(iwc).getPrintMessageTypes()[0];
     
	isBulkType=getDocumentBusiness(iwc).isBulkLetterType(currentType);
     
	if(iwc.isParameterSet(PARAM_MESSAGE_ID)){
		msgID = Integer.parseInt(iwc.getParameter(PARAM_MESSAGE_ID));
	}
	
	add(new Text("Action: " + action + "<br>"));
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
			cursor_u = Integer.parseInt(iwc.getParameter(PRM_CURSOR_U));
		if(iwc.isParameterSet(PRM_P_COUNT))
			count_p = Integer.parseInt(iwc.getParameter(PRM_P_COUNT));
		if(iwc.isParameterSet(PRM_U_COUNT))
			count_u = Integer.parseInt(iwc.getParameter(PRM_U_COUNT));
  }

  private void printAllUnPrintedMessages(IWContext iwc)throws Exception{
	int userID = ((Integer)iwc.getCurrentUser().getPrimaryKey()).intValue();
	Collection unPrintedLetters = getDocumentBusiness(iwc).getUnPrintedMessages(currentType);
	getDocumentBusiness(iwc).writeBulkPDF(unPrintedLetters,iwc.getCurrentUser(),"BulkLetterPDF",iwc.getApplicationSettings().getDefaultLocale(),currentType,false,true,true);
  }
  
  private void printMessage(IWContext iwc) throws Exception {
	int userID = ((Integer)iwc.getCurrentUser().getPrimaryKey()).intValue();
	if(msgID>0){
		PrintedLetterMessage msg = (PrintedLetterMessage)getDocumentBusiness(iwc).getPrintedLetterMessageHome().findByPrimaryKey(new Integer(msgID));
		fileID = getDocumentBusiness(iwc).writePDF(msg,iwc.getCurrentUser(),"LetterPDF",iwc.getApplicationSettings().getDefaultLocale(),true);
		//System.err.println("file id written :"+fileID);
		//getDocumentBusiness(iwc).writePrintedLetterPDF(msgId,userID);
	}
  }
  
  private void printSelected(IWContext iwc) throws Exception {
	  int userID = ((Integer)iwc.getCurrentUser().getPrimaryKey()).intValue();
	  boolean bulk = iwc.isParameterSet("prv_bulk");
	  boolean flag = !iwc.isParameterSet("prv_mark");
	  String[] ids = iwc.getParameterValues(PRM_U_CHK);
	  if(ids !=null && ids.length >0){
			getDocumentBusiness(iwc).writeBulkPDF(ids,iwc.getCurrentUser(),"BulkLetterPDF",iwc.getApplicationSettings().getDefaultLocale(),currentType,true,flag,bulk);
	  }
	 
	  /*
	  if(msgID>0){
		  PrintedLetterMessage msg = (PrintedLetterMessage)getDocumentBusiness(iwc).getPrintedLetterMessageHome().findByPrimaryKey(new Integer(msgID));
		  fileID = getDocumentBusiness(iwc).writePDF(msg,iwc.getCurrentUser(),"LetterPDF",iwc.getApplicationSettings().getDefaultLocale());
		  //System.err.println("file id written :"+fileID);
		  //getDocumentBusiness(iwc).writePrintedLetterPDF(msgId,userID);
	  }
	  */
	}
  
  private void viewMessages(IWContext iwc) throws Exception{
  
		//add(getLocalizedHeader("printdoc.letters", "Letters for printing"));
/*RS
 		addTypeMenu(iwc);
		if(isBulkType)
			//addDocumentsList(iwc);
		else
RS*/
				addMessagesList(iwc);
  }
  
  private void addTypeMenu(IWContext iwc) throws Exception{
  	 
	 String[] types = getDocumentBusiness(iwc).getPrintMessageTypes();
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
  
  private PresentationObject getPrintedDatesForm(IWContext iwc){
  	
	Table T = new Table();
	DateInput from = new DateInput(PRM_STAMP_P_FROM);
	from =(DateInput) getStyledInterface(from);
	from.setYearRange(today.getYear()-5,today.getYear()+2);
	DateInput to = new DateInput(PRM_STAMP_P_TO);
	to = (DateInput)getStyledInterface(to);
	to.setYearRange(today.getYear()-5,today.getYear()+2);
  	
	from.setDate(pFrom.getSQLDate());
	to.setDate(pTo.getSQLDate());
	SubmitButton search = new SubmitButton(getResourceBundle().getLocalizedString("printdoc.fetch","Fetch"));
	search = (SubmitButton)getButton(search);
	T.add(getHeader(getResourceBundle().getLocalizedString("printdoc.date_from","From:")),1,1);
	T.add(from,2,1);
	T.add(getHeader(getResourceBundle().getLocalizedString("printdoc.date_to","To:")),3,1);
	T.add(to,4,1);
	//T.add(getHeader(getResourceBundle().getLocalizedString("printdoc.count","Count")),5,1);
	T.add(getCountDrop(PRM_P_COUNT,count_p),6,1);
	T.add(search,7,1);
	T.add(new HiddenInput(PRM_STAMP_U_FROM,uFrom.toString()));
	T.add(new HiddenInput(PRM_STAMP_U_TO,uTo.toString()));
	T.add(new HiddenInput(PRM_U_COUNT,String.valueOf(count_u)));
	T.add(new HiddenInput(PARAM_LETTER_TYPE,this.currentType));
	T.setTopLine(true);
	T.setBottomLine(true);
  	
	return T;
  
  }
  
  private DropdownMenu getCountDrop(String name,int selected){
	DropdownMenu drp = new DropdownMenu(name);
	drp.addMenuElement(String.valueOf(10));
	drp.addMenuElement(String.valueOf(25));
	drp.addMenuElement(String.valueOf(50));
	drp.addMenuElement(String.valueOf(75));
	drp.addMenuElement(String.valueOf(100));
	drp.addMenuElement(100000,localize("printdoc.all","All"));
	drp.setSelectedElement(selected);
	return drp;
  }
  
   private PresentationObject getUnPrintedDatesForm(IWContext iwc){
  	
	Table T = new Table();
	DateInput from = new DateInput(PRM_STAMP_U_FROM);
	from =(DateInput) getStyledInterface(from);
	from.setYearRange(today.getYear()-5,today.getYear()+2);
	DateInput to = new DateInput(PRM_STAMP_U_TO);
	to = (DateInput)getStyledInterface(to);
	to.setYearRange(today.getYear()-5,today.getYear()+2);
	from.setDate(uFrom.getSQLDate());
	to.setDate(uTo.getSQLDate());
	SubmitButton search = new SubmitButton(getResourceBundle().getLocalizedString("printdoc.fetch","Fetch"));
	search = (SubmitButton)getButton(search);
	T.add(getHeader(getResourceBundle().getLocalizedString("printdoc.date_from","From:")),1,1);
	T.add(from,2,1);
	T.add(getHeader(getResourceBundle().getLocalizedString("printdoc.date_to","To:")),3,1);
	T.add(to,4,1);
	//T.add(getHeader(getResourceBundle().getLocalizedString("printdoc.count","Count")),5,1);
	T.add(getCountDrop(PRM_U_COUNT,count_u),6,1);
	T.add(search,7,1);
	T.add(new HiddenInput(PRM_STAMP_P_FROM,pFrom.toString()));
	T.add(new HiddenInput(PRM_STAMP_P_TO,pTo.toString()));
	T.add(new HiddenInput(PRM_P_COUNT,String.valueOf(count_p)));
	T.add(new HiddenInput(PARAM_LETTER_TYPE,this.currentType));
	T.setTopLine(true);
	T.setBottomLine(true);
	T.setHeight(25);
	return T;
  
  }
  
  private PresentationObject getPrintButton(){
	Table T = new Table();
	T.setAlignment(T.HORIZONTAL_ALIGN_RIGHT);
	T.setCellpadding(2);
	SubmitButton print = new SubmitButton(PRM_PRINT_SELECTED,getResourceBundle().getLocalizedString("printdoc.print","Print"));
	print = (SubmitButton)getButton(print);
	CheckBox mark = new CheckBox("prv_mark");
	CheckBox bulk = new CheckBox("prv_bulk");
	T.add(mark,1,1);
	T.add(getLocalizedHeader("printdoc.keep_unprinted", "Keep unprinted status"),2,1);
	T.add(bulk,3,1);
	T.add(getLocalizedHeader("printdoc.create_bulk_letter", "Bulk letter"),4,1);
	T.add(print,5,1);
	return T;
  }
  
  private PresentationObject getCursorLinks(IWContext iwc,int totalsize,int cursor,String cursorPrm,int step){
	Table T = new Table();
	T.setAlignment(T.HORIZONTAL_ALIGN_RIGHT);
	T.setCellpadding(2);
	if(cursor>0){
		Link prev  = new Link(localize("printdoc.last","last")+"  "+step);
		prev.addParameter(PARAM_LETTER_TYPE,currentType);
		prev.addParameter(cursorPrm,String.valueOf(cursor-step));
		addDateParametersToLink(prev);
		T.add(prev,1,1);
	}
	if(cursor<=(totalsize-step)){
		Link next  = new Link(localize("printdoc.next","next")+"  "+step);
		next.addParameter(PARAM_LETTER_TYPE,currentType);
		next.addParameter(cursorPrm,String.valueOf(cursor+step));
		addDateParametersToLink(next);
		T.add(next,3,1);
	}
  	
	return T;
  }
  
  private void addDateParametersToLink(Link link){
		link.addParameter(PRM_STAMP_P_FROM,pFrom.toString());
		link.addParameter(PRM_STAMP_U_FROM,uFrom.toString());
		link.addParameter(PRM_STAMP_P_TO,pTo.toString());
		link.addParameter(PRM_STAMP_U_TO,uTo.toString());
		link.addParameter(PRM_U_COUNT,count_u);
		link.addParameter(PRM_P_COUNT,count_p);
  }
  
  private void addDocumentsList(IWContext iwc)throws Exception{
    Form uForm = new Form();
	Form pForm = new Form();
	Table  uT = new Table();
	Table pT = new Table();
	uForm.add(uT);
	pForm.add(pT);
	add(uForm);
	add(pForm);
	int urow = 1;
	
	
	
	uT.add(getLocalizedHeader("printdoc.unprinted_letters", "Letters for printing"),1,urow++);
  	
	ColumnList unPrintedLetterDocs = new ColumnList(4);
		
		unPrintedLetterDocs.setWidth(Table.HUNDRED_PERCENT);
		unPrintedLetterDocs.setBackroundColor("#e0e0e0");
		//messageList.setHeader(localize("printdoc.name","Name"),1);
		//unPrintedLetterDocs.setHeader(localize("printdoc.date","Date"),1);
		//unPrintedLetterDocs.setHeader(localize("printdoc.n_o_docs","Number of documents"),2);

		unPrintedLetterDocs.add(localize("printdoc.unprinted","Unprinted"));
		//messageList.add("-");
		unPrintedLetterDocs.add(Integer.toString(getDocumentBusiness(iwc).getUnprintedMessagesCountByType(currentType)));

		
		Link printLink = new Link(localize("printdoc.print","Print"));
		printLink.addParameter(PARAM_PRINT_UNPRINTED,"true");
		printLink.addParameter(PARAM_LETTER_TYPE,currentType);
		addDateParametersToLink(printLink);
		unPrintedLetterDocs.add(printLink);
		
		Link viewUnprintedLink = new Link(localize("printdoc.name_list","Namelist"));
		viewUnprintedLink.addParameter(PARAM_VIEW_UNPRINTED,"true");
		viewUnprintedLink.addParameter(PARAM_LETTER_TYPE,currentType);
		addDateParametersToLink(viewUnprintedLink);
		unPrintedLetterDocs.add(viewUnprintedLink);
		
		uT.add(unPrintedLetterDocs,1,urow++);
		uT.add(Text.getBreak(),1,urow++);
	
		ColumnList printedLetterDocs = new ColumnList(4);
		
		Collection printDocs = getDocumentBusiness(iwc).getPrintedDocuments(currentType,pFrom,pTo);
		
		int prow = 1;
	pT.add(getLocalizedHeader("printdoc.printed_letters", "Printed letters"),1,prow++);
	pT.add(getPrintedDatesForm(iwc),1,prow++);
	pT.add(printedLetterDocs,1,prow++);
	pT.add(getCursorLinks(iwc,printDocs.size(),cursor_p,PRM_CURSOR_P,count_p),1,prow++);
			
			printedLetterDocs.setHeader("#",1);
			printedLetterDocs.setHeader(localize("printdoc.printed_date","Printing date"),2);
			printedLetterDocs.setHeader(localize("printdoc.n_o_docs","Number of documents"),3);
			printedLetterDocs.setWidth(Table.HUNDRED_PERCENT);
			
			Iterator iter = printDocs.iterator();
			int count = cursor_p+1;
			int ccp = count_p+cursor_p;
			if(cursor_p >0){
				while(iter.hasNext() && cursor_p>0){
					iter.next();
					cursor_p--;
				}
			}
			while (iter.hasNext() && count <= ccp) {
				PrintDocuments doc = (PrintDocuments)iter.next();
				printedLetterDocs.add(String.valueOf(count));
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
	Form uForm = new Form();
/*RS	Form pForm = new Form(); RS*/
	
	Table  uT = new Table();
/*RS	Table pT = new Table(); RS*/
	uForm.add(uT);
/*RS	pForm.add(pT); RS*/
	add(uForm);
    //add(pForm);
	ColumnList unPrintedLetterDocs = new ColumnList(6);
	
	
	
	
  	
	int urow = 1;
	int childCareId = getChildCareSession(iwc).getChildCareID();
	uForm.add(new Text("ChildcareId:" + childCareId));
	//Collection unprintedLetters = getMessageBusiness(iwc).getUnPrintedLetterMessagesByType(currentType,uFrom,uTo);
	Collection unprintedLetters = getChildCareApplication(iwc).findAllChildCasesByProvider(childCareId);
	
/*RS	uT.add(getLocalizedHeader("printdoc.unprinted_letters", "Letters for printing"),1,urow++);  RS*/
	uT.add(getUnPrintedDatesForm(iwc),1,urow++);
	uT.add(unPrintedLetterDocs,1,urow++);
	uT.add(getPrintButton(),1,urow++);
	uT.add(getCursorLinks(iwc,unprintedLetters.size(),cursor_u,PRM_CURSOR_U,count_u),1,urow++);
  	
  
		unPrintedLetterDocs.setWidth(Table.HUNDRED_PERCENT);
		unPrintedLetterDocs.setBackroundColor("#e0e0e0");
		unPrintedLetterDocs.setHeader(localize("printdoc.subject","Subject"),1);
		unPrintedLetterDocs.setHeader("#",2);
		unPrintedLetterDocs.setHeader(localize("printdoc.receiver","Receiver"),3);
		unPrintedLetterDocs.setHeader(localize("printdoc.ssn","SSN"),4);
		unPrintedLetterDocs.setHeader(localize("printdoc.created_date","Message created"),5);

		//unPrintedLetterDocs.setHeader(localize("printdoc.file","File"),5);
		CheckBox checkAll = new CheckBox("checkall");
		checkAll.setToCheckOnClick(PRM_U_CHK,true);
		unPrintedLetterDocs.setHeader(checkAll,6);
		//unPrintedLetterDocs.setHeader(localize("printdoc.select","Select"),5);
/*RS		unPrintedLetterDocs.setHeader(localize("printdoc.bulk_file","Bulk ID"),6); RS*/


		Iterator iter = unprintedLetters.iterator();
		int count = cursor_u+1;
		int ccu = count_u+cursor_u;
			if(cursor_u >0){
				while(iter.hasNext() && cursor_u>0){
					iter.next();
					cursor_u--;
				}
			}
			
		int bulkId;
		while (iter.hasNext() && count <= ccu) {
			PrintedLetterMessage msg = (PrintedLetterMessage) iter.next();

			unPrintedLetterDocs.add(msg.getSubject());
			unPrintedLetterDocs.add(String.valueOf(msg.getHandlerId())); 

/*RS		PrintedLetterDocs.add(String.valueOf(count)); RS*/
			//messageList.add("-");
			
			unPrintedLetterDocs.add(msg.getOwner().getName());
			unPrintedLetterDocs.add(" "+msg.getOwner().getPersonalID());			
			unPrintedLetterDocs.add(msg.getCreated().toString());
			
/*RS		if(useCheckBox){ RS*/
				CheckBox box = new CheckBox(PRM_U_CHK,msg.getPrimaryKey().toString());
				uForm.addParameter(PARAM_LETTER_TYPE,currentType);
				unPrintedLetterDocs.add(box);
/*RS
 			}
			
			else{
				Link printLink = new Link(localize("printdoc.print","Print"));
				printLink.addParameter(PARAM_PRINT_MSG,"true");
				printLink.addParameter(PARAM_LETTER_TYPE,currentType);
				printLink.addParameter(PARAM_MESSAGE_ID,msg.getPrimaryKey().toString());
				addDateParametersToLink(printLink);
				unPrintedLetterDocs.add(printLink);
			}

			bulkId = msg.getMessageBulkDataFileID();
			if(bulkId >0){
				Link bulkLink = new Link(String.valueOf(msg.getMessageBulkDataFileID()));
				bulkLink.setFile(msg.getMessageBulkDataFileID());
				unPrintedLetterDocs.add(bulkLink);
			}
			else{
				unPrintedLetterDocs.add("-");
			}
RS*/			
			count++;
		}
			
/*RS			
			
		ColumnList printedLetterDocs = new ColumnList(6);
			printedLetterDocs.setWidth(Table.HUNDRED_PERCENT);
			printedLetterDocs.setBackroundColor("#e0e0e0");
			printedLetterDocs.setHeader("#",1);
			printedLetterDocs.setHeader(localize("printdoc.created_date","Message created"),2);
			printedLetterDocs.setHeader(localize("printdoc.receiver","Receiver"),3);
			printedLetterDocs.setHeader(localize("printdoc.subject","Subject"),4);
			printedLetterDocs.setHeader(localize("printdoc.file","File"),5);
			printedLetterDocs.setHeader(localize("printdoc.bulk_file","Bulk File"),6);
		Collection printedLetters = getMessageBusiness(iwc).getPrintedLetterMessagesByType(currentType,pFrom,pTo);
		
		int prow  = 1;
		pT.add(Text.getBreak(),1,prow++);
		pT.add(getLocalizedHeader("printdoc.printed_letters", "Printed letters"),1,prow++);
		pT.add(getPrintedDatesForm(iwc),1,prow++);
		pT.add(printedLetterDocs,1,prow++);
		pT.add(getCursorLinks(iwc,printedLetters.size(),cursor_p,PRM_CURSOR_P,count_p),1,prow++);
		
			iter = printedLetters.iterator();
			count = cursor_p+1;
			int ccp = cursor_p+count_p;
			if(cursor_p >0){
				while(iter.hasNext() && cursor_p>0){
					iter.next();
					cursor_p--;
				}
			}
			while (iter.hasNext()&& count <= ccp) {
				PrintedLetterMessage msg = (PrintedLetterMessage) iter.next();
				printedLetterDocs.add(String.valueOf(count));
				printedLetterDocs.add(msg.getCreated().toString());
				//messageList.add("-");
				printedLetterDocs.add(msg.getOwner().getName());
				printedLetterDocs.add(msg.getSubject());
				int fileID = msg.getMessageDataFileID();
				Link viewLink = new Link(localize("printdoc.view","View"));
				viewLink.setFile(fileID);
				printedLetterDocs.add(viewLink);
				bulkId = msg.getMessageBulkDataFileID();
				if(bulkId >0){
				
					Link bulkLink = new Link(String.valueOf(bulkId));
					bulkLink.setFile(bulkId);
					printedLetterDocs.add(bulkLink);
				}
				else
					printedLetterDocs.add("");
				count++;
			}  	
		RS*/
  }
  
  private void addUnPrintedNameList(IWContext iwc)throws Exception{
	 ColumnList unPrintedNames = new ColumnList(3);
	 unPrintedNames.setWidth(Table.HUNDRED_PERCENT);
			unPrintedNames.setBackroundColor("#e0e0e0");
			//messageList.setHeader(localize("printdoc.name","Name"),1);
			unPrintedNames.setHeader(localize("printdoc.created_date","Message created"),1);
			unPrintedNames.setHeader(localize("printdoc.receiver","Receiver"),2);
			unPrintedNames.setHeader(localize("printdoc.address","Address"),3);
	 Collection unprintedLetters = getMessageBusiness(iwc).getUnPrintedLetterMessagesByType(currentType);
	 Iterator iter = unprintedLetters.iterator();
	 PrintMessage msg;
	 User owner;
	 UserBusiness ub = getUserBusiness(iwc);
	 Address addr;
	 String sAddr = "";
	 while(iter.hasNext()){
		msg = (PrintMessage) iter.next();
		owner = msg.getOwner();
		unPrintedNames.add(msg.getCreated().toString());
		unPrintedNames.add(owner.getName());
		try{
			addr = ub.getUsersMainAddress(owner);
			unPrintedNames.add(addr.getStreetAddress());
		}
		catch(Exception ex){
			unPrintedNames.add(getErrorText(localize("printdoc.noaddress","No address")));
		}
	 }  	 
	 addTypeMenu(iwc);
	 add(unPrintedNames);
  }

  private MessageBusiness getMessageBusiness(IWContext iwc) throws Exception {
	return (MessageBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,MessageBusiness.class);
  }

  private DocumentBusiness getDocumentBusiness(IWContext iwc) throws Exception {
	return (DocumentBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,DocumentBusiness.class);
  }
  
  private UserBusiness getUserBusiness(IWApplicationContext iwac)throws Exception{
	return (UserBusiness) IBOLookup.getServiceInstance(iwac,UserBusiness.class);
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
  
  
  private ChildCareBusiness getChildCareBusiness(IWContext iwc) {
	  try {
		  return (ChildCareBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);
	  }
	  catch (RemoteException e) { 
		  return null;
	  }
  }
  
private ChildCareApplicationHome getChildCareApplication(IWContext iwc) {
	try {
		return (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
	}
	catch (RemoteException e) { 
		return null;
	}
}

private ChildCareSession getChildCareSession(IWContext iwc) throws RemoteException {
	return (ChildCareSession) IBOLookup.getSessionInstance(iwc, ChildCareSession.class);	
}

        

  
}
