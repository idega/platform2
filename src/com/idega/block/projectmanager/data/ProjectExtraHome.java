package com.idega.block.projectmanager.data;


public interface ProjectExtraHome extends com.idega.data.IDOHome
{
 public ProjectExtra create() throws javax.ejb.CreateException;
 public ProjectExtra findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}