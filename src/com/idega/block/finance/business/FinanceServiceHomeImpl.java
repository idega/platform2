package com.idega.block.finance.business;


public class FinanceServiceHomeImpl extends com.idega.business.IBOHomeImpl implements FinanceServiceHome
{
 protected Class getBeanInterfaceClass(){
  return FinanceService.class;
 }


 public FinanceService create() throws javax.ejb.CreateException{
  return (FinanceService) super.createIBO();
 }



}