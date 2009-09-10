package com.idega.block.albumcollection.data;


public interface CriticHome extends com.idega.data.IDOHome
{
 public Critic create() throws javax.ejb.CreateException;
 public Critic createLegacy();
 public Critic findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Critic findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Critic findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}