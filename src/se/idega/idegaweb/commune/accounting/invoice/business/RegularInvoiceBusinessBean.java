/*
 * Created on 22.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.business;

import java.util.Collection;
import java.util.HashSet;
import java.sql.Date;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntryHome;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RegularInvoiceBusinessBean extends IBOServiceBean implements RegularInvoiceBusiness {
	public Collection findRegularInvoicesForPeriodeUserAndCategory(Date from, Date to, int userId, String schoolCategoryId) throws IDOLookupException, FinderException{

		RegularInvoiceEntryHome home =(RegularInvoiceEntryHome) IDOLookup.getHome(RegularInvoiceEntry.class);
		return home.findRegularInvoicesForPeriodeUserAndCategory(from, to, userId, schoolCategoryId);

	}
	public Collection findRegularInvoicesForPeriode(Date from, Date to){
		return new HashSet();
	}
}
