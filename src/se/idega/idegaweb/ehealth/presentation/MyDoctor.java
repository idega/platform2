/*
 * Created on 2004-okt-04
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.ehealth.presentation;


import javax.faces.component.UIComponent;

import se.idega.util.PIDChecker;

import com.idega.business.IBOLookup;
import com.idega.core.builder.data.ICPage;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
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
public class MyDoctor extends EHealthBlock {
	
	private String prefix = "patient_";
	private String prmForm = prefix + "form_visit";
	private String prmSendMessage = prefix + "send_message";
	private String prmVisitBooking = prefix + "visit_booking";
	
	
	private int userID = -1;
	private User user;
	IWContext _iwc = null;
	ICPage _messagePage = null;
	ICPage _appointmentPage = null;
	
	public void main(IWContext iwc) throws Exception {
		_iwc = iwc;
		
		userID = iwc.getUserId();
		
		if (userID > 0) {
			user = ((UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class)).getUser(userID);
		}
		add(getDoctorForm());
		
	}
	
	
	//public PresentationObject getVisitForm(IWContext iwc, User userVK) throws java.rmi.RemoteException {
	public UIComponent getDoctorForm() {
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
		table.setWidth(2, 1, 15);
		table.setWidth(1, 1, 73);
		
		myForm.add(table);
			
		Age age = null;
		String doctorInfo = null;
		
		if (user != null && user.getDateOfBirth() != null)
			age = new Age(user.getDateOfBirth());
		else if (user != null && user.getPersonalID() != null)
			age = new Age(PIDChecker.getInstance().getDateFromPersonalID(user.getPersonalID()));
		
			int userIDDr = 45;
					
			if (age != null && age.getYears() >= 70){		
				userIDDr = 46; //45   41047
			}
			
			
			PresentationObject picture=null;
			User doctor = null;
			
			int userImageID = -1;
			try {
				doctor = ((UserBusiness) IBOLookup.getServiceInstance(_iwc, UserBusiness.class)).getUser(userIDDr);	
			}
			catch (Exception fe){
				log(fe);
			}
			
			if (doctor != null)
				userImageID = doctor.getSystemImageID();
			
			if(userImageID==-1){
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
			else{
				Image image = new Image();
				image.setImageID(userImageID);
				image.setWidth(73);
				
				picture=image;
			}
			table.add(picture,1,1);
			
			
			if (age != null && age.getYears() >= 70){	
				doctorInfo = "<b>Dr. Inga Pren</b><br>Allmänläkare<br>Telefon:<br>018-987624<br>Telefontid: Mån-ons, kl 08:30-10.00<br>Webbsida: <a href='http://www.flogsta.se' target='_new'>www.flogsta.se</a>";	
			}
			else{
				doctorInfo = "<b>Dr. Magne Syhl</b><br>Allmänläkare<br>Telefon:<br>018-987654<br>Telefontid: Mån-ons, kl 08-10.00<br>Webbsida: <a href='http://www.gimovc.se' target='_new'>www.gimovc.se</a>";	
			}
			
			
			table.add(doctorInfo,3,1);
			GenericButton message = getButton(new GenericButton("send_message", localize(prmSendMessage, "Send message")));
			if (_messagePage != null) 
				message.setPageToOpen(_messagePage);
								
			table.add(message, 3, 2);
			table.setHeight(3,2,"25");
			
			GenericButton visitbooking = getButton(new GenericButton("visit", localize(prmVisitBooking, "Book an appointment")));
			if (_appointmentPage != null) 
				visitbooking.setPageToOpen(_appointmentPage);
			
			SubmitButton smb = new SubmitButton();
			smb.setStyleClass("ehealth_InterfaceButton");
			smb.setValue("Skicka meddelande >");
			smb.setPageToOpen(132);
		
			table.add(visitbooking, 3, 3);
				
		
		return myForm;
	}
	
	
	/**
	 * @param messagePage The message Page to set.
	 */
	public void setMessagePage(ICPage messagePage) {
		_messagePage = messagePage;
	}
	
	/**
	 * @param appointmentPage The appointment Page to set.
	 */
	public void setAppointmentPage(ICPage appointmentPage) {
		_appointmentPage = appointmentPage;
	}
	
	}
