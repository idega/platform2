/*
 * Created on 22.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.business;

/**
 * @author Roar
 *
  */
public class RegularPaymentBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements RegularPaymentBusinessHome {
	
	protected Class getBeanInterfaceClass(){
	 return RegularPaymentBusiness.class;
	}


	public RegularPaymentBusiness create() throws javax.ejb.CreateException{
	 return (RegularPaymentBusiness) super.createIBO();
	}
}
