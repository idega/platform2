/**
 * 
 */
package com.idega.block.finance.business;



import com.idega.business.IBOHomeImpl;

/**
 * @author bluebottle
 *
 */
public class BankInfoBusinessHomeImpl extends IBOHomeImpl implements
		BankInfoBusinessHome {
	protected Class getBeanInterfaceClass() {
		return BankInfoBusiness.class;
	}

	public BankInfoBusiness create() throws javax.ejb.CreateException {
		return (BankInfoBusiness) super.createIBO();
	}

}
