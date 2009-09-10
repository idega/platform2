package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolComparator;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author Laddi
 */
public class SchoolDropdown extends DropdownMenu {

	private int _schoolTypeID = -1;
	private int _schoolAreaID = -1;

	/**
	 * Creates a <code>SchoolDropdown</code> with all schools in database.
	 * @param name					The name of the <code>SchoolDropdown</code>
	 */
	public SchoolDropdown(String name) {
		this(name, -1);
	}
	
	/**
	 * Creates a SchoolDropdown with schools that are of a particular school type.
	 * @param name					The name of the <code>SchoolDropdown</code>
	 * @param schoolTypeID	The school type ID to use.
	 */
	public SchoolDropdown(String name, int schoolTypeID) {
		this(name, schoolTypeID, -1);
	}
	
	/**
	 * Creates a SchoolDropdown with schools that are of a particular school type and
	 * in a specific school area.
	 * @param name					The name of the <code>SchoolDropdown</code>
	 * @param schoolTypeID	The school type ID to use.
	 * @param schoolAreaID	The school area ID to use.
	 */
	public SchoolDropdown(String name, int schoolTypeID, int schoolAreaID) {
		super(name);
		this._schoolTypeID = schoolTypeID;
		this._schoolAreaID = schoolAreaID;
	}
	
	public void main(IWContext iwc) throws Exception {
		List schools;
		if (getSchoolTypeID() == -1 && getSchoolAreaID() == -1) {
			schools = new Vector(getSchoolBusiness(iwc).findAllSchools());
		}
		else if (getSchoolAreaID() == -1) {
			schools = new Vector(getSchoolBusiness(iwc).findAllSchoolsByType(getSchoolTypeID()));
		}
		else {
			schools = new Vector(getSchoolBusiness(iwc).findAllSchoolsByAreaAndType(getSchoolAreaID(), getSchoolTypeID()));
		}
			
		if (schools != null) {
			Collections.sort(schools, new SchoolComparator(iwc.getCurrentLocale()));
			Iterator iter = schools.iterator();
			while (iter.hasNext()) {
				School school = (School) iter.next();
				addMenuElement(school.getPrimaryKey().toString(), school.getSchoolName());
			}	
		}
	}
	
	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}
	
	/**
	 * Returns the schoolTypeID.
	 * @return int
	 */
	public int getSchoolTypeID() {
		return this._schoolTypeID;
	}

	/**
	 * Sets the schoolTypeID.
	 * @param schoolTypeID The schoolTypeID to set
	 */
	public void setSchoolTypeID(int schoolTypeID) {
		this._schoolTypeID = schoolTypeID;
	}

	/**
	 * Returns the schoolAreaID.
	 * @return int
	 */
	public int getSchoolAreaID() {
		return this._schoolAreaID;
	}

	/**
	 * Sets the schoolAreaID.
	 * @param schoolAreaID The schoolAreaID to set
	 */
	public void setSchoolAreaID(int schoolAreaID) {
		this._schoolAreaID = schoolAreaID;
	}

}
