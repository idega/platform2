package com.idega.block.school.data;


public interface SchoolClassHome extends com.idega.data.IDOHome
{
 public SchoolClass create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public SchoolClass findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public SchoolClass findBySchoolClassNameSchoolSchoolYearSchoolSeason(java.lang.String p0,com.idega.block.school.data.School p1,com.idega.block.school.data.SchoolYear p2,com.idega.block.school.data.SchoolSeason p3)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;

}