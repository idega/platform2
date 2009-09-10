package com.idega.block.finance.data;


import com.idega.data.IDOException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import java.sql.SQLException;
import javax.ejb.FinderException;
import java.sql.Date;

public interface AccountEntryHome extends IDOHome {
	public AccountEntry create() throws CreateException;

	public AccountEntry findByPrimaryKey(Object pk) throws FinderException;

	public Collection findByAccountAndAssessmentRound(Integer accountID,
			Integer assessmentRoundID) throws FinderException;

	public double getTotalSumByAccountAndAssessmentRound(Integer accountID,
			Integer assessmentRoundID) throws SQLException;

	public double getTotalSumByAccount(Integer accountID) throws SQLException;

	public double getTotalSumByAccount(Integer accountID, String roundStatus)
			throws SQLException;

	public double getTotalSumByAssessmentRound(Integer roundID)
			throws SQLException;

	public Collection findByAssessmentRound(Integer assessmentRoundID)
			throws FinderException;

	public Collection findByAccountAndStatus(Integer accountID, String status,
			Date fromDate, Date toDate, String assessmentStatus)
			throws FinderException;

	public int countByGroup(Integer groupID) throws IDOException;

	public Collection findUnGrouped(Date from, Date to) throws FinderException;

	public Collection findByEntryGroup(Integer groupID) throws FinderException;

	public Date getMaxDateByAccount(Integer accountID) throws IDOException;

	public AccountEntry findByInvoiceNumber(int invoiceNumber)
			throws FinderException;

	public Collection findByBatchNumber(int batchNumber) throws FinderException;

	public Collection findInvoicesByBatchNumber(int batchNumber)
			throws FinderException;
}