/*
 * Created on 2004-okt-04
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.ehealth.presentation;


import javax.ejb.FinderException;

import se.idega.util.PIDChecker;

import com.idega.business.IBOLookup;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.user.business.UserBusiness;
import com.idega.util.Age;



/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class MyDoctor extends EHealthBlock {
	
	private String prefix = "visit_booking_";
	private String prmForm = prefix + "the_frm";
	private String prmInform = prefix + "info_channel";
	private String prmReason = prefix + "reason";
	private String prmClear = prefix + "clear";
	private String prmDay = prefix + "day";
	private String prmMonth = prefix + "month";
	private String prmTime = prefix + "time";
	private String prmChoose = prefix + "choose";
	private String prmCooseWay = prefix + "chooseWay";
	private String prmEmail = prefix + "email";
	private String prmSMS = prefix + "sms";
	private String prmHeadingCase = prefix + "heading_case";
	private String prmVisitReason = prefix + "visit_reason";
	
	private String prmMessage = prefix + "message";
	private String prmVisitBooking = prefix + "visit_booking";
	
	
	private int userID;
	private User user;

	public void main(IWContext iwc) throws Exception {
		
		
		userID = iwc.getUserId();
		
		if (userID > 0) {
			user = ((UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class)).getUser(userID);
		}
		add(getVisitForm(iwc));
		
	}
	
	
	//public PresentationObject getVisitForm(IWContext iwc, User userVK) throws java.rmi.RemoteException {
	public PresentationObject getVisitForm(IWContext iwc) throws java.rmi.RemoteException {
		IWResourceBundle iwrb = this.getResourceBundle(iwc);
		Form myForm = new Form();
		myForm.setName("form_visit");
		
		Table table = new Table(3, 3);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(1);
		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(3, 1, Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(1, 2, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_BOTTOM);
			
		table.setWidth(400);	
		table.setWidth(2, 1, 15);
		table.setWidth(1, 1, 73);
		
		myForm.add(table);
		int row = 1;
		
		Age age = null;
		if (user.getDateOfBirth() != null)
			age = new Age(user.getDateOfBirth());
		else if (user.getPersonalID() != null)
			age = new Age(PIDChecker.getInstance().getDateFromPersonalID(user.getPersonalID()));
			
					
			if (age != null && age.getYears() >= 70){		
			
			}
			
			PresentationObject picture=null;
			User doctor = null;
			int userIDDr = 41047; //45   41047
			int userImageID = -1;
			try {
				doctor = ((UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class)).getUser(userIDDr);	
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
			
			String doctorInfo = "Dr. Magne Syhl<br>Allmänläkare<br>Telefon:<br>018-987654<br>Telefontid: Mån-ons, kl 08-10.00<br>Webbsida: <a href='http://www.norrudden.se' target='_new'>www.norrudden.se</a>";
			
			table.add(doctorInfo,3,1);
			Link message = new Link(getResourceBundle().getLocalizedImageButton(prmMessage, "Send message"));
			message.setPage(121);			
			message.setAsImageButton(false);			
			table.add(message, 3, 2);
			table.setHeight(3,2,"25");
			
			Link visitbooking = new Link(getResourceBundle().getLocalizedImageButton(prmVisitBooking, "Book an appointment"));			message.setPage(121);			
			visitbooking.setAsImageButton(false);
			visitbooking.setPage(132);
			table.add(visitbooking, 3, 3);
				
		
		return myForm;
	}
	
	}
