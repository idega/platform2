/*
 * Created on Oct 24, 2003
 *
 */
package se.idega.idegaweb.commune.childcare.presentation;

/**
 * ChildContractsWindow
 * @author aron 
 * @version 1.0
 */
public class ChildContractsWindow extends ChildCareWindow {
	
	public final static String PARAMETER_CHILD_ID = ChildCareChildContracts.PARAMETER_CHILD_ID;
	
	public ChildContractsWindow(){
		super();		
		this.setWidth(650);
	}
	
	public void main(com.idega.presentation.IWContext iwc) throws Exception {
		ChildCareChildContracts contracts = new ChildCareChildContracts();
		contracts.setInsideWindow(true);
			add(contracts);
		}
}
