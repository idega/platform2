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
public class BankBranchHomeImpl extends IDOFactory implements BankBranchHome {
	protected Class getEntityInterfaceClass() {
		return BankBranch.class;
	}

	public BankBranch create() throws javax.ejb.CreateException {
		return (BankBranch) super.createIDO();
	}

	public BankBranch findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (BankBranch) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findByBank(Bank bank) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((BankBranchBMPBean) entity)
				.ejbFindByBank(bank);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
