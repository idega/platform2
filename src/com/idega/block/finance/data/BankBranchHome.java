/**
 * 
 */
package com.idega.block.finance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface BankBranchHome extends IDOHome {
	public BankBranch create() throws javax.ejb.CreateException;

	public BankBranch findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.finance.data.BankBranchBMPBean#ejbFindByBank
	 */
	public Collection findByBank(Bank bank) throws FinderException;

}
