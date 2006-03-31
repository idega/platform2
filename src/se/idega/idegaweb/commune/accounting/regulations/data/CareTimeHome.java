/**
 * 
 */
package se.idega.idegaweb.commune.accounting.regulations.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface CareTimeHome extends IDOHome {
	public CareTime create() throws javax.ejb.CreateException;

	public CareTime findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.CareTimeBMPBean#ejbFindAllCareTimeValues
	 */
	public Collection findAllCareTimeValues() throws FinderException;

}
