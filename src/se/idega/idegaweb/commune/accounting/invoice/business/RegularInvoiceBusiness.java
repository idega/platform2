/*
 * Created on 30.9.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.business;

import java.util.Collection;
import java.sql.Date;

import javax.ejb.FinderException;

import com.idega.data.IDOLookupException;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface RegularInvoiceBusiness extends com.idega.business.IBOService {
	/**
	 * 
	 * @param from
	 * @param to
	 * @param userId
	 * @return Collection of RegularPaymentEntry
	 */
	Collection findRegularInvoicesForPeriodeAndUser(Date from, Date to, int userId, String schoolCategoryId) throws IDOLookupException, FinderException;
	/**
	 * 
	 * @param from
	 * @param to
	 * @return Collection of RegularPaymentEntry
	 */
	Collection findRegularInvoicesForPeriode(Date from, Date to);


}
