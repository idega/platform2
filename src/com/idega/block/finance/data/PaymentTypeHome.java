package com.idega.block.finance.data;


public interface PaymentTypeHome extends com.idega.data.IDOHome
{
 public PaymentType create() throws javax.ejb.CreateException;
 public PaymentType createLegacy();
 public PaymentType findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public PaymentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public PaymentType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}