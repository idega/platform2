package com.idega.block.user.business;


public interface UserInfoBusiness extends com.idega.business.IBOService
{
 public java.lang.String getAge(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public java.util.List getEmailList(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public com.idega.user.data.Group getGroup(com.idega.presentation.IWContext p0,java.lang.String p1) throws java.rmi.RemoteException;
 public com.idega.core.location.data.Address getGroupAddress(com.idega.presentation.IWContext p0,com.idega.user.data.Group p1) throws java.rmi.RemoteException;
 public java.util.List getGroups(com.idega.presentation.IWContext p0,java.lang.String[] p1,java.lang.String[] p2,java.util.Comparator p3) throws java.rmi.RemoteException;
 public com.idega.block.user.data.UserExtraInfo getInfo(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public java.lang.String[] getPhones(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public java.lang.String getStatusKey(com.idega.presentation.IWContext p0,com.idega.user.data.User p1,com.idega.user.data.Group p2) throws java.rmi.RemoteException;
 public com.idega.user.data.User getUser(com.idega.presentation.IWContext p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.util.List getUsersByGroup(com.idega.presentation.IWContext p0,com.idega.user.data.Group p1,java.util.Comparator p2) throws java.rmi.RemoteException;
}
