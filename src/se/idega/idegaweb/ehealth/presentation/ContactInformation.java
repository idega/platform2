/*
 * Created on 2004-okt-11
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.ehealth.presentation;


import se.idega.util.PIDChecker;

import com.idega.business.IBOLookup;
import com.idega.core.user.data.User;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.ResetButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.util.Age;



/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ContactInformation extends EHealthBlock {
	
	private String prefix = "patient_";
	private String prmForm = prefix + "form_contact";
	private String prmInform = prefix + "info_channel";
	private String prmClear = prefix + "clear";
	private String prmChoose = prefix + "choose";
	private String prmSendEmail = prefix + "send_email";
	private String prmYourEmail = prefix + "your_email";
	private String prmMessage = prefix + "message";
	private String prmHeadingCase = prefix + "heading_case";
	private String prmConfirm = prefix + "confirm";
	private String prmSend = prefix + "send";
	
	private int userID = -1;
	private User user;
	IWContext _iwc = null;
	
	public void main(IWContext iwc) throws Exception {
		
		_iwc = iwc;
		
		userID = iwc.getUserId();
		
		if (userID > 0) {
			user = ((UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class)).getUser(userID);
		}
		add(getContactForm());
		
	}
	
	
	//public PresentationObject getVisitForm(IWContext iwc, User userVK) throws java.rmi.RemoteException {
	public PresentationObject getContactForm() {
		Form myForm = new Form();
		myForm.setName(prmForm);
		
		Table table = new Table(1, 9);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 2, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 4, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 5, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 6, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 7, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 8, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 9, Table.VERTICAL_ALIGN_BOTTOM);
		table.setWidth(300);
		
		
		myForm.add(table);
		int row = 1;
		
		Age age = null;
		if (user != null && user.getDateOfBirth() != null)
			age = new Age(user.getDateOfBirth());
		else if (user != null && user.getPersonalID() != null)
			age = new Age(PIDChecker.getInstance().getDateFromPersonalID(user.getPersonalID()));
				
			Text sendEmail = getLocalizedSmallHeader(prmSendEmail, "Send an email message");
			Text messsage = getLocalizedText(prmMessage,"Message");
			Text yourEmail = getLocalizedText(prmYourEmail,"Your email address ");
			
			table.setHeight(1, row, "25");	
			table.add(sendEmail, 1, row++);
			table.setHeight(1, row, "20");	
			table.add(getLocalizedText(prmHeadingCase,"Heading/case"), 1, row++);
			
			TextInput textSubject  = (TextInput) getStyledInterface(new TextInput(prmInform));
			textSubject.setLength(50);
			textSubject.setStyleClass("lul_text");
			table.setHeight(1, row, "20");
			table.add(textSubject, 1, row++);
			
			table.setHeight(1, row, "20");	
			table.add(messsage, 1, row++);
			TextArea textMessage = (TextArea) getStyledInterface(new TextArea(prmMessage));
			textMessage.setStyleClass("lul_text");
			textMessage.setRows(5);
			table.add(textMessage, 1, row++);	
			
			table.setHeight(1, row, "20");	
			table.add(yourEmail, 1, row++);
			TextInput textYourEmail  = (TextInput) getStyledInterface(new TextInput(prmYourEmail));
			textYourEmail.setLength(50);
			textYourEmail.setStyleClass("lul_text");
			table.add(textYourEmail, 1, row++);
			
			ResetButton btnClear = (ResetButton ) getStyledInterface(new ResetButton (prmClear));
			btnClear.setStyleClass("lul_form");
			btnClear.setValue(localize(prmClear,"Clear"));
			table.setHeight(1, row, "20");			
			table.add(btnClear, 1, row);
			
			table.setHeight(1, row++, "25");
			SubmitButton confirm = (SubmitButton) getStyledInterface(new SubmitButton(prmChoose));
			
			if (age != null && age.getYears() >= 70)
				confirm.setValue(localize(prmConfirm,"Confirm"));
			else
				confirm.setValue(localize(prmSend,"Send"));
			
			confirm.setStyleClass("lul_form");
			confirm.setOnClick("alert('Din fråga har skickats!');");
			
			table.add(confirm, 1, row);
			table.setHeight(1, row, "30");
			
			
		
		return myForm;
	}
	
}
