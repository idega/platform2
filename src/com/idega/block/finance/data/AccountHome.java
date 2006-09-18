/**
 * 
 */
package com.idega.block.finance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface AccountHome extends IDOHome {
	public Account create() throws javax.ejb.CreateException;

	public Account findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#ejbFindAllByUserId
	 */
	public Collection findAllByUserId(int userId)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#ejbFindAllByUserIdAndType
	 */
	public Collection findAllByUserIdAndType(int userId, String type)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#ejbFindBySearch
	 */
	public Collection findBySearch(String id, String name, String pid,
			String type, int iCategoryId) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#ejbFindByAssessmentRound
	 */
	public Collection findByAssessmentRound(int roundid) throws FinderException;

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#ejbFindBySQL
	 */
	public Collection findBySQL(String sql) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#ejbHomeCountByTypeAndCategory
	 */
	public int countByTypeAndCategory(String type, Integer categoryID)
			throws IDOException;

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#ejbHomeCountByAssessmentRound
	 */
	public int countByAssessmentRound(Integer roundID) throws IDOException;

	/**
	 * @see com.idega.block.finance.data.AccountBMPBean#ejbFindByAssessmentRound
	 */
	public Collection findByAssessmentRound(Integer roundID, int resultSize,
			int startindex) throws FinderException;

}
