package se.idega.idegaweb.commune.accounting.userinfo.business;

import java.rmi.RemoteException;

import com.idega.user.data.User;
import is.idega.block.family.business.NoCustodianFound;
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
	int getSiblingOrder(com.idega.user.data.User child,com.idega.util.TimePeriod period) throws java.rmi.RemoteException, SiblingOrderException;
	int getSiblingOrder(com.idega.user.data.User child, java.util.Map siblingOrders,com.idega.util.TimePeriod period) throws java.rmi.RemoteException, SiblingOrderException;
	java.util.Collection getCustodiansAndTheirPartners(User child) throws RemoteException, NoCustodianFound;
	java.util.Collection getCustodiansAndTheirPartnersAtSameAddress(User child) throws RemoteException, NoCustodianFound;
}


