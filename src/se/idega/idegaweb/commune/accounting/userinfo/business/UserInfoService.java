package se.idega.idegaweb.commune.accounting.userinfo.business;


public interface UserInfoService extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.accounting.userinfo.data.BruttoIncome createBruttoIncome(java.lang.Integer userID,java.lang.Float incomeAmount,java.util.Date validFrom,Integer creatorID)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.userinfo.data.BruttoIncomeHome getBruttoIncomeHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
}
