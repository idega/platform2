/*
 * Created on 2004-okt-13
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.ehealth.presentation;


//import se.idega.util.PIDChecker;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.ResetButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
//import com.idega.util.Age;



/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SendMessage extends EHealthBlock {
	
	private String prefix = "patient_";
	private String prmForm = prefix + "form_contact";
	private String prmInform = prefix + "info_channel";
	private String prmClear = prefix + "clear";
	private String prmChoose = prefix + "choose";
	private String prmMessage = prefix + "message";
	private String prmHeadingCase = prefix + "heading_case";
	private String prmSend = prefix + "send";
	private String prmDoctor = prefix + "doctor";
	
	//private int userID = -1;
	//private User user;
	IWContext _iwc = null;
	
	public void main(IWContext iwc) throws Exception {
		
		_iwc = iwc;
		
		//userID = iwc.getUserId();
		
		/*if (userID > 0) {
			user = ((UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class)).getUser(userID);
		}
		*/
		add(getSendMessageForm());
		
	}
	
	
	//public PresentationObject getVisitForm(IWContext iwc, User userVK) throws java.rmi.RemoteException {
	public PresentationObject getSendMessageForm() {
		Form myForm = new Form();
		myForm.setName(prmForm);
		
		Table table = new Table(4, 5);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(2, 2, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(2, 3, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(2, 4, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(2, 5, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(4, 5, Table.VERTICAL_ALIGN_BOTTOM);
	
		
		
		table.setWidth("300");
		
		
		myForm.add(table);
		int row = 1;
		
		/*Age age = null;
		if (user != null && user.getDateOfBirth() != null)
			age = new Age(user.getDateOfBirth());
		else if (user != null && user.getPersonalID() != null)
			age = new Age(PIDChecker.getInstance().getDateFromPersonalID(user.getPersonalID()));
		*/		
			DropdownMenu dropDr = new DropdownMenu(prmDoctor);
			dropDr.addMenuElementFirst("-1", "Till");
			dropDr.addMenuElement("1", "Dr Magne Syhl");
			dropDr.addMenuElement("2", "Dr Alve Don");
			dropDr.addMenuElement("2", "Dr Inga Pren");
			dropDr.setStyleClass("lul_form");
			table.mergeCells(2, row, 4, row);
			table.setWidth(1, row, "10");
			
			table.add(dropDr,2,row++);			
			table.setHeight(1, row, "25");	
			table.mergeCells(2, row, 4, row);			
			table.add(getLocalizedText(prmHeadingCase,"Heading/case"), 2, row++);		
			
			TextInput textSubject  = (TextInput) getStyledInterface(new TextInput(prmInform));
			textSubject.setLength(50);
			textSubject.setStyleClass("lul_text");
			table.setHeight(2, row, "20");
			table.mergeCells(2, row, 4, row);
			table.add(textSubject, 2, row++);
			
			table.setHeight(1, row, "20");	
			
			TextArea textMessage = (TextArea) getStyledInterface(new TextArea(prmMessage));
			textMessage.setStyleClass("lul_text_big");
			textMessage.setRows(10);
			table.setHeight(1, row, "140");
			table.mergeCells(1, row, 4, row);
			table.add(textMessage, 1, row++);	
			
			ResetButton btnClear = (ResetButton ) getStyledInterface(new ResetButton (prmClear));
			btnClear.setStyleClass("lul_form");
			btnClear.setValue(localize(prmClear,"Clear"));
			//table.setHeight(1, row, "20");	
			table.add(btnClear, 2, row);
		//	table.mergeCells(1, row, 2, row);
			
			//table.setHeight(1, row++, "25");
			SubmitButton confirm = (SubmitButton) getStyledInterface(new SubmitButton(prmChoose));
			confirm.setValue(localize(prmSend,"Send"));			
			confirm.setStyleClass("lul_form");
			table.setAlignment(4, row, Table.HORIZONTAL_ALIGN_LEFT);
			confirm.setOnClick("alert('Ditt meddelande har skickats!');");
			table.setWidth(4, row, "400");
			table.setWidth(3, row, "10");
			table.setWidth(2, row, "5");
			table.setWidth(1, row, "10");
			table.add(confirm, 4, row);
			table.setHeight(1, row, "30");
			
			
		
		return myForm;
	}
	
}
