package com.idega.block.school.data;


public interface SchoolUserPaymentHome extends com.idega.data.IDOHome
{
 public SchoolUserPayment create() throws javax.ejb.CreateException;
 public SchoolUserPayment findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}