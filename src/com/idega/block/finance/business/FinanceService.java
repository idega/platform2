/*
 * $Id: FinanceService.java,v 1.5 2004/11/17 22:50:25 aron Exp $
 * Created on 17.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.finance.business;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.finance.data.AccountEntryHome;
import com.idega.block.finance.data.AccountHome;
import com.idega.block.finance.data.AccountInfoHome;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.AccountKeyHome;
import com.idega.block.finance.data.AccountPhoneEntryHome;
import com.idega.block.finance.data.AccountTypeHome;
import com.idega.block.finance.data.AccountUserHome;
import com.idega.block.finance.data.AssessmentRoundHome;
import com.idega.block.finance.data.EntryGroupHome;
import com.idega.block.finance.data.FinanceHandlerInfoHome;
import com.idega.block.finance.data.PaymentTypeHome;
import com.idega.block.finance.data.RoundInfoHome;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.TariffGroup;
import com.idega.block.finance.data.TariffGroupHome;
import com.idega.block.finance.data.TariffHome;
import com.idega.block.finance.data.TariffIndex;
import com.idega.block.finance.data.TariffIndexHome;
import com.idega.block.finance.data.TariffKey;
import com.idega.block.finance.data.TariffKeyHome;
import com.idega.business.IBOService;

/**
 * 
 *  Last modified: $Date: 2004/11/17 22:50:25 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.5 $
 */
public interface FinanceService extends IBOService {
    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getAccountHome
     */
    public AccountHome getAccountHome() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getAccountEntryHome
     */
    public AccountEntryHome getAccountEntryHome() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getAccountKeyHome
     */
    public AccountKeyHome getAccountKeyHome() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getAccountInfoHome
     */
    public AccountInfoHome getAccountInfoHome() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getAccountPhoneEntryHome
     */
    public AccountPhoneEntryHome getAccountPhoneEntryHome()
            throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getAccountTypeHome
     */
    public AccountTypeHome getAccountTypeHome() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getAssessmentRoundHome
     */
    public AssessmentRoundHome getAssessmentRoundHome() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getRoundInfoHome
     */
    public RoundInfoHome getRoundInfoHome() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getTariffHome
     */
    public TariffHome getTariffHome() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getTariffKeyHome
     */
    public TariffKeyHome getTariffKeyHome() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getTariffGroupHome
     */
    public TariffGroupHome getTariffGroupHome() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getEntryGroupHome
     */
    public EntryGroupHome getEntryGroupHome() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getAccountUserHome
     */
    public AccountUserHome getAccountUserHome() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getTariffIndexHome
     */
    public TariffIndexHome getTariffIndexHome() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getPaymentTypeHome
     */
    public PaymentTypeHome getPaymentTypeHome() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getFinanceHandlerInfoHome
     */
    public FinanceHandlerInfoHome getFinanceHandlerInfoHome()
            throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getFinanceHandler
     */
    public FinanceHandler getFinanceHandler(Integer handlerInfoID)
            throws java.rmi.RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getFinanceBusiness
     */
    public AssessmentBusiness getFinanceBusiness() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getAccountBusiness
     */
    public AccountBusiness getAccountBusiness() throws RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#removeAccountKey
     */
    public void removeAccountKey(Integer keyID) throws FinderException,
            RemoteException, RemoveException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#createOrUpdateAccountKey
     */
    public AccountKey createOrUpdateAccountKey(Integer ID, String name,
            String info, Integer tariffKeyID, Integer ordinal,
            Integer categoryID) throws CreateException, RemoteException,
            FinderException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#createOrUpdateTariffKey
     */
    public TariffKey createOrUpdateTariffKey(Integer ID, String name,
            String info, Integer categoryID) throws FinderException,
            RemoteException, CreateException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#createOrUpdateTariff
     */
    public Tariff createOrUpdateTariff(Integer ID, String name, String info,
            String attribute, String index, boolean useIndex,
            Timestamp indexStamp, float Price, Integer accountKeyID,
            Integer tariffGroupID) throws FinderException, RemoteException,
            CreateException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#updateTariffPrice
     */
    public Tariff updateTariffPrice(Integer ID, Float Price,
            Timestamp indexStamp) throws RemoteException, FinderException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#removeTariff
     */
    public void removeTariff(Integer ID) throws FinderException,
            RemoteException, RemoveException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#removeTariffKey
     */
    public void removeTariffKey(Integer ID) throws FinderException,
            RemoteException, RemoveException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#removeTariffIndex
     */
    public void removeTariffIndex(Integer ID) throws FinderException,
            RemoteException, RemoveException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#mapOfTariffIndicesByTypes
     */
    public Map mapOfTariffIndicesByTypes() throws RemoteException,
            FinderException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#createOrUpdateTariffGroup
     */
    public TariffGroup createOrUpdateTariffGroup(Integer ID, String name,
            String info, Integer handlerId, boolean useIndex, Integer categoryId)
            throws CreateException, FinderException, RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#createOrUpdateTariffIndex
     */
    public TariffIndex createOrUpdateTariffIndex(Integer ID, String name,
            String info, String type, float newvalue, float oldvalue,
            Timestamp stamp, Integer categoryId) throws RemoteException,
            CreateException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getAccountTypeFinance
     */
    public String getAccountTypeFinance() throws java.rmi.RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getAccountTypePhone
     */
    public String getAccountTypePhone() throws java.rmi.RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getKeySortedTariffsByAttribute
     */
    public Collection getKeySortedTariffsByAttribute(String attribute)
            throws FinderException, RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#mapOfAccountKeys
     */
    public Map mapOfAccountKeys() throws RemoteException, FinderException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#mapOfTariffKeys
     */
    public Map mapOfTariffKeys() throws RemoteException, FinderException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getAccountBalancePublished
     */
    public double getAccountBalancePublished(Integer accountID)
            throws java.rmi.RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getAccountBalance
     */
    public double getAccountBalance(Integer accountID)
            throws java.rmi.RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getAccountBalance
     */
    public double getAccountBalance(Integer accountID, String roundStatus)
            throws java.rmi.RemoteException;

    /**
     * @see com.idega.block.finance.business.FinanceServiceBean#getAccountLastUpdate
     */
    public Date getAccountLastUpdate(Integer accountID)
            throws java.rmi.RemoteException;

}
