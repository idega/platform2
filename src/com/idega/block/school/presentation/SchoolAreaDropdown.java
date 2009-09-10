package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.idega.block.school.business.SchoolAreaComparator;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolArea;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author Laddi
 */
public class SchoolAreaDropdown extends DropdownMenu {

	/**
	 * Creates a new <code>SchoolAreaDropdown</code> with all school areas.
	 * @param name	The name of the <code>SchoolAreaDropdown</code>
	 */
	public SchoolAreaDropdown(String name) {
		super(name);
	}
	
	public void main(IWContext iwc) throws Exception {
		List areas = new Vector(getSchoolBusiness(iwc).findAllSchoolAreas());
			
		if (areas != null) {
			Collections.sort(areas, new SchoolAreaComparator(iwc.getCurrentLocale()));
			Iterator iter = areas.iterator();
			while (iter.hasNext()) {
				SchoolArea area = (SchoolArea) iter.next();
				addMenuElement(area.getPrimaryKey().toString(), area.getSchoolAreaName());
			}	
		}
	}
	
	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}
}