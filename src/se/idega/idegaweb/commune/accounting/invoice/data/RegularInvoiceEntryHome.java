/*
 * Created on 1.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.util.Collection;
import java.sql.Date;

import javax.ejb.FinderException;

/**
 * @author Roar
 *
 */
public interface RegularInvoiceEntryHome extends com.idega.data.IDOHome{
	RegularInvoiceEntry create() throws javax.ejb.CreateException;
		
	Collection findRegularInvoicesForPeriodeAndUser(Date from, Date to, int userId) throws FinderException;

	Collection findRegularInvoicesForPeriode(Date from, Date to);
	
	RegularInvoiceEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
	
}
