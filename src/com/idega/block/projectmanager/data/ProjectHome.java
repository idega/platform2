package com.idega.block.projectmanager.data;


public interface ProjectHome extends com.idega.data.IDOHome
{
 public Project create() throws javax.ejb.CreateException;
 public Project findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllOrderByNumber()throws javax.ejb.FinderException;

}