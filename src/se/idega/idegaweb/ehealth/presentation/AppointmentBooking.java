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
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
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
public class AppointmentBooking extends EHealthBlock {
	
	private String prefix = "patient_";
	private String prmForm = prefix + "form_visit";
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
	private String prmSelect = prefix + "select";
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
		add(getBookingForm());
		
	}
	
	
	//public PresentationObject getVisitForm(IWContext iwc, User userVK) throws java.rmi.RemoteException {
	public PresentationObject getBookingForm() {
		Form myForm = new Form();
		myForm.setName(prmForm);
		
		Table table = new Table(1, 12);
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
		table.setVerticalAlignment(1, 10, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 11, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 12, Table.VERTICAL_ALIGN_BOTTOM);
		
		table.setWidth(300);
		
		
		myForm.add(table);
		int row = 1;
		
		Age age = null;
		if (user != null && user.getDateOfBirth() != null)
			age = new Age(user.getDateOfBirth());
		else if (user != null && user.getPersonalID() != null)
			age = new Age(PIDChecker.getInstance().getDateFromPersonalID(user.getPersonalID()));
	
		
		
		if (age != null && age.getYears() >= 70){
		Page pVisit = this.getParentPage();
		if (pVisit != null) {
			Script S = pVisit.getAssociatedScript();

			S.addFunction("setTime()", setTimeScript());
			Script timeScript = myForm.getAssociatedFormScript();
			if (timeScript == null) {
				timeScript = new Script();
				myForm.setAssociatedFormScript(timeScript);
			}
		}	
		}
			
			Text chooseWay = getLocalizedText(prmCooseWay,"Choose way: ");
			Text email = getLocalizedText(prmEmail,"Email ");
			Text sms = getLocalizedText(prmSMS,"SMS ");
			CheckBox cbEmail = getCheckBox("cbEmail", "true");
			CheckBox cbSMS = getCheckBox("cbEmail", "true");
			cbSMS.setChecked(true);
			
			table.add(chooseWay, 1, row);
			table.add(email, 1, row);
			table.add(cbEmail, 1, row);
			table.add(sms, 1, row);
			table.add(cbSMS, 1, row++);
			
			table.setHeight(1, row, "20");
			table.add(getLocalizedText(prmHeadingCase,"Heading/case"), 1, row++);
			
			TextInput textSubject  = (TextInput) getStyledInterface(new TextInput(prmInform));
			textSubject.setValue(age.getYears());
			textSubject.setLength(50);
			textSubject.setStyleClass("lul_text");
			
			table.add(textSubject, 1, row++);
			table.setHeight(1, row, "20");	
			table.add(getLocalizedText(prmVisitReason,"Reason for visit"), 1, row++);
			
			TextArea textReason = (TextArea) getStyledInterface(new TextArea(prmReason));
			textReason.setRows(5);
			textReason.setStyleClass("lul_text");
			
			table.add(textReason, 1, row++);	
			
		
			ResetButton btnClear = (ResetButton ) getStyledInterface(new ResetButton (prmClear));
			btnClear.setStyleClass("lul_form");
			btnClear.setValue(localize(prmClear,"Clear"));
			
			
			table.add(btnClear, 1, row);
			table.setHeight(1, row++, "25");
			
			//DatePicker dp = new DatePicker("date");		
			//table.add(dp, 1, row++);
			if (age != null && age.getYears() >= 70){		
			DropdownMenu dropMonth = new DropdownMenu(prmMonth);
			dropMonth.addMenuElementFirst("-1", "Välj månad");
			dropMonth.addMenuElement( 1, "Januari");
			dropMonth.addMenuElement( 2, "Februari");
			dropMonth.addMenuElement( 3, "Mars");
			dropMonth.addMenuElement( 4, "April");
			dropMonth.addMenuElement( 5, "Maj");
			dropMonth.addMenuElement( 6, "Juni");
			dropMonth.addMenuElement( 7, "Juli");
			dropMonth.addMenuElement( 8, "Augusti");
			dropMonth.addMenuElement( 9, "September");
			dropMonth.addMenuElement( 10, "Oktober");
			dropMonth.addMenuElement( 11, "November");
			dropMonth.addMenuElement( 12, "Descember");
			
			dropMonth.setStyleClass("lul_form");
			table.add(dropMonth, 1, row);
			table.setHeight(1, row++, "35");
			
			DropdownMenu dropDay = new DropdownMenu(prmDay);
			dropDay.addMenuElementFirst("-1", "Välj dag");
			dropDay.addMenuElement( 1, "1");
			dropDay.addMenuElement( 2, "2");
			dropDay.addMenuElement( 3, "3");
			dropDay.addMenuElement( 4, "4");
			dropDay.addMenuElement( 5, "5");
			dropDay.addMenuElement( 6, "6");
			dropDay.addMenuElement( 7, "7");
			dropDay.addMenuElement( 8, "8");
			dropDay.addMenuElement( 9, "9");
			dropDay.addMenuElement( 11, "11");
			dropDay.addMenuElement( 12, "12");
			dropDay.addMenuElement( 13, "13");
			dropDay.addMenuElement( 14, "14");
			dropDay.addMenuElement( 15, "15");
			dropDay.addMenuElement( 16, "16");
			dropDay.addMenuElement( 17, "17");
			dropDay.addMenuElement( 18, "18");
			dropDay.addMenuElement( 19, "19");
			dropDay.addMenuElement( 20, "20");
			dropDay.addMenuElement( 21, "21");
			dropDay.addMenuElement( 22, "22");
			dropDay.addMenuElement( 23, "23");
			dropDay.addMenuElement( 24, "24");
			dropDay.addMenuElement( 25, "25");
			dropDay.addMenuElement( 26, "26");
			dropDay.addMenuElement( 27, "27");
			dropDay.addMenuElement( 28, "28");
			dropDay.addMenuElement( 29, "29");
			dropDay.addMenuElement( 30, "30");
			dropDay.addMenuElement( 31, "31");
		
			dropDay.setStyleClass("lul_form");
			table.add(dropDay, 1, row);
			table.setHeight(1, row++, "25");
			
			
			DropdownMenu dropTime = new DropdownMenu(prmTime);
			dropTime.addMenuElementFirst("-1", "Välj klockslag");
			dropTime.addMenuElement( 8, "08:00");
			dropTime.addMenuElement( 9, "09:00");
			dropTime.addMenuElement( 10, "10:00");
			dropTime.addMenuElement( 11, "11:00");
			dropTime.addMenuElement( 12, "12:00");
			dropTime.addMenuElement( 13, "13:00");
			dropTime.addMenuElement( 14, "14:00");
			dropTime.addMenuElement( 15, "15:00");
			dropTime.addMenuElement( 16, "16:00");
			dropTime.addMenuElement( 17, "17:00");
			
			dropTime.setStyleClass("lul_form");
			table.add(dropTime, 1, row);
			table.setHeight(1, row++, "25");
			
			
			SubmitButton selectDate = (SubmitButton) getStyledInterface(new SubmitButton(prmChoose));
			selectDate.setOnClick("setTime(); return false");
			selectDate.setValue(localize(prmSelect,"Select"));
			selectDate.setStyleClass("lul_form");
			
			table.add(selectDate, 1, row);
			table.setHeight(1, row++, "25");
			
			Layer layer = new Layer(Layer.DIV);
			layer.setID("divShowTime");
			layer.setWidth(400);
			layer.setHeight(30);
			layer.setStyleAttribute("vertical-align", "bottom");
			
			
			//String div = "<div id='divShowTime' name='divShowTime' style='width:400px; height:30px; vertical-align:bottom;'></div>";
			table.add(layer, 1, row);
			table.setHeight(1, row++, "30");
			}
			SubmitButton confirm = (SubmitButton) getStyledInterface(new SubmitButton(prmChoose));
			
			if (age != null && age.getYears() >= 70)
				confirm.setValue(localize(prmConfirm,"Confirm"));
			else
				confirm.setValue(localize(prmSend,"Send"));
			
			confirm.setStyleClass("lul_form");
			confirm.setOnClick("alert('Bokningen har skickats!');");
			
			table.add(confirm, 1, row);
			table.setHeight(1, row, "30");
			
			
		
		return myForm;
	}
	
	private String setTimeScript() {
		StringBuffer s = new StringBuffer();
		s.append("function setTime(){").append(" \n\t");
		
		s.append("var day = document.patient_form_visit.elements['patient_day'].options[document.patient_form_visit.elements['patient_day'].selectedIndex];").append(" \n\t");
		s.append("var month = document.patient_form_visit.elements['patient_month'].options[document.patient_form_visit.elements['patient_month'].selectedIndex];").append(" \n\t");
		s.append("var time = document.patient_form_visit.elements['patient_time'].options[document.patient_form_visit.elements['patient_time'].selectedIndex];").append(" \n\t");
		s.append("if (day.value != -1 && month.value != -1 && time.value != -1){").append(" \n\t");
		s.append("document.all.divShowTime.innerHTML = '<br>Du har valt den ' + day.text + ' '  + month.text + ', kl ' + time.text").append("} \n\t");
		s.append("return false;").append(" \n\t");
		
		s.append("}").append("\n\t\t\t");
		
		return s.toString();
	}
	
}
