package se.idega.idegaweb.commune.accounting.invoice.business;


public interface CheckAmountBusiness extends com.idega.business.IBOService
{
 public void sendCheckAmountLists(com.idega.presentation.IWContext p0,java.lang.String p1) throws java.rmi.RemoteException;
 public void sendCheckAmountLists(com.idega.presentation.IWContext p0,com.idega.idegaweb.IWResourceBundle p1,java.lang.String p2) throws java.rmi.RemoteException;
	public int createInternalCheckAmountList (String schoolCategoryId, Integer providerId, java.sql.Date startPeriod, java.sql.Date endPeriod) throws javax.ejb.FinderException, java.rmi.RemoteException;
}
