package se.idega.idegaweb.commune.accounting.invoice.business;


public interface CheckAmountBusiness extends com.idega.business.IBOService
{
 public java.util.Map sendCheckAmountLists(com.idega.user.data.User p0,java.lang.String p2) throws java.rmi.RemoteException;
	public int createInternalCheckAmountList (String schoolCategoryId, Integer providerId, java.sql.Date startPeriod, java.sql.Date endPeriod) throws javax.ejb.FinderException, java.rmi.RemoteException;
}
