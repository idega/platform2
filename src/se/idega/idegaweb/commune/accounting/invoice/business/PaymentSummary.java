package se.idega.idegaweb.commune.accounting.invoice.business;

import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.data.IDOLookup;
import com.idega.user.data.User;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;
import se.idega.idegaweb.commune.accounting.regulations.data.MainRule;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;

/**
 * Last modified: $Date: 2004/01/20 20:03:02 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.4 $
 */
public class PaymentSummary {
	private int placementCount = 0;
	private Set individuals = new HashSet ();
	private long totalAmountVatExcluded = 0;
	private long totalAmountVat = 0;

	public PaymentSummary (final PaymentRecord [] records)
		throws RemoteException, FinderException {
		// get home objects
		final SchoolClassMemberHome placementHome
				= (SchoolClassMemberHome) IDOLookup.getHome (SchoolClassMember.class);
		final InvoiceRecordHome invoiceRecordHome
				= (InvoiceRecordHome) IDOLookup.getHome (InvoiceRecord.class);
		final RegulationSpecTypeHome regSpecTypeHome
				= (RegulationSpecTypeHome) IDOLookup.getHome (RegulationSpecType.class);

		// count values
		for (int i = 0; i < records.length; i++) {
			final PaymentRecord paymentRecord = records [i];
			placementCount += paymentRecord.getPlacements ();
			final String regSpecTypeName = paymentRecord.getRuleSpecType ();
			String mainRuleName = "";
			try {
				final	RegulationSpecType regSpecType
						= regSpecTypeHome.findByRegulationSpecType (regSpecTypeName);
				final MainRule mainRule = regSpecType.getMainRule ();
				mainRuleName = mainRule.getMainRule ();
			} catch (Exception e) {
				System.err.println ("PaymentRecord id with incorrect reg spec type = "
														+ paymentRecord.getPrimaryKey ());
				e.printStackTrace ();
			}
			final long amountExcl = se.idega.idegaweb.commune.accounting.business.AccountingUtil.roundAmount (paymentRecord.getTotalAmount ());
			if (mainRuleName.equals (RegSpecConstant.MAIN_RULE_VAT)) {
				totalAmountVat += amountExcl;
			} else {
				totalAmountVatExcluded += amountExcl;
			}
			final Collection invoiceRecords
					= invoiceRecordHome.findByPaymentRecord (paymentRecord);
			for (Iterator j = invoiceRecords.iterator (); j.hasNext ();) {
				final InvoiceRecord invoiceRecord = (InvoiceRecord) j.next ();
				try {
					final Integer placementId = new Integer
							(invoiceRecord.getSchoolClassMemberId ());
					final SchoolClassMember placement
							= placementHome.findByPrimaryKey (placementId);
					final User user = placement.getStudent ();
					individuals.add (user.getPrimaryKey ());
				} catch (Exception e) {
					e.printStackTrace ();
				}
			}
		}		
	}
	
	public long getPlacementCount () {
		return placementCount;
	}

	public long getIndividualsCount () {
		return individuals.size (); 
	}

	public long getTotalAmountVatExcluded () {
		return totalAmountVatExcluded;
	}
	
	public long getTotalAmountVat () {
		 return totalAmountVat;
	}
}
