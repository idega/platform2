/*
 * Created on 9.9.2003
 */
package se.idega.idegaweb.commune.accounting.presentation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import se.idega.idegaweb.commune.accounting.event.AccountingEventListener;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;

/**
 * @author laddi
 */
public class OperationalFieldsMenu extends AccountingBlock {

	private List _maintainParameters = new ArrayList();
	private List _setParameters = new ArrayList();	
	private Form _form;
	
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
			_form = new Form();
			_form.setEventListener(AccountingEventListener.class);
			_form.add(operationalField);
			add(_form);
		}
		else {
			_form = getParentForm();
			_form.setEventListener(AccountingEventListener.class);
			add(operationalField);
		}
		
		_form.maintainParameters(_maintainParameters);
		Iterator i = _setParameters.iterator();
		while (i.hasNext()){
			Parameter par = (Parameter) i.next();
			_form.add(new HiddenInput(par.getName(), par.getValueAsString()));
		}
				
	}
	
	public void maintainParameter(String par){
		_maintainParameters.add(par);
	}
	
	public void setParameter(String name, String value){
		_setParameters.add(new Parameter(name, value));
	}
		
}