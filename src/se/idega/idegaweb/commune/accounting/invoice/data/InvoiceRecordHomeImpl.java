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
import com.idega.data.IDOFactory;
import com.idega.util.CalendarMonth;

/**
 * @author bluebottle
 *
 */
public class InvoiceRecordHomeImpl extends IDOFactory implements
		InvoiceRecordHome {
	protected Class getEntityInterfaceClass() {
		return InvoiceRecord.class;
	}

	public InvoiceRecord create() throws javax.ejb.CreateException {
		return (InvoiceRecord) super.createIDO();
	}

	public InvoiceRecord findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (InvoiceRecord) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findByInvoiceHeader(InvoiceHeader invoiceHeader)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((InvoiceRecordBMPBean) entity)
				.ejbFindByInvoiceHeader(invoiceHeader);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByContract(ChildCareContract contract)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((InvoiceRecordBMPBean) entity)
				.ejbFindByContract(contract);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByPaymentRecord(PaymentRecord paymentRecord)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((InvoiceRecordBMPBean) entity)
				.ejbFindByPaymentRecord(paymentRecord);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByPaymentRecords(PaymentRecord[] paymentRecords)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((InvoiceRecordBMPBean) entity)
				.ejbFindByPaymentRecords(paymentRecords);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getIndividualCountByPaymentRecords(PaymentRecord[] paymentRecords)
			throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((InvoiceRecordBMPBean) entity)
				.ejbHomeGetIndividualCountByPaymentRecords(paymentRecords);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findByPaymentRecordOrderedByStudentName(
			PaymentRecord paymentRecord) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((InvoiceRecordBMPBean) entity)
				.ejbFindByPaymentRecordOrderedByStudentName(paymentRecord);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getPlacementCountForSchoolCategoryAndPeriod(
			String schoolCategoryID, Date period) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((InvoiceRecordBMPBean) entity)
				.ejbHomeGetPlacementCountForSchoolCategoryAndPeriod(
						schoolCategoryID, period);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfHandledChildrenForSchoolTypesAndMonth(
			Collection schoolTypes, CalendarMonth month) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((InvoiceRecordBMPBean) entity)
				.ejbHomeGetNumberOfHandledChildrenForSchoolTypesAndMonth(
						schoolTypes, month);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfInvoicesForStudent(SchoolClassMember student)
			throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((InvoiceRecordBMPBean) entity)
				.ejbHomeGetNumberOfInvoicesForStudent(student);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfInvoicesForStudentWithVUXGradePayment(
			SchoolClassMember student) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((InvoiceRecordBMPBean) entity)
				.ejbHomeGetNumberOfInvoicesForStudentWithVUXGradePayment(student);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfInvoicesForStudentWithVUXPayment(
			SchoolClassMember student) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((InvoiceRecordBMPBean) entity)
				.ejbHomeGetNumberOfInvoicesForStudentWithVUXPayment(student);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findBySchoolClassMember(SchoolClassMember student)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((InvoiceRecordBMPBean) entity)
				.ejbFindBySchoolClassMember(student);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public double getTotalAmountForSchoolTypesAndMonth(Collection schoolTypes,
			CalendarMonth month) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		double theReturn = ((InvoiceRecordBMPBean) entity)
				.ejbHomeGetTotalAmountForSchoolTypesAndMonth(schoolTypes, month);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findByMonthAndCategory(CalendarMonth month,
			String categoryId) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((InvoiceRecordBMPBean) entity)
				.ejbFindByMonthAndCategory(month, categoryId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
