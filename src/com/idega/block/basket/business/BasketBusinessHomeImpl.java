package com.idega.block.basket.business;


public class BasketBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements BasketBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return BasketBusiness.class;
 }


 public BasketBusiness create() throws javax.ejb.CreateException{
  return (BasketBusiness) super.createIBO();
 }



}