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
public class RegularInvoiceBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements RegularInvoiceBusinessHome {
	
	protected Class getBeanInterfaceClass(){
	 return RegularInvoiceBusiness.class;
	}


	public RegularInvoiceBusiness create() throws javax.ejb.CreateException{
	 return (RegularInvoiceBusiness) super.createIBO();
	}
}
