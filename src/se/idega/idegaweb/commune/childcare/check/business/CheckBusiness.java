package se.idega.idegaweb.commune.childcare.check.business;

import javax.ejb.*;

public interface CheckBusiness extends com.idega.business.IBOService
{
 public void createCheck(int p0,int p1,int p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,int p6,int p7,int p8,int p9)throws java.lang.Exception, java.rmi.RemoteException;
 public java.util.Collection findChecks()throws java.lang.Exception, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.childcare.check.data.Check getCheck(int p0)throws java.lang.Exception, java.rmi.RemoteException;
}
