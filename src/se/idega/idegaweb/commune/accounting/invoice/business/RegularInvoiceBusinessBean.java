/*
 * Created on 22.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.business;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntryHome;
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;

import com.idega.block.school.data.SchoolCategory;
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

	public Collection findRegularInvoicesForPeriodeAndCategory(Date date, SchoolCategory cat) throws IDOLookupException, FinderException{
		RegulationSpecTypeHome rstHome = (RegulationSpecTypeHome) IDOLookup.getHome(RegulationSpecType.class);
		int lagPrimary = ((Integer)rstHome.findByRegulationSpecType(RegSpecConstant.LAGINKOMSTSKYDD).getPrimaryKey()).intValue();
		RegularInvoiceEntryHome home =(RegularInvoiceEntryHome) IDOLookup.getHome(RegularInvoiceEntry.class);
		return home.findRegularInvoicesForPeriodeAbdCategory(date,cat.getCategory(),lagPrimary);
	}
}
