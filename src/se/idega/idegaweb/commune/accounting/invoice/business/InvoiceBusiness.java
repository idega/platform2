/*
 * $Id: InvoiceBusiness.java,v 1.64 2004/10/20 17:05:09 thomas Exp $
 * Created on Oct 20, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.invoice.business;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRun;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.care.business.CareInvoiceBusiness;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.care.data.ChildCareContractHome;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOService;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOException;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MemoryFileBuffer;
import com.idega.user.data.User;
import com.idega.util.CalendarMonth;


/**
 * 
 *  Last modified: $Date: 2004/10/20 17:05:09 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.64 $
 */
public interface InvoiceBusiness extends IBOService, CareInvoiceBusiness {

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#generatePdf
	 */
	public int generatePdf(String title, MemoryFileBuffer buffer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#removePreliminaryInvoice
	 */
	public void removePreliminaryInvoice(CalendarMonth month, String category) throws FinderException, RemoveException,
			BatchAlreadyRunningException, SchoolCategoryNotFoundException, IDOLookupException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#removePreliminaryPayment
	 */
	public void removePreliminaryPayment(CalendarMonth month, String category) throws RemoveException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#removeTestRecordsForProvider
	 */
	public void removeTestRecordsForProvider(CalendarMonth month, String category, School school)
			throws RemoveException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#removePreliminaryInvoice
	 */
	public void removePreliminaryInvoice(InvoiceHeader header) throws RemoveException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#removeInvoiceRecord
	 */
	public void removeInvoiceRecord(InvoiceRecord invoiceRecord) throws RemoteException, RemoveException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#removePaymentRecord
	 */
	public void removePaymentRecord(PaymentRecord paymentRecord) throws RemoteException, RemoveException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#isChildCare
	 */
	public boolean isChildCare(String category) throws IDOLookupException, FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getBatchRunByCategory
	 */
	public BatchRun getBatchRunByCategory(String category, boolean test) throws IDOLookupException, FinderException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getNoProviders
	 */
	public int getNoProviders(BatchRun batchRun) throws RemoteException, IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getNoPlacements
	 */
	public int getNoPlacements(BatchRun batchRun) throws RemoteException, IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getTotAmountWithoutVAT
	 */
	public int getTotAmountWithoutVAT(BatchRun batchRun) throws RemoteException, IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getTotalAmountOfInvoices
	 */
	public double getTotalAmountOfInvoices(BatchRun batchRun) throws RemoteException, IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getNumberOfHandledChildren
	 */
	public int getNumberOfHandledChildren(BatchRun batchRun) throws RemoteException, IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getNumberOfInvoices
	 */
	public int getNumberOfInvoices(BatchRun batchRun) throws RemoteException, IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getInvoiceHeadersByCustodianOrChild
	 */
	public InvoiceHeader[] getInvoiceHeadersByCustodianOrChild(String schoolCategory, User user,
			CalendarMonth fromPeriod, CalendarMonth toPeriod) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getInvoiceRecordsByInvoiceHeader
	 */
	public InvoiceRecord[] getInvoiceRecordsByInvoiceHeader(InvoiceHeader header) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getChildByInvoiceRecord
	 */
	public User getChildByInvoiceRecord(InvoiceRecord record) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getPaymentRecordsBySchoolCategoryAndProviderAndPeriod
	 */
	public PaymentRecord[] getPaymentRecordsBySchoolCategoryAndProviderAndPeriod(String schoolCategory,
			Integer providerId, Date startPeriod, Date endPeriod) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#createInvoiceHeader
	 */
	public InvoiceHeader createInvoiceHeader(String schoolCategoryKey, User createdBy, int custodianId, Date period)
			throws CreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#createInvoiceRecord
	 */
	public InvoiceRecord createInvoiceRecord(PaymentRecord paymentRecord, SchoolClassMember placement,
			PostingDetail postingDetail, PlacementTimes checkPeriod, Date startPlacementDate, Date endPlacementDate,
			String createdBySignature) throws CreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#createInvoiceRecord
	 */
	public InvoiceRecord createInvoiceRecord(User createdBy, InvoiceHeader header, Integer placementId,
			Integer providerId, String ruleText, String invoiceText, String invoiceText2, String note,
			Date placementStartPeriod, Date placementEndPeriod, Date checkStartPeriod, Date checkEndPeriod,
			Integer amount, Integer vatAmount, Integer numberOfDays, Integer regSpecTypeId, Integer vatRule,
			String ownPosting, String doublePosting, Integer pieceAmount, String ownPaymentPosting,
			String doublePaymentPosting, Integer orderId) throws CreateException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#createOrUpdateVatPaymentRecord
	 */
	public PaymentRecord createOrUpdateVatPaymentRecord(PaymentRecord paymentRecord, SchoolType schoolType,
			SchoolYear schoolYear, String createdBySignature) throws RemoteException, CreateException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#saveInvoiceRecord
	 */
	public void saveInvoiceRecord(Integer recordId, User currentUser, Integer placementId, Integer providerId,
			String invoiceText, String invoiceText2, String ruleText, String note, Date checkEndPeriod,
			Date checkStartPeriod, Date placementStartPeriod, Date placementEndPeriod, String ownPosting,
			String doublePosting, Integer amount, Integer vatAmount, Integer vatRule, Integer regSpecTypeId)
			throws RemoteException, FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#findInvoiceRecordsByContract
	 */
	public Collection findInvoiceRecordsByContract(ChildCareContract contract) throws FinderException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getSchoolClassMembers
	 */
	public SchoolClassMember[] getSchoolClassMembers(InvoiceHeader header) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#createDetailedPaymentRecord
	 */
	public InvoiceRecord createDetailedPaymentRecord(User child, PaymentRecord paymentRecord, User registrator)
			throws IDOLookupException, FinderException, RemoteException, CreateException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#removeInvoiceRecords
	 */
	public void removeInvoiceRecords(ChildCareContract contract) throws RemoveException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getAllRegulationSpecTypes
	 */
	public RegulationSpecType[] getAllRegulationSpecTypes() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getAllVATRuleRegulations
	 */
	public Collection getAllVATRuleRegulations() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getVATRuleRegulation
	 */
	public Regulation getVATRuleRegulation(int primaryKey) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#exportToExcel
	 */
	public ICFile exportToExcel(IWResourceBundle iwrb, String fileName, BatchRun batchRun, boolean isTestRun)
			throws FinderException, IOException, CreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getPaymentHeaderHome
	 */
	public PaymentHeaderHome getPaymentHeaderHome() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getChildCareContractHome
	 */
	public ChildCareContractHome getChildCareContractHome() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getPaymentRecordHome
	 */
	public PaymentRecordHome getPaymentRecordHome() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getInvoiceHeaderHome
	 */
	public InvoiceHeaderHome getInvoiceHeaderHome() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getInvoiceRecordHome
	 */
	public InvoiceRecordHome getInvoiceRecordHome() throws java.rmi.RemoteException;
}
