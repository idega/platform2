/*
 * Created on 22.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.business;

import java.util.Collection;
import java.sql.Date;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntry;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntryHome;

import com.idega.block.school.data.School;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RegularPaymentBusinessBean extends IBOServiceBean implements RegularPaymentBusiness {
	public Collection findRegularPaymentsForPeriodeAndUser(Date from, Date to, int userId) throws IDOLookupException, FinderException{
 
		RegularPaymentEntryHome home =(RegularPaymentEntryHome) IDOLookup.getHome(RegularPaymentEntry.class);
		return home.findRegularPaymentsForPeriodeAndUser(from, to, userId);

	}
	public Collection findRegularPaymentsForPeriode(Date from, Date to){
		
		return null;
	}
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.RegularPaymentBusiness#findRegularPaymentsForPeriodeAndSchool(java.sql.Date, java.sql.Date, com.idega.block.school.data.School)
	 */
	public Collection findRegularPaymentsForPeriodeAndSchool(Date from, Date to, School provider)  throws IDOLookupException, FinderException{
		RegularPaymentEntryHome home =(RegularPaymentEntryHome) IDOLookup.getHome(RegularPaymentEntry.class);
		return home.findRegularPaymentsForPeriodeAndProvider(from, to, provider);
	}
}
