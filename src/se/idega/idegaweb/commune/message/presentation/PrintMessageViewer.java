/*
 * Created on Feb 16, 2004
 *
 */
package se.idega.idegaweb.commune.message.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.message.data.PrintMessage;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessageHome;
import se.idega.idegaweb.commune.presentation.ColumnList;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.printing.business.DocumentBusiness;

import com.idega.business.IBORuntimeException;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOHome;
import com.idega.data.IDOLookup;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * PrintMessageViewer
 * A class to view and manage PrintedLetterMessage in the idegaWeb Commune application
 * @author aron 
 * @version 1.0
 */
public class PrintMessageViewer extends CommuneBlock {

	
	protected static final int ACTION_VIEW_MESSAGE_LIST = 1;
	protected static final int ACTION_VIEW_MESSAGE = 2;
	protected static final int ACTION_PRINT_MESSAGE = 7;
	protected static final int ACTION_PRINT_SELECTED = 8;
	protected static final String PARAM_MESSAGE_ID = "prv_id";
	protected static final String PARAM_PRINT_MSG = "prv_pr_msg";
	protected static final String PARAM_LETTER_TYPE = "prv_let_tp";
	protected static final String PARAM_VIEW_MESSAGE = "prm_view_msg";
	protected static final String PRM_STAMP_U_FROM = "prv_ufrm";
	protected static final String PRM_STAMP_U_TO = "prv_uto";
	protected static final String PRM_P_COUNT = "prv_pcnt";
	protected static final String PRM_U_COUNT = "prv_ucnt";
	protected static final String PRM_U_CHK = "prv_uchk";
	protected static final String PRM_CURSOR_U = "prv_crs_u";
	protected static final String PRM_PRINT_SELECTED = "prv_pr_sel";
	protected static final String PRM_SSN = "prv_ssn";
	protected static final String PRM_MSGID = "prv_msgid";
	protected static final String LOCALE_DATE_FROM = "eventlist.date_from";
	protected static final String LOCALE_DATE_TO = "eventlist.date_to";
	protected static final String LOCALE_SSN = "eventlist.ssn";
	protected static final String LOCALE_MSGID = "eventlist.msgid";
	protected static final String LOCALE_DATE_CREATED = "eventlist.created";
	protected static final String LOCALE_EVENT = "eventlist.event";
	protected static final String LOCALE_RECEIVER = "eventlist.receiver";
	protected static final String LOCALE_LAST = "eventlist.last";
	protected static final String LOCALE_NEXT = "eventlist.next";
	protected static final String LOCALE_MSGID_INT = "eventlist.msgid_int";
	protected static final String LOCALE_BULK_MESSAGE = "eventlist.bulk_message";
	public boolean showTypesAsDropdown = false;
	private String currentType = "";
	private IWTimestamp today = IWTimestamp.RightNow();
	private IWTimestamp uFrom = null;
	private IWTimestamp uTo = null;
	private int defaultDays = 7;
	public int defaultShown = 25;
	private int cursor_u = 0;
	private int count_p = 25;
	private int count_u = 25;
	private String searchSsn = "";
	private String searchMsgId = "";
	private Table mainTable = null;
	private IWContext _iwc = null;
	
	public void main(IWContext iwc) {
		//this.debugParameters(iwc);
		_iwc = iwc;
		this.setResourceBundle(getResourceBundle(iwc));
		if (iwc.isLoggedOn()) {
			try {
				initDates(iwc);
				initCursors(iwc);
				initSearch(iwc);
				int action = parseAction(iwc);
				switch (action) {
					case ACTION_VIEW_MESSAGE_LIST :
						viewMessages(iwc);
						break;
					case ACTION_VIEW_MESSAGE :
						viewMessage(iwc);
						break;
					case ACTION_PRINT_SELECTED :
						printSelected(iwc);
						break;
	
					default :
						break;
				}
				super.add(mainTable);
			} catch (Exception e) {
				super.add(new ExceptionWrapper(e, this));
			}
		} else {
			add(
				getLocalizedHeader(
					"printdoc.not_logged_on",
					"You must be logged on to use this function"));
		}
	}
	public void add(PresentationObject po) {
		if (mainTable == null) {
			mainTable = new Table();
			mainTable.setCellpadding(14);
			mainTable.setCellspacing(0);
			mainTable.setColor(getBackgroundColor());
			mainTable.setWidth(getWidth());
		}
		mainTable.add(po);
	}
	private int parseAction(IWContext iwc) throws Exception {
		int action = ACTION_VIEW_MESSAGE_LIST;
	
		if (iwc.isParameterSet(PARAM_VIEW_MESSAGE)) {
			action = ACTION_VIEW_MESSAGE;
		} else if (iwc.isParameterSet(PARAM_PRINT_MSG)) {
			action = ACTION_PRINT_MESSAGE;
		} else if (iwc.isParameterSet(PRM_PRINT_SELECTED)) {
			action = ACTION_PRINT_SELECTED;
		}
	
	
		/*if (iwc.isParameterSet(PARAM_MESSAGE_ID)) {
			msgID = Integer.parseInt(iwc.getParameter(PARAM_MESSAGE_ID));
		}*/
	
		//	add(new Text("Action: " + action + "<br>"));
		return action;
	}
	private void initDates(IWContext iwc) throws Exception {
		uFrom = IWTimestamp.RightNow();
		uFrom.addDays(-defaultDays);
		uTo = IWTimestamp.RightNow();
	
		if (iwc.isParameterSet(PRM_STAMP_U_FROM))
			uFrom = new IWTimestamp(iwc.getParameter(PRM_STAMP_U_FROM));
	
		if (iwc.isParameterSet(PRM_STAMP_U_TO))
			uTo = new IWTimestamp(iwc.getParameter(PRM_STAMP_U_TO));
	}
	private void initSearch(IWContext iwc) throws Exception {
		if (iwc.isParameterSet(PRM_SSN)) {
			searchSsn = PersonalIDFormatter.stripForDatabaseSearch(iwc.getParameter(PRM_SSN));
		}
		if (iwc.isParameterSet(PRM_MSGID)) {
			searchMsgId = iwc.getParameter(PRM_MSGID);
		}
	
	}
	private void initCursors(IWContext iwc) throws Exception {
		/*if (iwc.isParameterSet(PRM_CURSOR_P))
			cursor_p = Integer.parseInt(iwc.getParameter(PRM_CURSOR_P));*/
		if (iwc.isParameterSet(PRM_CURSOR_U))
			cursor_u = Integer.parseInt(iwc.getParameter(PRM_CURSOR_U));
		if (iwc.isParameterSet(PRM_P_COUNT))
			count_p = Integer.parseInt(iwc.getParameter(PRM_P_COUNT));
		if (iwc.isParameterSet(PRM_U_COUNT))
			count_u = Integer.parseInt(iwc.getParameter(PRM_U_COUNT));
	}
	private void viewMessage(IWContext iwc) throws Exception {
		String id = iwc.getParameter(PARAM_MESSAGE_ID);
		if (id == null) { //no messages selected
			addMessagesList(iwc);
	
		} else {
			viewMessages(new String[] { id });
		}
	}
	private void printSelected(IWContext iwc) throws Exception {
		String[] ids = iwc.getParameterValues(PRM_U_CHK);
		if (ids == null || ids.length == 0) { //no messages selected
			addMessagesList(iwc);
	
		} else if (ids != null && ids.length > 0) {
			viewMessages(ids);
		}
	}
	private void viewMessages(String[] ids) throws FinderException {
		
		if (_iwc.isParameterSet("prv_bulk")){ //bulk
			int fileID = createPrintableBulkMessage(ids);
			if(fileID!=-1) {
				//System.out.println("adding link for pdf");
				Link viewLink = new Link(getLocalizedString(LOCALE_BULK_MESSAGE, "bulk message", _iwc));
				viewLink.setFile(fileID);
				add(viewLink);
				addBreak();
			} 		
		}else {
			Collection selectedLetters = getPrintedLetter().findLetters(ids);
			Iterator iter = selectedLetters.iterator();
			while (iter.hasNext()) {
				PrintedLetterMessage msg = (PrintedLetterMessage) iter.next();
				int fileID = createPrintableMessage(msg);
				if(fileID!=-1) {
					//System.out.println("adding link for pdf");
					Link viewLink = new Link(msg.getSubject() + "  (" + msg.getOwner().getName() + ")");
					viewLink.setFile(fileID);
					add(viewLink);
					addBreak();
				} else {
					//System.out.println("Could not create pdf, no link added");
				}
			}
		}
	}
	private int createPrintableMessage(PrintMessage msg) {
		try {
			DocumentBusiness docBiz = getDocumentBusiness();
			//String userName = _iwc.getCurrentUser().getName();
			String fileName = "EventListLetter-" + msg.getNodeID() + "-" + _iwc.getCurrentLocaleId() + ".pdf";
			ICFile file = null;
			try {
				file = getICFileHome().findByFileName(fileName);
				//System.out.println("found pdf file " + fileName);
			} catch(FinderException e) {
				// ok, just means we need to create this file
			}
			if(file==null) {
				//System.out.println("creating pdf file " + fileName);
				return docBiz.writePDF(msg, _iwc.getCurrentUser(), fileName, _iwc.getCurrentLocale(), false);
			} else {
				//System.out.println("Using existing pdf");
				return Integer.parseInt(file.getPrimaryKey().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
		
	private int createPrintableBulkMessage(String[] msgIds) {
		try {
			DocumentBusiness docBiz = getDocumentBusiness();
			//String userName = _iwc.getCurrentUser().getName();
			StringBuffer ids = new StringBuffer();
			for (int i = 0; i < msgIds.length; i++){
				ids.append(msgIds[i]);
				ids.append('-');
			}
			String fileName = "EventListLetter-" + ids + _iwc.getCurrentLocaleId() + ".pdf";
			
			ICFile file = null;
			try {
				file = getICFileHome().findByFileName(fileName);
				//System.out.println("found pdf file " + fileName);
			} catch(FinderException e) {
				// ok, just means we need to create this file
			}
			if(file==null) {
				//System.out.println("creating pdf file " + fileName);
				return docBiz.writeBulkPDF(msgIds,
						_iwc.getCurrentUser(),
						fileName,
						_iwc.getCurrentLocale(),
						"DEFA",
						true,
						false,
						true);
			} else {
				//System.out.println("Using existing pdf");
				return Integer.parseInt(file.getPrimaryKey().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
		
	private DocumentBusiness getDocumentBusiness() throws RemoteException {
		return (DocumentBusiness) com.idega.business.IBOLookup.getServiceInstance(
			_iwc,
			DocumentBusiness.class);
	}
	private ICFileHome getICFileHome() {
		try {
			return (ICFileHome) getIDOHome(ICFile.class);
		} catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}
	public Collection getPrintedMessagesByPrimaryKeys(String[] primaryKeys) throws FinderException {
		PrintedLetterMessageHome msgHome = null;
		PrintMessage msg;
		ArrayList coll = new ArrayList(primaryKeys.length);
		msgHome = getPrintedLetterMessageHome();
		if (msgHome != null) {
			for (int i = 0; i < primaryKeys.length; i++) {
				msg = (PrintMessage) msgHome.findByPrimaryKey(primaryKeys[i]);
				coll.add(msg);
			}
		}
		return coll;
	}
	public PrintedLetterMessageHome getPrintedLetterMessageHome() {
		try {
			return (PrintedLetterMessageHome) getIDOHome(
				PrintedLetterMessage.class);
		} catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}
	protected IDOHome getIDOHome(Class beanClass) throws RemoteException {
		return IDOLookup.getHome(beanClass);
	}
	private void viewMessages(IWContext iwc) throws Exception {
		addMessagesList(iwc);
	}
	private DropdownMenu getCountDrop(String name, int selected) {
		DropdownMenu drp = new DropdownMenu(name);
		//	drp.addMenuElement(String.valueOf(3)); //only for test
		drp.addMenuElement(String.valueOf(10));
		drp.addMenuElement(String.valueOf(25));
		drp.addMenuElement(String.valueOf(50));
		drp.addMenuElement(String.valueOf(75));
		drp.addMenuElement(String.valueOf(100));
		drp.addMenuElement(100000, localize("printdoc.all", "All"));
		drp.setSelectedElement(selected);
		return drp;
	}
	private PresentationObject getSearchForm(IWContext iwc) {
	
		Table T = new Table();
	
		DateInput from = new DateInput(PRM_STAMP_U_FROM);
		from = (DateInput) getStyledInterface(from);
		from.setYearRange(today.getYear() - 5, today.getYear() + 2);
	
		DateInput to = new DateInput(PRM_STAMP_U_TO);
		to = (DateInput) getStyledInterface(to);
		to.setYearRange(today.getYear() - 5, today.getYear() + 2);
	
		from.setDate(uFrom.getDate());
		to.setDate(uTo.getDate());
	
		TextInput msgid = new TextInput(PRM_MSGID);
		msgid = (TextInput) getStyledInterface(msgid);
		if (iwc.getParameter(PRM_MSGID) != null) {
			msgid.setValue(iwc.getParameter(PRM_MSGID));
		}
		msgid.setAsIntegers(localize(LOCALE_MSGID_INT, "Message id must be an integer."));
		
	
		TextInput ssn = new TextInput(PRM_SSN);
		ssn = (TextInput) getStyledInterface(ssn);
		if (iwc.getParameter(PRM_SSN) != null) {
			ssn.setValue(iwc.getParameter(PRM_SSN));
		}
	
		SubmitButton search =
			new SubmitButton(
				localize(
					"printdoc.fetch",
					"Fetch"));
		search = (SubmitButton) getButton(search);
	
		T.add(
			getHeader(
				localize(
					LOCALE_DATE_FROM,
					"From")
					+ ":"),
			1,
			1);
		T.mergeCells(2, 1, 8, 1);
		T.add(from, 2, 1);
	
		T.add(
			getHeader(
				localize(LOCALE_DATE_TO, "To")
					+ ":"),
			1,
			2);
		T.mergeCells(2, 2, 8, 2);
		T.add(to, 2, 2);
	
		T.add(
			getHeader(
				localize(
					LOCALE_MSGID,
					"Message Id")
					+ ":"),
			1,
			3);
		T.add(msgid, 2, 3);
		T.add(
			getHeader(
				localize(LOCALE_SSN, "SSN")
					+ ":"),
			4,
			3);
		T.add(ssn, 5, 3);
		//T.add(getHeader(localize("printdoc.count","Count")),5,1);
		T.setWidth(6, 3, 50);
		T.add(getStyledInterface(getCountDrop(PRM_U_COUNT, count_u)), 7, 3);
		T.add(search, 8, 3);
	
		T.setStyleAttribute("border-bottom: medium solid black");
		T.setStyleAttribute("border-top: medium solid black");
		//	T.setName("searchtable");
		//	T.setTopLine(true);
		//	T.setLeftLine(true);
		//	T.setBottomLine(true);
		T.setHeight(75);
		return T;
	}
	private PresentationObject getPrintButton() {
		Table T = new Table();
		T.setCellpadding(2);
		SubmitButton print =
			new SubmitButton(
				PRM_PRINT_SELECTED,
				localize(
					"printdoc.print",
					"Print"));
		print = (SubmitButton) getButton(print);
		
		CheckBox bulk = new CheckBox("prv_bulk");

		T.add(bulk, 1, 1);
		T.add(getLocalizedHeader("printdoc.create_bulk_letter", "Bulk letter"), 2, 1);
		
		T.add(print, 3, 1);
		return T;
	}
	private PresentationObject getCursorLinks(int totalsize, int cursor, String cursorPrm, int step) {
		Table T = new Table();
		T.setCellpadding(2);
		if (cursor > 0) {
			Link prev = new Link(localize(LOCALE_LAST, "last") + "  " + step);
			prev.addParameter(PARAM_LETTER_TYPE, currentType);
			prev.addParameter(cursorPrm, String.valueOf(cursor - step));
			addDateParametersToLink(prev);
			T.add(prev, 1, 1);
		}
		if (cursor <= (totalsize - step)) {
			Link next = new Link(localize(LOCALE_NEXT, "next") + "  " + step);
			next.addParameter(PARAM_LETTER_TYPE, currentType);
			next.addParameter(cursorPrm, String.valueOf(cursor + step));
			addDateParametersToLink(next);
			T.add(next, 3, 1);
		}
	
		return T;
	}
	private void addDateParametersToLink(Link link) {
		//		link.addParameter(PRM_STAMP_P_FROM,pFrom.toString());
		link.addParameter(PRM_STAMP_U_FROM, uFrom.toString());
		//		link.addParameter(PRM_STAMP_P_TO,pTo.toString());
		link.addParameter(PRM_STAMP_U_TO, uTo.toString());
		link.addParameter(PRM_U_COUNT, count_u);
		link.addParameter(PRM_P_COUNT, count_p);
		link.addParameter(PRM_SSN, searchSsn);
		link.addParameter(PRM_MSGID, searchMsgId);
	}
	private void addMessagesList(IWContext iwc) throws Exception {
		Form uForm = new Form();
	
		Table uT = new Table();
		uForm.add(uT);
		add(uForm);
		ColumnList unPrintedLetterDocs = new ColumnList(6);
	
		int urow = 1;
		Collection unprintedLetters = getLetters(iwc);
	
		uT.add(getSearchForm(iwc), 1, urow++);
		uT.setStyle(1, urow - 1, "padding-bottom", "15px");
	
		uT.add(unPrintedLetterDocs, 1, urow++);
		uT.setAlignment(1, urow, Table.HORIZONTAL_ALIGN_RIGHT);
		uT.add(getPrintButton(), 1, urow++);
		uT.setAlignment(1, urow, Table.HORIZONTAL_ALIGN_RIGHT);
		uT.add(getCursorLinks(
				unprintedLetters.size(),	
				cursor_u,
				PRM_CURSOR_U,
				count_u),
				1,
				urow++);
	
		unPrintedLetterDocs.setWidth(Table.HUNDRED_PERCENT);
		unPrintedLetterDocs.setBackroundColor("#e0e0e0");
		unPrintedLetterDocs.setHeader(localize(LOCALE_EVENT, "Event"), 1);
		unPrintedLetterDocs.setHeader(localize(LOCALE_MSGID, "Message Id"), 2);
		unPrintedLetterDocs.setHeader(localize(LOCALE_RECEIVER, "Receiver"), 3);
		unPrintedLetterDocs.setHeader(localize(LOCALE_SSN, "SSN"), 4);
		unPrintedLetterDocs.setHeader(
			localize(LOCALE_DATE_CREATED, "Message created"),
			5);
	
		CheckBox checkAll = new CheckBox("checkall");
		checkAll.setToCheckOnClick(PRM_U_CHK, true);
		unPrintedLetterDocs.setHeader(checkAll, 6);
	
		Iterator iter = unprintedLetters.iterator();
		int count = cursor_u + 1;
		int ccu = count_u + cursor_u;
		if (cursor_u > 0) {
			while (iter.hasNext() && cursor_u > 0) {
				iter.next();
				cursor_u--;
			}
		}
	
		//int bulkId;
		while (iter.hasNext() && count <= ccu) {
			PrintedLetterMessage msg = (PrintedLetterMessage) iter.next();
	
			Link link = new Link(msg.getSubject());
			link.addParameter(PARAM_VIEW_MESSAGE, "true");
			link.addParameter(PARAM_MESSAGE_ID, msg.getNodeID());
			unPrintedLetterDocs.add(link);
			unPrintedLetterDocs.add(String.valueOf(msg.getNodeID()));
	
			unPrintedLetterDocs.add(msg.getOwner().getName());
			unPrintedLetterDocs.add(" " + msg.getOwner().getPersonalID());
			unPrintedLetterDocs.add(msg.getCreated().toString());
	
			CheckBox box =
				new CheckBox(PRM_U_CHK, msg.getPrimaryKey().toString());
			uForm.addParameter(PARAM_LETTER_TYPE, currentType);
			unPrintedLetterDocs.add(box);
			count++;
		}
	
	}
	protected Collection getLetters(IWContext iwc) throws FinderException {
		throw new FinderException("Default viewer does not find messages "+iwc.getUserId());
	}
	public void setShowTypesInDropdown(boolean showInDropdown) {
		this.showTypesAsDropdown = showInDropdown;
	}
	public void setDefaultNumberOfShownMessages(int number) {
		this.defaultShown = number;
	}
	public void setDefaultNumberOfShownDays(int number) {
		this.defaultDays = number;
	}
	protected PrintedLetterMessageHome getPrintedLetter() {
		try {
			return (PrintedLetterMessageHome) IDOLookup.getHome(
				PrintedLetterMessage.class);
		} catch (RemoteException e) {
			return null;
		}
	}
	
	

	/**
	 * @return Returns the defaultDays.
	 */
	public int getDefaultDays() {
		return defaultDays;
	}

	/**
	 * @return Returns the defaultShown.
	 */
	public int getDefaultShown() {
		return defaultShown;
	}

	/**
	 * @return Returns the mainTable.
	 */
	public Table getMainTable() {
		return mainTable;
	}

	/**
	 * @return Returns the searchMsgId.
	 */
	public String getSearchMsgId() {
		return searchMsgId;
	}

	/**
	 * @return Returns the searchSsn.
	 */
	public String getSearchSsn() {
		return searchSsn;
	}

	/**
	 * @return Returns the today.
	 */
	public IWTimestamp getToday() {
		return today;
	}

	/**
	 * @return Returns the uFrom.
	 */
	public IWTimestamp getUFrom() {
		return uFrom;
	}

	/**
	 * @return Returns the uTo.
	 */
	public IWTimestamp getUTo() {
		return uTo;
	}

}
