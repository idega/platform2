package se.idega.idegaweb.commune.childcare.export.business;

import java.sql.Date;


public interface ChildCareExportBusiness extends com.idega.business.IBOService
{
 public java.lang.String exportPlacementFile(int p0, Date p1)throws se.idega.idegaweb.commune.childcare.export.business.ChildCareExportException, java.rmi.RemoteException;
 public java.lang.String exportTaxekatFile(int p0, java.sql.Date p1, Date p2)throws se.idega.idegaweb.commune.childcare.export.business.ChildCareExportException, java.rmi.RemoteException;
 public java.util.Iterator getAllExportFiles() throws java.rmi.RemoteException;
 public java.lang.String getPlacementExportFileNamePrefix() throws java.rmi.RemoteException;
 public java.lang.String getTaxekatExportFileNamePrefix() throws java.rmi.RemoteException;
 public java.lang.String storePlacementExportFileTimestamp(int p0, Date to)throws se.idega.idegaweb.commune.childcare.export.business.ChildCareExportException, java.rmi.RemoteException;
 public java.lang.String storeTaxekatExportFileTimestamp(int p0, java.sql.Date p1, Date p2)throws se.idega.idegaweb.commune.childcare.export.business.ChildCareExportException, java.rmi.RemoteException;
 public String getFileDateInterval(String fileName);
}
