/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.raindance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface RaindanceCheckRecordHome extends IDOHome {
	public RaindanceCheckRecord create() throws javax.ejb.CreateException;

	public RaindanceCheckRecord findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckRecordBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckRecordBMPBean#ejbFindAllByHeaderId
	 */
	public Collection findAllByHeaderId(int id) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckRecordBMPBean#ejbFindAllByHeader
	 */
	public Collection findAllByHeader(RaindanceCheckHeader header)
			throws FinderException;

}
