/*
 * $Id: ContractRemoverWindow.java,v 1.2 2004/12/05 09:54:08 laddi Exp $
 * Created on 24.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.presentation.admin;

import java.text.DateFormat;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.care.data.ChildCareContractHome;
import se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock;
import se.idega.idegaweb.commune.childcare.presentation.ChildCareWindow;

/**
 * 
 *  Last modified: $Date: 2004/12/05 09:54:08 $ by $Author: laddi $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.2 $
 */
public class ContractRemoverWindow extends ChildCareWindow {
    
    public static final String PARAMETER_CONTRACT_ID = "cc_cid";
    //private static final String PARAMETER_CLOSE = "cc_crmcl";
    
    
    /* (non-Javadoc)
     * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareWindow#main(com.idega.presentation.IWContext)
     */
    public void main(IWContext iwc) throws Exception {
       add(new ContractRemover());
    }
    
    private class ContractRemover extends ChildCareBlock{
        
        private static final String PRM_DEL = "cc_cidd";
        private ChildCareContract contract = null;
	    /**
		 * @see com.idega.presentation.PresentationObject#main(IWContext)
		 */
		public void init(IWContext iwc) throws Exception {
		    
		    if(iwc.isLoggedOn() && iwc.isParameterSet(PARAMETER_CONTRACT_ID)){
		        
		        ChildCareContractHome contractHome = (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
			    contract = contractHome.findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_CONTRACT_ID)));
			    
		        if(iwc.isParameterSet(PRM_DEL)){
		            removeContract(iwc);
		            super.getParentPage().close();
		        }
		        
		        int row = 1;
		        Table infoTable = new Table();
		        infoTable.add(getSmallHeader(localize("child_care.child","Child")),1,row);
		        infoTable.add(getSmallText(contract.getChild().getName()),2,row++);
		        
		        infoTable.add(getSmallHeader(localize("child_care.provider","Provider")),1,row);
		        infoTable.add(getSmallText(contract.getApplication().getProvider().getName()),2,row++);
		        
		        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT,iwc.getCurrentLocale());
		        infoTable.add(getSmallHeader(localize("child_care.valid_from","Valid from")),1,row);
		        infoTable.add(getSmallText(dateFormat.format(contract.getValidFromDate())),2,row++);
		        
		        if(contract.getTerminatedDate()!=null){
		            infoTable.add(getSmallHeader(localize("child_care.valid_to","Valid to")),1,row);
		            infoTable.add(getSmallText(dateFormat.format(contract.getTerminatedDate())),2,row++);
		        }
		        
		        add(infoTable);
		        add(new Break());
		        add(new Break());
		        Form form = new Form();
		        form.maintainParameter(PARAMETER_CONTRACT_ID);
		        SubmitButton deleteButton = new SubmitButton(localize("child_care.button.remove_contract","Remove"));
		        CloseButton closeButton = new CloseButton(localize("child_care.button.cancel_remove","Cancel"));
		        form.add(getButton(deleteButton));
		        form.add(getButton(closeButton));
		            
		        add(form);
		    }
		    
		}
		
		private void removeContract(IWContext iwc) {
			try {
				getBusiness().removeContract(contract, iwc.getCurrentUser());
				add(getLocalizedSmallHeader("child_care.contract_delete_successful", "Contract successfully deleted"));
			} catch (Exception e) {
				add(getLocalizedSmallHeader("child_care.contract_delete_failed", "Contract was NOT deleted"));
			}
		}
		
    }

}
