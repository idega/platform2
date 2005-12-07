/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import is.idega.idegaweb.member.isi.block.accounting.export.data.Batch;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author bluebottle
 *
 */
public interface FinanceEntryHome extends IDOHome {
	public FinanceEntry create() throws javax.ejb.CreateException;

	public FinanceEntry findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllByAssessmentRound
	 */
	public Collection findAllByAssessmentRound(AssessmentRound round)
			throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllByUser
	 */
	public Collection findAllByUser(User user) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllByUser
	 */
	public Collection findAllByUser(Group club, Group division, User user)
			throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllOpenAssessmentByUser
	 */
	public Collection findAllOpenAssessmentByUser(Group club, Group division,
			User user) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllAssessmentByUser
	 */
	public Collection findAllAssessmentByUser(Group club, Group division,
			User user) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllAssessmentByUser
	 */
	public Collection findAllAssessmentByUser(Group club, Group division,
			User user, IWTimestamp entriesAfter) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllPaymentsByUser
	 */
	public Collection findAllPaymentsByUser(Group club, Group division,
			User user) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate
	 */
	public Collection findAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate(
			Group club, String[] types, Date dateFrom, Date dateTo,
			Collection divisions, Collection groups, String personalID)
			throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllFinanceEntriesByPaymentDateDivisionsAndGroupsOrderedByDivisionGroupAndDate
	 */
	public Collection findAllFinanceEntriesByPaymentDateDivisionsAndGroupsOrderedByDivisionGroupAndDate(
			Group club, String[] types, Collection divisions,
			Collection groups, String personalID) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllByGroupAndPaymentTypeNotInBatch
	 */
	public Collection findAllByGroupAndPaymentTypeNotInBatch(Group group,
			PaymentType type, IWTimestamp dateFrom, IWTimestamp dateTo)
			throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllByPaymentTypeNotInBatch
	 */
	public Collection findAllByPaymentTypeNotInBatch(PaymentType type,
			IWTimestamp dateFrom, IWTimestamp dateTo) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllByPaymentTypesNotInBatch
	 */
	public Collection findAllByPaymentTypesNotInBatch(String[] paymentTypes,
			IWTimestamp dateFrom, IWTimestamp dateTo) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllByBatch
	 */
	public Collection findAllByBatch(Batch batch) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllByBatchID
	 */
	public Collection findAllByBatchID(int batchID) throws FinderException;

}
