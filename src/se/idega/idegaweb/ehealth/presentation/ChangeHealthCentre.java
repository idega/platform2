/*
 * Created on 2004-okt-07
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
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.UserBusiness;
import com.idega.util.Age;



/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ChangeHealthCentre extends EHealthBlock {
	
	private String prefix = "visit_booking_";
	private String prmForm = prefix + "form_change_h_centre";	
	private String prmShowSelect = prefix + "show_select";
	private String prmPhotoText = prefix + "photo_text";
	private String prmHealthCentre = prefix + "health_centre";
	private String prmDoctor = prefix + "doctor";
	private String prmChoose = prefix + "choose";
	private String prmConfirm = prefix + "confirm";
	
	IWResourceBundle iwrb = null;
	IWContext _iwc = null;
	
	private int userID = -1;
	private User user;

	public void main(IWContext iwc) throws Exception {
		iwrb = this.getResourceBundle(iwc);
		_iwc = iwc;
		
		userID = iwc.getUserId();
		
		if (userID > 0) {
			user = ((UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class)).getUser(userID);
		}
		add(getDoctorForm(iwc));
		
	}
	
	
	
	public PresentationObject getDoctorForm(IWContext iwc) throws java.rmi.RemoteException {
		
		Form myForm = new Form();
		myForm.setName(prmForm);
		
		Table T = new Table(3, 1);
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		T.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		T.setVerticalAlignment(3, 1, Table.VERTICAL_ALIGN_TOP);
	
		T.setWidth(2, 1, 20);
		T.add(getTableHealthCentre(), 1, 1);		
		T.add(getTableDoctor(), 3, 1);		
		myForm.add(T);
		return myForm;
	}
	
	
	private Table getTableDoctor() {
		Table table = new Table(3, 3);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(3, 1, Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(1, 2, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_BOTTOM);
		
		
		table.setWidth(1, 1, 73);
		table.setWidth(2, 1, 15);
		table.setWidth(3,1, "130");
		
		PresentationObject pictureDr=null;
		User doctor = null;
		int userIDDr = 41047; //45   41047
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
			pictureDr = fakeImageTable;
		}
		else{
			Image image = new Image();
			image.setImageID(userImageID);
			image.setWidth(73);
			
			pictureDr=image;
		}
		table.add(pictureDr,1,1);
		
		String doctorInfo = "<b>Dr. Magne Syhl</b><br>Allmänläkare<br>Telefon:<br>018-987654<br>Telefontid: Mån-ons, kl 08-10.00<br>Webbsida: <a href='http://www.norrudden.se' target='_new'>www.norrudden.se</a>";
		
		table.add(doctorInfo,3,1);
		
		return table;
	}
	
	
	
	private Table getTableHealthCentre(){
		Table tableHCentre = new Table(3, 5);
		tableHCentre.setCellpadding(0);
		tableHCentre.setCellspacing(0);
		tableHCentre.setBorder(0);
		tableHCentre.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		tableHCentre.setVerticalAlignment(3, 1, Table.VERTICAL_ALIGN_TOP);
		tableHCentre.setVerticalAlignment(1, 2, Table.VERTICAL_ALIGN_BOTTOM);
		tableHCentre.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_BOTTOM);
		tableHCentre.setVerticalAlignment(1, 4, Table.VERTICAL_ALIGN_BOTTOM);
		tableHCentre.setVerticalAlignment(1, 5, Table.VERTICAL_ALIGN_BOTTOM);
		
		
		tableHCentre.mergeCells(3, 1, 3, 5);
		
		tableHCentre.setWidth(1, 1, 120);		
		tableHCentre.setWidth(2, 1, 15);
		tableHCentre.setWidth(3,1, "130");
		
		tableHCentre.setHeight(1, 2, "25");
		tableHCentre.setHeight(1, 3, "25");
		tableHCentre.setHeight(1, 4, "25");
		tableHCentre.setHeight(1, 5, "25");
		
		
		
		int row = 1;
		
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
			Text photoText = new Text(iwrb.getLocalizedString(prmPhotoText,"Photo"));
			photoText.setBold();
			photoText.setFontColor(fakeImageColor);
			photoText.setFontSize(Text.FONT_SIZE_12_STYLE_TAG);
			fakeImageTable.add(photoText,1,1);
			fakeImageTable.setBorderColor(fakeImageColor);
			picture = fakeImageTable;
		}
		
		tableHCentre.add(picture,1,1);
		
		String centreInfo = "<b>Gimo <br>Husläkarmottagning</b><br>Lorem ipsum dolor sit amet, consecteuter andi elit, sed diam ninummy nibh.<br>Telefon:<br>018-987654<br>Telefontid: Mån-ons, kl 08-10.00<br>Webbsida: <a href='http://www.norrudden.se' target='_new'>www.norrudden.se</a>";
		
		tableHCentre.add(centreInfo,3,1);
		
		
		DropdownMenu dropHCentre = new DropdownMenu(prmHealthCentre);
		dropHCentre.addMenuElementFirst("1", "Gimo VC");
		dropHCentre.addMenuElement("2", "Östhammar VC");
		dropHCentre.addMenuElement("3", "Alunda VC");
		dropHCentre.addMenuElement("4", "Österbybruk VC");
		dropHCentre.addMenuElement("5", "Tierp VC");
		dropHCentre.addMenuElement("6", "Öregrund VC");
		dropHCentre.addMenuElement("7", "Skutskär VC");
		dropHCentre.addMenuElement("8", "Månkarbo VC");
		dropHCentre.setStyleClass("lul_form");
		tableHCentre.add(dropHCentre,1,2);
		
		DropdownMenu dropDr = new DropdownMenu(prmDoctor);
		dropDr.addMenuElementFirst("1", "Dr Magne Syhl");
		dropDr.addMenuElement("2", "Dr Alve Don");
		
		dropDr.setStyleClass("lul_form");
		tableHCentre.add(dropDr,1,3);
		
		SubmitButton selectCentre = (SubmitButton) getStyledInterface(new SubmitButton(prmChoose));
		selectCentre.setOnClick("setTime(); return false");
		selectCentre.setValue(localize(prmShowSelect,"Display/Select"));
		selectCentre.setStyleClass("lul_form");
		tableHCentre.add(selectCentre,1,4);
		
		SubmitButton confirm = (SubmitButton) getStyledInterface(new SubmitButton(prmConfirm));
		confirm.setValue(localize(prmConfirm,"Confirm"));		
		confirm.setStyleClass("lul_form");
		confirm.setOnClick("alert('Din ansökan om byte av husläkare har skickats');");	
		tableHCentre.add(confirm,1,5);
		
		return tableHCentre;
	}
}
