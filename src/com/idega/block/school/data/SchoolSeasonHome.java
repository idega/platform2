package com.idega.block.school.data;


public interface SchoolSeasonHome extends com.idega.data.IDOHome
{
 public SchoolSeason create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public SchoolSeason findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllPreviousSchoolSeasons(com.idega.block.school.data.SchoolSeason p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findAllSchoolSeasons()throws javax.ejb.FinderException, java.rmi.RemoteException;

}