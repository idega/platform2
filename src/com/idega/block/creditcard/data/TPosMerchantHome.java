package com.idega.block.creditcard.data;

public interface TPosMerchantHome extends com.idega.data.IDOHome
{
 public TPosMerchant create() throws javax.ejb.CreateException;
 public TPosMerchant findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}