package com.idega.block.application.data;


public interface ApplicationSubjectInfoHome extends com.idega.data.IDOHome
{
 public ApplicationSubjectInfo create() throws javax.ejb.CreateException;
 public ApplicationSubjectInfo createLegacy();
 public ApplicationSubjectInfo findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ApplicationSubjectInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ApplicationSubjectInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}