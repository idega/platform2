package se.idega.idegaweb.commune.accounting.regulations.business;


public interface RegulationsBusiness extends com.idega.business.IBOService
{
 public void deleteRegulationSpecType(int p0)throws se.idega.idegaweb.commune.accounting.regulations.business.RegulationException, java.rmi.RemoteException;
 public java.util.Collection findAllActivityTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllCommuneBelongingTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllCompanyTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllMainRules() throws java.rmi.RemoteException;
 public java.util.Collection findAllPaymentFlowTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllProviderTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllRegulationSpecTypes()throws se.idega.idegaweb.commune.accounting.regulations.business.RegulationException, java.rmi.RemoteException;
 public java.lang.Object findRegulationSpecType(int p0) throws java.rmi.RemoteException;
 public java.lang.String getActivityTypesAsString() throws java.rmi.RemoteException;
 public java.lang.String getCommuneBelongingsAsString() throws java.rmi.RemoteException;
 public java.lang.String getCompanyTypesAsString() throws java.rmi.RemoteException;
 public java.lang.String getRegulationSpecTypesAsString() throws java.rmi.RemoteException;
 public void saveRegulationSpecType(int p0,java.lang.String p1,int p2)throws se.idega.idegaweb.commune.accounting.regulations.business.RegulationException, java.rmi.RemoteException;
}
