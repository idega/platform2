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

/**
 * Last modified: $Date: 2004/01/15 09:56:35 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
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
		final InvoiceRecordHome home
				= (InvoiceRecordHome) IDOLookup.getHome (InvoiceRecord.class);

		// count values
		for (int i = 0; i < records.length; i++) {
			final PaymentRecord paymentRecord = records [i];
			placementCount += paymentRecord.getPlacements ();
			final String ruleSpecType = paymentRecord.getRuleSpecType ();
			final long amountExcl = se.idega.idegaweb.commune.accounting.business.AccountingUtil.roundAmount (paymentRecord.getTotalAmount ());
			if (null != ruleSpecType
					&& ruleSpecType.equals("cacc_reg_spec_type.moms")) {
				totalAmountVat += amountExcl;
			} else {
				totalAmountVatExcluded += amountExcl;
			}
			final Collection invoiceRecords
					= home.findByPaymentRecord (paymentRecord);
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
