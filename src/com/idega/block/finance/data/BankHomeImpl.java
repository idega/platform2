package com.idega.block.finance.data;


public class BankHomeImpl extends com.idega.data.IDOFactory implements BankHome
{
 protected Class getEntityInterfaceClass(){
  return Bank.class;
 }


 public Bank create() throws javax.ejb.CreateException{
  return (Bank) super.createIDO();
 }


 public Bank findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Bank) super.findByPrimaryKeyIDO(pk);
 }



}