package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRunError;
import se.idega.idegaweb.commune.accounting.invoice.data.ConstantStatus;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;

/**
 * @author Joakim
 *
 */
public abstract class BillingThread extends Thread{
	protected static final String BATCH_TEXT = "invoice.batchrun";		//Localize this text in the user interface
//	private static final String MANUALLY_TEXT = "invoice.manually";		//Localize this text in the user interface
	protected int days;
	protected float months;
	protected int errorOrder;
	protected IWTimestamp startPeriod;
	protected IWTimestamp endPeriod;
	protected IWContext iwc;
	private IWTimestamp time;
	protected IWTimestamp startTime, endTime;
	protected Date currentDate = new Date( System.currentTimeMillis());
	protected SchoolCategory category;
	protected ExportDataMapping categoryPosting;
	protected School school;

	
	public BillingThread(Date month, IWContext iwc){
		startPeriod = new IWTimestamp(month);
		startPeriod.setAsDate();
		startPeriod.setDay(1);
		endPeriod = new IWTimestamp(startPeriod);
		endPeriod.setAsDate();
		endPeriod.addMonths(1);
		this.iwc = iwc;
	}
	
	/**
	 * Finds existing payment reacord or creates a new payment record if needed, 
	 * and populates the values as needed. It also creates the payment header if needed
	 *  
	 * @return the created payment record
	 * @throws CreateException
	 * @throws IDOLookupException
	 */
	protected PaymentRecord createPaymentRecord(PostingDetail postingDetail, String ownPosting, String doublePosting) throws CreateException, IDOLookupException {
		PaymentHeader paymentHeader;
		PaymentRecord paymentRecord;
	
		//Get the payment header
		try {
			paymentHeader = ((PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class)).
					findBySchoolCategorySchoolPeriod(school,category,currentDate);
		} catch (FinderException e) {
			//If No header found, create it
			paymentHeader = (PaymentHeader) IDOLookup.create(PaymentHeader.class);
			paymentHeader.setSchoolID(school);
			paymentHeader.setSchoolCategoryID(category);
			if(categoryPosting.getProviderAuthorization()){
				paymentHeader.setStatus(ConstantStatus.BASE);
			} else {
				paymentHeader.setStatus(ConstantStatus.PRELIMINARY);
			}
		}

		//Update or create the payment record
		try {
			paymentRecord = ((PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class)).
					findByPaymentHeaderAndRuleSpecType(paymentHeader,postingDetail.getRuleSpecType());
			//If it already exists, just update the contents.
			paymentRecord.setPlacements(paymentRecord.getPlacements()+1);
			paymentRecord.setTotalAmount(paymentRecord.getTotalAmount()+postingDetail.getAmount()*months);
			paymentRecord.setTotalAmountVAT(paymentRecord.getTotalAmountVAT()+postingDetail.getVat()*months);
		} catch (FinderException e1) {
			//It didn't exist, so we create it
			paymentRecord = (PaymentRecord) IDOLookup.create(PaymentRecord.class);
			//Set all the values for the payment record
			paymentRecord.setPaymentHeader(paymentHeader);
			if(categoryPosting.getProviderAuthorization()){
				paymentRecord.setStatus(ConstantStatus.BASE);
			} else {
				paymentRecord.setStatus(ConstantStatus.PRELIMINARY);
			}
			paymentRecord.setPeriod(startPeriod.getDate());
			paymentRecord.setPaymentText(postingDetail.getTerm());
			paymentRecord.setDateCreated(currentDate);
			paymentRecord.setCreatedBy(BATCH_TEXT);
			paymentRecord.setPlacements(1);
			paymentRecord.setPieceAmount(postingDetail.getAmount());
			paymentRecord.setTotalAmount(postingDetail.getAmount()*months);
			paymentRecord.setTotalAmountVAT(postingDetail.getVat()*months);
			paymentRecord.setRuleSpecType(postingDetail.getRuleSpecType());
			paymentRecord.setOwnPosting(ownPosting);
			paymentRecord.setDoublePosting(doublePosting);
			paymentRecord.setVATType(postingDetail.getVatRegulationID());
			paymentRecord.store();
		}
		return paymentRecord;
	}

	/**
	 * calculatest the number of days and months between the start and end date 
	 * and sets the local variables monts and days
	 * 
	 * @param start
	 * @param end
	 */
	protected void calculateTime(Date start, Date end){
		startTime = new IWTimestamp(start);
		startTime.setAsDate();
		time = new IWTimestamp(startPeriod);
		time.setAsDate();
		startTime = startTime.isLater(startTime,time);
		//Then get end date
		endTime = new IWTimestamp(endPeriod);
		endTime.setAsDate();
		if(end!=null){
			time = new IWTimestamp(end);
			endTime = endTime.isEarlier(endTime, time);
		}
		//calc the how many months are in the given time.
		months = endTime.getMonth() - startTime.getMonth() + (endTime.getYear()-startTime.getYear())*12;
		months += 1.0;
		months -= percentOfMonthDone(startTime);
		months -= 1.0 - percentOfMonthDone(endTime);
		days = IWTimestamp.getDaysBetween(startTime, endTime);
	}

	/**
	 * Calculates the amount of the month that has passed at the given date.
	 * @param date
	 * @return the amount (%) of the month that has passed
	 */
	protected float percentOfMonthDone(IWTimestamp date){
		int daysInMonth;
		IWTimestamp firstDay, lastDay;

		firstDay = new IWTimestamp(date);
		firstDay.setDay(1);
		lastDay = new IWTimestamp(firstDay);
		lastDay.addMonths(1);
		daysInMonth = IWTimestamp.getDaysBetween(firstDay, lastDay);
		return (float)(date.getDay()-1)/(float)daysInMonth;
	}

	/**
	 * Creates a new error log message (to be used according to fonster 33 in C&P Req.Spec.)
	 *  
	 * @param related Description of related objects
	 * @param desc Description possible errors
	 */
	protected void createNewErrorMessage(String related, String desc){
		try {
			BatchRunError error = (BatchRunError) IDOLookup.create(BatchRunError.class);
			error.setRelated(related);
			error.setDescription(desc);
			error.setOrder(errorOrder);
			errorOrder++;
		} catch (Exception e) {
			System.out.println("Exception so complicated that it wasn't even possible to create an error message in the log!");
			e.printStackTrace();
		}
	}

	protected RegulationsBusiness getRegulationsBusiness() throws RemoteException {
		return (RegulationsBusiness) IBOLookup.getServiceInstance(iwc, RegulationsBusiness.class);
	}
	
}
