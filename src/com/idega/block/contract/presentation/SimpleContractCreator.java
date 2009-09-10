/*
 * Created on 27.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.contract.presentation;

import java.util.Collection;
import java.util.Iterator;

import com.idega.block.contract.business.ContractFinder;
import com.idega.block.contract.business.ContractService;
import com.idega.block.contract.data.ContractCategory;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SimpleContractCreator extends Block {
	
	private static String IW_BUNDLE_IDENTIFIER="com.idega.block.contract";
	private static String PARAM_MENU="scc_category";
	private static String PARAM_SUBMIT="scc_submit";

	public void main(IWContext iwc){
		IWResourceBundle iwrb = this.getResourceBundle(iwc);
		add(iwrb.getLocalizedString("scc.selectcon","Select contract:"));
		Form form = new Form();
		add(form);
		DropdownMenu menu = new DropdownMenu(PARAM_MENU);
		form.add(menu);
		Collection categories = ContractFinder.listOfContractCategories();
		for (Iterator iter = categories.iterator(); iter.hasNext();) {
			ContractCategory element = (ContractCategory) iter.next();
			menu.addMenuElement(element.getPrimaryKey().toString(),element.getName());
		}
		if(iwc.isParameterSet(PARAM_MENU))
		{
			int categoryID = Integer.parseInt(iwc.getParameter(PARAM_MENU));
			try {
				createContract(iwc,categoryID,iwc.getCurrentUser());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		form.add(new SubmitButton(PARAM_SUBMIT,iwrb.getLocalizedString("scc.createcon","Create contract")));
	}
	
	/**
	 * @param categoryID
	 * @param user
	 */
	private void createContract(IWContext iwc,int categoryId, User user)throws Exception {
		int userID = user.getID();
		((ContractService)IBOLookup.getServiceInstance(iwc,ContractService.class)).createAndPrintContract(userID,categoryId);
	}

	public String getBundleIdentifier(){
		return SimpleContractCreator.IW_BUNDLE_IDENTIFIER;
	}
}
