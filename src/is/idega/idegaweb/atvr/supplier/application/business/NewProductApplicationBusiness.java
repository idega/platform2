package is.idega.idegaweb.atvr.supplier.application.business;


public interface NewProductApplicationBusiness extends com.idega.business.IBOService
{
 public void confirmApplications(java.lang.String[] p0) throws java.rmi.RemoteException;
 public java.util.Collection getAllApplications() throws java.rmi.RemoteException;
 public java.util.Collection getAllConfirmedApplications() throws java.rmi.RemoteException;
 public java.util.Collection getAllSentToFileApplications() throws java.rmi.RemoteException;
 public java.util.Collection getAllUnconfirmedApplications() throws java.rmi.RemoteException;
 public is.idega.idegaweb.atvr.supplier.application.data.NewProductApplication getNewApplication() throws java.rmi.RemoteException;
 public void insertApplication(is.idega.idegaweb.atvr.supplier.application.data.NewProductApplication p0) throws java.rmi.RemoteException;
 public void markApplicationsAsSent(java.lang.String[] p0) throws java.rmi.RemoteException;
 public void markApplicationsAsSent(java.util.Collection p0) throws java.rmi.RemoteException;
 public boolean storeProductCategory(java.lang.String p0,java.lang.String p1,java.lang.String p2) throws java.rmi.RemoteException;
}
