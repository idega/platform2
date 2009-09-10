package com.idega.block.blog.data;


public interface BlogEntityHome extends com.idega.data.IDOHome
{
 public BlogEntity create() throws javax.ejb.CreateException;
 public BlogEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll(java.lang.String p0)throws javax.ejb.FinderException;

}