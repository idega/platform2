/*
 * Created on 2004-okt-04
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.ehealth.presentation;


import se.idega.util.PIDChecker;

import com.idega.business.IBOLookup;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.UserBusiness;
import com.idega.util.Age;



/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class MyHealthCentre extends EHealthBlock {
	
	private String prefix = "patient_";
	private String prmForm = prefix + "form_visit";

	
	private String prmMessage = prefix + "message";
	private String prmVisitBooking = prefix + "visit_booking";
	
	
	private int userID = -1;
	private User user;
	IWContext _iwc = null;
	
	public void main(IWContext iwc) throws Exception {
		
		_iwc = iwc;
		userID = iwc.getUserId();
		
		if (userID > 0) {
			user = ((UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class)).getUser(userID);
		}
		add(getDoctorForm());
		
	}
	
	
	//public PresentationObject getVisitForm(IWContext iwc, User userVK) throws java.rmi.RemoteException {
	public PresentationObject getDoctorForm() {
		IWResourceBundle iwrb = this.getResourceBundle(_iwc);
		Form myForm = new Form();
		myForm.setName(prmForm);
		
		Table table = new Table(3, 3);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(3, 1, Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(1, 2, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_BOTTOM);
			
		table.setWidth(400);
		table.mergeCells(1, 1, 1, 3);
		table.setWidth(2, 1, 15);
		table.setWidth(1, 1, 73);
		
		myForm.add(table);
			
		Age age = null;
		if (user != null && user.getDateOfBirth() != null)
			age = new Age(user.getDateOfBirth());
		else if (user != null && user.getPersonalID() != null)
			age = new Age(PIDChecker.getInstance().getDateFromPersonalID(user.getPersonalID()));
			
					
			if (age != null && age.getYears() >= 70){		
			
			}
			
			PresentationObject picture=null;
			
			picture = getHealthCareMap(_iwc);
			
			if(picture==null){
				Table fakeImageTable = new Table(1,1);
				fakeImageTable.setAlignment(1,1,Table.HORIZONTAL_ALIGN_CENTER);
				fakeImageTable.setBorder(4);
				fakeImageTable.setHeight(140);
				fakeImageTable.setWidth(100);
				String fakeImageColor = "#CCCCCC";
				Text photoText = new Text(iwrb.getLocalizedString("managerview.photo_text","Photo"));
				photoText.setBold();
				photoText.setFontColor(fakeImageColor);
				photoText.setFontSize(Text.FONT_SIZE_12_STYLE_TAG);
				fakeImageTable.add(photoText,1,1);
				fakeImageTable.setBorderColor(fakeImageColor);
				picture = fakeImageTable;
			}
			
			table.add(picture,1,1);
			
			String doctorInfo = "<b>Gimo VC</b><br>Lorem ipsum dolor sit amet, consecteuter andi elit, sed diam ninummy nibh.<br>Telefon:<br>018-987654<br>Telefontid: Mån-ons, kl 08-10.00<br>Webbsida: <a href='http://www.gimovc.se' target='_new'>www.gimovc.se</a>";
			
			table.add(doctorInfo,3,1);
			GenericButton message = getButton(new GenericButton("message", localize(prmMessage, "Send message")));
			message.setPageToOpen(121);			
						
			table.add(message, 3, 2);
			table.setHeight(3,2,"25");
			
			
			GenericButton visitbooking = getButton(new GenericButton("visit", localize(prmVisitBooking, "Book an appointment")));
			visitbooking.setPageToOpen(132);
			
			SubmitButton smb = new SubmitButton();
			smb.setStyleClass("ehealth_InterfaceButton");
			smb.setValue("Skicka meddelande >");
			smb.setPageToOpen(132);
		
			table.add(visitbooking, 3, 3);
				
		
		return myForm;
	}
	
	
	
	}
