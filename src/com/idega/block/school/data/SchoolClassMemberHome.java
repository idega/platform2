package com.idega.block.school.data;


public interface SchoolClassMemberHome extends com.idega.data.IDOHome
{
 public SchoolClassMember create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public SchoolClassMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}