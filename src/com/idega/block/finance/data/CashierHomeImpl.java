package com.idega.block.finance.data;


public class CashierHomeImpl extends com.idega.data.IDOFactory implements CashierHome
{
 protected Class getEntityInterfaceClass(){
  return Cashier.class;
 }


 public Cashier create() throws javax.ejb.CreateException{
  return (Cashier) super.createIDO();
 }


 public Cashier findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Cashier) super.findByPrimaryKeyIDO(pk);
 }



}