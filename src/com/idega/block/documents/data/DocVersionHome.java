package com.idega.block.documents.data;


public interface DocVersionHome extends com.idega.data.IDOHome
{
 public DocVersion create() throws javax.ejb.CreateException;
 public DocVersion createLegacy();
 public DocVersion findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public DocVersion findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public DocVersion findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}