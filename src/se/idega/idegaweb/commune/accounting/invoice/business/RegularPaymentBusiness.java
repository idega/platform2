/*
 * Created on 30.9.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.business;

import java.util.Collection;
import java.util.Date;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface RegularPaymentBusiness {
	Collection findRegularPaymentsForPeriodeAndUser(Date from, Date to, int userId);
	Collection findRegularPaymentsForPeriode(Date from, Date to);
}
