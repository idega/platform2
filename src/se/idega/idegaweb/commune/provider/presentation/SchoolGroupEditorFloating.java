/*
 * Created on 4.11.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.provider.presentation;

import java.rmi.RemoteException;

import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;


/**
 * @author Goran
 *
 * Block for creating and editing a school group by a centralized administrator (BUN)
 */
public class SchoolGroupEditorFloating extends SchoolGroupEditorAdmin {

	protected Table getOverview() throws RemoteException {
		Table table = new Table(1, 5);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setHeight(2, 12);
		table.setHeight(4, 12);

		table.add(getNavigationForm(false), 1, 1);		
		
		if (_provider != null){
			table.add(getGroupTable(), 1, 3);
			
			Link createLink = new Link(localize("school.create_group", "Create group"));
			createLink.setAsImageButton(true);
			createLink.addParameter(PARAMETER_ACTION, ACTION_EDIT);
			createLink.addParameter(getProviderAsParameter());
			createLink.addParameter(PARAMETER_TYPE_ID, _iwc.getParameter(PARAMETER_TYPE_ID));
			table.add(createLink, 1, 5);

		}
		return table;
	}

	protected void setSelectedSchoolType(DropdownMenu types) {
			types.setSelectedElement(_iwc.getParameter(PARAMETER_TYPE_ID));
	}
	
	protected void setSelectedSeason(DropdownMenu seasons) {				
			try {
				seasons.setSelectedElement(getSession().getSeasonID());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
	}

}
