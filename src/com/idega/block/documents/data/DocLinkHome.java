package com.idega.block.documents.data;


public interface DocLinkHome extends com.idega.data.IDOHome
{
 public DocLink create() throws javax.ejb.CreateException;
 public DocLink createLegacy();
 public DocLink findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public DocLink findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public DocLink findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}