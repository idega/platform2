package se.idega.idegaweb.commune.accounting.business;


public interface AccountingBusiness extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.accounting.export.business.ExportBusiness getExportBusiness() throws java.rmi.RemoteException;
}
