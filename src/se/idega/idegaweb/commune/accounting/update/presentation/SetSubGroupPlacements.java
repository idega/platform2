/*
 * Created on 4.11.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.accounting.update.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.update.business.SchoolSubGroupPlacementsBusiness;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;

/**
 * @author laddi
 */
public class SetSubGroupPlacements extends AccountingBlock {

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.presentation.AccountingBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (handleAction(iwc)) {
			add("School placements/Childcare contract update done!");
		}
		else {
			add("Press Save to change sub group placements.");
	
			Form form = new Form();
			form.add(this.getSaveButton());
			add(form);
		}
	}
	
	/**
	 * @param iwc
	 */
	private boolean handleAction(IWContext iwc) {
		if(iwc.isParameterSet(PARAM_SAVE)){
			try {
				SchoolSubGroupPlacementsBusiness business = (SchoolSubGroupPlacementsBusiness) IBOLookup.getServiceInstance(iwc, SchoolSubGroupPlacementsBusiness.class);
				business.changeSubGroupPlacements();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
}
