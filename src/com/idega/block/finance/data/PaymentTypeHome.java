package com.idega.block.finance.data;


public interface PaymentTypeHome extends com.idega.data.IDOHome
{
 public PaymentType create() throws javax.ejb.CreateException;
 public PaymentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}