package se.idega.idegaweb.commune.accounting.regulations.business;



public interface RegulationsBusiness extends com.idega.business.IBOService
{
 public void deleteRegulation(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void deleteRegulationSpecType(int p0)throws se.idega.idegaweb.commune.accounting.regulations.business.RegulationException, java.rmi.RemoteException;
 public java.util.Collection findAllActivityTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllAgeIntervals() throws java.rmi.RemoteException;
 public java.util.Collection findAllCommuneBelongingTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllCompanyTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllConditionSelections(java.lang.String p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllConditionTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllConditionsByRegulation(se.idega.idegaweb.commune.accounting.regulations.data.Regulation p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllConditionsByRegulationID(int p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllDiscountValues() throws java.rmi.RemoteException;
 public java.util.Collection findAllHourIntervals() throws java.rmi.RemoteException;
 public java.util.Collection findAllMainRules() throws java.rmi.RemoteException;
 public java.util.Collection findAllMaxAmounts() throws java.rmi.RemoteException;
 public java.util.Collection findAllPaymentFlowTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllProviderTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllRegulationSpecTypes()throws se.idega.idegaweb.commune.accounting.regulations.business.RegulationException, java.rmi.RemoteException;
 public java.util.Collection findAllRegulations() throws java.rmi.RemoteException;
 public java.util.Collection findAllSchoolYearIntervals() throws java.rmi.RemoteException;
 public java.util.Collection findAllSiblingValues() throws java.rmi.RemoteException;
 public java.util.Collection findAllSpecialCalculationTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllVATRuleRegulations() throws java.rmi.RemoteException;
 public java.lang.Object findConditionByRegulationAndIndex(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.Regulation findRegulation(int p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType  findRegulationSpecType(int p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType  findRegulationSpecType(String name) throws java.rmi.RemoteException;
 public java.util.Collection findRegulationsByPeriod(java.sql.Date p0,java.sql.Date p1,java.lang.String p2,int p3,int p4) throws java.rmi.RemoteException;
 public java.util.Collection findRegulationsByPeriod(java.sql.Date p0,java.sql.Date p1) throws java.rmi.RemoteException;
 public java.util.Collection getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(java.lang.String p0,java.lang.String p1,java.sql.Date p2,java.lang.String p3,java.lang.String p4,java.util.Collection p5) throws java.rmi.RemoteException;
 public java.lang.String getBundleIdentifier() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(java.lang.String p0,java.lang.String p1,java.sql.Date p2,java.util.Collection p3,java.lang.String p4,int p5,se.idega.idegaweb.commune.accounting.childcare.data.ChildCareContract p6) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(java.lang.String p0,java.lang.String p1,java.sql.Date p2,java.lang.String p3,java.lang.String p4,java.util.Collection p5,float p6,se.idega.idegaweb.commune.accounting.childcare.data.ChildCareContract p7, com.idega.block.school.data.SchoolClassMember placement)throws se.idega.idegaweb.commune.accounting.regulations.business.RegulationException,se.idega.idegaweb.commune.accounting.regulations.business.MissingFlowTypeException,se.idega.idegaweb.commune.accounting.regulations.business.MissingConditionTypeException,se.idega.idegaweb.commune.accounting.regulations.business.MissingRegSpecTypeException,se.idega.idegaweb.commune.accounting.regulations.business.TooManyRegulationsException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail getPostingDetailForContract(float p0,se.idega.idegaweb.commune.accounting.childcare.data.ChildCareContract p1,se.idega.idegaweb.commune.accounting.regulations.data.Regulation p2,java.sql.Date p3,java.util.Collection p4,se.idega.idegaweb.commune.accounting.invoice.business.PlacementTimes p5)throws se.idega.idegaweb.commune.accounting.regulations.business.BruttoIncomeException,se.idega.idegaweb.commune.accounting.regulations.business.LowIncomeException,se.idega.idegaweb.commune.accounting.regulations.business.RegulationException,se.idega.idegaweb.commune.accounting.regulations.business.MissingFlowTypeException,se.idega.idegaweb.commune.accounting.regulations.business.MissingConditionTypeException,se.idega.idegaweb.commune.accounting.regulations.business.MissingRegSpecTypeException,se.idega.idegaweb.commune.accounting.regulations.business.TooManyRegulationsException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail getPostingDetailForPlacement(float p0,com.idega.block.school.data.SchoolClassMember p1,se.idega.idegaweb.commune.accounting.regulations.data.Regulation p2,java.sql.Date p3,java.util.Collection p4,se.idega.idegaweb.commune.accounting.invoice.business.PlacementTimes p5)throws se.idega.idegaweb.commune.accounting.regulations.business.BruttoIncomeException,se.idega.idegaweb.commune.accounting.regulations.business.LowIncomeException,se.idega.idegaweb.commune.accounting.regulations.business.RegulationException,se.idega.idegaweb.commune.accounting.regulations.business.MissingFlowTypeException,se.idega.idegaweb.commune.accounting.regulations.business.MissingConditionTypeException,se.idega.idegaweb.commune.accounting.regulations.business.MissingRegSpecTypeException,se.idega.idegaweb.commune.accounting.regulations.business.TooManyRegulationsException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolType getSchoolType(se.idega.idegaweb.commune.accounting.regulations.data.Regulation p0) throws java.rmi.RemoteException;
 public java.util.Collection getYesNo() throws java.rmi.RemoteException;
 public java.lang.String replaceToDot(java.lang.String p0) throws java.rmi.RemoteException;
 public void saveCondition(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3)throws se.idega.idegaweb.commune.accounting.regulations.business.RegulationException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int saveRegulation(java.lang.String p0,java.sql.Date p1,java.sql.Date p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,java.lang.String p9,java.lang.String p10,java.lang.String p11,java.lang.String p12,java.lang.String p13,java.lang.String p14,java.lang.String p15)throws se.idega.idegaweb.commune.accounting.regulations.business.RegulationException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void saveRegulationSpecType(int p0,java.lang.String p1,int p2)throws se.idega.idegaweb.commune.accounting.regulations.business.RegulationException, java.rmi.RemoteException;
}
