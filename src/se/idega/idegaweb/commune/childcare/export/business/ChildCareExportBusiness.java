package se.idega.idegaweb.commune.childcare.export.business;


public interface ChildCareExportBusiness extends com.idega.business.IBOService
{
 public java.lang.String exportPlacementFile(int p0)throws se.idega.idegaweb.commune.childcare.export.business.ChildCareExportException, java.rmi.RemoteException;
 public java.lang.String exportTaxekatFile(int p0)throws se.idega.idegaweb.commune.childcare.export.business.ChildCareExportException, java.rmi.RemoteException;
 public java.util.Iterator getAllExportFiles() throws java.rmi.RemoteException;
 public java.lang.String getPlacementExportFileNamePrefix() throws java.rmi.RemoteException;
 public java.lang.String getTaxekatExportFileNamePrefix() throws java.rmi.RemoteException;
 public java.lang.String storePlacementExportFileTimestamp(int p0)throws se.idega.idegaweb.commune.childcare.export.business.ChildCareExportException, java.rmi.RemoteException;
 public java.lang.String storeTaxekatExportFileTimestamp(int p0)throws se.idega.idegaweb.commune.childcare.export.business.ChildCareExportException, java.rmi.RemoteException;
}
