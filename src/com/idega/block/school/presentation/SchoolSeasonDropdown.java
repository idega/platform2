package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author Laddi
 */
public class SchoolSeasonDropdown extends DropdownMenu {

	/**
	 * Creates a new <code>SchoolSeasonDropdown</code> with all school seasons.
	 * @param name	The name of the <code>SchoolSeasonDropdown</code>
	 */
	public SchoolSeasonDropdown(String name) {
		super(name);
	}
	
	public void main(IWContext iwc) throws Exception {
		Collection seasons = getSchoolBusiness(iwc).findAllSchoolSeasons();
			
		if (seasons != null) {
			Iterator iter = seasons.iterator();
			while (iter.hasNext()) {
				SchoolSeason season = (SchoolSeason) iter.next();
				addMenuElement(season.getPrimaryKey().toString(), season.getSchoolSeasonName());
			}	
		}
	}
	
	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}
}
