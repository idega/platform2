package com.idega.block.creditcard.data;


public class KortathjonustanMerchantHomeImpl extends com.idega.data.IDOFactory implements KortathjonustanMerchantHome
{
 protected Class getEntityInterfaceClass(){
  return KortathjonustanMerchant.class;
 }


 public KortathjonustanMerchant create() throws javax.ejb.CreateException{
  return (KortathjonustanMerchant) super.createIDO();
 }


 public KortathjonustanMerchant findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (KortathjonustanMerchant) super.findByPrimaryKeyIDO(pk);
 }



}