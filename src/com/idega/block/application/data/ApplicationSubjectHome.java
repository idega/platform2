package com.idega.block.application.data;


public interface ApplicationSubjectHome extends com.idega.data.IDOHome
{
 public ApplicationSubject create() throws javax.ejb.CreateException;
 public ApplicationSubject createLegacy();
 public ApplicationSubject findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ApplicationSubject findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ApplicationSubject findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}