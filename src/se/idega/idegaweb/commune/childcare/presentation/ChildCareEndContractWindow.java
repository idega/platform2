/*
 * Created on 8.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.childcare.presentation;

import com.idega.presentation.ui.Window;
import java.rmi.RemoteException;
import java.util.Calendar;
import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.presentation.CommuneBlock;


import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;



/**
 * @author Roar
 *
 */
public class ChildCareEndContractWindow extends Window {

	private final static int ACTION_END_CONTRACT = 0;

	private final static String[] FROM_DATE_LABEL = new String[] {"ccnctw_from_date", "From date"};	
	final static String CARE_TIME = "CARE_TIME";	
	final static String FROM_DATE = "FROM_DATE";	
	private final static String[] INFO = new String[] {"ccnctw_info", "Info about care time."};
	
	private CommuneBlock style = new CommuneBlock();

	public void main(IWContext iwc) throws Exception {
		ChildCareApplication application =
			getChildCareBusiness(iwc).getApplicationByPrimaryKey(
				iwc.getParameter(CCConstants.APPID));	
		
		if (iwc.getParameter(CCConstants.ACTION) != null 
			&& Integer.parseInt(iwc.getParameter(CCConstants.ACTION)) == ACTION_END_CONTRACT){
			
			sendRequest(iwc, application);
		} else {
			makeGUI(iwc);			
		}


	}
	
	private void makeGUI(IWContext iwc) {
		Form form = new Form();
		Table layoutTbl = new Table();
		
		DateInput fromDate = new DateInput(FROM_DATE);
		fromDate.setAsNotEmpty(style.localize("ccecw_date_missing","Please choose a valid from date."));
		fromDate.setStyleAttribute("style", style.getSmallTextFontStyle());
		
		HiddenInput action = new HiddenInput(CCConstants.ACTION);
		action.setValue(ACTION_END_CONTRACT);
		
		HiddenInput appid = new HiddenInput(CCConstants.APPID);
		appid.setValue(iwc.getParameter(CCConstants.APPID));
		
		SubmitButton submit = new SubmitButton(style.localize(CCConstants.OK));
		submit.setAsImageButton(true);
		CloseButton close = new CloseButton(style.localize(CCConstants.CANCEL));
		close.setAsImageButton(true);
				
		int row = 1;
		
		layoutTbl.add(style.getSmallText(style.localize(FROM_DATE_LABEL) + ":"), 1, row);		
		layoutTbl.add(fromDate, 2, row++);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 2);
		fromDate.setEarliestPossibleDate(cal.getTime(), style.localize("ccecw_date_alert", "Date must be not earlier than two months from today."));
		
		row++;
		
		layoutTbl.add(close, 2, row);
		layoutTbl.add(submit, 2, row);	
		layoutTbl.setAlignment(2, row++, "right");	
		
		layoutTbl.add(style.getSmallText(style.localize(INFO)), 1, row);
		
		form.add(action);
		form.add(appid);
		form.add(layoutTbl);
		form.setOnSubmit("window.close()");
		
		add(form);
		
		setWidth(50);
		setHeight(20);
	}
	
	private void sendRequest(IWContext iwc, ChildCareApplication application) throws RemoteException{
		
		User owner = application.getOwner();
		User child = UserBusiness.getUser(application.getChildId());
		
		getChildCareBusiness(iwc).sendMessageToParents(
			application, 
			style.localize("ccecw_encon_par1", "Begäran om uppsägning av kontrakt gjord"), 
			style.localize("ccecw_encon_par2", "Du har skickat en begäran om uppsägning av kontrakt för") + " " +
			child.getName() + " " +  child.getPersonalID() + " " +
			style.localize("ccecw_encon_par3", "fr.o.m.")+ " " + iwc.getParameter(FROM_DATE) + ".");
		
		
		getChildCareBusiness(iwc).sendMessageToProvider(
			application,
			style.localize("ccecw_encon_prov1", "Uppsägning av kontrakt"),
			owner.getName() + " " + style.localize("ccecw_encon_prov2", "har begärt uppsägning av kontrakt för") + " " +
			child.getName() + " " +  child.getPersonalID() + ". " + 
			style.localize("ccecw_encon_prov3", "Kontraktet ska upphöra fr.o.m.") + " " + iwc.getParameter(FROM_DATE) + ".",
			application.getOwner());	
	}
		
	/**
	 * Method getChildCareBusiness returns the ChildCareBusiness object.
	 * @param iwc
	 * @return ChildCareBusiness
	 */
	ChildCareBusiness getChildCareBusiness(IWContext iwc) {
		try {
			return (
				ChildCareBusiness) com
					.idega
					.business
					.IBOLookup
					.getServiceInstance(
				iwc,
				ChildCareBusiness.class);
		} catch (RemoteException e) {
			return null;
		}
	}
		
	
	
}


