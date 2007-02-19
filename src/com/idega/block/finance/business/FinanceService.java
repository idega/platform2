package com.idega.block.finance.business;


import com.idega.block.finance.data.FinanceHandlerInfoHome;
import javax.ejb.CreateException;
import com.idega.block.finance.data.RoundInfoHome;
import com.idega.block.finance.data.TariffGroupHome;
import com.idega.block.finance.data.AccountHome;
import com.idega.block.finance.data.AccountPhoneEntryHome;
import com.idega.block.finance.data.PaymentTypeHome;
import com.idega.block.finance.data.AccountTypeHome;
import com.idega.block.finance.data.TariffKey;
import com.idega.block.finance.data.AccountUserHome;
import com.idega.block.finance.data.AccountKeyHome;
import com.idega.business.IBOService;
import com.idega.block.finance.data.AccountEntryHome;
import com.idega.block.finance.data.AssessmentRoundHome;
import com.idega.block.finance.data.EntryGroupHome;
import com.idega.block.finance.data.TariffIndexHome;
import com.idega.block.finance.data.TariffKeyHome;
import java.util.Map;
import com.idega.block.finance.data.AccountInfoHome;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.TariffGroup;
import java.util.Date;
import com.idega.block.finance.data.AccountKey;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import com.idega.block.finance.data.TariffIndex;
import javax.ejb.FinderException;
import com.idega.block.finance.data.TariffHome;
import javax.ejb.RemoveException;

public interface FinanceService extends IBOService {
	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getAccountHome
	 */
	public AccountHome getAccountHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getAccountEntryHome
	 */
	public AccountEntryHome getAccountEntryHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getAccountKeyHome
	 */
	public AccountKeyHome getAccountKeyHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getAccountInfoHome
	 */
	public AccountInfoHome getAccountInfoHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getAccountPhoneEntryHome
	 */
	public AccountPhoneEntryHome getAccountPhoneEntryHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getAccountTypeHome
	 */
	public AccountTypeHome getAccountTypeHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getAssessmentRoundHome
	 */
	public AssessmentRoundHome getAssessmentRoundHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getRoundInfoHome
	 */
	public RoundInfoHome getRoundInfoHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getTariffHome
	 */
	public TariffHome getTariffHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getTariffKeyHome
	 */
	public TariffKeyHome getTariffKeyHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getTariffGroupHome
	 */
	public TariffGroupHome getTariffGroupHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getEntryGroupHome
	 */
	public EntryGroupHome getEntryGroupHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getAccountUserHome
	 */
	public AccountUserHome getAccountUserHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getTariffIndexHome
	 */
	public TariffIndexHome getTariffIndexHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getPaymentTypeHome
	 */
	public PaymentTypeHome getPaymentTypeHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getFinanceHandlerInfoHome
	 */
	public FinanceHandlerInfoHome getFinanceHandlerInfoHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getFinanceHandler
	 */
	public FinanceHandler getFinanceHandler(Integer handlerInfoID) throws RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getFinanceBusiness
	 */
	public AssessmentBusiness getFinanceBusiness() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getAccountBusiness
	 */
	public AccountBusiness getAccountBusiness() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#removeAccountKey
	 */
	public void removeAccountKey(Integer keyID) throws FinderException, RemoteException, RemoveException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#createOrUpdateAccountKey
	 */
	public AccountKey createOrUpdateAccountKey(Integer ID, String name, String info, Integer tariffKeyID, Integer ordinal, Integer categoryID) throws CreateException, RemoteException, FinderException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#createOrUpdateTariffKey
	 */
	public TariffKey createOrUpdateTariffKey(Integer ID, String name, String info, Integer categoryID) throws FinderException, RemoteException, CreateException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#createOrUpdateTariff
	 */
	public Tariff createOrUpdateTariff(Integer ID, String name, String info, String attribute, String index, boolean useIndex, Timestamp indexStamp, float Price, Integer accountKeyID, Integer tariffGroupID) throws FinderException, RemoteException, CreateException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#updateTariffPrice
	 */
	public Tariff updateTariffPrice(Integer ID, Float Price, Timestamp indexStamp) throws RemoteException, FinderException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#removeTariff
	 */
	public void removeTariff(Integer ID) throws FinderException, RemoteException, RemoveException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#removeTariffKey
	 */
	public void removeTariffKey(Integer ID) throws FinderException, RemoteException, RemoveException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#removeTariffIndex
	 */
	public void removeTariffIndex(Integer ID) throws FinderException, RemoteException, RemoveException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#mapOfTariffIndicesByTypes
	 */
	public Map mapOfTariffIndicesByTypes() throws RemoteException, FinderException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#createOrUpdateTariffGroup
	 */
	public TariffGroup createOrUpdateTariffGroup(Integer ID, String name, String info, Integer handlerId, boolean useIndex, Integer categoryId) throws CreateException, FinderException, RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#createOrUpdateTariffIndex
	 */
	public TariffIndex createOrUpdateTariffIndex(Integer ID, String name, String info, String type, double newvalue, double oldvalue, Timestamp stamp, Integer categoryId) throws RemoteException, CreateException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getAccountTypeFinance
	 */
	public String getAccountTypeFinance() throws RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getAccountTypePhone
	 */
	public String getAccountTypePhone() throws RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getKeySortedTariffsByAttribute
	 */
	public Collection getKeySortedTariffsByAttribute(String attribute) throws FinderException, RemoteException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#mapOfAccountKeys
	 */
	public Map mapOfAccountKeys() throws RemoteException, FinderException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#mapOfTariffKeys
	 */
	public Map mapOfTariffKeys() throws RemoteException, FinderException, RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getAccountBalancePublished
	 */
	public double getAccountBalancePublished(Integer accountID) throws RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getAccountBalance
	 */
	public double getAccountBalance(Integer accountID) throws RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getAccountBalance
	 */
	public double getAccountBalance(Integer accountID, String roundStatus) throws RemoteException;

	/**
	 * @see com.idega.block.finance.business.FinanceServiceBean#getAccountLastUpdate
	 */
	public Date getAccountLastUpdate(Integer accountID) throws RemoteException;
}