package com.idega.block.contract.data;


public interface ContractCategoryHome extends com.idega.data.IDOHome
{
 public ContractCategory create() throws javax.ejb.CreateException;
 public ContractCategory createLegacy();
 public ContractCategory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ContractCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ContractCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}