package com.idega.block.trade.stockroom.business;


public class ProductBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ProductBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ProductBusiness.class;
 }


 public ProductBusiness create() throws javax.ejb.CreateException{
  return (ProductBusiness) super.createIBO();
 }



}