package com.idega.block.finance.business;


public class BankInfoBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements BankInfoBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return BankInfoBusiness.class;
 }


 public BankInfoBusiness create() throws javax.ejb.CreateException{
  return (BankInfoBusiness) super.createIBO();
 }



}