/*
 * Created on 21.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import se.idega.block.pki.data.NBSSignedEntity;
import se.idega.block.pki.presentation.NBSSigningBlock;

import com.idega.block.contract.business.ContractFinder;
import com.idega.block.contract.data.Contract;
import com.idega.builder.data.IBPage;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ChildCareContractSigner extends Block implements Builderaware{
	
	public final static String PAR_CONTRACT_ID = "ChildCareContractSigner_CONTRACT_ID";
	
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
		
	public ChildCareContractSigner() {
		super();
	}	
		
	public void main(IWContext iwc) {
		try{
			control(iwc);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}	
	
	private static String ACTION = "ACTION";
	private static String ACTION_SIGN = "SIGN";
		
	private void control(IWContext iwc) throws SQLException{
		System.out.println("control()");	
		String action = iwc.getParameter(ACTION);
		
		if (action != null && action.equals(ACTION_SIGN)){
			System.out.println("Forwarding");	
			int contractId = Integer.parseInt(iwc.getParameter(PAR_CONTRACT_ID));
			
			Contract contract =
				((com.idega.block.contract.data.ContractHome) com.idega.data.IDOLookup.getHomeLegacy(Contract.class))
					.findByPrimaryKeyLegacy(contractId);			
			
			iwc.setSessionAttribute(NBSSigningBlock.NBS_SIGNED_ENTITY, 
				new NBSSignedEntity() {
					private Contract _contract = null;
					
					public Object init(Contract contract){
						_contract = contract;
						return this;
					}
					
					public void setXmlSignedData(String data) {
						_contract.setXmlSignedData(data);
					}
	
					public void setSignedBy(int userId) {
					}
	
					public void setSignedDate(Date time) {
					}
	
					public void setSignedFlag(boolean flag) {
						_contract.setSignedFlag(new Boolean(flag));												
					}
	
					public void store() {
						_contract.store();
					}
	
					public String getText() {
						return _contract.getText();
					}
				}
				.init(contract)
			);
					
			iwc.forwardToIBPage(getParentPage(), getResponsePage());		
			
			
		} else {
			Table t = makeTableOfContracts(iwc);
			Form f = new Form();
			f.add(t);
			add(f);
		}
		System.out.println("control() done");	
	}


	private Table makeTableOfContracts(IWContext iwc) {
		System.out.println("makeTableOfContracts()");			
		
		Collection contracts = ContractFinder.findContractsByUserId(iwc.getCurrentUser().getID());
		if (contracts == null){
			return new Table();
		}
		
		Iterator i = contracts.iterator();
		
		Table t = new Table(2, contracts.size());
		t.setCellpadding(0);
		t.setCellspacing(0);		
		
		int row = 1;
		while (i.hasNext()){
			Contract contract = (Contract) i.next();

			System.out.println("Contract.id: " + contract.getID());
			
			if (contract.getSignedFlag() == null || ! contract.getSignedFlag().booleanValue()) {
				t.add(new Text(contract.getValidFrom().toString()), 1, row);
				Link signBtn = new Link("Sign Contract");
				signBtn.setParameter(PAR_CONTRACT_ID, ""+contract.getID());
				signBtn.setParameter(ACTION, ACTION_SIGN);
				t.add(signBtn, 2, row);
			}
			row ++;
		}
		
		return t;
	}

	private static IBPage _page;	

	public void setResponsePage(IBPage page){
		System.out.println("******* S E T T I N G   Page: " + page.getName());		
		_page = page;
	}
	
	public IBPage getResponsePage(){
		return _page;
	}
	
}
