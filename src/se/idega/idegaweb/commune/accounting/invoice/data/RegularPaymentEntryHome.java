/*
 * Created on 28.10.2003
 *
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.util.Collection;
import java.sql.Date;

import javax.ejb.FinderException;

/**
 * @author Roar
 *
 */
public interface RegularPaymentEntryHome extends com.idega.data.IDOHome{
	RegularPaymentEntry create() throws javax.ejb.CreateException;
		
	Collection findRegularPaymentsForPeriodeAndUser(Date from, Date to, int userId) throws FinderException;

	Collection findRegularPaymentsForPeriode(Date from, Date to);
	
	RegularPaymentEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
	
}
