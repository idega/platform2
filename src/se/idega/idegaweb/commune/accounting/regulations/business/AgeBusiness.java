package se.idega.idegaweb.commune.accounting.regulations.business;


public interface AgeBusiness extends com.idega.business.IBOService
{
 public void deleteAgeRegulation(int p0)throws se.idega.idegaweb.commune.accounting.regulations.business.AgeException, java.rmi.RemoteException;
 public java.util.Collection findAgeRegulations(java.lang.String p0,java.sql.Date p1,java.sql.Date p2,java.lang.String p3,java.lang.String p4)throws se.idega.idegaweb.commune.accounting.regulations.business.AgeException, java.rmi.RemoteException;
 public java.util.Collection findAllAgeRegulations() throws java.rmi.RemoteException;
 public java.util.Collection findByOperationalField(java.lang.String p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.AgeRegulation getAgeRegulation(int p0)throws se.idega.idegaweb.commune.accounting.regulations.business.AgeException, java.rmi.RemoteException;
 public java.util.Collection getAllAgeRuleTypes() throws java.rmi.RemoteException;
 public int getChildAge(java.lang.String p0) throws java.rmi.RemoteException;
 public int getChildAge(java.lang.String p0,java.sql.Date p1) throws java.rmi.RemoteException;
 public void saveAgeRegulation(int p0,java.lang.String p1,java.sql.Date p2,java.sql.Date p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,java.sql.Date p9,java.lang.String p10)throws se.idega.idegaweb.commune.accounting.regulations.business.AgeException, java.rmi.RemoteException;
}
