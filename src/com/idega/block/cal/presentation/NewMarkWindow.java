/*
 * Created on Mar 17, 2004
 */
package com.idega.block.cal.presentation;

import com.idega.block.cal.business.CalBusiness;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
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
	
	
	//parameter names
	private static String markFieldParameterName = "mark";
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
	
	private Table table;
	private Form form;
	
	public NewMarkWindow() {
		setHeight(200);
		setWidth(200);
		setResizable(true);
	}
	
	protected void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		markText = new Text(iwrb.getLocalizedString(markFieldParameterName,"Mark"));
		markDescriptionText = new Text(iwrb.getLocalizedString(markDescriptionFieldParameterName,"Mark description"));		
	}
	
	protected void initializeFields() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		markField = new TextInput(markFieldParameterName);
		markField.setMaxlength(1);
		markField.setSize(10);
		markDescriptionField = new TextInput(markDescriptionFieldParameterName);	
		markDescriptionField.setSize(20);
	
		submitButton = new SubmitButton(iwrb.getLocalizedString("save","Save"),submitButtonParameterName,submitButtonParameterValue);

		//closes the window
		closeButton = new CloseButton(iwrb.getLocalizedString("close","Close"));
	}	
	public void lineUp() {
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
		form.add(table);
	}
	public void main(IWContext iwc) throws Exception{
		form = new Form();
		initializeTexts();
		initializeFields();
		lineUp();
		add(form,iwc);
		String mark = iwc.getParameter(markFieldParameterName);
		String description = iwc.getParameter(markDescriptionFieldParameterName);
		String save = iwc.getParameter("submit");
		if(save != null && !save.equals("")) {
			getCalBusiness(iwc).createNewMark(mark,description);
			Link l = new Link();
			l.setWindowToOpen(LedgerWindow.class);
			String script = "window.opener." + l.getWindowToOpenCallingScript(iwc);
			setOnLoad(script);
			close();
			
		}
		
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
