/*
 * Created on Oct 24, 2003
 *
 */
package se.idega.idegaweb.commune.childcare.presentation;

import com.idega.presentation.PresentationObject;


/**
 * ChildContractsWindow
 * @author aron 
 * @version 1.0
 */
public class ChildContractsWindow extends ChildCareWindow {
	
	public ChildContractsWindow(){
		super();		
		this.setWidth(650);
	}
	
	public void main(com.idega.presentation.IWContext iwc) throws Exception {
		ChildCareChildContracts contracts = ChildContractsImpl.getPresentationObject();
		contracts.setInsideWindow(true);
			add(contracts);
		}


	
 
}
