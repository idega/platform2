package com.idega.block.creditcard.data;

public interface CreditCardInformationHome extends com.idega.data.IDOHome
{
 public CreditCardInformation create() throws javax.ejb.CreateException;
 public CreditCardInformation findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}