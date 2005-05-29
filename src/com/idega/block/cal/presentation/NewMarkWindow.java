/*
 * Created on Mar 17, 2004
 */
package com.idega.block.cal.presentation;

import java.util.Collection;
import java.util.Iterator;
import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.data.AttendanceMark;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.StyledButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class NewMarkWindow extends StyledIWAdminWindow{
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.cal";
	
	public final static String MARK = "mark";
	//parameter names
	private static String markFieldParameterName = "mark";
	private static String markIDParameterName = "markID";
	private static String markDescriptionFieldParameterName = "mark_description";
	private static String submitButtonParameterName = "submit";
	private static String submitButtonParameterValue = "save";
	
	private Help help; 
	private static final String HELP_TEXT_KEY = "new_marker";
	
	//texts
	private Text markText;
	private Text markDescriptionText;
	
	//fields
	private TextInput markField;
	private TextInput markDescriptionField;
	
	//buttons
	private SubmitButton saveButton;
	private StyledButton styledSaveButton;
	private CloseButton closeButton;
	private StyledButton styledCloseButton;
	private SubmitButton deleteButton;
	private StyledButton styledDeleteButton;
	
	private Table mainTable;
	private Form form;
	
	public NewMarkWindow() {
		setHeight(200);
		setWidth(300);
		setResizable(true);
		setScrollbar(false);
	}
	
	protected void initializeTexts(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);

		markText = new Text(iwrb.getLocalizedString(markFieldParameterName,"Mark"));
		markDescriptionText = new Text(iwrb.getLocalizedString(markDescriptionFieldParameterName,"Mark description"));		
	}
	
	protected void initializeFields(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		//IWBundle iwb = getBundle(iwc);
		
		markField = new TextInput(markFieldParameterName);
		markField.setMaxlength(1);
		markField.setSize(10);
		markDescriptionField = new TextInput(markDescriptionFieldParameterName);	
		markDescriptionField.setSize(20);
	
		saveButton = new SubmitButton(iwrb.getLocalizedString("save","Save"),submitButtonParameterName,submitButtonParameterValue);
		styledSaveButton = new StyledButton(saveButton);

		//closes the window
		closeButton = new CloseButton(iwrb.getLocalizedString("close","Close"));
		styledCloseButton = new StyledButton(closeButton);
		
		String markID = iwc.getParameter(markIDParameterName);
		
		if(markID != null && !markID.equals("")) {
			
			deleteButton = new SubmitButton(iwrb.getLocalizedString("delete","Delete"));
			deleteButton.setWindowToOpen(ConfirmDeleteWindow.class);
			deleteButton.addParameter(ConfirmDeleteWindow.PRM_DELETE_ID, markID);
			deleteButton.addParameter(ConfirmDeleteWindow.PRM_DELETE, CalendarParameters.PARAMETER_TRUE);
			deleteButton.addParameter(ConfirmDeleteWindow.PRM_ENTRY_OR_LEDGER,MARK);
			deleteButton.addParameter(LedgerWindow.LEDGER,iwc.getParameter(LedgerWindow.LEDGER));
			styledDeleteButton = new StyledButton(deleteButton);
			
			
//			deleteButton = new SubmitButton(iwrb.getLocalizedString("delete", "Delete"),markIDParameterName,markID,"window.open("+ deleteWindow.getURL(iwc) +")");
			AttendanceMark mark = getCalBusiness(iwc).getMark(Integer.parseInt(markID));
			markField.setContent(mark.getMark());
			markDescriptionField.setContent(mark.getMarkDescription());
			
		}
		
	}	
	public void lineUp(IWContext iwc) {	
		mainTable = new Table(1,3);
		mainTable.setCellspacing(0);
		mainTable.setCellpadding(0);
		mainTable.setWidth(Table.HUNDRED_PERCENT);
		mainTable.setHeight(2, 5);
	    
	    Table inputTable = new Table();
		inputTable.setWidth(Table.HUNDRED_PERCENT);
		inputTable.setCellspacing(12);
		inputTable.setCellpadding(0);
		inputTable.setStyleClass("main");
		inputTable.add(markText + ":",1,1);
		inputTable.add(markField,2,1);
		inputTable.add(markDescriptionText + ":",1,2);
		inputTable.add(markDescriptionField,2,2);
		inputTable.setAlignment(2,3,"right");
		
		Table buttonTable = new Table();
		buttonTable.setCellspacing(0);
		buttonTable.setCellpadding(0);
		buttonTable.setAlignment(2,1,Table.HORIZONTAL_ALIGN_RIGHT);
		buttonTable.add(styledSaveButton,1,1);
		buttonTable.setWidth(2, "5");
		buttonTable.add(styledCloseButton,3,1);
		String markID = iwc.getParameter(markIDParameterName);
		if(markID != null && !markID.equals("")) {
		    buttonTable.setWidth(4, "5");
		    buttonTable.add(styledDeleteButton,5,1);
		}
		
		Table helpTable = new Table();
		helpTable.setCellpadding(0);
		helpTable.setCellspacing(0);
		helpTable.add(getHelp(HELP_TEXT_KEY),1,1);
		
		Table bottomTable = new Table();
		bottomTable.setCellpadding(0);
		bottomTable.setCellspacing(5);
		bottomTable.setWidth(Table.HUNDRED_PERCENT);
		bottomTable.setStyleClass("main");
		bottomTable.add(helpTable,1,1);
		bottomTable.setAlignment(2,1,Table.HORIZONTAL_ALIGN_RIGHT);
		bottomTable.add(buttonTable,2,1);
		
		mainTable.add(inputTable,1,1);
		mainTable.add(bottomTable,1,3);
		
		form.add(mainTable);
	}
	
	public void saveMark(IWContext iwc) {
		CalBusiness calBiz = getCalBusiness(iwc);
		IWResourceBundle iwrb = getResourceBundle(iwc);
		boolean b = false;
		
		String mark = iwc.getParameter(markFieldParameterName);
		String description = iwc.getParameter(markDescriptionFieldParameterName);
		String markID = iwc.getParameter(markIDParameterName);
		
		Collection marks = calBiz.getAllMarks();
		Iterator marksIter = marks.iterator();
		while(marksIter.hasNext()) {
			AttendanceMark m = (AttendanceMark) marksIter.next();
			if(mark.equals(m.getMark())) {
				b = true;
			}
		}
		if(b && (markID == null || markID.equals(""))) {
			setAlertOnLoad(iwrb.getLocalizedString("newMarkWindow.mark_exists_msg","The selected Mark exists already. Please choose another one"));
		}
		else {
			if(markID != null && !markID.equals("")) {
				calBiz.createNewMark(Integer.parseInt(markID),mark, description);
			}
			else {
				calBiz.createNewMark(-1,mark,description);
			}
			
//			setOnLoad("window.opener.parent.location.reload()");
			close();
		}
		
	}
	public void main(IWContext iwc) throws Exception{
	  IWResourceBundle iwrb = getResourceBundle(iwc);
		//IWBundle iwb = getBundle(iwc);
		
	  addTitle(iwrb.getLocalizedString("create_new_mark", "Create a new mark"), TITLE_STYLECLASS);
	    
		form = new Form();
		form.maintainParameter(markIDParameterName);
		initializeTexts(iwc);
		initializeFields(iwc);
		lineUp(iwc);
		
		form.maintainParameter(LedgerWindow.LEDGER);

		String save = iwc.getParameter("submit");
		if(save != null && !save.equals("")) {
			saveMark(iwc);
		}
		setOnUnLoad("window.opener.parent.location.reload()");
		add(form,iwc);
		
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	public CalBusiness getCalBusiness(IWApplicationContext iwc) {
		CalBusiness calBiz = null;
		if (calBiz == null) {
			try {
				calBiz = (CalBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CalBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return calBiz;
	}

}
