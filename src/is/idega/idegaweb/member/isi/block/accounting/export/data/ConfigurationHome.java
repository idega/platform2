/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.export.data;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface ConfigurationHome extends IDOHome {
	public Configuration create() throws javax.ejb.CreateException;

	public Configuration findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.ConfigurationBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.ConfigurationBMPBean#ejbFindByCreditcardType
	 */
	public Configuration findByCreditcardType(CreditCardType type)
			throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.ConfigurationBMPBean#ejbFindByCreditcardTypeID
	 */
	public Configuration findByCreditcardTypeID(int typeID)
			throws FinderException;

}
