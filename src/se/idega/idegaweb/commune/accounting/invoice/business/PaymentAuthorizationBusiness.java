package se.idega.idegaweb.commune.accounting.invoice.business;


public interface PaymentAuthorizationBusiness extends com.idega.business.IBOService
{
 public void authorizePayments(com.idega.presentation.IWContext iwc, com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public java.lang.String getProviderNameForUser(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public boolean hasAuthorizablePayments(com.idega.presentation.IWContext iwc, com.idega.user.data.User p0);
}
