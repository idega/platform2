package se.idega.idegaweb.commune.accounting.invoice.business;

import com.lowagie.text.PageSize;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.business.IBOServiceBean;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRun;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRunHome;
import se.idega.idegaweb.commune.accounting.invoice.data.ConstantStatus;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.VATRule;
import se.idega.idegaweb.commune.accounting.regulations.data.VATRuleHome;
import com.lowagie.text.Phrase;

/**
 * Holds most of the logic for the batchjob that creates the information that is
 * base for invoicing and payment data, that is sent to external finance system.
 * Now moved to InvoiceThread
 * <p>
 * Last modified: $Date: 2003/11/25 17:39:38 $ by $Author: joakim $
 *
 * @author Joakim
 * @version $Revision: 1.48 $
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceThread
 */
public class InvoiceBusinessBean extends IBOServiceBean implements InvoiceBusiness {

	/**
	 * Spawns a new thread and starts the execution of the posting calculation and then returns
	 * @param month
	 */
	public void startPostingBatch(Date month, Date readDate, String schoolCategory, IWContext iwc)
		throws IDOLookupException, FinderException, SchoolCategoryNotFoundException {
		//Select correct thread to start
		SchoolCategoryHome sch = (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
		if (sch.findChildcareCategory().getCategory().equals(schoolCategory)) {
			new InvoiceChildcareThread(month, iwc).start();
		} else if (sch.findElementarySchoolCategory().getCategory().equals(schoolCategory)) {
			new PaymentThreadElementarySchool(month, iwc).start();
		} else if (sch.findHighSchoolCategory().getCategory().equals(schoolCategory)) {
			new PaymentThreadHighSchool(readDate, iwc).start();
		} else {
			logWarning ("Error: couldn't find any Schoolcategory for billing named " + schoolCategory);
			throw new SchoolCategoryNotFoundException(
				"Couldn't find any Schoolcategory for billing named " + schoolCategory);
		}
	}

	private static float mmToPoints (final float mm) {
		return mm*72/25.4f;
	}
    
	/**
	 * @return int id of document
	 */
	public int generateInvoiceCompilationPdf (final InvoiceHeader header)
        throws RemoteException {
        final InvoiceRecord [] records
                = getInvoiceRecordsByInvoiceHeader (header);
		try{
			final MemoryFileBuffer buffer = new MemoryFileBuffer ();
			final OutputStream outStream = new MemoryOutputStream (buffer);
			final Document document = new Document
					(PageSize.A4, mmToPoints (30), mmToPoints (30),
					 mmToPoints (30), mmToPoints (30));
			final PdfWriter writer = PdfWriter.getInstance(document, outStream);
			writer.setViewerPreferences
					(PdfWriter.HideMenubar | PdfWriter.PageLayoutOneColumn |
                     PdfWriter.FitWindow | PdfWriter.CenterWindow);
            final String title = "Fakturaunderlag " + header.getPrimaryKey ();
			document.addTitle(title);
			document.addCreationDate();
			document.open();
            document.add (new Phrase (new Chunk ("Hallo!")));
			document.close();
			final ICFileHome icFileHome
					= (ICFileHome) getIDOHome(ICFile.class);
			final ICFile file = icFileHome.create();
			final InputStream inStream = new MemoryInputStream (buffer);
			file.setFileValue(inStream);
			file.setMimeType("application/x-pdf");
			file.setName("invoice_" + header.getPrimaryKey () + ".pdf");
			file.setFileSize(buffer.length());
			file.store();
			return ((Integer)file.getPrimaryKey()).intValue();
		} catch (Exception e) {
			e.printStackTrace ();
			throw new RemoteException ("Couldn't generate reminder "
									   + header.getPrimaryKey (), e);
		}
	}

	/**
	 * removes all the invoice records and header and the related information in 
	 * the payment records for the given month where the status was set to preliminary
	 * 
	 * @param month
	 */
	public void removePreliminaryInvoice(Date month, String category) throws RemoveException {
		PaymentRecord paymentRecord;
		Iterator headerIter;
		InvoiceHeader header;

		try {
			SchoolCategory schoolCategory =
				((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findByPrimaryKey(category);
			headerIter = getInvoiceHeaderHome().findByMonthAndSchoolCategory(month, schoolCategory).iterator();
			while (headerIter.hasNext()) {
				header = (InvoiceHeader) headerIter.next();
				removePreliminaryInvoice(header);
			}
			if(getPaymentRecordHome().getCountForMonthAndStatusLH(month) == 0){
				Iterator recordIter = getPaymentRecordHome().findByMonth(month).iterator();
				while(recordIter.hasNext()){
					paymentRecord = (PaymentRecord) recordIter.next();
					paymentRecord.remove();
				}
			}else{
				throw new RemoveException("invoice.remove_not_allowed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException("invoice.Could not remove the records.");
		}
	}

	/**
	 * Removes the invoice records and header and the related information in 
	 * the payment records if the given header status was set to preliminary
	 * 
	 * @param header invoice to remove
	 */
	public void removePreliminaryInvoice(InvoiceHeader header) throws RemoveException {
		Iterator recordIter;
		InvoiceRecord invoiceRecord;
		UserTransaction transaction = null;
		try {
			transaction = getSessionContext().getUserTransaction();
			transaction.begin();
			if (header.getStatus() == ConstantStatus.PRELIMINARY);
			{
				recordIter = getInvoiceRecordHome().findByInvoiceHeader(header).iterator();
				while (recordIter.hasNext()) {
					invoiceRecord = (InvoiceRecord) recordIter.next();
					removeInvoiceRecord(invoiceRecord);
				}
				header.remove();
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				try {
					transaction.rollback();
				} catch (SystemException se) {
					se.printStackTrace();
				}
			}
			e.printStackTrace();
			throw new RemoveException("Could not remove the records.");
		}
	}

	/**
	 * Removes an invoice record and it's associated payments record if the
	 * rule spec type is check.
	 *
	 * @param invoiceRecord the record to remove
	 * @excpetion RemoteException if data layer fails
	 * @exception RemoveException if it wasn't possible to remove this record
	 */
	public void removeInvoiceRecord(InvoiceRecord invoiceRecord) throws RemoteException, RemoveException {
		PaymentRecord paymentRecord;
		String ruleSpecType = invoiceRecord.getRuleSpecType();
		if (null != ruleSpecType && RegSpecConstant.CHECK.equals(ruleSpecType)) {
			try {
				paymentRecord =
					getPaymentRecordHome().findByPrimaryKey(new Integer(invoiceRecord.getPaymentRecordId()));
				//Remove this instance from the payment record
				paymentRecord.setPlacements(paymentRecord.getPlacements() - 1);
				paymentRecord.setTotalAmount(paymentRecord.getTotalAmount() - invoiceRecord.getAmount());
				paymentRecord.setTotalAmountVAT(paymentRecord.getTotalAmountVAT() - invoiceRecord.getAmountVAT());
			} catch (FinderException e1) {
				e1.printStackTrace();
			}
		}
		invoiceRecord.remove();
	}

	public boolean isHighShool(String category) throws IDOLookupException, FinderException {
		SchoolCategory highSchool =
			((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findHighSchoolCategory();
		if (((String) highSchool.getPrimaryKey()).equals(category)) {
			return true;
		}
		return false;
	}

	public BatchRun getBatchRunByCategory(String category) throws IDOLookupException, FinderException {
		SchoolCategory schoolCategory =
			((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findByPrimaryKey(category);
		BatchRun batchRun = ((BatchRunHome) IDOLookup.getHome(BatchRun.class)).findBySchoolCategory(schoolCategory);
		return batchRun;
	}

	public int getNoProviders(BatchRun batchRun) throws RemoteException, FinderException, IDOException {
		Date period = batchRun.getPeriod();
		String schoolCategoryID = batchRun.getSchoolCategoryID();
		return getPaymentHeaderHome().getProviderCountForSchoolCategoryAndPeriod(schoolCategoryID, period);
	}

	public int getNoPlacements(BatchRun batchRun) throws RemoteException, FinderException, IDOException {
		Date period = batchRun.getPeriod();
		String schoolCategoryID = batchRun.getSchoolCategoryID();
		return getPaymentRecordHome().getPlacementCountForSchoolCategoryAndPeriod(schoolCategoryID, period);
	}

	public int getTotAmountWithoutVAT(BatchRun batchRun) throws RemoteException, FinderException, IDOException {
		Date period = batchRun.getPeriod();
		String schoolCategoryID = batchRun.getSchoolCategoryID();
		return getPaymentRecordHome().getTotAmountForSchoolCategoryAndPeriod(schoolCategoryID, period);
	}

	/**
	 * Retreives an array of all InvoiceHeaders where the user given is either
	 * custodian or the child and in the period. If any of the dates
	 * are null, that constraint will be ignored. An empty list is returned if
	 * no invoice header was found.
	 *
	 * @param user the user to search for
	 * @param fromDate first month in search span
	 * @param toDate last month in search span
	 * @return array of invoice headers
	 */
	public InvoiceHeader[] getInvoiceHeadersByCustodianOrChild(
		final User user,
		final java.util.Date fromPeriod,
		java.util.Date toPeriod) {
		Collection collection = null;
		try {
			collection = getInvoiceHeaderHome().findByCustodianOrChild(user, fromPeriod, toPeriod);
		} catch (RemoteException exception) {
			exception.printStackTrace();
		} catch (FinderException exception) {
			// no problem, return empty array
		}
		return null == collection ? new InvoiceHeader[0] : (InvoiceHeader[]) collection.toArray(new InvoiceHeader[0]);
	}

	/**
	 * Retreives an array of all InvoiceRecords connected to this InvoiceHeader
	 * An empty list is returned if no invoice header was found.
	 *
	 * @param header the header to find records connected to
	 * @return array of invoice records
	 */
	public InvoiceRecord[] getInvoiceRecordsByInvoiceHeader(final InvoiceHeader header) {
		Collection collection = null;
		try {
			collection = getInvoiceRecordHome().findByInvoiceHeader(header);
		} catch (RemoteException exception) {
			exception.printStackTrace();
		} catch (FinderException exception) {
			// no problem, return empty array
		}
		return null == collection ? new InvoiceRecord[0] : (InvoiceRecord[]) collection.toArray(new InvoiceRecord[0]);
	}

	public User getChildByInvoiceRecord(final InvoiceRecord record)
        throws RemoteException {
		final SchoolClassMemberHome home = (SchoolClassMemberHome)
                IDOLookup.getHome (SchoolClassMember.class);
		try {
			final SchoolClassMember student = home.findByPrimaryKey
                    (new Integer (record.getSchoolClassMemberId()));
			return null != student ? student.getStudent () : null;
		} catch (final FinderException e) {
			return null;
		}
	}

	public Collection getPaymentRecordsByCategoryProviderAndPeriod(String category, String provider, Date period)
		throws RemoteException, FinderException {
		SchoolCategory schoolCategory =
			((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findByPrimaryKey(category);
		School school = ((SchoolHome) IDOLookup.getHome(School.class)).findByPrimaryKey(provider);
		return getPaymentRecordsByCategoryProviderAndPeriod(schoolCategory, school, period);
	}

	public Collection getPaymentRecordsByCategoryProviderAndPeriod(
		SchoolCategory category,
		School provider,
		Date period)
		throws RemoteException, FinderException {
		PaymentHeader paymentHeader =
			getPaymentHeaderHome().findBySchoolCategorySchoolPeriod(provider, category, period);
		return getPaymentRecordHome().findByPaymentHeader(paymentHeader);
	}

	public PaymentRecord []
        getPaymentRecordsBySchoolCategoryAndProviderAndPeriod
        (final String schoolCategory, final Integer providerId,
         final Date startPeriod, final Date endPeriod) throws RemoteException {
        final PaymentRecord [] recordArray = new PaymentRecord [0];
        try {
            final Collection paymentHeaders = getPaymentHeaderHome ()
                    .findBySchoolCategoryAndSchoolAndPeriod
                    (schoolCategory, providerId, startPeriod, endPeriod);
            if (null == paymentHeaders || paymentHeaders.isEmpty ()) {
                return recordArray;
            }
            final Collection paymentRecords = getPaymentRecordHome ()
                    .findByPaymentHeaders (paymentHeaders);
            return null == paymentRecords ? recordArray
                    : (PaymentRecord []) paymentRecords.toArray (recordArray);
        } catch (FinderException e) {
            return recordArray;
        }            
	}

	/**
	 * Creates and stores a new Invoice Header. Status will be set to
	 * preliminary.
	 *
	 * @param schoolCategoryKey string constant from SchoolCategoryBMPBean
	 * @param createdBy the user who was logged on and initiated this
	 * @param custodianId the invoice receiver's user id
	 * @param ownPosting egen kontering
	 * @param doublePosting motkontering
	 * @param period the month this occurs
	 * @return the new Invoice Header
	 * @exception CreateException if lower level fails
	 */
	public InvoiceHeader createInvoiceHeader(
		final String schoolCategoryKey,
		final User createdBy,
		final int custodianId,
		final String ownPosting,
		final String doublePosting,
		final Date period)
		throws CreateException {
		try {
			final InvoiceHeader header = getInvoiceHeaderHome().create();
			header.setSchoolCategoryID(schoolCategoryKey);
			if (null != createdBy) {
				final String createdBySignature =
					createdBy.getFirstName().charAt(0) + "" + createdBy.getLastName().charAt(0);
				header.setCreatedBy(createdBySignature);
			}
			header.setCustodianId(custodianId);
			header.setDateCreated(new Date(new java.util.Date().getTime()));
			header.setDoublePosting(doublePosting);
			header.setOwnPosting(ownPosting);
			header.setPeriod(period);
			header.setStatus(ConstantStatus.PRELIMINARY);
			header.store();
			return header;
		} catch (RemoteException e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	protected SchoolCategoryHome getSchoolCategoryHome() throws RemoteException {
		return (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
	}

    public InvoiceRecord createInvoiceRecord
        (final User createdBy, final Integer amount,
         final Date checkStartPeriod, final Date checkEndPeriod,
         final String doublePosting, final Integer invoiceHeaderId,
         final String invoiceText, final String note,
         final Integer numberOfDays, final String ownPosting,
         final Date placementStartPeriod, final Date placementEndPeriod,
         final Integer providerId, final String regulationSpecType,
         final Integer vatAmount, final Integer vatRule, final String ruleText,
         final Integer placementId)
        throws CreateException {
        try {
            final InvoiceRecord record = getInvoiceRecordHome ().create ();
            if (null != createdBy) {
                final String createdBySignature
                        = createdBy.getFirstName ().charAt (0)
                        + "" + createdBy.getLastName ().charAt (0);
                record.setCreatedBy (createdBySignature);
            }
            record.setDateCreated (new Date (new java.util.Date ().getTime()));
            if (null != amount) record.setAmount (amount.floatValue ());
            if (null != vatAmount) record.setAmountVAT
                                           (vatAmount.floatValue ());
            if (null != numberOfDays) record.setDays (numberOfDays.intValue ());
            record.setDoublePosting (doublePosting);
            if (null != invoiceHeaderId) record.setInvoiceHeader
                                                 (invoiceHeaderId.intValue ());
            record.setInvoiceText
                    (null != invoiceText && 0 < invoiceText.length ()
                     ? invoiceText : ruleText);
            record.setRuleText (ruleText);
            record.setNotes (note);
            record.setOwnPosting (ownPosting);
            if (null != checkEndPeriod) record.setPeriodEndCheck
                                                (checkEndPeriod);
            if (null != placementEndPeriod) record.setPeriodEndPlacement
                                                    (placementEndPeriod);
            if (null != checkStartPeriod) record.setPeriodStartCheck
                                                  (checkStartPeriod);
            if (null != placementStartPeriod) record.setPeriodStartPlacement
                                                      (placementStartPeriod);
            record.setRuleSpecType (regulationSpecType);
            if (null != vatRule)  record.setVATType (vatRule.intValue ());
            if (null != providerId) record.setProviderId
                                            (providerId.intValue ());
            if (null != placementId) record.setSchoolClassMemberId
                                            (placementId.intValue ());
            record.store ();
            return record;
        } catch (RemoteException e) {
            e.printStackTrace ();
            throw new CreateException (e.getMessage ());
        }
    }

    public RegulationSpecType [] getAllRegulationSpecTypes ()
        throws RemoteException {
        try {
            final Collection collection = getRegulationSpecTypeHome ()
                    .findAllRegulationSpecTypes ();
            return null == collection ? new RegulationSpecType [0]
                    : (RegulationSpecType []) collection.toArray
                    (new RegulationSpecType [0]);
        } catch (FinderException e) {
            return new RegulationSpecType [0];
        }
    }

    public VATRule [] getAllVatRules () throws RemoteException {
        try {
            final Collection collection = getVatRuleHome ().findAllVATRules ();
            return null == collection ? new VATRule [0]
                    : (VATRule []) collection.toArray (new VATRule [0]);
        } catch (FinderException e) {
            return new VATRule [0];
        }
    }

    public VATRule getVatRule (int primaryKey) throws RemoteException {
        try {
            final VATRule rule = getVatRuleHome ().findByPrimaryKey
                    (new Integer (primaryKey));
            return rule;
        } catch (FinderException e) {
            return null;
        }
    }

	protected RegulationSpecTypeHome getRegulationSpecTypeHome ()
        throws RemoteException {
		return (RegulationSpecTypeHome)
                IDOLookup.getHome(RegulationSpecType.class);
	}

	protected VATRuleHome getVatRuleHome () throws RemoteException {
		return (VATRuleHome) IDOLookup.getHome(VATRule.class);
	}

	public PaymentHeaderHome getPaymentHeaderHome() throws RemoteException {
		return (PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class);
	}

	public PaymentRecordHome getPaymentRecordHome() throws RemoteException {
		return (PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class);
	}

	public InvoiceHeaderHome getInvoiceHeaderHome() throws RemoteException {
		return (InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class);
	}

	public InvoiceRecordHome getInvoiceRecordHome() throws RemoteException {
		return (InvoiceRecordHome) IDOLookup.getHome(InvoiceRecord.class);
	}
}
