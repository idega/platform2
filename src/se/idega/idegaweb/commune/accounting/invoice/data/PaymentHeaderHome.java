/**
 * 
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.data.IDOLookupException;
import com.idega.util.CalendarMonth;
import com.idega.util.TimePeriod;

/**
 * @author bluebottle
 *
 */
public interface PaymentHeaderHome extends IDOHome {
	public PaymentHeader create() throws javax.ejb.CreateException;

	public PaymentHeader findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#ejbFindBySchoolCategorySchoolPeriod
	 */
	public PaymentHeader findBySchoolCategorySchoolPeriod(School school,
			SchoolCategory schoolCategory, Date period) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#ejbFindBySchoolCategoryAndSchoolAndPeriodAndStatus
	 */
	public PaymentHeader findBySchoolCategoryAndSchoolAndPeriodAndStatus(
			School school, SchoolCategory schoolCategory, TimePeriod period,
			String status) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#ejbFindAllBySchoolCategoryAndSchoolAndPeriodAndStatus
	 */
	public Collection findAllBySchoolCategoryAndSchoolAndPeriodAndStatus(
			School school, SchoolCategory schoolCategory, TimePeriod period,
			String status) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#ejbHomeGetProviderCountForSchoolCategoryAndPeriod
	 */
	public int getProviderCountForSchoolCategoryAndPeriod(
			String schoolCategoryID, Date period) throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#ejbHomeGetPlacementCountForSchoolAndPeriod
	 */
	public int getPlacementCountForSchoolAndPeriod(int schoolID, Date period,
			String schoolCategoryID) throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#ejbFindBySchoolAndSchoolCategoryPKAndStatus
	 */
	public Collection findBySchoolAndSchoolCategoryPKAndStatus(Object schoolPK,
			Object schoolCategoryPK, String status) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#ejbFindBySchoolCategoryAndPeriodForPrivate
	 */
	public Collection findBySchoolCategoryAndPeriodForPrivate(
			SchoolCategory schoolCategory, Date period)
			throws IDOLookupException, EJBException, FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#ejbFindByStatusAndSchoolId
	 */
	public Collection findByStatusAndSchoolId(char status, int schoolID)
			throws EJBException, FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#ejbFindBySchoolCategoryAndSchoolAndPeriod
	 */
	public Collection findBySchoolCategoryAndSchoolAndPeriod(
			String schoolCategory, Integer providerId, Date startPeriod,
			Date endPeriod) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#ejbFindBySchoolCategoryStatusInCommuneWithCommunalManagement
	 */
	public Collection findBySchoolCategoryStatusInCommuneWithCommunalManagement(
			String schoolCategory, char status) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#ejbFindBySchoolCategoryStatusOutsideCommuneOrWithoutCommunalManagement
	 */
	public Collection findBySchoolCategoryStatusOutsideCommuneOrWithoutCommunalManagement(
			String schoolCategory, char status) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#ejbFindBySchoolCategoryAndStatus
	 */
	public Collection findBySchoolCategoryAndStatus(String schoolCategory,
			char status) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#ejbFindBySchoolCategoryAndPeriod
	 */
	public Collection findBySchoolCategoryAndPeriod(String sc, Date period)
			throws EJBException, FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#ejbFindByMonthAndSchoolCategory
	 */
	public Collection findByMonthAndSchoolCategory(CalendarMonth month,
			SchoolCategory schoolCategory) throws FinderException;

}
