/*
 * Created on 9.9.2003
 */
package se.idega.idegaweb.commune.accounting.presentation;

import se.idega.idegaweb.commune.accounting.event.AccountingEventListener;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;

/**
 * @author laddi
 */
public class OperationalFieldsMenu extends AccountingBlock {

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		DropdownMenu operationalField = getDropdownMenuLocalized(getSession().getParameterOperationalField(), getBusiness().getExportBusiness().getAllOperationalFields(), "getLocalizedKey");
		operationalField.addMenuElementFirst("", localize("export.select_operational_field", "Select operational field"));
		operationalField.setToSubmit();
		if (getSession().getOperationalField() != null)
			operationalField.setSelectedElement(getSession().getOperationalField());
		
		if (getParentForm() == null) {
			Form form = new Form();
			form.setEventListener(AccountingEventListener.class);
			form.add(operationalField);
			add(form);
		}
		else {
			getParentForm().setEventListener(AccountingEventListener.class);
			add(operationalField);
		}
	}
}