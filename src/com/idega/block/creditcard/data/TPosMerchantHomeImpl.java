package com.idega.block.creditcard.data;

public class TPosMerchantHomeImpl extends com.idega.data.IDOFactory implements TPosMerchantHome
{
 protected Class getEntityInterfaceClass(){
  return TPosMerchant.class;
 }


 public TPosMerchant create() throws javax.ejb.CreateException{
  return (TPosMerchant) super.createIDO();
 }


 public TPosMerchant findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TPosMerchant) super.findByPrimaryKeyIDO(pk);
 }



}