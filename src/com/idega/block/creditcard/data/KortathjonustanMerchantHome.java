package com.idega.block.creditcard.data;


public interface KortathjonustanMerchantHome extends com.idega.data.IDOHome
{
 public KortathjonustanMerchant create() throws javax.ejb.CreateException;
 public KortathjonustanMerchant findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}