/*
 * Created on 28.10.2003
 *
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.util.Collection;
import java.sql.Date;

import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.user.data.User;

/**
 * @author Roar
 *
 */
public interface RegularPaymentEntryHome extends com.idega.data.IDOHome{
	RegularPaymentEntry create() throws javax.ejb.CreateException;
		
	Collection findRegularPaymentsForPeriodeAndUser(Date from, Date to, int userId) throws FinderException;

	Collection findRegularPaymentsForPeriode(Date from, Date to);
	
	RegularPaymentEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @param from
	 * @param to
	 * @param provider
	 * @return
	 */
	Collection findRegularPaymentsForPeriodeAndProvider(Date from, Date to, School provider)   throws FinderException;
	
	/**
	 * @param user
	 * @param provider
	 * @param date
	 * @return
	 * @throws javax.ejb.FinderException
	 */
 	Collection findOngoingRegularPaymentsForUserAndProviderByDate(User user, School provider, Date date)throws javax.ejb.FinderException;

}