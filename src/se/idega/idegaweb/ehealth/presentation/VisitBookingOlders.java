/*
 * Created on 2004-okt-04
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.ehealth.presentation;


import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;



/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class VisitBookingOlders extends EHealthBlock {
	
	public void main(IWContext iwc) throws Exception {
		
		add(getVisitForm(iwc));
		
		
	}
	
	
	//public PresentationObject getVisitForm(IWContext iwc, User userVK) throws java.rmi.RemoteException {
	public PresentationObject getVisitForm(IWContext iwc) throws java.rmi.RemoteException {
		Form myForm = new Form();
		myForm.setName("form_visit");
		
		Table table = new Table(1, 12);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		
		table.setWidth(300);
		myForm.add(table);
		int row = 1;
		
		Page pVisit = this.getParentPage();
		if (pVisit != null) {
			Script S = pVisit.getAssociatedScript();

			S.addFunction("setTime()", setTimeScript());
			Script timeScript = myForm.getAssociatedFormScript();
			if (timeScript == null) {
				timeScript = new Script();
				myForm.setAssociatedFormScript(timeScript);
			}
			
			
			Text chooseWay = getLocalizedText("ehealth.chooseWay","Choose way: ");
			Text email = getLocalizedText("ehealth.email","Email ");
			Text sms = getLocalizedText("ehealth.sms","SMS ");
			CheckBox cbEmail = getCheckBox("cbEmail", "false");
			CheckBox cbSMS = getCheckBox("cbEmail", "true");
			
			table.add(chooseWay, 1, 1);
			table.add(email, 1, 1);
			table.add(cbEmail, 1, 1);
			table.add(sms, 1, 1);
			table.add(cbSMS, 1, 1);
			
			/*"
			 table = table & "SMS <input type='checkbox' name='checkbox2' value='checkbox' checked></td></tr><tr>";
			 table = table & "<td>Rubrik/&auml;rende</td></tr><tr><td><input type='text' name='textfield' class='lul_text'></td></tr><tr><td>Bes&ouml;ksorsak</td></tr><tr><td height='50'>";
			 table = table & "<textarea name='textfield2' wrap='VIRTUAL' cols='40' rows='4' class='lul_text'></textarea></td></tr><tr><td height='40'><input type='submit' name='Submit' value='Rensa/ Ta bort' class='lul_form'></td></tr><tr>";
			 table = table & "<td height='25' valign='bottom'><select name='month' size='1' class='lul_form' id='month'><option value='-1'>V&auml;lj m&aring;nad</option><option value='1'>Januari</option><option value='2'>Februari</option><option value='3'>Mars</option><option value='4'>April</option><option value='5'>Maj</option><option value='6'>Juni</option><option value='7'>Juli</option>";
			 table = table & "<option value='8'>Augusti</option><option value='9'>September</option><option value='10'>Oktober</option><option value='11'>November</option><option value='12'>December</option></select></td></tr><tr><td height='25' valign='bottom'><select name='day' class='lul_form' id='day'><option value='-1'>V&auml;lj dag</option>          <option value='1'>1</option>
			 <option value='2'>2</option>
			 <option value='3'>3</option>
			 <option value='4'>4</option>
			 <option value='5'>5</option>
			 <option value='6'>6</option>
			 <option value='7'>7</option>
			 <option value='8'>8</option>
			 <option value='9'>9</option>
			 <option value='10'>10</option>
			 <option value='11'>11</option>
			 <option value='12'>12</option>
			 <option value='13'>13</option>
			 <option value='14'>14</option>
			 <option value='15'>15</option>
			 <option value='16'>16</option>
			 <option value='17'>17</option>
			 <option value='18'>18</option>
			 <option value='19'>19</option>
			 <option value='20'>20</option>
			 <option value='21'>21</option>
			 <option value='22'>22</option>
			 <option value='23'>23</option>
			 <option value='24'>24</option>
			 <option value='25'>25</option>
			 <option value='26'>26</option>
			 <option value='27'>27</option>
			 <option value='28'>28</option>
			 <option value='29'>29</option>
			 <option value='30'>30</option>
			 <option value='31'>31</option>
			 </select>
			 </td>
			 </tr>
			 <tr>
			 <td height='25' valign='bottom'> 
			 <select name='time' class='lul_form' id='time'>
			 <option value='-1'>V&auml;lj klockslag</option>
			 <option value='7'>07:00</option>
			 <option value='8'>08:00</option>
			 <option value='9'>09:00</option>
			 <option value='10'>10:00</option>
			 <option value='11'>11:00</option>
			 <option value='12'>12:00</option>
			 <option value='13'>13:00</option>
			 <option value='14'>14:00</option>
			 <option value='15'>15:00</option>
			 <option value='16'>16:00</option>
			 <option value='17'>17:00</option>
			 </select>
			 </td>
			 </tr>
			 <tr>
			 <td height='35'> 
			 <input type='submit' name='Submit2' value='V&auml;lj' onClick='setTime();return false' class='lul_form'>
			 </td>
			 </tr>
			 <tr>
			 <td height='25'>
			 <div id='divShowTime' name='divShowTime' style='position:absolute; width:200px; height:30px; z-index:1; visibility: visible; overflow: visible'></div>
			 </td>
			 </tr>
			 <tr>
			 <td height='30' valign='bottom'> 
			 <input type='submit' name='Submit3' value='Bekr&auml;fta' class='lul_form'>
			 </td>
			 </tr>
			 </table>";
			 */
			
			
			
		}
		return myForm;
	}
	
	private String setTimeScript() {
		StringBuffer s = new StringBuffer();
		s.append("function setTime(){").append(" \n\t");
		s.append("var day = document.form1.elements['day'].options[document.form1.elements['day'].selectedIndex];").append(" \n\t");
		s.append("var month = document.form1.elements['month'].options[document.form1.elements['month'].selectedIndex];").append(" \n\t");
		s.append("var time = document.form1.elements['time'].options[document.form1.elements['time'].selectedIndex];").append(" \n\t");
		s.append("if (day.value != -1 && month.value != -1 && time.value != -1){").append(" \n\t");
		s.append("document.all.divShowTime.innerHTML = 'Du har valt den ' + day.text + ' '  + months.text + ', kl ' + time.text").append("} \n\t");
		s.append("return false();").append(" \n\t");
		
		s.append("}").append("\n\t\t\t");
		
		return s.toString();
	}
	
}
