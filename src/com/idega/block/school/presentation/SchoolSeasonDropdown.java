package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolSeasonHome;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropDownMenuInputHandler;

/**
 * @author Laddi
 */
public class SchoolSeasonDropdown extends DropDownMenuInputHandler {

	/**
	 * Creates a new <code>SchoolSeasonDropdown</code> with all school seasons.
	 * @param name	The name of the <code>SchoolSeasonDropdown</code>
	 */
	public SchoolSeasonDropdown(String name) {
		super(name);
	}
	
	public SchoolSeasonDropdown() {
		super();
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

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getResultingObject(java.lang.String, com.idega.presentation.IWContext)
	 */
	public Object getResultingObject(String[] value, IWContext iwc) throws Exception {
		if(value != null){
			Object pk = Integer.decode(value[0]);
			try {
				if(value != null){
					SchoolSeason season = ((SchoolSeasonHome)IDOLookup.getHome(SchoolSeason.class)).findByPrimaryKey(pk);
					if(season != null){
						return season;
					}
				}	
			} catch (IDOLookupException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		
		return (null);
		
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.String, com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		if(value != null){
				return ((SchoolSeason)value).getSchoolSeasonName();
		}		
		return "";
	}

}
