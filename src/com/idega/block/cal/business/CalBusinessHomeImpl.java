/*
 * Created on Feb 4, 2004
 */
package com.idega.block.cal.business;



public class CalBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements CalBusinessHome{

	protected Class getBeanInterfaceClass(){
		return CalBusiness.class;
	}


	public CalBusiness create() throws javax.ejb.CreateException{
		return (CalBusiness) super.createIBO();
	}



	

}
