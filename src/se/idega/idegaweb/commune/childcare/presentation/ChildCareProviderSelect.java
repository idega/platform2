/*
 * Created on 27.3.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import com.idega.block.school.data.School;
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
			if (iwc.isParameterSet(getSession().getParameterChildCareID())) {
				School provider = getBusiness().getSchoolBusiness().getSchool(new Integer(iwc.getParameter(getSession().getParameterChildCareID())));
				if (provider != null) {
					getSession().setProvider(provider);
					getSession().setChildCareID(((Integer) provider.getPrimaryKey()).intValue());
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		return false;
	}
}