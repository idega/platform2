package com.idega.block.school.business;

import javax.ejb.*;

public interface SchoolUserBusiness extends com.idega.business.IBOService
{
 public com.idega.block.school.data.SchoolUserHome getSchoolUserHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolUser addTeacher(com.idega.block.school.data.School p0,com.idega.user.data.User p1)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolUser addAssistantHeadmaster(com.idega.block.school.data.School p0,com.idega.user.data.User p1)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public java.util.Collection getAssistantHeadmasters(com.idega.block.school.data.School p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getHeadmasters(com.idega.block.school.data.School p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolUser addUser(com.idega.block.school.data.School p0,com.idega.user.data.User p1,int p2)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public void removeUser(com.idega.block.school.data.School p0,com.idega.user.data.User p1,int p2)throws javax.ejb.FinderException,java.rmi.RemoteException,javax.ejb.RemoveException, java.rmi.RemoteException;
 public void removeUser(com.idega.block.school.data.School p0,com.idega.user.data.User p1)throws javax.ejb.FinderException,java.rmi.RemoteException,javax.ejb.RemoveException, java.rmi.RemoteException;
 public java.util.Collection getWebAdmins(com.idega.block.school.data.School p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolUser addHeadmaster(com.idega.block.school.data.School p0,com.idega.user.data.User p1)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public java.util.Collection getTeachers(com.idega.block.school.data.School p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getTeachers(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolUser addWebAdmin(com.idega.block.school.data.School p0,com.idega.user.data.User p1)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
}
