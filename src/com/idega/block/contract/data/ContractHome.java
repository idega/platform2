package com.idega.block.contract.data;


public interface ContractHome extends com.idega.data.IDOHome
{
 public Contract create() throws javax.ejb.CreateException;
 public Contract createLegacy();
 public Contract findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Contract findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Contract findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findAllByCategory(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllByCategoryAndStatus(int p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllByUser(int p0)throws javax.ejb.FinderException;
 public com.idega.block.contract.data.Contract create(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,java.lang.String p3,java.util.Map p4);
 public com.idega.block.contract.data.Contract create(int p0,int p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3,java.lang.String p4,java.lang.String p5);
 public com.idega.block.contract.data.Contract create(int p0,int p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3,java.lang.String p4,java.util.Map p5);
 public java.util.Collection findFiles(int p0)throws javax.ejb.FinderException,com.idega.data.IDORelationshipException;
 public int getCountByCategory(int p0)throws com.idega.data.IDOException;
 public boolean setStatus(int p0,java.lang.String p1)throws javax.ejb.FinderException,com.idega.data.IDOLookupException;

}