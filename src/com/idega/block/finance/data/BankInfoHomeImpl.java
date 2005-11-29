/**
 * 
 */
package com.idega.block.finance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;
import com.idega.user.data.Group;

/**
 * @author bluebottle
 *
 */
public class BankInfoHomeImpl extends IDOFactory implements BankInfoHome {
	protected Class getEntityInterfaceClass() {
		return BankInfo.class;
	}

	public BankInfo create() throws javax.ejb.CreateException {
		return (BankInfo) super.createIDO();
	}

	public BankInfo findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (BankInfo) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((BankInfoBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public BankInfo findByGroupId(int groupId) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((BankInfoBMPBean) entity).ejbFindByGroupId(groupId);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllByClub(Group club) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((BankInfoBMPBean) entity)
				.ejbFindAllByClub(club);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public BankInfo findByGroup(Group group) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((BankInfoBMPBean) entity).ejbFindByGroup(group);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public BankInfo findByDivision(Group division) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((BankInfoBMPBean) entity).ejbFindByDivision(division);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public BankInfo findByClub(Group club) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((BankInfoBMPBean) entity).ejbFindByClub(club);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
