/**
 * 
 */
package com.idega.block.finance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class AccountInfoHomeImpl extends IDOFactory implements AccountInfoHome {
	protected Class getEntityInterfaceClass() {
		return AccountInfo.class;
	}

	public AccountInfo create() throws javax.ejb.CreateException {
		return (AccountInfo) super.createIDO();
	}

	public AccountInfo findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (AccountInfo) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findByOwner(Integer ownerID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AccountInfoBMPBean) entity)
				.ejbFindByOwner(ownerID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByOwnerAndType(Integer ownerID, String type)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AccountInfoBMPBean) entity)
				.ejbFindByOwnerAndType(ownerID, type);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByAssessmentRound(Integer roundID)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AccountInfoBMPBean) entity)
				.ejbFindByAssessmentRound(roundID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByAssessmentRound(Integer roundID, int resultSize,
			int startindex) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AccountInfoBMPBean) entity)
				.ejbFindByAssessmentRound(roundID, resultSize, startindex);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
