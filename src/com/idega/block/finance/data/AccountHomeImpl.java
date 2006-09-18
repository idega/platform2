/**
 * 
 */
package com.idega.block.finance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class AccountHomeImpl extends IDOFactory implements AccountHome {
	protected Class getEntityInterfaceClass() {
		return Account.class;
	}

	public Account create() throws javax.ejb.CreateException {
		return (Account) super.createIDO();
	}

	public Account findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Account) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByUserId(int userId)
			throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AccountBMPBean) entity)
				.ejbFindAllByUserId(userId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByUserIdAndType(int userId, String type)
			throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AccountBMPBean) entity)
				.ejbFindAllByUserIdAndType(userId, type);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySearch(String id, String name, String pid,
			String type, int iCategoryId) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AccountBMPBean) entity).ejbFindBySearch(
				id, name, pid, type, iCategoryId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByAssessmentRound(int roundid) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AccountBMPBean) entity)
				.ejbFindByAssessmentRound(roundid);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySQL(String sql) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AccountBMPBean) entity).ejbFindBySQL(sql);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int countByTypeAndCategory(String type, Integer categoryID)
			throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((AccountBMPBean) entity)
				.ejbHomeCountByTypeAndCategory(type, categoryID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int countByAssessmentRound(Integer roundID) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((AccountBMPBean) entity)
				.ejbHomeCountByAssessmentRound(roundID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findByAssessmentRound(Integer roundID, int resultSize,
			int startindex) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AccountBMPBean) entity)
				.ejbFindByAssessmentRound(roundID, resultSize, startindex);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
