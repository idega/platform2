package se.idega.idegaweb.commune.accounting.regulations.business;


public interface RegulationsBusiness extends com.idega.business.IBOService
{
 public void deleteRegulation(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void deleteRegulationSpecType(int p0)throws se.idega.idegaweb.commune.accounting.regulations.business.RegulationException, java.rmi.RemoteException;
 public java.util.Collection findAllActivityTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllCommuneBelongingTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllCompanyTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllConditionTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllConditionsByRegulation(se.idega.idegaweb.commune.accounting.regulations.data.Regulation p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllDiscountValues() throws java.rmi.RemoteException;
 public java.util.Collection findAllHourIntervals() throws java.rmi.RemoteException;
 public java.util.Collection findAllMainRules() throws java.rmi.RemoteException;
 public java.util.Collection findAllMaxAmounts() throws java.rmi.RemoteException;
 public java.util.Collection findAllOperations() throws java.rmi.RemoteException;
 public java.util.Collection findAllPaymentFlowTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllProviderTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllRegulationSpecTypes()throws se.idega.idegaweb.commune.accounting.regulations.business.RegulationException, java.rmi.RemoteException;
 public java.util.Collection findAllRegulations() throws java.rmi.RemoteException;
 public java.util.Collection findAllSiblingValues() throws java.rmi.RemoteException;
 public java.util.Collection findAllSpecialCalculationTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllVATRules() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.Regulation findRegulation(int p0) throws java.rmi.RemoteException;
 public java.lang.Object findRegulationSpecType(int p0) throws java.rmi.RemoteException;
 public java.util.Collection findRegulationsByPeriod(java.sql.Date p0,java.sql.Date p1) throws java.rmi.RemoteException;
 public java.lang.String replaceToDot(java.lang.String p0) throws java.rmi.RemoteException;
 public void saveRegulationSpecType(int p0,java.lang.String p1,int p2)throws se.idega.idegaweb.commune.accounting.regulations.business.RegulationException, java.rmi.RemoteException;
}
