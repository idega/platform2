package com.idega.block.contract.data;


public interface ContractTextHome extends com.idega.data.IDOHome
{
 public ContractText create() throws javax.ejb.CreateException;
 public ContractText createLegacy();
 public ContractText findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ContractText findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ContractText findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}