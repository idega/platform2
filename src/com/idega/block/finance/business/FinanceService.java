package com.idega.block.finance.business;


public interface FinanceService extends com.idega.business.IBOService
{
 public com.idega.block.finance.data.AccountKey createOrUpdateAccountKey(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,java.lang.Integer p3,java.lang.Integer p4)throws javax.ejb.CreateException,java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.finance.data.Tariff createOrUpdateTariff(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,boolean p5,java.sql.Timestamp p6,float p7,java.lang.Integer p8,java.lang.Integer p9)throws javax.ejb.FinderException,java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.finance.data.TariffGroup createOrUpdateTariffGroup(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,java.lang.Integer p3,boolean p4,java.lang.Integer p5)throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.TariffIndex createOrUpdateTariffIndex(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,float p4,float p5,java.sql.Timestamp p6,java.lang.Integer p7)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.finance.data.TariffKey createOrUpdateTariffKey(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,java.lang.Integer p3)throws javax.ejb.FinderException,java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public double getAccountBalance(java.lang.Integer accountID) throws java.rmi.RemoteException;
 public double getAccountBalance(java.lang.Integer accountID,String roundStatus) throws java.rmi.RemoteException;
 public com.idega.block.finance.business.AccountBusiness getAccountBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.AccountEntryHome getAccountEntryHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.AccountHome getAccountHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.AccountInfoHome getAccountInfoHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.AccountKeyHome getAccountKeyHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.AccountPhoneEntryHome getAccountPhoneEntryHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getAccountTypeFinance() throws java.rmi.RemoteException;
 public com.idega.block.finance.data.AccountTypeHome getAccountTypeHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getAccountTypePhone() throws java.rmi.RemoteException;
 public com.idega.block.finance.data.AccountUserHome getAccountUserHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.AssessmentRoundHome getAssessmentRoundHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.EntryGroupHome getEntryGroupHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.business.AssessmentBusiness getFinanceBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.business.FinanceHandler getFinanceHandler(java.lang.Integer p0) throws java.rmi.RemoteException;
 public com.idega.block.finance.data.FinanceHandlerInfoHome getFinanceHandlerInfoHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getKeySortedTariffsByAttribute(java.lang.String p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.PaymentTypeHome getPaymentTypeHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.RoundInfoHome getRoundInfoHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.TariffGroupHome getTariffGroupHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.TariffHome getTariffHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.TariffIndexHome getTariffIndexHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.finance.data.TariffKeyHome getTariffKeyHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map mapOfAccountKeys()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Map mapOfTariffIndicesByTypes()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Map mapOfTariffKeys()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public void removeAccountKey(java.lang.Integer p0)throws javax.ejb.FinderException,java.rmi.RemoteException,javax.ejb.RemoveException, java.rmi.RemoteException;
 public void removeTariff(java.lang.Integer p0)throws javax.ejb.FinderException,java.rmi.RemoteException,javax.ejb.RemoveException, java.rmi.RemoteException;
 public void removeTariffIndex(java.lang.Integer p0)throws javax.ejb.FinderException,java.rmi.RemoteException,javax.ejb.RemoveException, java.rmi.RemoteException;
 public void removeTariffKey(java.lang.Integer p0)throws javax.ejb.FinderException,java.rmi.RemoteException,javax.ejb.RemoveException, java.rmi.RemoteException;
 public com.idega.block.finance.data.Tariff updateTariffPrice(java.lang.Integer p0,java.lang.Float p1,java.sql.Timestamp p2)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
/**
 * @param accountID
 * @return
 */
public java.util.Date getAccountLastUpdate(Integer accountID);

}
