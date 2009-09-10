package com.idega.block.dictionary.data;


public interface WordHome extends com.idega.data.IDOHome
{
 public Word create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Word findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllWordsByCategory(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllWordsInCategories(int[] p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllWordsContaining(java.lang.String p0)throws javax.ejb.FinderException, java.rmi.RemoteException;

}