package com.idega.block.finance.business;


import com.idega.block.finance.data.AccountEntry;
import javax.ejb.CreateException;
import com.idega.util.IWTimestamp;
import java.util.Date;
import com.idega.business.IBOService;
import java.util.List;
import com.idega.block.finance.data.EntryGroup;
import java.rmi.RemoteException;

public interface AssessmentBusiness extends IBOService {
	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#groupEntriesWithSQL
	 */
	public EntryGroup groupEntriesWithSQL(IWTimestamp from, IWTimestamp to,
			IWTimestamp paymentDate, IWTimestamp dueDate) throws Exception,
			RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#groupEntries
	 */
	public void groupEntries(IWTimestamp from, IWTimestamp to)
			throws Exception, RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#getGroupEntryCount
	 */
	public int getGroupEntryCount(EntryGroup entryGroup) throws RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#assessTariffsToAccount
	 */
	public void assessTariffsToAccount(float price, String name, String info,
			Integer accountID, Integer accountKeyID, Date paydate,
			Integer tariffGroupID, Integer financeCategoryID,
			Integer externalID, boolean save, Integer assessmentRound)
			throws CreateException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#assessTariffsToAccount
	 */
	public void assessTariffsToAccount(Integer[] tariffIds,
			Double[] multiplyFactors, Integer accountID, Date paydate,
			int discount, Integer tariffGroupID, Integer financeCategoryID,
			Integer externalID, Integer assessmentRound)
			throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#assessTariffsToAccount
	 */
	public void assessTariffsToAccount(List tariffs, List multiplyFactors,
			Integer accountID, Date paydate, int discount,
			Integer tariffGroupID, Integer financeCategoryID,
			Integer externalID, Integer assessmentRound) throws RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#createAccountEntry
	 */
	public AccountEntry createAccountEntry(Integer accountID,
			Integer accountKeyID, Integer cashierID, Integer roundID,
			float netto, float VAT, float total, Date paydate, String Name,
			String Info, String status, Integer externalID)
			throws RemoteException, CreateException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#createAccountEntry
	 */
	public AccountEntry createAccountEntry(Integer accountID,
			Integer accountKeyID, Integer cashierID, Integer roundID,
			float netto, float VAT, float total, Date paydate, String Name,
			String Info, String status, Integer externalID, String division)
			throws RemoteException, CreateException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#rollBackAssessment
	 */
	public boolean rollBackAssessment(Integer assessmentRoundId)
			throws RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#publishAssessment
	 */
	public void publishAssessment(Integer roundId) throws RemoteException;
}