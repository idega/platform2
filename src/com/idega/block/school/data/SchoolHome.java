package com.idega.block.school.data;


public interface SchoolHome extends com.idega.data.IDOHome
{
 public School create() throws javax.ejb.CreateException;
 public School findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByAreaAndType(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllByAreaAndTypeAndYear(int p0,int p1,int p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllByAreaAndTypes(int p0,java.util.Collection p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllByCategory(com.idega.block.school.data.SchoolCategory p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllBySchoolArea(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllBySchoolGroup(com.idega.user.data.Group p0)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findAllBySchoolName(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllBySchoolType(java.util.Collection p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllBySchoolType(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllCentralizedAdministrated()throws javax.ejb.FinderException;
 public java.util.Collection findAllCentralizedAdministratedByType(java.util.Collection p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllPrivate()throws com.idega.data.IDOLookupException,javax.ejb.EJBException,javax.ejb.FinderException;
 public java.util.Collection findAllSchools()throws javax.ejb.FinderException;
 public java.util.Collection findAllSchoolsIncludingTerminated()throws javax.ejb.FinderException;
 public School findBySchoolName(java.lang.String p0)throws javax.ejb.FinderException;
 public int getNumberOfRelations(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolYear p1)throws com.idega.data.IDOException;

}