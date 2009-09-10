package com.idega.block.finance.business;


public class AccountBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements AccountBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return AccountBusiness.class;
 }


 public AccountBusiness create() throws javax.ejb.CreateException{
  return (AccountBusiness) super.createIBO();
 }



}