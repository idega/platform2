package com.idega.block.datareport.business;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JasperDesign;
import com.idega.block.datareport.util.ReportDescription;
import com.idega.user.business.UserGroupPlugInBusiness;


public interface JasperReportBusiness extends com.idega.business.IBOService, UserGroupPlugInBusiness
{
 public JasperDesign generateLayout(JRDataSource p0)throws java.io.IOException,JRException, java.rmi.RemoteException;
 public com.idega.block.datareport.data.DesignBox getDesignBox(int p0) throws java.rmi.RemoteException;
 public JasperDesign getDesignFromBundle(com.idega.idegaweb.IWBundle p0,java.lang.String p1) throws java.rmi.RemoteException;
 public com.idega.block.datareport.data.DesignBox getDynamicDesignBox(com.idega.block.dataquery.data.sql.SQLQuery p0,com.idega.idegaweb.IWResourceBundle p1,com.idega.presentation.IWContext p2)throws java.io.IOException,JRException, java.rmi.RemoteException;
 public java.lang.String getExcelReport(JasperPrint p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.lang.String getHtmlReport(JasperPrint p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.lang.String getPdfReport(JasperPrint p0,java.lang.String p1) throws java.rmi.RemoteException;
 public JasperPrint getReport(JRDataSource p0,java.util.Map p1,JasperDesign p2)throws JRException, java.rmi.RemoteException;
 public JasperPrint printSynchronizedReport(com.idega.block.dataquery.data.QueryResult p0,java.util.Map p1,com.idega.block.datareport.data.DesignBox p2) throws java.rmi.RemoteException;
 public void synchronizeResultAndDesign(com.idega.block.dataquery.data.QueryResult p0,java.util.Map p1,com.idega.block.datareport.data.DesignBox p2) throws java.rmi.RemoteException;
 public String getSimpleExcelReport(JRDataSource reportData, String nameOfReport, ReportDescription description);
}
