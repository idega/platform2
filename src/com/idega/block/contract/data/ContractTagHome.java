package com.idega.block.contract.data;


public interface ContractTagHome extends com.idega.data.IDOHome
{
 public ContractTag create() throws javax.ejb.CreateException;
 public ContractTag createLegacy();
 public ContractTag findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ContractTag findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ContractTag findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findAllByCategory(int p0) throws javax.ejb.FinderException;
 public java.util.Collection findAllByNameAndCategory(String name, int categoryId) throws javax.ejb.FinderException;


}