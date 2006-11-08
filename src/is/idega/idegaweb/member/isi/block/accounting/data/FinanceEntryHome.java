package is.idega.idegaweb.member.isi.block.accounting.data;


import com.idega.user.data.Group;
import is.idega.idegaweb.member.isi.block.accounting.export.data.Batch;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import com.idega.util.IWTimestamp;
import javax.ejb.FinderException;
import java.sql.Date;
import com.idega.user.data.User;

public interface FinanceEntryHome extends IDOHome {
	public FinanceEntry create() throws CreateException;

	public FinanceEntry findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAllByAssessmentRound(AssessmentRound round) throws FinderException;

	public Collection findAllByUser(User user) throws FinderException;

	public Collection findAllByUser(Group club, Group division, User user) throws FinderException;

	public Collection findAllOpenAssessmentByUser(Group club, Group division, User user) throws FinderException;

	public Collection findAllAssessmentByUser(Group club, Group division, User user) throws FinderException;

	public Collection findAllAssessmentByUser(Group club, Group division, User user, IWTimestamp entriesAfter) throws FinderException;

	public Collection findAllPaymentsByUser(Group club, Group division, User user) throws FinderException;

	public Collection findAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate(Group club, String[] types, Date dateFrom, Date dateTo, Collection divisions, Collection groups, String personalID) throws FinderException;

	public Collection findAllFinanceEntriesByPaymentDateDivisionsAndGroupsOrderedByDivisionGroupAndDate(Group club, String[] types, Collection divisions, Collection groups, String personalID) throws FinderException;

	public Collection findAllByGroupAndPaymentTypeNotInBatch(Group group, PaymentType type, IWTimestamp dateFrom, IWTimestamp dateTo) throws FinderException;

	public Collection findAllByPaymentTypeNotInBatch(PaymentType type, IWTimestamp dateFrom, IWTimestamp dateTo) throws FinderException;

	public Collection findAllByPaymentTypesNotInBatch(String[] paymentTypes, IWTimestamp dateFrom, IWTimestamp dateTo) throws FinderException;

	public Collection findAllByBatch(Batch batch) throws FinderException;

	public Collection findAllByClubId(int id) throws FinderException;

	public Collection findAllByBatchID(int batchID) throws FinderException;
}