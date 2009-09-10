package com.idega.block.blog.data;


public interface BlogEntityTranslationHome extends com.idega.data.IDOHome
{
 public BlogEntityTranslation create() throws javax.ejb.CreateException;
 public BlogEntityTranslation findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}