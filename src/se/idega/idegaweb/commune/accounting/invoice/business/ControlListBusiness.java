package se.idega.idegaweb.commune.accounting.invoice.business;


public interface ControlListBusiness extends com.idega.business.IBOService
{
 public java.util.Collection getControlListValues(java.sql.Date p0,java.sql.Date p1,java.lang.String p2)throws se.idega.idegaweb.commune.accounting.invoice.business.ControlListException, java.rmi.RemoteException;
}
