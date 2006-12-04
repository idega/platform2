package com.idega.block.finance.data;


import com.idega.data.IDOException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.user.data.User;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class AccountHomeImpl extends IDOFactory implements AccountHome {
	public Class getEntityInterfaceClass() {
		return Account.class;
	}

	public Account create() throws CreateException {
		return (Account) super.createIDO();
	}

	public Account findByPrimaryKey(Object pk) throws FinderException {
		return (Account) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByUserId(int userId) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountBMPBean) entity).ejbFindAllByUserId(userId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByUserIdAndType(int userId, String type) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountBMPBean) entity).ejbFindAllByUserIdAndType(userId, type);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Account findByUserAndType(User user, String type) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((AccountBMPBean) entity).ejbFindByUserAndType(user, type);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findBySearch(String id, String name, String pid, String type, int iCategoryId) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountBMPBean) entity).ejbFindBySearch(id, name, pid, type, iCategoryId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByAssessmentRound(int roundid) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountBMPBean) entity).ejbFindByAssessmentRound(roundid);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySQL(String sql) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountBMPBean) entity).ejbFindBySQL(sql);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int countByTypeAndCategory(String type, Integer categoryID) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((AccountBMPBean) entity).ejbHomeCountByTypeAndCategory(type, categoryID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int countByAssessmentRound(Integer roundID) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((AccountBMPBean) entity).ejbHomeCountByAssessmentRound(roundID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findByAssessmentRound(Integer roundID, int resultSize, int startindex) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountBMPBean) entity).ejbFindByAssessmentRound(roundID, resultSize, startindex);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}