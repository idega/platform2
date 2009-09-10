package com.idega.block.finance.data;


public class AccountTypeHomeImpl extends com.idega.data.IDOFactory implements AccountTypeHome
{
 protected Class getEntityInterfaceClass(){
  return AccountType.class;
 }


 public AccountType create() throws javax.ejb.CreateException{
  return (AccountType) super.createIDO();
 }


 public AccountType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AccountType) super.findByPrimaryKeyIDO(pk);
 }



}