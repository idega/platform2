package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolClass;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author Laddi
 */
public class SchoolClassDropdown extends DropdownMenu {

	private int _schoolID = -1;
	private int _schoolSeasonID = -1;
	private int _schoolYearID = -1;
	
	/**
	 * Creates a new <code>SchoolClassDropdown</code> with school classes in the given
	 * school.
	 * @param name			The name of the <code>SchoolClassDropdown</code>
	 * @param schoolID	The school ID to use.
	 */
	public SchoolClassDropdown(String name, int schoolID) {
		this(name, schoolID, -1);
	}
	
	/**
	 * Creates a new <code>SchoolClassDropdown</code> with school classes in the given
	 * school and school season.
	 * @param name						The name of the <code>SchoolClassDropdown</code>
	 * @param schoolID				The school ID to use.
	 * @param schoolSeasonID	The school season ID to use.
	 */
	public SchoolClassDropdown(String name, int schoolID, int schoolSeasonID) {
		this(name, schoolID, schoolSeasonID, -1);
	}
	
	/**
	 * Creates a new <code>SchoolClassDropdown</code> with school classes in the given
	 * school, school season and school year.
	 * @param name						The name of the <code>SchoolClassDropdown</code>
	 * @param schoolID				The school ID to use.
	 * @param schoolSeasonID	The school season ID to use.
	 * @param schoolYearID		The school year ID to use.
	 */
	public SchoolClassDropdown(String name, int schoolID, int schoolSeasonID, int schoolYearID) {
		super(name);
		this._schoolID = schoolID;
		this._schoolSeasonID = schoolSeasonID;
		this._schoolYearID = schoolYearID;
	}
	
	public void main(IWContext iwc) throws Exception {
		Collection classes;
		if (getSchoolSeasonID() == -1 && getSchoolYearID() == -1) {
			classes = getSchoolBusiness(iwc).findSchoolClassesBySchool(getSchoolID());
		}
		else if (this._schoolYearID == -1) {
			classes = getSchoolBusiness(iwc).findSchoolClassesBySchoolAndSeason(getSchoolID(), getSchoolSeasonID());
		}
		else {
			classes = getSchoolBusiness(iwc).findSchoolClassesBySchoolAndSeasonAndYear(getSchoolID(), getSchoolSeasonID(), getSchoolYearID());
		}
			
		if (classes != null) {
			Iterator iter = classes.iterator();
			while (iter.hasNext()) {
				SchoolClass schoolClass = (SchoolClass) iter.next();
				addMenuElement(schoolClass.getPrimaryKey().toString(), schoolClass.getSchoolClassName());
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
	 * Returns the schoolSeasonID.
	 * @return int
	 */
	public int getSchoolSeasonID() {
		return this._schoolSeasonID;
	}

	/**
	 * Returns the schoolYearID.
	 * @return int
	 */
	public int getSchoolYearID() {
		return this._schoolYearID;
	}

	/**
	 * Sets the schoolID.
	 * @param schoolID The schoolID to set
	 */
	public void setSchoolID(int schoolID) {
		this._schoolID = schoolID;
	}

	/**
	 * Sets the schoolSeasonID.
	 * @param schoolSeasonID The schoolSeasonID to set
	 */
	public void setSchoolSeasonID(int schoolSeasonID) {
		this._schoolSeasonID = schoolSeasonID;
	}

	/**
	 * Sets the schoolYearID.
	 * @param schoolYearID The schoolYearID to set
	 */
	public void setSchoolYearID(int schoolYearID) {
		this._schoolYearID = schoolYearID;
	}

}