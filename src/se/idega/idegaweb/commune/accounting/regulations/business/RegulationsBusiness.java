package se.idega.idegaweb.commune.accounting.regulations.business;


public interface RegulationsBusiness extends com.idega.business.IBOService
{
 public java.util.Collection findAllActivityTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllCommuneBelongingTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllCompanyTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllPaymentFlowTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllProviderTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllRegulationSpecTypes() throws java.rmi.RemoteException;
 public java.lang.String getActivityTypesAsString() throws java.rmi.RemoteException;
 public java.lang.String getCommuneBelongingsAsString() throws java.rmi.RemoteException;
 public java.lang.String getCompanyTypesAsString() throws java.rmi.RemoteException;
 public java.lang.String getRegulationSpecTypesAsString() throws java.rmi.RemoteException;
}
