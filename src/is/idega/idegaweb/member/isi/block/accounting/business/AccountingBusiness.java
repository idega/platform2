package is.idega.idegaweb.member.isi.block.accounting.business;


public interface AccountingBusiness extends com.idega.business.IBOService
{
 public boolean doAssessment(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.Group p2,com.idega.util.IWTimestamp p3) throws java.rmi.RemoteException;
 public java.util.Collection findAllCreditCardContractByClub(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllCreditCardType() throws java.rmi.RemoteException;
 public java.util.Collection findAllTariffByClub(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllTariffTypeByClub(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public boolean insertCreditCardContract(com.idega.user.data.Group p0,com.idega.user.data.Group p1,java.lang.String p2,is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType p3) throws java.rmi.RemoteException;
 public boolean insertCreditCardContract(com.idega.user.data.Group p0,java.lang.String p1,java.lang.String p2,java.lang.String p3) throws java.rmi.RemoteException;
 public boolean insertTariff(com.idega.user.data.Group p0,com.idega.user.data.Group p1,is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType p2,java.lang.String p3,float p4,java.sql.Date p5,java.sql.Date p6) throws java.rmi.RemoteException;
 public boolean insertTariff(com.idega.user.data.Group p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.sql.Date p5,java.sql.Date p6) throws java.rmi.RemoteException;
 public boolean insertTariffType(java.lang.String p0,java.lang.String p1,java.lang.String p2,com.idega.user.data.Group p3) throws java.rmi.RemoteException;
}
