package is.idega.idegaweb.member.isi.block.accounting.business;

import com.idega.user.business.UserGroupPlugInBusiness;


public interface AccountingBusiness extends com.idega.business.IBOService, UserGroupPlugInBusiness
{
 public boolean deleteAssessmentRound(java.lang.String[] p0) throws java.rmi.RemoteException;
 public boolean deleteContract(java.lang.String[] p0) throws java.rmi.RemoteException;
 public boolean deleteCreditCards(java.lang.String[] p0) throws java.rmi.RemoteException;
 public boolean deleteTariff(java.lang.String[] p0) throws java.rmi.RemoteException;
 public boolean deleteTariffType(java.lang.String[] p0) throws java.rmi.RemoteException;
 public boolean doAssessment(java.lang.String p0,com.idega.user.data.Group p1,com.idega.user.data.Group p2,java.lang.String p3,com.idega.user.data.User p4,boolean p5,java.lang.String[] p6,java.sql.Timestamp p7,java.sql.Timestamp p8) throws java.rmi.RemoteException;
 public void equalizeEntries(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2,double p3) throws java.rmi.RemoteException;
 public java.util.Collection findAllAssessmentRoundByClubAndDivision(com.idega.user.data.Group p0,com.idega.user.data.Group p1) throws java.rmi.RemoteException;
 public java.util.Collection findAllCreditCardContractByClub(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllCreditCardType() throws java.rmi.RemoteException;
 public java.util.Collection findAllOpenAssessmentEntriesByUserGroupAndDivision(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2) throws java.rmi.RemoteException;
 public java.util.Collection findAllPaymentEntriesByUserGroupAndDivision(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2) throws java.rmi.RemoteException;
 public java.util.Collection findAllPaymentTypes() throws java.rmi.RemoteException;
 public java.util.Collection findAllTariffByClub(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllTariffByClubAndDivision(com.idega.user.data.Group p0,com.idega.user.data.Group p1) throws java.rmi.RemoteException;
 public java.util.Collection findAllTariffTypeByClub(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllUsersCreditCards(com.idega.user.data.Group p0,com.idega.user.data.User p1,com.idega.user.data.Group p2) throws java.rmi.RemoteException;
 public java.util.Collection findAllValidTariffByGroup(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllValidTariffByGroup(java.lang.String p0) throws java.rmi.RemoteException;
 public com.idega.user.data.Group findClubForGroup(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public com.idega.user.data.Group findDivisionForGroup(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public java.util.Collection getFinanceEntriesByDateIntervalDivisionsAndGroups(com.idega.user.data.Group p0,java.lang.String[] p1,java.sql.Date p2,java.sql.Date p3,java.util.Collection p4,java.util.Collection p5,java.lang.String p6) throws java.rmi.RemoteException;
 public java.util.Collection getFinanceEntriesByPaymentDateDivisionsAndGroups(com.idega.user.data.Group p0,java.lang.String[] p1,java.util.Collection p2,java.util.Collection p3,java.lang.String p4) throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry getFinanceEntryByPrimaryKey(java.lang.Integer p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound insertAssessmentRound(java.lang.String p0,com.idega.user.data.Group p1,com.idega.user.data.Group p2,com.idega.user.data.Group p3,com.idega.user.data.User p4,java.sql.Timestamp p5,java.sql.Timestamp p6,boolean p7,java.sql.Timestamp p8,java.sql.Timestamp p9) throws java.rmi.RemoteException;
 public boolean insertCreditCard(com.idega.user.data.Group p0,com.idega.user.data.Group p1,is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,com.idega.user.data.User p6) throws java.rmi.RemoteException;
 public boolean insertCreditCard(com.idega.user.data.Group p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,com.idega.user.data.User p6) throws java.rmi.RemoteException;
 public boolean insertCreditCardContract(com.idega.user.data.Group p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4) throws java.rmi.RemoteException;
 public boolean insertCreditCardContract(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.Group p2,java.lang.String p3,is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType p4) throws java.rmi.RemoteException;
 public boolean insertManualAssessment(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,com.idega.user.data.User p7,java.sql.Timestamp p8) throws java.rmi.RemoteException;
 public boolean insertManualAssessment(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2,com.idega.user.data.Group p3,is.idega.idegaweb.member.isi.block.accounting.data.ClubTariff p4,double p5,java.lang.String p6,com.idega.user.data.User p7,java.sql.Timestamp p8) throws java.rmi.RemoteException;
 public boolean insertPayment(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2,java.lang.String p3,is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType p4,com.idega.util.IWTimestamp p5,com.idega.util.IWTimestamp p6,int p7,is.idega.idegaweb.member.isi.block.accounting.data.PaymentType p8,int[] p9,com.idega.user.data.User p10,java.util.Map p11,com.idega.idegaweb.IWUserContext p12) throws java.rmi.RemoteException;
 public boolean insertPayment(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2,java.lang.String p3,java.lang.String p4,com.idega.util.IWTimestamp p5,com.idega.util.IWTimestamp p6,int p7,java.lang.String p8,java.lang.String[] p9,com.idega.user.data.User p10,java.util.Map p11,com.idega.idegaweb.IWUserContext p12) throws java.rmi.RemoteException;
 public boolean insertPayment(is.idega.idegaweb.member.isi.block.accounting.data.PaymentType p0,int p1,com.idega.user.data.User p2,java.util.Map p3,com.idega.idegaweb.IWUserContext p4) throws java.rmi.RemoteException;
 public boolean insertPayment(java.lang.String p0,java.lang.String p1,com.idega.user.data.User p2,java.util.Map p3,com.idega.idegaweb.IWUserContext p4) throws java.rmi.RemoteException;
 public boolean insertTariff(com.idega.user.data.Group p0,com.idega.user.data.Group p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.sql.Date p6,java.sql.Date p7,boolean p8,java.lang.String p9) throws java.rmi.RemoteException;
 public boolean insertTariff(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.Group p2,is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType p3,java.lang.String p4,double p5,java.sql.Date p6,java.sql.Date p7,boolean p8,java.lang.String p9,java.util.List p10) throws java.rmi.RemoteException;
 public boolean insertTariffType(java.lang.String p0,java.lang.String p1,java.lang.String p2,com.idega.user.data.Group p3) throws java.rmi.RemoteException;
}
