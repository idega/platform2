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

import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.application.data.WaitingListHome;
import is.idega.idegaweb.campus.data.SystemProperties;
import is.idega.idegaweb.campus.presentation.CampusWindow;

import java.util.Collection;
import java.util.Iterator;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.core.user.data.User;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;


/**
 * A window class for making it possible to move people up and down
 * on a waiting list.
 * 
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class WaitingListOrganizerWindow extends CampusWindow {
	public static String COMPLEX_ID = "complex";
	public static String APARTMENT_TYPE_ID = "aprtType";
	public static String WL_ORDER = "order";
	public static String WL_ID = "wlId";
	public static String NUMBER_ON_LIST = "onlist";
	public static String PRIORITY = "priority";
	public static String MAX_LIST = "maxlist";

	
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
		
		if (isAdmin || isLoggedOn) {
			

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
			add(getHeader(localize("access_denied", "Access denied")));
	}

	protected boolean doRenumberWaitingList(IWContext iwc) {
		int complexId = Integer.parseInt(iwc.getParameter(COMPLEX_ID));
		int aprtTypeId = Integer.parseInt(iwc.getParameter(APARTMENT_TYPE_ID));
		int id = Integer.parseInt(iwc.getParameter(WL_ID));
		int moveTo = -1;
		try {
			moveTo = Integer.parseInt(iwc.getParameter("move_to"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		//String priority = iwc.getParameter(PRIORITY);
		
		Collection L = CampusApplicationFinder.listOfWaitinglist(aprtTypeId, complexId);
		if (L != null) {
			WaitingList currentListEntry = null;
			WaitingList beforeEntry = null;
			WaitingList afterEntry = null;
			WaitingList lastEntry = null;
			int count = 1;
			if(moveTo <= L.size() ){
				for (Iterator iter = L.iterator(); iter.hasNext();) {
					WaitingList entry = (WaitingList) iter.next();
					if(count == moveTo){
						afterEntry = entry;
						beforeEntry = lastEntry;
					}
					if(String.valueOf(id).equals(entry.getPrimaryKey().toString())){
						currentListEntry = entry;
					}
					lastEntry = entry;
					
					count++;
				}
				// update order number
				if(currentListEntry!=null){
					// TODO move to business
					if(moveTo>0){
						if(beforeEntry !=null && afterEntry!=null){
							int order = (int) (afterEntry.getOrder().intValue()+beforeEntry.getOrder().intValue())/2;
							currentListEntry.setOrder(order);
							if(currentListEntry.getPriorityLevel().compareTo(beforeEntry.getPriorityLevel())!=0){
								currentListEntry.setSamePriority(beforeEntry);
							}
							currentListEntry.store();
						}
					}
				}
			}
		}		
		return false;
	}
	/**
	 * Returns the level with the higher priority
	 * @param one
	 * @param two
	 * @return
	 */
	//TODO put into business classes
	private String getHigherPriorityLevel(String one, String two){
		if(one.compareTo(two)<0)
			return one;
		else
			return two;
	}
	// TODO move to general CampusBlock / ApplicationBlock
	private DropdownMenu priorityDrop(String name, String selected) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement("A", "A");
		drp.addMenuElement("B", "B");
		drp.addMenuElement("C", "C");
		drp.addMenuElement("D", "D");
		drp.addMenuElement("E", "E");
		drp.addMenuElement("T", "T");
		drp.setSelectedElement(selected);
		return drp;
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
		table.add(getHeader(localize("name","Name")),1,1);
		table.add(getHeader(localize("no_on_list","No. On list")),1,2);
		table.add(getHeader(localize("list_count","List count")),1,3);
		table.add(getHeader(localize("move_to_no","Move to No.")),1,4);
		table.add(getHeader(localize("priority","Priority")),1,5);

		WaitingList item = null;
		Applicant applicant = null;
		String prioritySelected = "";

		try {
			item = ((WaitingListHome)IDOLookup.getHome(WaitingList.class)).findByPrimaryKey(new Integer(id));
			prioritySelected = item.getPriorityLevel();
			applicant = ((ApplicantHome)IDOLookup.getHome(Applicant.class)).findByPrimaryKey(item.getApplicantId());
		}
		catch (Exception e) {
		}
		
		table.add(getText(applicant.getFullName()),2,1);
		table.add(getText(String.valueOf(onList)),2,2);
		table.add(getText(String.valueOf(max)),2,3);
		TextInput input = new TextInput("move_to");
		
		table.add(input,2,4);
		DropdownMenu priorities = priorityDrop(PRIORITY,prioritySelected);
		
		table.add(priorities,2,5);
		
		table.addTitle(localize("move_person_on_waitinglist","Move person on waitinglist"));
		
		SubmitButton ok = new SubmitButton("move",localize("move","Move"));
		SubmitButton cancel = new SubmitButton("cancel",localize("cancel","Cancel"));
		table.addButton(ok);
		table.addButton(cancel);
		
		form.add(table);
		form.add(Text.getBreak());
		form.add( getHeader(localize("waitinglist_priority_will_be_upated","Priority level of will be changed to be the same as in the target area")));
		
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