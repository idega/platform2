/*
 * Created on Mar 8, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.member.isi.block.clubs.presentation;

import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ClubPropertyHandler implements ICPropertyHandler {
	/* (non-Javadoc)
	 * @see com.idega.builder.handler.ICPropertyHandler#getDefaultHandlerTypes()
	 */
	public List getDefaultHandlerTypes() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.builder.handler.ICPropertyHandler#getHandlerObject(java.lang.String, java.lang.String, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc) {
		Collection clubs = getGroups(iwc);
		DropdownMenu clubMenu = new DropdownMenu("clubs");
		if(clubs!=null && !clubs.isEmpty()) {
			Iterator clubIter = clubs.iterator();
			while (clubIter.hasNext()) {
				Group club = (Group) clubIter.next();
				String clubId = club.getPrimaryKey().toString();
				String clubName = club.getName();
				clubMenu.addMenuElement(clubId, clubName);
			}
			if(stringValue!=null) {
				clubMenu.setSelectedElement(stringValue);
			}
		}
		return clubMenu;
	}
	/* (non-Javadoc)
	 * @see com.idega.builder.handler.ICPropertyHandler#onUpdate(java.lang.String[], com.idega.presentation.IWContext)
	 */
	public void onUpdate(String[] values, IWContext iwc) {
		System.out.println("updated club selection " + values[0]);
	}
	
	protected Collection getGroups(IWContext iwc) {
		Collection groups = null;

		String[] type = { IWMemberConstants.GROUP_TYPE_CLUB };
		try {
			groups = getGroupBusiness(iwc).getGroups(type, true);
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
		return groups;
	}
	
	private GroupBusiness getGroupBusiness(IWApplicationContext iwac) throws RemoteException {
		return (GroupBusiness) com.idega.business.IBOLookup.getServiceInstance(iwac, GroupBusiness.class);
	}
}
