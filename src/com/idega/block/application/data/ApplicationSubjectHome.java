package com.idega.block.application.data;


public interface ApplicationSubjectHome extends com.idega.data.IDOHome
{
 public ApplicationSubject create() throws javax.ejb.CreateException;
 public ApplicationSubject findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findNonExpired()throws javax.ejb.FinderException;

}