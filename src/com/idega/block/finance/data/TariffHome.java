package com.idega.block.finance.data;


public interface TariffHome extends com.idega.data.IDOHome
{
 public Tariff create() throws javax.ejb.CreateException;
 public Tariff findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByColumn(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllByColumn(java.lang.String p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllByColumnOrdered(java.lang.String p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllByColumnOrdered(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4)throws javax.ejb.FinderException;
 public java.util.Collection findAllByPrimaryKeyArray(java.lang.String[] p0)throws javax.ejb.FinderException;
 public java.util.Collection findByAttribute(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findByTariffGroup(java.lang.Integer p0)throws javax.ejb.FinderException;

}