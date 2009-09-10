package com.idega.block.trade.stockroom.data;


public class VariantValueHomeImpl extends com.idega.data.IDOFactory implements VariantValueHome
{
 protected Class getEntityInterfaceClass(){
  return VariantValue.class;
 }


 public VariantValue create() throws javax.ejb.CreateException{
  return (VariantValue) super.createIDO();
 }


 public VariantValue findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (VariantValue) super.findByPrimaryKeyIDO(pk);
 }



}