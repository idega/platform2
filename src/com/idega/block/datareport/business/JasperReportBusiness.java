package com.idega.block.datareport.business;

import com.idega.user.business.UserGroupPlugInBusiness;


public interface JasperReportBusiness extends com.idega.business.IBOService, UserGroupPlugInBusiness
{
 public dori.jasper.engine.design.JasperDesign generateLayout(dori.jasper.engine.JRDataSource p0)throws java.io.IOException,dori.jasper.engine.JRException, java.rmi.RemoteException;
 public com.idega.block.datareport.data.DesignBox getDesignBox(int p0) throws java.rmi.RemoteException;
 public dori.jasper.engine.design.JasperDesign getDesignFromBundle(com.idega.idegaweb.IWBundle p0,java.lang.String p1) throws java.rmi.RemoteException;
 public com.idega.block.datareport.data.DesignBox getDynamicDesignBox(com.idega.block.dataquery.data.sql.SQLQuery p0,com.idega.idegaweb.IWResourceBundle p1,com.idega.presentation.IWContext p2)throws java.io.IOException,dori.jasper.engine.JRException, java.rmi.RemoteException;
 public java.lang.String getExcelReport(dori.jasper.engine.JasperPrint p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.lang.String getHtmlReport(dori.jasper.engine.JasperPrint p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.lang.String getPdfReport(dori.jasper.engine.JasperPrint p0,java.lang.String p1) throws java.rmi.RemoteException;
 public dori.jasper.engine.JasperPrint getReport(dori.jasper.engine.JRDataSource p0,java.util.Map p1,dori.jasper.engine.design.JasperDesign p2)throws dori.jasper.engine.JRException, java.rmi.RemoteException;
 public dori.jasper.engine.JasperPrint printSynchronizedReport(com.idega.block.dataquery.data.QueryResult p0,java.util.Map p1,com.idega.block.datareport.data.DesignBox p2) throws java.rmi.RemoteException;
 public void synchronizeResultAndDesign(com.idega.block.dataquery.data.QueryResult p0,java.util.Map p1,com.idega.block.datareport.data.DesignBox p2) throws java.rmi.RemoteException;
}
