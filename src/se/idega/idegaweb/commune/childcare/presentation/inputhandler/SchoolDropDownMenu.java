/*
 * Created on Nov 24, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package se.idega.idegaweb.commune.childcare.presentation.inputhandler;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author laddi
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SchoolDropDownMenu extends DropdownMenu implements InputHandler {

	protected static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle rb = this.getResourceBundle(iwc);

		addMenuElement("-1", rb.getLocalizedString("child_care.all_areas", "All areas"));
		Collection areas = getSchoolBusiness(iwc).findAllSchools();
		Iterator iter = areas.iterator();
		while (iter.hasNext()) {
			School area = (School) iter.next();
			addMenuElement(area.getPrimaryKey().toString(), area.getSchoolName());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getHandlerObject(java.lang.String, java.lang.String, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc) {
		this.setName(name);

		if (stringValue != null) {
			this.setSelectedElement(stringValue);
		}

		return this;
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getResultingObject(java.lang.String[], com.idega.presentation.IWContext)
	 */
	public Object getResultingObject(String[] value, IWContext iwc) throws Exception {
		if (value[0].equals("-1")) {
			return null;
		}
		return value[0];
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.Object, com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if (value == null) {
			return iwrb.getLocalizedString("child_care.all_areas", "All areas");
		}
		else {
			try {
				return getSchoolBusiness(iwc).getSchool(value).getSchoolName();
			}
			catch (RemoteException re) {
				return "";
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	private SchoolBusiness getSchoolBusiness(IWContext iwc) {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
}