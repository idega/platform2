package com.idega.block.creditcard.business;


public class CreditCardBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements CreditCardBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return CreditCardBusiness.class;
 }


 public CreditCardBusiness create() throws javax.ejb.CreateException{
  return (CreditCardBusiness) super.createIBO();
 }



}