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
public interface BankHome extends IDOHome {
	public Bank create() throws javax.ejb.CreateException;

	public Bank findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.finance.data.BankBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

}
