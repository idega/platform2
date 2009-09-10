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
public interface AccountInfoHome extends IDOHome {
	public AccountInfo create() throws javax.ejb.CreateException;

	public AccountInfo findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.finance.data.AccountInfoBMPBean#ejbFindByOwner
	 */
	public Collection findByOwner(Integer ownerID) throws FinderException;

	/**
	 * @see com.idega.block.finance.data.AccountInfoBMPBean#ejbFindByOwnerAndType
	 */
	public Collection findByOwnerAndType(Integer ownerID, String type)
			throws FinderException;

	/**
	 * @see com.idega.block.finance.data.AccountInfoBMPBean#ejbFindByAssessmentRound
	 */
	public Collection findByAssessmentRound(Integer roundID)
			throws FinderException;

	/**
	 * @see com.idega.block.finance.data.AccountInfoBMPBean#ejbFindByAssessmentRound
	 */
	public Collection findByAssessmentRound(Integer roundID, int resultSize,
			int startindex) throws FinderException;

}
