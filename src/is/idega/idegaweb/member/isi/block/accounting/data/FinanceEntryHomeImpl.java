package is.idega.idegaweb.member.isi.block.accounting.data;


import com.idega.user.data.Group;
import is.idega.idegaweb.member.isi.block.accounting.export.data.Batch;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.util.IWTimestamp;
import javax.ejb.FinderException;
import java.sql.Date;
import com.idega.user.data.User;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class FinanceEntryHomeImpl extends IDOFactory implements FinanceEntryHome {
	public Class getEntityInterfaceClass() {
		return FinanceEntry.class;
	}

	public FinanceEntry create() throws CreateException {
		return (FinanceEntry) super.createIDO();
	}

	public FinanceEntry findByPrimaryKey(Object pk) throws FinderException {
		return (FinanceEntry) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByAssessmentRound(AssessmentRound round) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllByAssessmentRound(round);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByUser(User user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByUser(Group club, Group division, User user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllByUser(club, division, user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllOpenAssessmentByUser(Group club, Group division, User user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllOpenAssessmentByUser(club, division, user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllAssessmentByUser(Group club, Group division, User user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllAssessmentByUser(club, division, user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllAssessmentByUser(Group club, Group division, User user, IWTimestamp entriesAfter) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllAssessmentByUser(club, division, user, entriesAfter);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllPaymentsByUser(Group club, Group division, User user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllPaymentsByUser(club, division, user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate(Group club, String[] types, Date dateFrom, Date dateTo, Collection divisions, Collection groups, String personalID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate(club, types, dateFrom, dateTo, divisions, groups, personalID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllFinanceEntriesByPaymentDateDivisionsAndGroupsOrderedByDivisionGroupAndDate(Group club, String[] types, Collection divisions, Collection groups, String personalID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllFinanceEntriesByPaymentDateDivisionsAndGroupsOrderedByDivisionGroupAndDate(club, types, divisions, groups, personalID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByGroupAndPaymentTypeNotInBatch(Group group, PaymentType type, IWTimestamp dateFrom, IWTimestamp dateTo) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllByGroupAndPaymentTypeNotInBatch(group, type, dateFrom, dateTo);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByPaymentTypeNotInBatch(PaymentType type, IWTimestamp dateFrom, IWTimestamp dateTo) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllByPaymentTypeNotInBatch(type, dateFrom, dateTo);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByPaymentTypesNotInBatch(String[] paymentTypes, IWTimestamp dateFrom, IWTimestamp dateTo) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllByPaymentTypesNotInBatch(paymentTypes, dateFrom, dateTo);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByBatch(Batch batch) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllByBatch(batch);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByClubId(int id) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllByClubId(id);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByClubAndDivisionAndGroupAndSerial(Group club, Group division, Group group, int fromSerialNumber) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllByClubAndDivisionAndGroupAndSerial(club, division, group, fromSerialNumber);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByClubAndDivisionAndGroupAndDate(Group club, Group division, Group group, IWTimestamp fromDate) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllByClubAndDivisionAndGroupAndDate(club, division, group, fromDate);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByBatchID(int batchID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FinanceEntryBMPBean) entity).ejbFindAllByBatchID(batchID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}