package com.idega.block.school.data;


public interface SchoolStudyPathHome extends com.idega.data.IDOHome
{
 public SchoolStudyPath create() throws javax.ejb.CreateException;
 public SchoolStudyPath findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}