package se.idega.idegaweb.commune.accounting.regulations.business;


public interface VATBusiness extends com.idega.business.IBOService
{
 public void deleteVATRegulation(int p0)throws se.idega.idegaweb.commune.accounting.regulations.business.VATException, java.rmi.RemoteException;
 public void deleteVATRegulations(java.lang.String[] p0)throws se.idega.idegaweb.commune.accounting.regulations.business.VATException, java.rmi.RemoteException;
 public java.util.Collection findAllVATRegulations() throws java.rmi.RemoteException;
 public java.util.Collection findVATRegulations(java.sql.Date p0,java.sql.Date p1,java.lang.String p2,java.lang.String p3)throws se.idega.idegaweb.commune.accounting.regulations.business.VATException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.regulations.data.VATRegulation getVATRegulation(int p0)throws se.idega.idegaweb.commune.accounting.regulations.business.VATException, java.rmi.RemoteException;
 public void saveVATRegulation(int p0,java.sql.Date p1,java.sql.Date p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8)throws se.idega.idegaweb.commune.accounting.regulations.business.VATException, java.rmi.RemoteException;
}
