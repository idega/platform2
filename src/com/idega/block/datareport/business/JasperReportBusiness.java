package com.idega.block.datareport.business;


public interface JasperReportBusiness extends com.idega.business.IBOService
{
 public dori.jasper.engine.design.JasperDesign generateLayout(dori.jasper.engine.JRDataSource p0)throws java.io.IOException,dori.jasper.engine.JRException, java.rmi.RemoteException;
 public dori.jasper.engine.design.JasperDesign getDesign(int p0) throws java.rmi.RemoteException;
 public java.lang.String getExcelReport(dori.jasper.engine.JasperPrint p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.lang.String getHtmlReport(dori.jasper.engine.JasperPrint p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.lang.String getPdfReport(dori.jasper.engine.JasperPrint p0,java.lang.String p1) throws java.rmi.RemoteException;
 public dori.jasper.engine.JasperPrint getReport(dori.jasper.engine.JRDataSource p0,java.util.Map p1,dori.jasper.engine.design.JasperDesign p2)throws dori.jasper.engine.JRException, java.rmi.RemoteException;
 public dori.jasper.engine.JasperPrint printSynchronizedReport(com.idega.block.dataquery.data.QueryResult p0,java.util.Map p1,dori.jasper.engine.design.JasperDesign p2) throws java.rmi.RemoteException;
 public void synchronizeResultAndDesign(com.idega.block.dataquery.data.QueryResult p0,java.util.Map p1,dori.jasper.engine.design.JasperDesign p2) throws java.rmi.RemoteException;
}
