package com.idega.block.creditcard.data;


public interface TPosAuthorisationEntriesBeanHome extends com.idega.data.IDOHome
{
 public TPosAuthorisationEntriesBean create() throws javax.ejb.CreateException;
 public TPosAuthorisationEntriesBean findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TPosAuthorisationEntriesBean findByAuthorisationIdRsp(java.lang.String p0)throws javax.ejb.FinderException;

}