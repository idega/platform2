package com.idega.block.trade.data;



public class CreditCardInformationHomeImpl extends com.idega.data.IDOFactory implements CreditCardInformationHome
{
 protected Class getEntityInterfaceClass(){
  return CreditCardInformation.class;
 }


 public CreditCardInformation create() throws javax.ejb.CreateException{
  return (CreditCardInformation) super.createIDO();
 }


 public CreditCardInformation findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CreditCardInformation) super.findByPrimaryKeyIDO(pk);
 }



}