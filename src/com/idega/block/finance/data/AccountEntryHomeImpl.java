package com.idega.block.finance.data;


import com.idega.data.IDOException;
import java.util.Collection;
import javax.ejb.CreateException;
import java.sql.SQLException;
import javax.ejb.FinderException;
import java.sql.Date;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class AccountEntryHomeImpl extends IDOFactory implements
		AccountEntryHome {
	public Class getEntityInterfaceClass() {
		return AccountEntry.class;
	}

	public AccountEntry create() throws CreateException {
		return (AccountEntry) super.createIDO();
	}

	public AccountEntry findByPrimaryKey(Object pk) throws FinderException {
		return (AccountEntry) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findByAccountAndAssessmentRound(Integer accountID,
			Integer assessmentRoundID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountEntryBMPBean) entity)
				.ejbFindByAccountAndAssessmentRound(accountID,
						assessmentRoundID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public double getTotalSumByAccountAndAssessmentRound(Integer accountID,
			Integer assessmentRoundID) throws SQLException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		double theReturn = ((AccountEntryBMPBean) entity)
				.ejbHomeGetTotalSumByAccountAndAssessmentRound(accountID,
						assessmentRoundID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public double getTotalSumByAccount(Integer accountID) throws SQLException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		double theReturn = ((AccountEntryBMPBean) entity)
				.ejbHomeGetTotalSumByAccount(accountID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public double getTotalSumByAccount(Integer accountID, String roundStatus)
			throws SQLException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		double theReturn = ((AccountEntryBMPBean) entity)
				.ejbHomeGetTotalSumByAccount(accountID, roundStatus);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public double getTotalSumByAssessmentRound(Integer roundID)
			throws SQLException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		double theReturn = ((AccountEntryBMPBean) entity)
				.ejbHomeGetTotalSumByAssessmentRound(roundID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findByAssessmentRound(Integer assessmentRoundID)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountEntryBMPBean) entity)
				.ejbFindByAssessmentRound(assessmentRoundID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByAccountAndStatus(Integer accountID, String status,
			Date fromDate, Date toDate, String assessmentStatus)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountEntryBMPBean) entity)
				.ejbFindByAccountAndStatus(accountID, status, fromDate, toDate,
						assessmentStatus);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int countByGroup(Integer groupID) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((AccountEntryBMPBean) entity)
				.ejbHomeCountByGroup(groupID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findUnGrouped(Date from, Date to) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountEntryBMPBean) entity).ejbFindUnGrouped(from,
				to);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByEntryGroup(Integer groupID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountEntryBMPBean) entity)
				.ejbFindByEntryGroup(groupID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Date getMaxDateByAccount(Integer accountID) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Date theReturn = ((AccountEntryBMPBean) entity)
				.ejbHomeGetMaxDateByAccount(accountID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public AccountEntry findByInvoiceNumber(int invoiceNumber)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((AccountEntryBMPBean) entity)
				.ejbFindByInvoiceNumber(invoiceNumber);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findByBatchNumber(int batchNumber) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountEntryBMPBean) entity)
				.ejbFindByBatchNumber(batchNumber);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findInvoicesByBatchNumber(int batchNumber)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountEntryBMPBean) entity)
				.ejbFindInvoicesByBatchNumber(batchNumber);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}