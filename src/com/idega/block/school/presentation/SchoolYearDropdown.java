package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolYearComparator;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author Laddi
 */
public class SchoolYearDropdown extends DropdownMenu {

	private int _schoolID = -1;
	
	/**
	 * Creates a new <code>SchoolYearDropdown</code> with the given school.
	 * @param name			The name of the <code>SchoolYearDropdown</code>
	 * @param schoolID	The school to use.
	 */
	public SchoolYearDropdown(String name, int schoolID) {
		super(name);
		this._schoolID = schoolID;
	}
	
	public void main(IWContext iwc) throws Exception {
		List years = new Vector(getSchoolBusiness(iwc).findAllSchoolYearsInSchool(getSchoolID()));
			
		if (years != null) {
			Collections.sort(years, new SchoolYearComparator());
			Iterator iter = years.iterator();
			while (iter.hasNext()) {
				SchoolYear year = (SchoolYear) iter.next();
				addMenuElement(year.getPrimaryKey().toString(), year.getSchoolYearName());
			}	
		}
	}
	
	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}
	
	/**
	 * Returns the schoolID.
	 * @return int
	 */
	public int getSchoolID() {
		return this._schoolID;
	}

	/**
	 * Sets the schoolID.
	 * @param schoolID The schoolID to set
	 */
	public void setSchoolID(int schoolID) {
		this._schoolID = schoolID;
	}
}