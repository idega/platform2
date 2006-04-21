/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface CreditCardTypeHome extends IDOHome {
	public CreditCardType create() throws javax.ejb.CreateException;

	public CreditCardType findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardTypeBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardTypeBMPBean#ejbFindTypeVisa
	 */
	public CreditCardType findTypeVisa() throws FinderException;

}
