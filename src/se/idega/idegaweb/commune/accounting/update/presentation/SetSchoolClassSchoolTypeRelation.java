package se.idega.idegaweb.commune.accounting.update.presentation;

import java.util.ArrayList;
import java.util.Iterator;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.update.business.SchoolClassSchoolTypeBusiness;

import com.idega.business.IBOLookup;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;

/**
 * @author Joakim
 *
 */
public class SetSchoolClassSchoolTypeRelation extends AccountingBlock {
	ArrayList list;
	
	public void init(IWContext iwc){
	
		handleAction(iwc);
		
		add("Press Save to set relation");

		Form form = new Form();
		add(form);
		
		
		GenericButton saveButton = this.getSaveButton();
		GenericButton cancelButton = this.getCancelButton();
		form.add(saveButton);
		form.add(cancelButton);

		if(list!=null){
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				String element = (String) iter.next();
				add(element+"<br>");
			}
		}
		
	}
	
	/**
	 * @param iwc
	 */
	private void handleAction(IWContext iwc) {
		if(iwc.isParameterSet(PARAM_SAVE)){
			handleSave(iwc);
		}
	}
	
	/**
	 * @param iwc
	 */
	private void handleSave(IWContext iwc) {
		try {
			SchoolClassSchoolTypeBusiness ctBusiness = (SchoolClassSchoolTypeBusiness)IBOLookup.getServiceInstance(iwc, SchoolClassSchoolTypeBusiness.class);
			list = ctBusiness.setClassTypeRelation();
		} catch (Exception e) {
			add(new ExceptionWrapper(e));
		}
	}
}
