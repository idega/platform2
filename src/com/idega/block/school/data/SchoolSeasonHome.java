package com.idega.block.school.data;


public interface SchoolSeasonHome extends com.idega.data.IDOHome
{
 public SchoolSeason create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public SchoolSeason findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}