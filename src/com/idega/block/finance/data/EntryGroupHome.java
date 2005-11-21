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
public interface EntryGroupHome extends IDOHome {
	public EntryGroup create() throws javax.ejb.CreateException;

	public EntryGroup findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

}
