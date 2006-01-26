/**
 * 
 */
package se.idega.idegaweb.commune.care.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface ProviderAccountingPropertiesHome extends IDOHome {
	public ProviderAccountingProperties create()
			throws javax.ejb.CreateException;

	public ProviderAccountingProperties findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#ejbFindAllByPaymentByInvoice
	 */
	public Collection findAllByPaymentByInvoice(boolean hasPaymentByInvoice)
			throws FinderException;

}
