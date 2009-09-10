/**
 * 
 */
package com.idega.block.finance.business;

import java.util.Collection;


import com.idega.block.finance.data.BankBranchHome;
import com.idega.block.finance.data.BankHome;
import com.idega.block.finance.data.BankInfoHome;
import com.idega.business.IBOService;
import com.idega.user.data.Group;

/**
 * @author bluebottle
 *
 */
public interface BankInfoBusiness extends IBOService {
	/**
	 * @see com.idega.block.finance.business.BankInfoBusinessBean#getBankInfoHome
	 */
	public BankInfoHome getBankInfoHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.finance.business.BankInfoBusinessBean#getBankHome
	 */
	public BankHome getBankHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.finance.business.BankInfoBusinessBean#getBankBranchHome
	 */
	public BankBranchHome getBankBranchHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.finance.business.BankInfoBusinessBean#insertBankInfoContract
	 */
	public boolean insertBankInfoContract(Group club, String div, String group,
			int branch, String accountId, String ssn, String name,
			String username, String password) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.finance.business.BankInfoBusinessBean#deleteContract
	 */
	public boolean deleteContract(String[] ids) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.finance.business.BankInfoBusinessBean#findAllContractsByClub
	 */
	public Collection findAllContractsByClub(Group club)
			throws java.rmi.RemoteException;

}
