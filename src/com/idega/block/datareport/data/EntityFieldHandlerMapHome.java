package com.idega.block.datareport.data;


public interface EntityFieldHandlerMapHome extends com.idega.data.IDOHome
{
 public EntityFieldHandlerMap create() throws javax.ejb.CreateException;
 public EntityFieldHandlerMap findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}