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
import com.idega.block.school.data.SchoolArea;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropDownMenuInputHandler;

/**
 * @author laddi
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SchoolAreaDropDownMenu extends DropDownMenuInputHandler  {

	protected static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle rb = this.getResourceBundle(iwc);

		addMenuElement("-1", rb.getLocalizedString("child_care.all_areas", "All areas"));
		Collection areas = getSchoolBusiness(iwc).findAllSchoolAreas();
		Iterator iter = areas.iterator();
		while (iter.hasNext()) {
			SchoolArea area = (SchoolArea) iter.next();
			addMenuElement(area.getPrimaryKey().toString(), area.getSchoolAreaName());
		}
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
				return getSchoolBusiness(iwc).getSchoolArea(value).getSchoolAreaName();
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