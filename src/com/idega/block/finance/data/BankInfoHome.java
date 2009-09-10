/**
 * 
 */
package com.idega.block.finance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.Group;

/**
 * @author bluebottle
 *
 */
public interface BankInfoHome extends IDOHome {
	public BankInfo create() throws javax.ejb.CreateException;

	public BankInfo findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#ejbFindByGroupId
	 */
	public BankInfo findByGroupId(int groupId) throws FinderException;

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#ejbFindAllByClub
	 */
	public Collection findAllByClub(Group club) throws FinderException;

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#ejbFindByGroup
	 */
	public BankInfo findByGroup(Group group) throws FinderException;

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#ejbFindByDivision
	 */
	public BankInfo findByDivision(Group division) throws FinderException;

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#ejbFindByClub
	 */
	public BankInfo findByClub(Group club) throws FinderException;

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#ejbFindByUserNameAndBankShortName
	 */
	public BankInfo findByUserNameAndBankShortName(String userName,
			String bankShortName) throws FinderException;

}
