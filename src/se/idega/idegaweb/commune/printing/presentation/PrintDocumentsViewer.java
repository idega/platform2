package se.idega.idegaweb.commune.printing.presentation;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.business.MessageComparator;
import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.message.data.PrintMessage;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
import se.idega.idegaweb.commune.presentation.ColumnList;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.printing.business.DocumentBusiness;
import se.idega.idegaweb.commune.printing.data.PrintDocuments;

import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseBMPBean;
import com.idega.block.process.data.CaseHome;
import com.idega.business.IBOLookup;
import com.idega.core.data.Address;
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
	private final static int ACTION_VIEW_UNPRINTED = 2;
	private final static int ACTION_SHOW_DELETE_INFO = 3;
	private final static int ACTION_DELETE_MESSAGE = 4;
	private final static int ACTION_PRINT_UNPRINTED_MESSAGES = 5;
	private final static int ACTION_PRINT_MESSAGE = 6;
	private final static int ACTION_PROC_SELECTED = 7;

	private final static String PARAM_VIEW_UNPRINTED = "prv_view_upr";
	private final static String PARAM_VIEW_MESSAGE_LIST = "prv_view_msg_list";
	private final static String PARAM_MESSAGE_ID = "prv_id";
	private final static String PARAM_SHOW_DELETE_INFO = "prv_s_delete_i";
	private final static String PARAM_DELETE_MESSAGE = "prv_delete_message";
	private final static String PARAM_PRINT_UNPRINTED = "prv_unprinted";
	private final static String PARAM_PRINT_MSG = "prv_pr_msg";
	private final static String PARAM_LETTER_TYPE = "prv_let_tp";
	private final static String PARAM_LETTER_STATUS = "prv_let_sts";

	private final static String PRM_STAMP_P_FROM = "prv_pfrm";
	;
	private final static String PRM_STAMP_U_FROM = "prv_ufrm";
	private final static String PRM_STAMP_P_TO = "prv_pto";
	private final static String PRM_STAMP_U_TO = "prv_uto";
	private final static String PRM_P_COUNT = "prv_pcnt";
	private final static String PRM_U_COUNT = "prv_ucnt";
	private final static String PRM_U_CHK = "prv_uchk";

	private final static String PRM_CURSOR_P = "prv_crs_p";
	private final static String PRM_CURSOR_U = "prv_crs_u";

	private final static String PRM_PROC_SELECTED = "prv_proc_sel";
	private final static String PRM_PRINT_SELECTED = "prv_pr_sel";
	private final static String PRM_DEL_SELECTED= "prv_del_sel";
	private final static String PRM_UNDEL_SELECTED= "prv_udel_sel";
	
	private final static String PRM_BULK_VIEW = "prv_bulk_view";
	private boolean isBulkType = false;
	private boolean isBulkManual = false;
	private boolean showTypesAsDropdown = false;
	private boolean useCheckBox = true;
	private String currentType = "";
	private String currentStatus = "";
	private int msgID = -1;
	private int fileID = -1;
	private IWTimestamp today = IWTimestamp.RightNow(),
		pFrom = null,
		pTo = null,
		uFrom = null,
		uTo = null;
	private int defaultDays = 7;
	private int defaultShown = 25;
	private int cursor_p = 0;
	private int cursor_u = 0;
	private int count_p = 25;
	private int count_u = 25;
	
	private String statusPrinted, statusUnprinted,statusDeleted,statusError;

	private Table mainTable = null;

	public PrintDocumentsViewer() {
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void main(IWContext iwc) {
		//debugParameters(iwc);
		this.setResourceBundle(getResourceBundle(iwc));
		if (iwc.isLoggedOn()) {
			try {
				init(iwc);
				initDates(iwc);
				initCursors(iwc);
				int action = parseAction(iwc);
				switch (action) {
					case ACTION_VIEW_MESSAGE_OVERVIEW :
						viewMessages(iwc);
						break;
					case ACTION_VIEW_MESSAGE_LIST :
						viewMessages(iwc);
						break;
					case ACTION_PRINT_UNPRINTED_MESSAGES :
						printAllUnPrintedMessages(iwc);
						viewMessages(iwc);
						break;
					case ACTION_PRINT_MESSAGE :
						printMessage(iwc);
						viewMessages(iwc);
						break;
					case ACTION_VIEW_UNPRINTED :
						addUnPrintedNameList(iwc);
						break;
					case ACTION_PROC_SELECTED :
						processSelected(iwc);
						viewMessages(iwc);
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
	
	public void init(IWContext iwc)throws RemoteException{
		MessageBusiness msgBuiz = getMessageBusiness(iwc);
		statusUnprinted = msgBuiz.getCaseStatusOpen().getStatus();
		statusPrinted = msgBuiz.getCaseStatusReady().getStatus();
		statusDeleted = msgBuiz.getCaseStatusInactive().getStatus();
		statusError = msgBuiz.getCaseStatusError().getStatus();
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
		int action = ACTION_VIEW_MESSAGE_OVERVIEW;

		if (iwc.isParameterSet(PARAM_VIEW_UNPRINTED)) {
			action = ACTION_VIEW_UNPRINTED;
		} else if (iwc.isParameterSet(PARAM_SHOW_DELETE_INFO)) {
			action = ACTION_SHOW_DELETE_INFO;
		} else if (iwc.isParameterSet(PARAM_DELETE_MESSAGE)) {
			action = ACTION_DELETE_MESSAGE;
		} else if (iwc.isParameterSet(PARAM_PRINT_UNPRINTED)) {
			action = ACTION_PRINT_UNPRINTED_MESSAGES;
		} else if (iwc.isParameterSet(PARAM_PRINT_MSG)) {
			action = ACTION_PRINT_MESSAGE;
		} else if (iwc.isParameterSet(PRM_PROC_SELECTED)) {
			action = ACTION_PROC_SELECTED;
		}

		if (iwc.isParameterSet(PARAM_LETTER_TYPE)) {
			currentType = iwc.getParameter(PARAM_LETTER_TYPE);
		}
		if (iwc.isParameterSet(PARAM_LETTER_STATUS)) {
			currentStatus = iwc.getParameter(PARAM_LETTER_STATUS);
		}
		/**@TODO: get this status from beans */
		if ("".equals(currentStatus))
			currentStatus=(statusUnprinted);

		if ("".equals(currentType))
			currentType = getDocumentBusiness(iwc).getPrintMessageTypes()[0];
		
		if(iwc.isParameterSet(PRM_BULK_VIEW)){
			isBulkType = true;
			isBulkManual = true;
		}else
			isBulkType = getDocumentBusiness(iwc).isBulkLetterType(currentType);

		if (iwc.isParameterSet(PARAM_MESSAGE_ID)) {
			msgID = Integer.parseInt(iwc.getParameter(PARAM_MESSAGE_ID));
		}
		return action;
	}

	private void initDates(IWContext iwc) throws Exception {
		pFrom = IWTimestamp.RightNow();
		pFrom.addDays(-defaultDays);
		pTo = IWTimestamp.RightNow();
		uFrom = IWTimestamp.RightNow();
		uFrom.addDays(-defaultDays);
		uTo = IWTimestamp.RightNow();
		if (iwc.isParameterSet(PRM_STAMP_P_FROM))
			pFrom = new IWTimestamp(iwc.getParameter(PRM_STAMP_P_FROM));
		if (iwc.isParameterSet(PRM_STAMP_U_FROM))
			uFrom = new IWTimestamp(iwc.getParameter(PRM_STAMP_U_FROM));
		if (iwc.isParameterSet(PRM_STAMP_P_TO))
			pTo = new IWTimestamp(iwc.getParameter(PRM_STAMP_P_TO));
		if (iwc.isParameterSet(PRM_STAMP_U_TO))
			uTo = new IWTimestamp(iwc.getParameter(PRM_STAMP_U_TO));

	}

	private void initCursors(IWContext iwc) throws Exception {
		if (iwc.isParameterSet(PRM_CURSOR_P))
			cursor_p = Integer.parseInt(iwc.getParameter(PRM_CURSOR_P));
		if (iwc.isParameterSet(PRM_CURSOR_U))
			cursor_u = Integer.parseInt(iwc.getParameter(PRM_CURSOR_U));
		if (iwc.isParameterSet(PRM_P_COUNT))
			count_p = Integer.parseInt(iwc.getParameter(PRM_P_COUNT));
		if (iwc.isParameterSet(PRM_U_COUNT))
			count_u = Integer.parseInt(iwc.getParameter(PRM_U_COUNT));
	}

	private void printAllUnPrintedMessages(IWContext iwc) throws Exception {
		int userID = ((Integer) iwc.getCurrentUser().getPrimaryKey()).intValue();
		Collection unPrintedLetters = getDocumentBusiness(iwc).getUnPrintedMessages(currentType);
		getDocumentBusiness(iwc).writeBulkPDF(
			unPrintedLetters,
			iwc.getCurrentUser(),
			"BulkLetterPDF",
			iwc.getApplicationSettings().getDefaultLocale(),
			currentType,
			false,
			true,
			true);
	}

	private void printMessage(IWContext iwc) throws Exception {
		int userID = ((Integer) iwc.getCurrentUser().getPrimaryKey()).intValue();
		if (msgID > 0) {
			PrintedLetterMessage msg =
				(PrintedLetterMessage) getDocumentBusiness(iwc)
					.getPrintedLetterMessageHome()
					.findByPrimaryKey(
					new Integer(msgID));
			fileID =
				getDocumentBusiness(iwc).writePDF(
					msg,
					iwc.getCurrentUser(),
					"LetterPDF",
					iwc.getApplicationSettings().getDefaultLocale(),
					true);
			//System.err.println("file id written :"+fileID);
			//getDocumentBusiness(iwc).writePrintedLetterPDF(msgId,userID);
		}
	}
	
	private void processSelected(IWContext iwc) throws Exception {
		String processPrm = iwc.getParameter(PRM_PROC_SELECTED);
		if(iwc.isParameterSet(PRM_PRINT_SELECTED)){	
			printSelected(iwc);
		}else if("undel".equals(processPrm)){
			undeleteSelected(iwc);
		}
		else if("del".equals(processPrm)){
			deleteSelected(iwc);
		}
	}

	private void printSelected(IWContext iwc) throws Exception {
		int userID = ((Integer) iwc.getCurrentUser().getPrimaryKey()).intValue();
		boolean bulk = iwc.isParameterSet("prv_bulk");
		boolean flag = !iwc.isParameterSet("prv_mark");
		System.err.println("bulk is"+String.valueOf(bulk));
		System.err.println("mark is"+String.valueOf(flag));
		String[] ids = iwc.getParameterValues(PRM_U_CHK);
		if (ids != null && ids.length > 0) {
			getDocumentBusiness(iwc).writeBulkPDF(
				ids,
				iwc.getCurrentUser(),
				"BulkLetterPDF",
				iwc.getApplicationSettings().getDefaultLocale(),
				currentType,
				true,
				flag,
				bulk);
		}
	}
		
	private void deleteSelected(IWContext iwc) throws Exception {
		String[] ids = iwc.getParameterValues(PRM_U_CHK);
		if (ids != null && ids.length > 0) {
				getMessageBusiness(iwc).flagMessagesWithStatus(iwc.getCurrentUser(),ids,statusDeleted);
		}
	}
	
	private void undeleteSelected(IWContext iwc) throws Exception {
			String[] ids = iwc.getParameterValues(PRM_U_CHK);
			if (ids != null && ids.length > 0) {
					getMessageBusiness(iwc).flagMessagesWithStatus(iwc.getCurrentUser(),ids,statusUnprinted);
			}
		}

	private void viewMessages(IWContext iwc) throws Exception {

		//add(getLocalizedHeader("printdoc.letters", "Letters for printing"));
		addTypeMenu(iwc);
		if (isBulkType)
			if(isBulkManual){
				addDocumentsListView(iwc);
			}
			else{
				addDocumentsList(iwc);
			}
		else{
			addMessagesListView(iwc);
		}
	}

	private void addTypeMenu(IWContext iwc) throws Exception {

		String[] types = getDocumentBusiness(iwc).getPrintMessageTypes();
		if (showTypesAsDropdown) {
			DropdownMenu drp = new DropdownMenu(PARAM_LETTER_TYPE);
			for (int i = 0; i < types.length; i++) {
				drp.addMenuElement(
					types[i],
					localize("printdoc.letter_type_" + types[i], types[i]));
			}
			drp.setToSubmit();
			drp.setSelectedElement(currentType);
			Form F = new Form();
			F.add(drp);
			F.add(new HiddenInput(PRM_STAMP_P_FROM, pFrom.toString()));
			F.add(new HiddenInput(PRM_STAMP_P_TO, pTo.toString()));
			F.add(new HiddenInput(PRM_STAMP_U_FROM, uFrom.toString()));
			F.add(new HiddenInput(PRM_STAMP_U_TO, uTo.toString()));
			add(F);
		} else {
			Table T = new Table();
			T.setCellpadding(2);
			int col = 1;
			for (int i = 0; i < types.length; i++) {
				Link typeLink =
					new Link(getHeader(localize("printdoc.letter_type_" + types[i], types[i])));
				typeLink.addParameter(PARAM_LETTER_TYPE, types[i]);
				typeLink.addParameter(PRM_STAMP_P_FROM, pFrom.toString());
				typeLink.addParameter(PRM_STAMP_U_FROM, uFrom.toString());
				typeLink.addParameter(PRM_STAMP_P_TO, pTo.toString());
				typeLink.addParameter(PRM_STAMP_U_TO, uTo.toString());
				T.add(typeLink, col++, 1);
			}
			add(T);
		}

	}

	private PresentationObject getPrintedDatesForm(IWContext iwc) {

		Table T = new Table();
		DateInput from = new DateInput(PRM_STAMP_P_FROM, true);
		from = (DateInput) getStyledInterface(from);
		from.setYearRange(today.getYear() - 5, today.getYear() + 2);
		DateInput to = new DateInput(PRM_STAMP_P_TO, true);
		to = (DateInput) getStyledInterface(to);
		to.setYearRange(today.getYear() - 5, today.getYear() + 2);

		from.setDate(pFrom.getSQLDate());
		to.setDate(pTo.getSQLDate());
		SubmitButton search =
			new SubmitButton(getResourceBundle().getLocalizedString("printdoc.fetch", "Fetch"));
		search = (SubmitButton) getButton(search);
		T.add(
			getHeader(getResourceBundle().getLocalizedString("printdoc.date_from", "From:")),
			1,
			1);
		T.add(from, 2, 1);
		T.add(getHeader(getResourceBundle().getLocalizedString("printdoc.date_to", "To:")), 3, 1);
		T.add(to, 4, 1);
		//T.add(getHeader(getResourceBundle().getLocalizedString("printdoc.count","Count")),5,1);
		T.add(getCountDrop(PRM_P_COUNT, count_p), 6, 1);
		T.add(search, 7, 1);
		T.add(new HiddenInput(PRM_STAMP_U_FROM, uFrom.toString()));
		T.add(new HiddenInput(PRM_STAMP_U_TO, uTo.toString()));
		T.add(new HiddenInput(PRM_U_COUNT, String.valueOf(count_u)));
		T.add(new HiddenInput(PARAM_LETTER_TYPE, this.currentType));
		
		T.setTopLine(true);
		T.setBottomLine(true);

		return T;

	}

	private DropdownMenu getCountDrop(String name, int selected) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement(String.valueOf(10));
		drp.addMenuElement(String.valueOf(25));
		drp.addMenuElement(String.valueOf(50));
		drp.addMenuElement(String.valueOf(75));
		drp.addMenuElement(String.valueOf(100));
		drp.addMenuElement(100000, localize("printdoc.all", "All"));
		drp.setSelectedElement(selected);
		return drp;
	}

	private DropdownMenu getStatusDrop(IWContext iwc, String name, String selected){
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement(statusUnprinted,localize("printdoc.unprinted", "Unprinted"));
		drp.addMenuElement(statusPrinted, localize("printdoc.printed", "Printed"));
		drp.addMenuElement(statusDeleted, localize("printdoc.deleted", "Deleted"));
		//drp.addMenuElement(statusDeleted, localize("printdoc.errors", "Errors"));
		drp.setSelectedElement(selected);
		return drp;
	}

	private PresentationObject getUnPrintedDatesForm(IWContext iwc) throws RemoteException {

		Table T = new Table();
		int col = 1;
		DateInput from = new DateInput(PRM_STAMP_U_FROM, true);
		from = (DateInput) getStyledInterface(from);
		from.setYearRange(today.getYear() - 5, today.getYear() + 2);
		DateInput to = new DateInput(PRM_STAMP_U_TO, true);
		to = (DateInput) getStyledInterface(to);
		to.setYearRange(today.getYear() - 5, today.getYear() + 2);
		from.setDate(uFrom.getSQLDate());
		to.setDate(uTo.getSQLDate());
		SubmitButton search =
			new SubmitButton(getResourceBundle().getLocalizedString("printdoc.fetch", "Fetch"));
		search = (SubmitButton) getButton(search);
		T.add(
			getHeader(getResourceBundle().getLocalizedString("printdoc.date_from", "From:")),
			col++,
			1);
		T.add(from, col++, 1);
		T.add(
			getHeader(getResourceBundle().getLocalizedString("printdoc.date_to", "To:")),
			col++,
			1);
		T.add(to, col++, 1);
		//T.add(getHeader(getResourceBundle().getLocalizedString("printdoc.count","Count")),5,1);
		T.add(getCountDrop(PRM_U_COUNT, count_u), col++, 1);
		T.add(getStatusDrop(iwc, PARAM_LETTER_STATUS, currentStatus), col++, 1);
		T.add(search, col++, 1);
		T.add(new HiddenInput(PRM_STAMP_P_FROM, pFrom.toString()));
		T.add(new HiddenInput(PRM_STAMP_P_TO, pTo.toString()));
		T.add(new HiddenInput(PRM_P_COUNT, String.valueOf(count_p)));
		T.add(new HiddenInput(PARAM_LETTER_TYPE, this.currentType));
		
		T.setTopLine(true);
		T.setBottomLine(true);
		T.setHeight(25);
		return T;

	}

	private PresentationObject getPrintButton() {
		Table T = new Table();
		T.setAlignment(T.HORIZONTAL_ALIGN_RIGHT);
		T.setCellpadding(2);
		SubmitButton print =new SubmitButton(	PRM_PRINT_SELECTED,getResourceBundle().getLocalizedString("printdoc.create_file", "Create file(s)"));
		
		print = (SubmitButton) getButton(print);
		int col = 1;
		// make sure PRM_PROC_SELECTED parameter is set, for the form to be processed
		if(currentStatus.equals(statusDeleted)){
			SubmitButton undelete =new SubmitButton(	PRM_UNDEL_SELECTED,getResourceBundle().getLocalizedString("printdoc.undelete", "Undelete"));
			undelete = (SubmitButton) getButton(undelete);
			undelete.setSubmitConfirm(localize("printdoc.undelete_message","Are you sure you want to mark these letters as unprinted ?"));
			T.add(new HiddenInput(PRM_PROC_SELECTED,"undel"));
			T.add(undelete, col++, 1);
		}
		else{
			SubmitButton delete =new SubmitButton(	PRM_DEL_SELECTED,getResourceBundle().getLocalizedString("printdoc.delete", "Delete"));
			delete = (SubmitButton) getButton(delete);
			delete.setSubmitConfirm(localize("printdoc.delete_message","Are you sure you want to mark these letters as deleted ?"));
			T.add(new HiddenInput(PRM_PROC_SELECTED,"del"));
			T.add(delete, col++, 1);
		}
		
		if(!currentStatus.equals(statusDeleted)){
			CheckBox mark = new CheckBox("prv_mark");
			T.add(mark, col++, 1);
			T.add(getLocalizedHeader("printdoc.keep_unprinted", "Keep unprinted status"),  col++, 1);
		}
		CheckBox bulk = new CheckBox("prv_bulk");
		T.add(bulk,  col++, 1);
		T.add(getLocalizedHeader("printdoc.create_bulk_letter", "Bulk letter"),  col++, 1);
		T.add(print,  col++, 1);
		return T;
	}

	private PresentationObject getCursorLinks(
		IWContext iwc,
		int totalsize,
		int cursor,
		String cursorPrm,
		int step) {
		Table T = new Table();
		T.setAlignment(T.HORIZONTAL_ALIGN_RIGHT);
		T.setCellpadding(2);
		if (cursor > 0) {
			Link prev = new Link(localize("printdoc.last", "last") + "  " + step);
			prev.addParameter(PARAM_LETTER_TYPE, currentType);
			prev.addParameter(PARAM_LETTER_STATUS, currentStatus);
			prev.addParameter(cursorPrm, String.valueOf(cursor - step));
			addDateParametersToLink(prev);
			T.add(prev, 1, 1);
		}
		if (cursor <= (totalsize - step)) {
			Link next = new Link(localize("printdoc.next", "next") + "  " + step);
			next.addParameter(PARAM_LETTER_TYPE, currentType);
			next.addParameter(PARAM_LETTER_STATUS, currentStatus);
			next.addParameter(cursorPrm, String.valueOf(cursor + step));
			addDateParametersToLink(next);
			T.add(next, 3, 1);
		}

		return T;
	}

	private void addDateParametersToLink(Link link) {
		link.addParameter(PRM_STAMP_P_FROM, pFrom.toString());
		link.addParameter(PRM_STAMP_U_FROM, uFrom.toString());
		link.addParameter(PRM_STAMP_P_TO, pTo.toString());
		link.addParameter(PRM_STAMP_U_TO, uTo.toString());
		link.addParameter(PRM_U_COUNT, count_u);
		link.addParameter(PRM_P_COUNT, count_p);
	}

	private void addDocumentsList(IWContext iwc) throws Exception {
		Form uForm = new Form();
		Form pForm = new Form();
		Table uT = new Table();
		Table pT = new Table();
		uForm.add(uT);
		pForm.add(pT);
		add(uForm);
		add(pForm);
		int urow = 1;
		uT.add(getLocalizedHeader("printdoc.unprinted_letters", "Letters for printing"), 1, urow++);

		ColumnList unPrintedLetterDocs = new ColumnList(4);

		unPrintedLetterDocs.setWidth(Table.HUNDRED_PERCENT);
		unPrintedLetterDocs.setBackroundColor("#e0e0e0");
		//messageList.setHeader(localize("printdoc.name","Name"),1);
		//unPrintedLetterDocs.setHeader(localize("printdoc.date","Date"),1);
		//unPrintedLetterDocs.setHeader(localize("printdoc.n_o_docs","Number of documents"),2);

		unPrintedLetterDocs.add(localize("printdoc.unprinted", "Unprinted"));
		//messageList.add("-");
		unPrintedLetterDocs.add(
			Integer.toString(
				getDocumentBusiness(iwc).getUnprintedMessagesCountByType(currentType)));

		Link printLink = new Link(localize("printdoc.print", "Print"));
		printLink.addParameter(PARAM_PRINT_UNPRINTED, "true");
		printLink.addParameter(PARAM_LETTER_TYPE, currentType);
		addDateParametersToLink(printLink);
		unPrintedLetterDocs.add(printLink);

		Link viewUnprintedLink = new Link(localize("printdoc.name_list", "Namelist"));
		viewUnprintedLink.addParameter(PARAM_VIEW_UNPRINTED, "true");
		viewUnprintedLink.addParameter(PARAM_LETTER_TYPE, currentType);
		addDateParametersToLink(viewUnprintedLink);
		unPrintedLetterDocs.add(viewUnprintedLink);

		uT.add(unPrintedLetterDocs, 1, urow++);
		uT.add(Text.getBreak(), 1, urow++);

		ColumnList printedLetterDocs = new ColumnList(4);

		Collection printDocs =
			getDocumentBusiness(iwc).getPrintedDocuments(currentType, pFrom, pTo);

		int prow = 1;
		pT.add(getLocalizedHeader("printdoc.printed_letters", "Printed letters"), 1, prow++);
		pT.add(getPrintedDatesForm(iwc), 1, prow++);
		pT.add(printedLetterDocs, 1, prow++);
		pT.add(getCursorLinks(iwc, printDocs.size(), cursor_p, PRM_CURSOR_P, count_p), 1, prow++);

		printedLetterDocs.setHeader("#", 1);
		printedLetterDocs.setHeader(localize("printdoc.printed_date", "Printing date"), 2);
		printedLetterDocs.setHeader(localize("printdoc.n_o_docs", "Number of documents"), 3);
		printedLetterDocs.setWidth(Table.HUNDRED_PERCENT);

		Iterator iter = printDocs.iterator();
		int count = cursor_p + 1;
		int ccp = count_p + cursor_p;
		if (cursor_p > 0) {
			while (iter.hasNext() && cursor_p > 0) {
				iter.next();
				cursor_p--;
			}
		}
		while (iter.hasNext() && count <= ccp) {
			PrintDocuments doc = (PrintDocuments) iter.next();
			printedLetterDocs.add(String.valueOf(count));
			printedLetterDocs.add(doc.getCreated().toString());
			//messageList.add("-");
			printedLetterDocs.add(Integer.toString(doc.getNumberOfSubDocuments()));
			int fileID = doc.getDocumentFileID();
			Link viewLink = new Link(localize("printdoc.view", "View"));
			viewLink.setFile(fileID);
			printedLetterDocs.add(viewLink);
			count++;
		}
	}
	
	private void addDocumentsListView(IWContext iwc) throws Exception {
			Form myForm = new Form();
			Table T = new Table();
			myForm.add(T);
			add(myForm);
			int urow = 1;
			
			if(isBulkManual)
					T.add(new HiddenInput(PRM_BULK_VIEW,"true"));

			ColumnList printedLetterDocs = new ColumnList(4);

			Collection printDocs = getDocumentBusiness(iwc).getPrintedDocuments(currentType, pFrom, pTo);

			int prow = 1;
			Table hT = new Table(2,1);
				hT.setWidth(hT.HUNDRED_PERCENT);
				hT.setAlignment(2,1,hT.HORIZONTAL_ALIGN_RIGHT);
		
				hT.add(getLocalizedHeader("printdoc.printed_letters", "Printed letters"), 1,1);
				Link singleMsgs = new Link(localize("printdoc.normal_view","Normal view"));
				singleMsgs.addParameter(PARAM_LETTER_TYPE, currentType);
				singleMsgs.addParameter(PARAM_LETTER_STATUS, currentStatus);
				addDateParametersToLink(singleMsgs);
				hT.add(singleMsgs,2,1);
		
				T.add(hT,1,prow++);
			T.add(getUnPrintedDatesForm(iwc), 1, prow++);
			T.add(printedLetterDocs, 1, prow++);
			T.add(getCursorLinks(iwc, printDocs.size(), cursor_p, PRM_CURSOR_P, count_p), 1, prow++);

			printedLetterDocs.setHeader("#", 1);
			printedLetterDocs.setHeader(localize("printdoc.printed_date", "Printing date"), 2);
			printedLetterDocs.setHeader(localize("printdoc.n_o_docs", "Number of documents"), 3);
			printedLetterDocs.setWidth(Table.HUNDRED_PERCENT);

			Iterator iter = printDocs.iterator();
			int count = cursor_p + 1;
			int ccp = count_p + cursor_p;
			if (cursor_p > 0) {
				while (iter.hasNext() && cursor_p > 0) {
					iter.next();
					cursor_p--;
				}
			}
			while (iter.hasNext() && count <= ccp) {
				PrintDocuments doc = (PrintDocuments) iter.next();
				printedLetterDocs.add(String.valueOf(count));
				printedLetterDocs.add(doc.getCreated().toString());
				//messageList.add("-");
				int subcount  = doc.getNumberOfSubDocuments();
				
				Link namesLink = new Link(String.valueOf(subcount));
				namesLink.addParameter(PARAM_VIEW_UNPRINTED, "true");
				namesLink.addParameter(PARAM_LETTER_TYPE, currentType);
				namesLink.addParameter(PARAM_LETTER_STATUS, currentStatus);
				namesLink.addParameter("prv_file_id",doc.getDocumentFileID());
				addDateParametersToLink(namesLink);
				printedLetterDocs.add(namesLink);
				
				int fileID = doc.getDocumentFileID();
				Link viewLink = new Link(localize("printdoc.view", "View"));
				viewLink.setFile(fileID);
				printedLetterDocs.add(viewLink);
				count++;
			}
		}

	private void addMessagesList(IWContext iwc) throws Exception {
		Form uForm = new Form();
		Form pForm = new Form();
		Table uT = new Table();
		Table pT = new Table();
		uForm.add(uT);
		pForm.add(pT);
		add(uForm);
		add(pForm);
		ColumnList unPrintedLetterDocs = new ColumnList(7);

		int urow = 1;
		Collection unprintedLetters =
			getMessageBusiness(iwc).getSingleUnPrintedLetterMessagesByType(currentType, uFrom, uTo);
		uT.add(getLocalizedHeader("printdoc.unprinted_letters", "Letters for printing"), 1, urow++);
		uT.add(getUnPrintedDatesForm(iwc), 1, urow++);
		uT.add(unPrintedLetterDocs, 1, urow++);
		uT.add(getPrintButton(), 1, urow++);
		uT.add(
			getCursorLinks(iwc, unprintedLetters.size(), cursor_u, PRM_CURSOR_U, count_u),
			1,
			urow++);

		unPrintedLetterDocs.setWidth(Table.HUNDRED_PERCENT);
		unPrintedLetterDocs.setBackroundColor("#e0e0e0");
		unPrintedLetterDocs.setHeader("#", 1);
		unPrintedLetterDocs.setHeader(localize("printdoc.created_date", "Message created"), 2);
		unPrintedLetterDocs.setHeader(localize("printdoc.receiver", "Receiver"), 3);
		unPrintedLetterDocs.setHeader(localize("printdoc.subject", "Subject"), 4);

		CheckBox checkAll = new CheckBox("checkall");
		checkAll.setToCheckOnClick(PRM_U_CHK, true);
		unPrintedLetterDocs.setHeader(checkAll, 5);
		unPrintedLetterDocs.setHeader(localize("printdoc.file", "File"), 6);
		//unPrintedLetterDocs.setHeader(localize("printdoc.select","Select"),5);
		unPrintedLetterDocs.setHeader(localize("printdoc.bulk_file", "Bulk ID"), 7);

		Iterator iter = unprintedLetters.iterator();
		int count = cursor_u + 1;
		int ccu = count_u + cursor_u;
		if (cursor_u > 0) {
			while (iter.hasNext() && cursor_u > 0) {
				iter.next();
				cursor_u--;
			}
		}

		int bulkId;
		while (iter.hasNext() && count <= ccu) {
			PrintedLetterMessage msg = (PrintedLetterMessage) iter.next();
			unPrintedLetterDocs.add(String.valueOf(count));
			unPrintedLetterDocs.add(msg.getCreated().toString());
			//messageList.add("-");

			unPrintedLetterDocs.add(msg.getOwner().getName());

			unPrintedLetterDocs.add(msg.getSubject());
			if (useCheckBox) {
				CheckBox box = new CheckBox(PRM_U_CHK, msg.getPrimaryKey().toString());
				uForm.addParameter(PARAM_LETTER_TYPE, currentType);
				uForm.addParameter(PARAM_LETTER_STATUS, currentStatus);
				unPrintedLetterDocs.add(box);
			} else {
				Link printLink = new Link(localize("printdoc.print", "Print"));
				printLink.addParameter(PARAM_PRINT_MSG, "true");
				printLink.addParameter(PARAM_LETTER_TYPE, currentType);
				printLink.addParameter(PARAM_LETTER_STATUS, currentStatus);
				printLink.addParameter(PARAM_MESSAGE_ID, msg.getPrimaryKey().toString());
				addDateParametersToLink(printLink);
				unPrintedLetterDocs.add(printLink);
			}
			int fileID = msg.getMessageDataFileID();
			if (fileID > 0) {
				Link viewLink = new Link(localize("printdoc.view", "View"));
				viewLink.setFile(fileID);
				unPrintedLetterDocs.add(viewLink);
			} else {
				unPrintedLetterDocs.add("-");
			}

			bulkId = msg.getMessageBulkDataFileID();
			if (bulkId > 0) {
				Link bulkLink = new Link(String.valueOf(msg.getMessageBulkDataFileID()));
				bulkLink.setFile(msg.getMessageBulkDataFileID());
				unPrintedLetterDocs.add(bulkLink);
			} else {
				unPrintedLetterDocs.add("-");
			}
			count++;
		}

		ColumnList printedLetterDocs = new ColumnList(6);
		printedLetterDocs.setWidth(Table.HUNDRED_PERCENT);
		printedLetterDocs.setBackroundColor("#e0e0e0");
		printedLetterDocs.setHeader("#", 1);
		printedLetterDocs.setHeader(localize("printdoc.created_date", "Message created"), 2);
		printedLetterDocs.setHeader(localize("printdoc.receiver", "Receiver"), 3);
		printedLetterDocs.setHeader(localize("printdoc.subject", "Subject"), 4);
		printedLetterDocs.setHeader(localize("printdoc.file", "File"), 5);
		printedLetterDocs.setHeader(localize("printdoc.bulk_file", "Bulk File"), 6);
		Collection printedLetters =
			getMessageBusiness(iwc).getSinglePrintedLetterMessagesByType(currentType, pFrom, pTo);

		int prow = 1;
		pT.add(Text.getBreak(), 1, prow++);
		pT.add(getLocalizedHeader("printdoc.printed_letters", "Printed letters"), 1, prow++);
		pT.add(getPrintedDatesForm(iwc), 1, prow++);
		pT.add(printedLetterDocs, 1, prow++);
		pT.add(
			getCursorLinks(iwc, printedLetters.size(), cursor_p, PRM_CURSOR_P, count_p),
			1,
			prow++);

		iter = printedLetters.iterator();
		count = cursor_p + 1;
		int ccp = cursor_p + count_p;
		if (cursor_p > 0) {
			while (iter.hasNext() && cursor_p > 0) {
				iter.next();
				cursor_p--;
			}
		}
		while (iter.hasNext() && count <= ccp) {
			PrintedLetterMessage msg = (PrintedLetterMessage) iter.next();
			printedLetterDocs.add(String.valueOf(count));
			printedLetterDocs.add(msg.getCreated().toString());
			//messageList.add("-");
			printedLetterDocs.add(msg.getOwner().getName());
			printedLetterDocs.add(msg.getSubject());
			int fileID = msg.getMessageDataFileID();
			Link viewLink = new Link(localize("printdoc.view", "View"));
			viewLink.setFile(fileID);
			printedLetterDocs.add(viewLink);
			bulkId = msg.getMessageBulkDataFileID();
			if (bulkId > 0) {

				Link bulkLink = new Link(String.valueOf(bulkId));
				bulkLink.setFile(bulkId);
				printedLetterDocs.add(bulkLink);
			} else
				printedLetterDocs.add("");
			count++;
		}
	}

	private void addMessagesListView(IWContext iwc) throws Exception {
		Form myForm = new Form();
		Table T = new Table();
		myForm.add(T);
		add(myForm);

		ColumnList letterList = new ColumnList(6);

		int row = 1;
		Collection letters =
			getMessageBusiness(iwc).getSingleLettersByTypeAndStatus(currentType,	currentStatus,	uFrom,uTo);
		Table hT = new Table(2,1);
		hT.setWidth(hT.HUNDRED_PERCENT);
		hT.setAlignment(2,1,hT.HORIZONTAL_ALIGN_RIGHT);
		
		hT.add(getLocalizedHeader("printdoc.letters", "Letters"), 1,1);
		Link singleMsgs = new Link(localize("printdoc.bulk_view","Bulk view"));
		singleMsgs.addParameter(PRM_BULK_VIEW,"true");
		singleMsgs.addParameter(PARAM_LETTER_TYPE, currentType);
		singleMsgs.addParameter(PARAM_LETTER_STATUS, currentStatus);
		addDateParametersToLink(singleMsgs);
		hT.add(singleMsgs,2,1);
		
		T.add(hT,1,row++);
		T.add(getUnPrintedDatesForm(iwc), 1, row++);
		T.add(letterList, 1, row++);
		T.add(getPrintButton(), 1, row++);
		T.add(getCursorLinks(iwc, letters.size(), cursor_u, PRM_CURSOR_U, count_u), 1, row++);

		letterList.setWidth(Table.HUNDRED_PERCENT);
		letterList.setBackroundColor("#e0e0e0");
		letterList.setHeader("#", 1);
		letterList.setHeader(localize("printdoc.created_date", "Message created"), 2);
		letterList.setHeader(localize("printdoc.receiver", "Receiver"), 3);
		letterList.setHeader(localize("printdoc.subject", "Subject"), 4);

		CheckBox checkAll = new CheckBox("checkall");
		checkAll.setToCheckOnClick(PRM_U_CHK, true);
		letterList.setHeader(checkAll, 5);
		letterList.setHeader(localize("printdoc.file", "File"), 6);
		//letterList.setHeader(localize("printdoc.bulk_file", "Bulk ID"), 7);

		Iterator iter = letters.iterator();
		int count = cursor_u + 1;
		int ccu = count_u + cursor_u;
		if (cursor_u > 0) {
			while (iter.hasNext() && cursor_u > 0) {
				iter.next();
				cursor_u--;
			}
		}

		int bulkId;
		while (iter.hasNext() && count <= ccu) {
			PrintedLetterMessage msg = (PrintedLetterMessage) iter.next();
			letterList.add(String.valueOf(count));
			letterList.add(msg.getCreated().toString());
			//messageList.add("-");

			letterList.add(msg.getOwner().getName());

			letterList.add(msg.getSubject());
			if (useCheckBox) {
				CheckBox box = new CheckBox(PRM_U_CHK, msg.getPrimaryKey().toString());
				myForm.addParameter(PARAM_LETTER_TYPE, currentType);
				myForm.addParameter(PARAM_LETTER_STATUS, currentStatus);
				letterList.add(box);
			} else {
				Link printLink = new Link(localize("printdoc.print", "Print"));
				printLink.addParameter(PARAM_PRINT_MSG, "true");
				printLink.addParameter(PARAM_LETTER_TYPE, currentType);
				printLink.addParameter(PARAM_LETTER_STATUS, currentStatus);
				printLink.addParameter(PARAM_MESSAGE_ID, msg.getPrimaryKey().toString());
				addDateParametersToLink(printLink);
				letterList.add(printLink);
			}
			int fileID = msg.getMessageDataFileID();
			if (fileID > 0) {
				//Link viewLink = new Link(localize("printdoc.view", "View"));
				Link viewLink = new Link(String.valueOf(fileID));
				viewLink.setFile(fileID);
				letterList.add(viewLink);
			} else {
				letterList.add("-");
			}

/*
			bulkId = msg.getMessageBulkDataFileID();
			if (bulkId > 0) {
				Link bulkLink = new Link(String.valueOf(msg.getMessageBulkDataFileID()));
				bulkLink.setFile(msg.getMessageBulkDataFileID());
				letterList.add(bulkLink);
			} else {
				letterList.add("-");
			}
		*/
			count++;
		}

	}

	private void addUnPrintedNameList(IWContext iwc) throws Exception {
		String prmFile_id  = iwc.getParameter("prv_file_id");
		int file_id = -1;
		if(!"".equals(prmFile_id))
			file_id = Integer.parseInt(prmFile_id);
		ColumnList unPrintedNames = new ColumnList(3);
		unPrintedNames.setWidth(Table.HUNDRED_PERCENT);
		unPrintedNames.setBackroundColor("#e0e0e0");
		//messageList.setHeader(localize("printdoc.name","Name"),1);
		unPrintedNames.setHeader(localize("printdoc.created_date", "Message created"), 1);
		unPrintedNames.setHeader(localize("printdoc.receiver", "Receiver"), 2);
		unPrintedNames.setHeader(localize("printdoc.address", "Address"), 3);
		Collection letters = null;
		if(file_id > 0){
			letters = getMessageBusiness(iwc).getLettersByBulkFile(file_id,currentType,currentStatus);
		}
		else{
			letters =	getMessageBusiness(iwc).getUnPrintedLetterMessagesByType(currentType);
		}
		Iterator iter = letters.iterator();
		PrintMessage msg;
		User owner;
		UserBusiness ub = getUserBusiness(iwc);
		Address addr;
		String sAddr = "";
		while (iter.hasNext()) {
			msg = (PrintMessage) iter.next();
			owner = msg.getOwner();
			unPrintedNames.add(msg.getCreated().toString());
			unPrintedNames.add(owner.getName());
			try {
				addr = ub.getUsersMainAddress(owner);
				unPrintedNames.add(addr.getStreetAddress());
			} catch (Exception ex) {
				unPrintedNames.add(getErrorText(localize("printdoc.noaddress", "No address")));
			}
		}
		addTypeMenu(iwc);
		add(unPrintedNames);
	}

	private MessageBusiness getMessageBusiness(IWContext iwc) throws RemoteException {
		return (MessageBusiness) com.idega.business.IBOLookup.getServiceInstance(
			iwc,
			MessageBusiness.class);
	}

	private DocumentBusiness getDocumentBusiness(IWContext iwc) throws Exception {
		return (DocumentBusiness) com.idega.business.IBOLookup.getServiceInstance(
			iwc,
			DocumentBusiness.class);
	}

	private UserBusiness getUserBusiness(IWApplicationContext iwac) throws Exception {
		return (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
	}

	private Message getMessage(String id, IWContext iwc) throws Exception {
		int msgId = Integer.parseInt(id);
		Message msg = getMessageBusiness(iwc).getUserMessage(msgId);
		return msg;
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
	// http://localhost:8080/nacka/index.jsp?prv_let_tp=PASS&prv_pfrm=2003-02-14+08%3A41%3A08&prv_ufrm=2003-02-14+08%3A41%3A08&prv_pto=2003-02-21+08%3A41%3A08&prv_uto=2003-02-21+08%3A41%3A08&iw_language=sv_SE&ib_page=299&idega_session_id=8BEE24A6C87C5C9514E48C3D31503DCA
}
