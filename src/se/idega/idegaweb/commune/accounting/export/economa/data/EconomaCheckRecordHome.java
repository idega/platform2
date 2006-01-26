/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.economa.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface EconomaCheckRecordHome extends IDOHome {
	public EconomaCheckRecord create() throws javax.ejb.CreateException;

	public EconomaCheckRecord findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckRecordBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckRecordBMPBean#ejbFindAllByHeaderId
	 */
	public Collection findAllByHeaderId(int id) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckRecordBMPBean#ejbFindAllByHeader
	 */
	public Collection findAllByHeader(EconomaCheckHeader header)
			throws FinderException;

}
