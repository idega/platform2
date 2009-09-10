package com.idega.block.projectmanager.data;


public interface ImportanceHome extends com.idega.data.IDOHome
{
 public Importance create() throws javax.ejb.CreateException;
 public Importance findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}