package se.idega.idegaweb.commune.accounting.invoice.business;

import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;
import se.idega.idegaweb.commune.accounting.regulations.data.MainRule;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;

/**
 * Last modified: $Date: 2004/05/05 09:42:16 $ by $Author: sigtryggur $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.6 $
 */
public class PaymentSummary {
	private int placementCount = 0;
	private int individualCount = 0;
	private long totalAmountVatExcluded = 0;
	private long totalAmountVat = 0;

	public PaymentSummary (final PaymentRecord [] records) throws RemoteException {
		this(records, null);
		//Optimization by Sigtryggur New constructor with HashMap added to cache MainRule strings
	}

	public PaymentSummary (final PaymentRecord [] records, Map mainRuleStringValues) throws RemoteException {
		// get home objects
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
			if (mainRuleStringValues != null && mainRuleStringValues.containsKey(regSpecTypeName))
				mainRuleName = (String)mainRuleStringValues.get(regSpecTypeName);
			else if (regSpecTypeName != null){
				try {
					final	RegulationSpecType regSpecType
							= regSpecTypeHome.findByRegulationSpecType (regSpecTypeName);
					final MainRule mainRule = regSpecType.getMainRule ();
					mainRuleName = mainRule.getMainRule ();
					if (mainRuleStringValues != null)
						mainRuleStringValues.put(regSpecTypeName, mainRuleName);
				} catch (FinderException e) {
					System.err.println ("PaymentRecord id with incorrect reg spec type = "
															+ paymentRecord.getPrimaryKey ());
					e.printStackTrace ();
				}
			}
			final long amountExcl = se.idega.idegaweb.commune.accounting.business.AccountingUtil.roundAmount (paymentRecord.getTotalAmount ());
			if (mainRuleName.equals (RegSpecConstant.MAIN_RULE_VAT)) {
				totalAmountVat += amountExcl;
			} else {
				totalAmountVatExcluded += amountExcl;
			}
		}		

		try {
			individualCount
					= invoiceRecordHome.getIndividualCountByPaymentRecords (records);
		} catch (IDOException e) {
			e.printStackTrace ();
		}
	}

	/*	
	private static int getIndividualCount(final PaymentRecord[] records, final SchoolClassMemberHome placementHome, final InvoiceRecordHome invoiceRecordHome) {
		final Set result = new HashSet ();
		try {
			final Collection invoiceRecords
					= invoiceRecordHome.findByPaymentRecords (records);
			for (Iterator j = invoiceRecords.iterator (); j.hasNext ();) {
				final InvoiceRecord invoiceRecord = (InvoiceRecord) j.next ();
				try {
					final Integer placementId = new Integer
							(invoiceRecord.getSchoolClassMemberId ());
					final SchoolClassMember placement
							= placementHome.findByPrimaryKey (placementId);
					final User user = placement.getStudent ();
					result.add (user.getPrimaryKey ());
				} catch (Exception e) {
					e.printStackTrace ();
				}
			}
		} catch (FinderException e) {
			// no problem, no invoice records found
		}
		return result.size ();
	}
	*/

	public long getPlacementCount () {
		return placementCount;
	}

	public long getIndividualsCount () {
		return individualCount; 
	}

	public long getTotalAmountVatExcluded () {
		return totalAmountVatExcluded;
	}
	
	public long getTotalAmountVat () {
		 return totalAmountVat;
	}
}
