/*
 * Created on 30.5.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;


/**
 * @author laddi
 */
public class ClubNavigationDropdown extends ClubBlock {

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		int location = 1;
		if (iwc.isParameterSet(PARAMETER_CLUB_LOCATION)) {
			location = Integer.parseInt(iwc.getParameter(PARAMETER_CLUB_LOCATION));
		}
		
		Form form = new Form();
		
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu("i_golf_field_location"));
		menu.addMenuElement(1, localize(getLocationNameKey(1),"Capital Area"));
		menu.addMenuElement(2, localize(getLocationNameKey(2),"Reykjane"));
		menu.addMenuElement(3, localize(getLocationNameKey(3),"West"));
		menu.addMenuElement(4, localize(getLocationNameKey(4),"Westfjords"));
		menu.addMenuElement(5, localize(getLocationNameKey(5),"North West"));
		menu.addMenuElement(6, localize(getLocationNameKey(6),"North East"));
		menu.addMenuElement(7, localize(getLocationNameKey(7),"East"));
		menu.addMenuElement(8, localize(getLocationNameKey(8),"South"));
		menu.addMenuElement(10, localize(getLocationNameKey(10),"The Whole Country"));
		menu.addMenuElement(12, localize(getLocationNameKey(12),"Others"));
		menu.setToSubmit(true);
		menu.setSelectedElement(location);
		form.add(menu);

		add(form);
	}
}