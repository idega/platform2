/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.presentation;

import com.idega.business.IBOLookup;
import com.idega.core.data.Address;
import com.idega.core.data.Phone;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;

import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.business.SchoolChoiceComparator;
import se.idega.idegaweb.commune.school.data.SchoolChoice;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class SchoolFreeTimeList extends SchoolCommuneBlock {

	public SchoolFreeTimeList() {		
	}

	/**
	 * @see se.idega.idegaweb.commune.school.presentation.SchoolCommuneBlock#init(IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		int schoolId = getSchoolID();
	
		Collection choices = getSchoolChoiceBusiness(iwc).findBySchoolAndFreeTime(schoolId,getSchoolSeasonID(),true);
		Map students = getBusiness().getUserMapFromChoices(choices);
		Map addresses = getBusiness().getUserAddressMapFromChoicesUserIdPK(choices);
		Map phones = getBusiness().getUserPhoneMapFromChoicesUserIdPK(choices);

		List ordered = new Vector(choices);
		
		Collections.sort(ordered,new SchoolChoiceComparator(SchoolChoiceComparator.NAME_SORT,iwc.getCurrentLocale(),null,students));
		
		Table table = new Table();
		table.setColumns(4);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		add(table);

		int row = 1;		
		table.setRowColor(row, getHeaderColor());
		table.add(getSmallHeader(localize("freetime.name", "Name")),1,row);
		table.add(getSmallHeader(localize("freetime.pid", "PID")),2,row);	
		table.add(getSmallHeader(localize("freetime.address", "Address")),3,row);	
		table.add(getSmallHeader(localize("freetime.phone", "Phone")),4,row++);	
		
		Iterator it = ordered.iterator();
		SchoolChoice choice = null;
		User pupil = null;
		Address address = null;
		Phone phone = null;
		while (it.hasNext()) {
			choice = (SchoolChoice) it.next();
			Integer id = new Integer(choice.getChildId());
			pupil = (User)students.get(id);
			if (pupil != null) {
				address = (Address)addresses.get(id);
				phone = (Phone)phones.get(id);

				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());

				table.add(getSmallText(pupil.getNameLastFirst(true)),1,row);
				table.add(getSmallText(PersonalIDFormatter.format(pupil.getPersonalID(),iwc.getCurrentLocale())),2,row);
				if (address != null)
					table.add(getSmallText(address.getStreetAddress()),3,row);
				if (phone != null)
					table.add(getSmallText(phone.getNumber()),4,row);
					
				row++;
			}
		}
	}
	
	protected SchoolChoiceBusiness getSchoolChoiceBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwac, SchoolChoiceBusiness.class);
	}
}