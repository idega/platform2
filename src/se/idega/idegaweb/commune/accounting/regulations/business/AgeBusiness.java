package se.idega.idegaweb.commune.accounting.regulations.business;


public interface AgeBusiness extends com.idega.business.IBOService
{
 public void deleteAgeRegulation(int p0)throws se.idega.idegaweb.commune.accounting.regulations.business.AgeException, java.rmi.RemoteException;
 public java.util.Collection findAgeRegulations(java.sql.Date p0,java.sql.Date p1,java.lang.String p2,java.lang.String p3)throws se.idega.idegaweb.commune.accounting.regulations.business.AgeException, java.rmi.RemoteException;
 public java.util.Collection findAllAgeRegulations() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.AgeRegulation getAgeRegulation(int p0)throws se.idega.idegaweb.commune.accounting.regulations.business.AgeException, java.rmi.RemoteException;
 public java.util.Collection getAllAgeRuleTypes() throws java.rmi.RemoteException;
 public void saveAgeRegulation(int p0,java.sql.Date p1,java.sql.Date p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.sql.Date p8,java.lang.String p9)throws se.idega.idegaweb.commune.accounting.regulations.business.AgeException, java.rmi.RemoteException;
}
