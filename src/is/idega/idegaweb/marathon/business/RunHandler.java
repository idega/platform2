/*
 * Created on 20.8.2004
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.idega.business.IBOLookup;
import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.user.data.Group;


/**
 * @author laddi
 */
public class RunHandler implements ICPropertyHandler {

	public RunHandler() {
	}
	
	/* (non-Javadoc)
	 * @see com.idega.core.builder.presentation.ICPropertyHandler#getDefaultHandlerTypes()
	 */
	public List getDefaultHandlerTypes() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.core.builder.presentation.ICPropertyHandler#getHandlerObject(java.lang.String, java.lang.String, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc) {
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc.getCurrentLocale());
		DropdownMenu menu = new DropdownMenu(IWMarathonConstants.GROUP_TYPE_RUN);
		
		try {
			RunBusiness business = (RunBusiness) IBOLookup.getServiceInstance(iwc, RunBusiness.class);
			Collection runs = business.getRuns();
			if (runs != null) {
				Iterator iter = runs.iterator();
				while (iter.hasNext()) {
					Group element = (Group) iter.next();
					menu.addMenuElement(element.getPrimaryKey().toString(), iwrb.getLocalizedString(element.getName(), element.getName()));
				}
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}

		return menu;
	}

	/* (non-Javadoc)
	 * @see com.idega.core.builder.presentation.ICPropertyHandler#onUpdate(java.lang.String[], com.idega.presentation.IWContext)
	 */
	public void onUpdate(String[] values, IWContext iwc) {
	}

}
