/*
 * Created on 27.3.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.business.ChildCareSession;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;

/**
 * @author laddi
 */
public class ChildCareProviderSelect extends ChildCareBlock implements IWPageEventListener {

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		Form form = new Form();
		form.setEventListener(ChildCareProviderSelect.class);
		
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(getSession().getParameterChildCareID()));
		menu.addMenuElementFirst("-1", localize("child_care.select_provider","Select provider"));
		menu.setSelectedElement(getSession().getChildCareID());
		menu.setToSubmit();
		
		Collection providers = getBusiness().getSchoolBusiness().findAllSchoolsByType(getBusiness().getSchoolBusiness().findAllSchoolTypesForChildCare());
		if (providers != null) {
			Iterator iter = providers.iterator();
			while (iter.hasNext()) {
				School element = (School) iter.next();
				menu.addMenuElement(element.getPrimaryKey().toString(), element.getSchoolName());
			}
		}
		
		form.add(getSmallHeader(localize("child_care.providers","Providers")+":"+Text.NON_BREAKING_SPACE));
		form.add(menu);
		add(form);
	}

	public boolean actionPerformed(IWContext iwc) throws IWException {
		try {
			if (iwc.isParameterSet(getSession(iwc).getParameterChildCareID())) {
				School provider = getBusiness(iwc).getSchoolBusiness().getSchool(new Integer(iwc.getParameter(getSession(iwc).getParameterChildCareID())));
				if (provider != null) {
					getSession(iwc).setProvider(provider);
					getSession(iwc).setChildCareID(((Integer) provider.getPrimaryKey()).intValue());
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		return false;
	}

	private ChildCareBusiness getBusiness(IWContext iwc) throws RemoteException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);	
	}
	
	private ChildCareSession getSession(IWContext iwc) throws RemoteException {
		return (ChildCareSession) IBOLookup.getSessionInstance(iwc, ChildCareSession.class);	
	}
}