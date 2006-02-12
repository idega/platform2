/**
 * 
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;

import com.idega.data.IDOException;
import com.idega.data.IDOFactory;
import com.idega.util.CalendarMonth;

/**
 * @author bluebottle
 *
 */
public class PaymentRecordHomeImpl extends IDOFactory implements
		PaymentRecordHome {
	protected Class getEntityInterfaceClass() {
		return PaymentRecord.class;
	}

	public PaymentRecord create() throws javax.ejb.CreateException {
		return (PaymentRecord) super.createIDO();
	}

	public PaymentRecord findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (PaymentRecord) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findByPaymentHeader(PaymentHeader paymentHeader)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentRecordBMPBean) entity)
				.ejbFindByPaymentHeader(paymentHeader);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByPaymentHeaders(Collection headers)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentRecordBMPBean) entity)
				.ejbFindByPaymentHeaders(headers);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public PaymentRecord findByPostingStrings(String ownPostingString,
			String doublePostingString) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PaymentRecordBMPBean) entity).ejbFindByPostingStrings(
				ownPostingString, doublePostingString);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public PaymentRecord findByPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth(
			String ownPostingString, String doublePostingString,
			String ruleSpecType, String text, CalendarMonth month)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PaymentRecordBMPBean) entity)
				.ejbFindByPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth(
						ownPostingString, doublePostingString, ruleSpecType,
						text, month);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public PaymentRecord findByPaymentHeaderAndPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth(
			PaymentHeader header, String ownPostingString,
			String doublePostingString, String ruleSpecType, String text,
			CalendarMonth month) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PaymentRecordBMPBean) entity)
				.ejbFindByPaymentHeaderAndPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth(
						header, ownPostingString, doublePostingString,
						ruleSpecType, text, month);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public PaymentRecord findByPaymentHeaderAndPostingStringsAndVATRuleRegulationAndPaymentTextAndMonth(
			PaymentHeader pHeader, String ownPostingString,
			String doublePostingString, Regulation vatRuleRegulation,
			String text, CalendarMonth month) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PaymentRecordBMPBean) entity)
				.ejbFindByPaymentHeaderAndPostingStringsAndVATRuleRegulationAndPaymentTextAndMonth(
						pHeader, ownPostingString, doublePostingString,
						vatRuleRegulation, text, month);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public PaymentRecord findByPostingStringsAndVATRuleRegulationAndPaymentTextAndMonthAndStatus(
			String ownPostingString, String doublePostingString,
			Regulation vatRuleRegulation, String text, CalendarMonth month,
			char status) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PaymentRecordBMPBean) entity)
				.ejbFindByPostingStringsAndVATRuleRegulationAndPaymentTextAndMonthAndStatus(
						ownPostingString, doublePostingString,
						vatRuleRegulation, text, month, status);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findByMonth(CalendarMonth month) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentRecordBMPBean) entity)
				.ejbFindByMonth(month);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByMonthAndCategory(CalendarMonth month,
			String categoryId) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentRecordBMPBean) entity)
				.ejbFindByMonthAndCategory(month, categoryId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getCountForMonthAndStatusLH(CalendarMonth month)
			throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((PaymentRecordBMPBean) entity)
				.ejbHomeGetCountForMonthAndStatusLH(month);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getCountForMonthCategoryAndStatusLH(CalendarMonth month,
			String category) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((PaymentRecordBMPBean) entity)
				.ejbHomeGetCountForMonthCategoryAndStatusLH(month, category);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getCountForMonthCategoryAndStatusLHorT(CalendarMonth month,
			String category) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((PaymentRecordBMPBean) entity)
				.ejbHomeGetCountForMonthCategoryAndStatusLHorT(month, category);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getPlacementCountForSchoolCategoryAndMonth(
			String schoolCategoryID, CalendarMonth month) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((PaymentRecordBMPBean) entity)
				.ejbHomeGetPlacementCountForSchoolCategoryAndMonth(
						schoolCategoryID, month);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getPlacementCountForSchoolIdAndDateAndSchoolCategory(
			int schoolID, Date period, String schoolCategoryID)
			throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((PaymentRecordBMPBean) entity)
				.ejbHomeGetPlacementCountForSchoolIdAndDateAndSchoolCategory(
						schoolID, period, schoolCategoryID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getTotalVATAmountForPaymentHeaderAndMonthAndVATRuleRegulation(
			PaymentHeader ph, CalendarMonth month, Regulation vatRuleRegulation)
			throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((PaymentRecordBMPBean) entity)
				.ejbHomeGetTotalVATAmountForPaymentHeaderAndMonthAndVATRuleRegulation(
						ph, month, vatRuleRegulation);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getTotAmountForSchoolCategoryAndPeriod(String schoolCategoryID,
			Date period) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((PaymentRecordBMPBean) entity)
				.ejbHomeGetTotAmountForSchoolCategoryAndPeriod(
						schoolCategoryID, period);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getTotAmountForProviderAndPeriod(int providerID, Date period,
			String schoolCategoryID) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((PaymentRecordBMPBean) entity)
				.ejbHomeGetTotAmountForProviderAndPeriod(providerID, period,
						schoolCategoryID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

}
