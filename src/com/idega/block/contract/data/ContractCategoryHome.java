package com.idega.block.contract.data;


public interface ContractCategoryHome extends com.idega.data.IDOHome
{
 public ContractCategory create() throws javax.ejb.CreateException;
 public ContractCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findByObjectInstance(com.idega.core.component.data.ICObjectInstance p0)throws javax.ejb.FinderException,com.idega.data.IDORelationshipException;
 public com.idega.block.contract.data.ContractCategory create(int p0,int p1,java.lang.String p2,java.lang.String p3)throws javax.ejb.CreateException;
 public boolean updateDescription(int p0,java.lang.String p1);

}