package com.idega.block.trade.stockroom.data;


public class VariantHomeImpl extends com.idega.data.IDOFactory implements VariantHome
{
 protected Class getEntityInterfaceClass(){
  return Variant.class;
 }


 public Variant create() throws javax.ejb.CreateException{
  return (Variant) super.createIDO();
 }


 public Variant findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Variant) super.findByPrimaryKeyIDO(pk);
 }



}