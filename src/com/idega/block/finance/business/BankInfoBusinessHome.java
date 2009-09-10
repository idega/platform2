/**
 * 
 */
package com.idega.block.finance.business;



import com.idega.business.IBOHome;

/**
 * @author bluebottle
 *
 */
public interface BankInfoBusinessHome extends IBOHome {
	public BankInfoBusiness create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
