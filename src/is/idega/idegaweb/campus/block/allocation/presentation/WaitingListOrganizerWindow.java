/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.allocation.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.application.data.WaitingListHome;
import is.idega.idegaweb.campus.data.SystemProperties;
import is.idega.idegaweb.campus.data.SystemPropertiesBMPBean;
import is.idega.idegaweb.campus.presentation.Edit;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.core.user.data.User;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;

/**
 * A window class for making it possible to move people up and down
 * on a waiting list.
 * 
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class WaitingListOrganizerWindow extends Window {
	public static String COMPLEX_ID = "complex";
	public static String APARTMENT_TYPE_ID = "aprtType";
	public static String WL_ORDER = "order";
	public static String WL_ID = "wlId";
	public static String NUMBER_ON_LIST = "onlist";
	public static String MAX_LIST = "maxlist";

	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus";
	protected IWResourceBundle iwrb;
	protected IWBundle iwb;
	private boolean isAdmin;
	private boolean isLoggedOn;
	private SystemProperties SysProps = null;
	private User eUser = null;
	public static final String prmAdmin = "is_camp_isit";

	public WaitingListOrganizerWindow() {
		setWidth(300);
		setHeight(300);
		setResizable(true);
	}

	protected void control(IWContext iwc) {
		iwrb = getResourceBundle(iwc);
		iwb = getBundle(iwc);

		if (isAdmin || isLoggedOn) {
			if (iwc.getApplicationAttribute(SystemPropertiesBMPBean.getEntityTableName()) != null) {
				SysProps = (SystemProperties) iwc.getApplicationAttribute(SystemPropertiesBMPBean.getEntityTableName());
			}

			if (iwc.isParameterSet("move")) {
				doRenumberWaitingList(iwc);
				setParentToReload();
				close();
			}
			else if (iwc.isParameterSet("cancel")) {
				close();	
			}
			
			add(getRenumberTable(iwc));
		}
		else
			add(Edit.formatText(iwrb.getLocalizedString("access_denied", "Access denied")));
	}

	protected boolean doRenumberWaitingList(IWContext iwc) {
		int complexId = Integer.parseInt(iwc.getParameter(COMPLEX_ID));
		int aprtTypeId = Integer.parseInt(iwc.getParameter(APARTMENT_TYPE_ID));
		int id = Integer.parseInt(iwc.getParameter(WL_ID));
		int moveTo = Integer.parseInt(iwc.getParameter("move_to"));
		
		Collection L = CampusApplicationFinder.listOfWaitinglist(aprtTypeId, complexId);
		if (L != null) {
			WaitingList currentListEntry = null;
			Integer beforeOrder = null;
			Integer afterOrder = null;
			Integer lastOrder = null;
			int count = 1;
			if(moveTo <= L.size()){
				for (Iterator iter = L.iterator(); iter.hasNext();) {
					WaitingList entry = (WaitingList) iter.next();
					if(count == moveTo){
						afterOrder = entry.getOrder();
						beforeOrder = lastOrder;
					}
					if(String.valueOf(id).equals(entry.getPrimaryKey().toString())){
						currentListEntry = entry;
					}
					lastOrder = entry.getOrder();
					count++;
				}
				// update order number
				if(currentListEntry!=null){
					// TODO move to business
					if(beforeOrder !=null && afterOrder!=null){
						int order = (int) (afterOrder.intValue()+beforeOrder.intValue())/2;
						currentListEntry.setOrder(order);
						currentListEntry.store();
					}
				}
			}
		}		
		
		return false;
	}

	protected PresentationObject getRenumberTable(IWContext iwc) {
		int complexId = Integer.parseInt(iwc.getParameter(COMPLEX_ID));
		int aprtTypeId = Integer.parseInt(iwc.getParameter(APARTMENT_TYPE_ID));
		//int wlOrder = Integer.parseInt(iwc.getParameter(WL_ORDER));
		int onList = Integer.parseInt(iwc.getParameter(NUMBER_ON_LIST));
		int max = Integer.parseInt(iwc.getParameter(MAX_LIST));
		int id = Integer.parseInt(iwc.getParameter(WL_ID));
		
		Form form = new Form();
		form.maintainParameter(COMPLEX_ID);
		form.maintainParameter(APARTMENT_TYPE_ID);
		form.maintainParameter(WL_ID);
		DataTable table = new DataTable();
		table.setTitlesVertical(true);
		table.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),1,1);
		table.add(Edit.formatText(iwrb.getLocalizedString("no_on_list","No. On list")),1,2);
		table.add(Edit.formatText(iwrb.getLocalizedString("list_count","List count")),1,3);
		table.add(Edit.formatText(iwrb.getLocalizedString("move_to_no","Move to No.")),1,4);

		WaitingList item = null;
		Applicant applicant = null;

		try {
			item = ((WaitingListHome)IDOLookup.getHomeLegacy(WaitingList.class)).findByPrimaryKeyLegacy(id);
			applicant = ((ApplicantHome)IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(item.getApplicantId().intValue());
		}
		catch (SQLException e) {
		}
		
		table.add(Edit.formatText(applicant.getFullName()),2,1);
		table.add(Edit.formatText(onList),2,2);
		table.add(Edit.formatText(max),2,3);
		TextInput input = new TextInput("move_to");
		Edit.setStyle(input);
		table.add(input,2,4);
		
		table.addTitle(iwrb.getLocalizedString("move_person_on_waitinglist","Move person on waitinglist"));
		
		SubmitButton ok = new SubmitButton("move",iwrb.getLocalizedString("move","Move"));
		SubmitButton cancel = new SubmitButton("cancel",iwrb.getLocalizedString("cancel","Cancel"));
		table.addButton(ok);
		table.addButton(cancel);
		
		form.add(table);
		
		return form;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void main(IWContext iwc) {
		eUser = iwc.getUser();
		isAdmin = iwc.isParameterSet(prmAdmin);
		isLoggedOn = com.idega.core.accesscontrol.business.LoginBusinessBean.isLoggedOn(iwc);
		control(iwc);
	}
}