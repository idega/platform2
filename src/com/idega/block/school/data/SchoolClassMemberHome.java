package com.idega.block.school.data;

import com.idega.user.data.Group;


public interface SchoolClassMemberHome extends com.idega.data.IDOHome
{
 public SchoolClassMember create() throws javax.ejb.CreateException;
 public SchoolClassMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllBySeasonAndInvoiceInterval(int p0,java.lang.String p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findAllBySeasonAndMaximumAge(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findAllByUserAndSeason(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllByUserAndSeason(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findBySchool(int p0,int p1,java.sql.Date p2)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findBySchool(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findBySchool(int p0,int p1,java.sql.Date p2,boolean p3)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findBySchoolAndSeason(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findBySchoolAndSeasonAndYear(int p0,int p1,int p2)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findBySchoolClass(int p0)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findBySchoolClass(com.idega.block.school.data.SchoolClass p0)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findByStudent(int p0)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findByStudent(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findByStudentAndSchool(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findByStudentAndTypes(int p0,java.util.Collection p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public SchoolClassMember findByUserAndSchool(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public SchoolClassMember findByUserAndSchoolAndSeason(int p0,int p1,int p2)throws javax.ejb.FinderException;
 public SchoolClassMember findByUserAndSchoolClass(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public SchoolClassMember findByUserAndSchoolClass(com.idega.user.data.User p0,com.idega.block.school.data.SchoolClass p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public SchoolClassMember findByUserAndSeason(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public SchoolClassMember findByUserAndSeason(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public SchoolClassMember findLatestByUserAndSchool(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public int getNumberOfUsersNotAssignedToClassOnGivenDate(Group citizenGroup, java.sql.Date p0,java.util.Collection p1,java.sql.Date p2,java.sql.Date p3)throws com.idega.data.IDOException,com.idega.data.IDOLookupException;

}