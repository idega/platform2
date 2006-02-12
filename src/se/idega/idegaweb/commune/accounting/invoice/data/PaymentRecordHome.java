/**
 * 
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;

import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.util.CalendarMonth;

/**
 * @author bluebottle
 *
 */
public interface PaymentRecordHome extends IDOHome {
	public PaymentRecord create() throws javax.ejb.CreateException;

	public PaymentRecord findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbFindByPaymentHeader
	 */
	public Collection findByPaymentHeader(PaymentHeader paymentHeader)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbFindByPaymentHeaders
	 */
	public Collection findByPaymentHeaders(Collection headers)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbFindByPostingStrings
	 */
	public PaymentRecord findByPostingStrings(String ownPostingString,
			String doublePostingString) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbFindByPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth
	 */
	public PaymentRecord findByPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth(
			String ownPostingString, String doublePostingString,
			String ruleSpecType, String text, CalendarMonth month)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbFindByPaymentHeaderAndPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth
	 */
	public PaymentRecord findByPaymentHeaderAndPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth(
			PaymentHeader header, String ownPostingString,
			String doublePostingString, String ruleSpecType, String text,
			CalendarMonth month) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbFindByPaymentHeaderAndPostingStringsAndVATRuleRegulationAndPaymentTextAndMonth
	 */
	public PaymentRecord findByPaymentHeaderAndPostingStringsAndVATRuleRegulationAndPaymentTextAndMonth(
			PaymentHeader pHeader, String ownPostingString,
			String doublePostingString, Regulation vatRuleRegulation,
			String text, CalendarMonth month) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbFindByPostingStringsAndVATRuleRegulationAndPaymentTextAndMonthAndStatus
	 */
	public PaymentRecord findByPostingStringsAndVATRuleRegulationAndPaymentTextAndMonthAndStatus(
			String ownPostingString, String doublePostingString,
			Regulation vatRuleRegulation, String text, CalendarMonth month,
			char status) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbFindByMonth
	 */
	public Collection findByMonth(CalendarMonth month) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbFindByMonthAndCategory
	 */
	public Collection findByMonthAndCategory(CalendarMonth month,
			String categoryId) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbHomeGetCountForMonthAndStatusLH
	 */
	public int getCountForMonthAndStatusLH(CalendarMonth month)
			throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbHomeGetCountForMonthCategoryAndStatusLH
	 */
	public int getCountForMonthCategoryAndStatusLH(CalendarMonth month,
			String category) throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbHomeGetCountForMonthCategoryAndStatusLHorT
	 */
	public int getCountForMonthCategoryAndStatusLHorT(CalendarMonth month,
			String category) throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbHomeGetPlacementCountForSchoolCategoryAndMonth
	 */
	public int getPlacementCountForSchoolCategoryAndMonth(
			String schoolCategoryID, CalendarMonth month) throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbHomeGetPlacementCountForSchoolIdAndDateAndSchoolCategory
	 */
	public int getPlacementCountForSchoolIdAndDateAndSchoolCategory(
			int schoolID, Date period, String schoolCategoryID)
			throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbHomeGetTotalVATAmountForPaymentHeaderAndMonthAndVATRuleRegulation
	 */
	public int getTotalVATAmountForPaymentHeaderAndMonthAndVATRuleRegulation(
			PaymentHeader ph, CalendarMonth month, Regulation vatRuleRegulation)
			throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbHomeGetTotAmountForSchoolCategoryAndPeriod
	 */
	public int getTotAmountForSchoolCategoryAndPeriod(String schoolCategoryID,
			Date period) throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordBMPBean#ejbHomeGetTotAmountForProviderAndPeriod
	 */
	public int getTotAmountForProviderAndPeriod(int providerID, Date period,
			String schoolCategoryID) throws IDOException;

}
