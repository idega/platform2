package se.idega.idegaweb.commune.childcare.business;

import javax.ejb.*;

public interface ChildCareBusiness extends com.idega.business.IBOService
{
 public java.util.Collection getUnhandledApplicationsByProvider(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public java.util.Collection getUnhandledApplicationsByProvider(com.idega.block.school.data.School p0) throws java.rmi.RemoteException;
 public java.util.Collection getUnhandledApplicationsByProvider(int p0) throws java.rmi.RemoteException;
 public java.util.Collection getUnsignedApplicationsByProvider(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.childcare.data.ChildCareApplication getApplicationByPrimaryKey(java.lang.String p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllGrantedApplications() throws java.rmi.RemoteException;
 public boolean acceptApplication(se.idega.idegaweb.commune.childcare.data.ChildCareApplication p0,java.lang.String p1,java.lang.String p2,com.idega.user.data.User p3) throws java.rmi.RemoteException;
 public java.util.Collection getUnsignedApplicationsByProvider(com.idega.block.school.data.School p0) throws java.rmi.RemoteException;
 public java.util.Collection getUnsignedApplicationsByProvider(int p0) throws java.rmi.RemoteException;
 public java.util.Collection getApplicationsByProvider(com.idega.block.school.data.School p0) throws java.rmi.RemoteException;
 public java.util.Collection getGrantedApplicationsByUser(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public boolean acceptApplication(int p0,java.lang.String p1,java.lang.String p2,com.idega.user.data.User p3) throws java.rmi.RemoteException;
 public boolean assignContractToApplication(int p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public boolean rejectApplication(int p0,java.lang.String p1,java.lang.String p2,com.idega.user.data.User p3) throws java.rmi.RemoteException;
 public boolean rejectApplication(se.idega.idegaweb.commune.childcare.data.ChildCareApplication p0,java.lang.String p1,java.lang.String p2,com.idega.user.data.User p3) throws java.rmi.RemoteException;
 public boolean insertApplications(com.idega.user.data.User p0,int[] p1,java.lang.String[] p2,int p3,int p4,java.lang.String p5,java.lang.String p6, boolean p7) throws java.rmi.RemoteException;
 public java.util.Collection getApplicationsByProvider(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public java.util.Collection getApplicationsByProvider(int p0) throws java.rmi.RemoteException;
 public boolean redeemApplication(java.lang.String p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public boolean assignContractToApplication(java.lang.String[] p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public boolean assignApplication(java.lang.String[] p0,com.idega.user.data.User p1,java.lang.String p2,java.lang.String p3) throws java.rmi.RemoteException;
 public java.util.Collection findAllApplicationsWithChecksToRedeem() throws java.rmi.RemoteException;
 public boolean assignApplication(int p0,com.idega.user.data.User p1,java.lang.String p2,java.lang.String p3) throws java.rmi.RemoteException;
}
