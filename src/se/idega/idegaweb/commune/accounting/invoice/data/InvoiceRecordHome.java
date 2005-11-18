/**
 * 
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.care.data.ChildCareContract;

import com.idega.block.school.data.SchoolClassMember;
import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.util.CalendarMonth;

/**
 * @author bluebottle
 *
 */
public interface InvoiceRecordHome extends IDOHome {
	public InvoiceRecord create() throws javax.ejb.CreateException;

	public InvoiceRecord findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#ejbFindByInvoiceHeader
	 */
	public Collection findByInvoiceHeader(InvoiceHeader invoiceHeader)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#ejbFindByContract
	 */
	public Collection findByContract(ChildCareContract contract)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#ejbFindByPaymentRecord
	 */
	public Collection findByPaymentRecord(PaymentRecord paymentRecord)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#ejbFindByPaymentRecords
	 */
	public Collection findByPaymentRecords(PaymentRecord[] paymentRecords)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#ejbHomeGetIndividualCountByPaymentRecords
	 */
	public int getIndividualCountByPaymentRecords(PaymentRecord[] paymentRecords)
			throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#ejbFindByPaymentRecordOrderedByStudentName
	 */
	public Collection findByPaymentRecordOrderedByStudentName(
			PaymentRecord paymentRecord) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#ejbHomeGetPlacementCountForSchoolCategoryAndPeriod
	 */
	public int getPlacementCountForSchoolCategoryAndPeriod(
			String schoolCategoryID, Date period) throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#ejbHomeGetNumberOfHandledChildrenForSchoolTypesAndMonth
	 */
	public int getNumberOfHandledChildrenForSchoolTypesAndMonth(
			Collection schoolTypes, CalendarMonth month) throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#ejbHomeGetNumberOfInvoicesForStudent
	 */
	public int getNumberOfInvoicesForStudent(SchoolClassMember student)
			throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#ejbHomeGetTotalAmountForSchoolTypesAndMonth
	 */
	public double getTotalAmountForSchoolTypesAndMonth(Collection schoolTypes,
			CalendarMonth month) throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordBMPBean#ejbFindByMonthAndCategory
	 */
	public Collection findByMonthAndCategory(CalendarMonth month,
			String categoryId) throws FinderException;

}
