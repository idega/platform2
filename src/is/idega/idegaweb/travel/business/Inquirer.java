package is.idega.idegaweb.travel.business;

import javax.ejb.*;

public interface Inquirer extends com.idega.business.IBOService
{
 public is.idega.idegaweb.travel.data.InqueryHome getInquiryHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.Inquery[] getInqueries(int p0,com.idega.util.IWTimestamp p1,boolean p2,java.lang.String p3)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int inquiryResponse(com.idega.presentation.IWContext p0,com.idega.idegaweb.IWResourceBundle p1,int p2,boolean p3,com.idega.block.trade.stockroom.data.Supplier p4,com.idega.block.trade.stockroom.data.Reseller p5) throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.Inquery[] getInqueries(int p0,com.idega.util.IWTimestamp p1,boolean p2)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int inquiryResponse(com.idega.presentation.IWContext p0,com.idega.idegaweb.IWResourceBundle p1,int p2,boolean p3,boolean p4,com.idega.block.trade.stockroom.data.Supplier p5) throws java.rmi.RemoteException;
 public int inquiryResponse(com.idega.presentation.IWContext p0,com.idega.idegaweb.IWResourceBundle p1,int p2,boolean p3,boolean p4,com.idega.block.trade.stockroom.data.Supplier p5,com.idega.block.trade.stockroom.data.Reseller p6) throws java.rmi.RemoteException;
 public int inquiryResponse(com.idega.presentation.IWContext p0,com.idega.idegaweb.IWResourceBundle p1,int p2,boolean p3,com.idega.block.trade.stockroom.data.Supplier p4) throws java.rmi.RemoteException;
 public int sendInquiryEmails(com.idega.presentation.IWContext p0,com.idega.idegaweb.IWResourceBundle p1,int p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.Inquery[] getInqueries(int p0,com.idega.util.IWTimestamp p1,boolean p2,com.idega.block.trade.stockroom.data.TravelAddress p3,java.lang.String p4)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.Inquery[] collectionToInqueryArray(java.util.Collection p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.Inquery[] getInqueries(int p0,com.idega.util.IWTimestamp p1,boolean p2,com.idega.block.trade.stockroom.data.TravelAddress p3)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int[] getMultibleInquiriesNumber(is.idega.idegaweb.travel.data.Inquery p0)throws javax.ejb.FinderException,javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.presentation.Table getInquiryResponseError(com.idega.idegaweb.IWResourceBundle p0) throws java.rmi.RemoteException;
 public int getInqueredSeats(int p0,com.idega.util.IWTimestamp p1,boolean p2)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int sendInquery(java.lang.String p0,java.lang.String p1,com.idega.util.IWTimestamp p2,int p3,int p4,String comment, int p5,com.idega.block.trade.stockroom.data.Reseller p6)throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
}
