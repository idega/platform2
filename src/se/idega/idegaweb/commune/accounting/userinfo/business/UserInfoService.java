package se.idega.idegaweb.commune.accounting.userinfo.business;

import com.idega.user.data.User;

import se.idega.idegaweb.commune.accounting.userinfo.data.HouseHoldFamily;


public interface UserInfoService extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.accounting.userinfo.data.BruttoIncome createBruttoIncome(java.lang.Integer p0,java.lang.Float p1,java.util.Date p2,java.lang.Integer p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.userinfo.data.InvoiceReceiver createInvoiceReceiver(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.userinfo.data.BruttoIncomeHome getBruttoIncomeHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.userinfo.data.InvoiceReceiverHome getInvoiceReceiverHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public HouseHoldFamily getHouseHoldFamily(User headOfFamily) throws java.rmi.RemoteException;
 public boolean isInvoiceReceiver(int p0) throws java.rmi.RemoteException;
 public boolean isInvoiceReceiver(com.idega.user.data.User p0) throws java.rmi.RemoteException;
	boolean isSameAddress (com.idega.core.location.data.Address adress1, com.idega.core.location.data.Address address2);
	int getSiblingOrder(com.idega.user.data.User child,com.idega.util.IWTimestamp startPeriod) throws java.rmi.RemoteException, SiblingOrderException;
	int getSiblingOrder(com.idega.user.data.User child, java.util.Map siblingOrders,com.idega.util.IWTimestamp startPeriod) throws java.rmi.RemoteException, SiblingOrderException;
	class SiblingOrderException extends Exception{
		SiblingOrderException(String s){
			super(s);
		}
	}
}


