package se.idega.idegaweb.commune.accounting.message.business;


public interface NoticeBusiness extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.message.business.MessageBusiness getMessageBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolBusiness getSchoolBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.business.UserBusiness getUserBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection sendNotice(java.lang.String p0,java.lang.String p1,java.lang.String[] p2, boolean p3)throws se.idega.idegaweb.commune.accounting.message.business.NoticeException, java.rmi.RemoteException;
}
