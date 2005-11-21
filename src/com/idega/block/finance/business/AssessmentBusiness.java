/**
 * 
 */
package com.idega.block.finance.business;

import java.util.Date;
import java.util.List;


import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.EntryGroup;
import com.idega.business.IBOService;
import com.idega.util.IWTimestamp;

/**
 * @author bluebottle
 *
 */
public interface AssessmentBusiness extends IBOService {
	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#groupEntriesWithSQL
	 */
	public EntryGroup groupEntriesWithSQL(IWTimestamp from, IWTimestamp to,
			IWTimestamp paymentDate, IWTimestamp dueDate) throws Exception,
			java.rmi.RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#groupEntries
	 */
	public void groupEntries(IWTimestamp from, IWTimestamp to)
			throws Exception, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#getGroupEntryCount
	 */
	public int getGroupEntryCount(EntryGroup entryGroup)
			throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#assessTariffsToAccount
	 */
	public void assessTariffsToAccount(float price, String name, String info,
			Integer accountID, Integer accountKeyID, Date paydate,
			Integer tariffGroupID, Integer financeCategoryID,
			Integer externalID, boolean save, Integer assessmentRound)
			throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#assessTariffsToAccount
	 */
	public void assessTariffsToAccount(Integer[] tariffIds,
			Double[] multiplyFactors, Integer accountID, Date paydate,
			int discount, Integer tariffGroupID, Integer financeCategoryID,
			Integer externalID, Integer assessmentRound)
			throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#assessTariffsToAccount
	 */
	public void assessTariffsToAccount(List tariffs, List multiplyFactors,
			Integer accountID, Date paydate, int discount,
			Integer tariffGroupID, Integer financeCategoryID,
			Integer externalID, Integer assessmentRound)
			throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#createAccountEntry
	 */
	public AccountEntry createAccountEntry(Integer accountID,
			Integer accountKeyID, Integer cashierID, Integer roundID,
			float netto, float VAT, float total, Date paydate, String Name,
			String Info, String status, Integer externalID)
			throws java.rmi.RemoteException, javax.ejb.CreateException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#rollBackAssessment
	 */
	public boolean rollBackAssessment(Integer assessmentRoundId)
			throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.finance.business.AssessmentBusinessBean#publishAssessment
	 */
	public void publishAssessment(Integer roundId)
			throws java.rmi.RemoteException;

}
