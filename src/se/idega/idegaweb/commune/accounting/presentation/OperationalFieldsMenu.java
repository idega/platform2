/*
 * Created on 9.9.2003
 */
package se.idega.idegaweb.commune.accounting.presentation;

import java.rmi.RemoteException;

import com.idega.business.IWEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;

/**
 * @author laddi
 */
public class OperationalFieldsMenu extends AccountingBlock implements IWEventListener {

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		DropdownMenu operationalField = getDropdownMenuLocalized(getSession().getParameterOperationalField(), getBusiness().getExportBusiness().getAllOperationalFields(), "getLocalizedKey");
		operationalField.addMenuElementFirst("", localize("export.select_operational_field", "Select operational field"));
		operationalField.setToSubmit();
		if (getSession().getOperationalField() != null)
			operationalField.setSelectedElement(getSession().getOperationalField());
		
		if (operationalField.getForm() == null) {
			Form form = new Form();
			form.setEventListener(this.getClass());
			form.add(operationalField);
			add(form);
		}
		else {
			operationalField.getForm().setEventListener(this.getClass());
			add(operationalField);
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.business.IWEventListener#actionPerformed(com.idega.presentation.IWContext)
	 */
	public boolean actionPerformed(IWContext iwc) throws IWException {
		try {
			if (iwc.isParameterSet(getSession().getParameterOperationalField())) {
				getSession().setOperationalField(iwc.getParameter(getSession().getParameterOperationalField()));
				return true;
			}
		}
		catch (RemoteException e) {
			return false;
		}
		
		return false;
	}
}