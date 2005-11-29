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
public interface PaymentTypeHome extends IDOHome {
	public PaymentType create() throws javax.ejb.CreateException;

	public PaymentType findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeBMPBean#ejbFindAllPaymentTypes
	 */
	public Collection findAllPaymentTypes() throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeBMPBean#ejbFindPaymentTypeCreditcard
	 */
	public PaymentType findPaymentTypeCreditcard() throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeBMPBean#ejbFindPaymentTypeCreditcardSystem
	 */
	public PaymentType findPaymentTypeCreditcardSystem() throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeBMPBean#ejbFindPaymentTypeBankSystem
	 */
	public PaymentType findPaymentTypeBankSystem() throws FinderException;

}
