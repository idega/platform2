/*
 * Created on Mar 17, 2004
 */
package com.idega.block.cal.presentation;

import java.util.Collection;
import java.util.Iterator;

import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.data.AttendanceMark;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
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
	
	//texts
	private Text markText;
	private Text markDescriptionText;
	
	//fields
	private TextInput markField;
	private TextInput markDescriptionField;
	
	//buttons
	private SubmitButton submitButton;
	private CloseButton closeButton;
	private Link deleteLink;
	
	private Table table;
	private Form form;
	
	public NewMarkWindow() {
		setHeight(200);
		setWidth(200);
		setResizable(true);
	}
	
	protected void initializeTexts(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);

		markText = new Text(iwrb.getLocalizedString(markFieldParameterName,"Mark"));
		markDescriptionText = new Text(iwrb.getLocalizedString(markDescriptionFieldParameterName,"Mark description"));		
	}
	
	protected void initializeFields(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		IWBundle iwb = getBundle(iwc);
		
		markField = new TextInput(markFieldParameterName);
		markField.setMaxlength(1);
		markField.setSize(10);
		markDescriptionField = new TextInput(markDescriptionFieldParameterName);	
		markDescriptionField.setSize(20);
	
		submitButton = new SubmitButton(iwrb.getLocalizedString("save","Save"),submitButtonParameterName,submitButtonParameterValue);

		//closes the window
		closeButton = new CloseButton(iwrb.getLocalizedString("close","Close"));
		
		String markID = iwc.getParameter(markIDParameterName);
		
		if(markID != null && !markID.equals("")) {
			
			deleteLink = new Link(iwrb.getLocalizedString("delete","Delete"));
			deleteLink.setWindowToOpen(ConfirmDeleteWindow.class);
			deleteLink.addParameter(ConfirmDeleteWindow.PRM_DELETE_ID, markID);
			deleteLink.addParameter(ConfirmDeleteWindow.PRM_DELETE, CalendarParameters.PARAMETER_TRUE);
			deleteLink.addParameter(ConfirmDeleteWindow.PRM_ENTRY_OR_LEDGER,MARK);
			deleteLink.addParameter(LedgerWindow.LEDGER,iwc.getParameter(LedgerWindow.LEDGER));
			Image del = iwb.getImage("delete2.gif");
			deleteLink.setImage(del);
			
			
//			deleteButton = new SubmitButton(iwrb.getLocalizedString("delete", "Delete"),markIDParameterName,markID,"window.open("+ deleteWindow.getURL(iwc) +")");
			AttendanceMark mark = getCalBusiness(iwc).getMark(Integer.parseInt(markID));
			markField.setContent(mark.getMark());
			markDescriptionField.setContent(mark.getMarkDescription());
			
		}
		
	}	
	public void lineUp(IWContext iwc) {
		table = new Table();
		table.setCellspacing(0);
		table.setCellpadding(0);
		table.setStyleClass("main");
		table.add(markText + ":",1,1);
		table.add(markField,2,1);
		table.add(markDescriptionText + ":",1,2);
		table.add(markDescriptionField,2,2);
		table.setAlignment(2,3,"right");
		table.add(submitButton,2,3);
		table.add(Text.NON_BREAKING_SPACE,2,3);
		table.add(closeButton,2,3);
		String markID = iwc.getParameter(markIDParameterName);
		if(markID != null && !markID.equals("")) {
			table.add(Text.NON_BREAKING_SPACE,2,3);
			table.add(deleteLink,2,3);
		}		
		form.add(table);
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
