package se.idega.idegaweb.commune.accounting.export.business;


public interface ExportBusiness extends com.idega.business.IBOService
{
 public int getAccountSettlementTypeDayByDay() throws java.rmi.RemoteException;
 public int getAccountSettlementTypeSpecificDate() throws java.rmi.RemoteException;
 public java.util.Collection getAllOperationalFields() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping getExportDataMapping(java.lang.String p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public void storeExportDataMapping(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,int p6,int p7,boolean p8,boolean p9,boolean p10) throws java.rmi.RemoteException;
}
