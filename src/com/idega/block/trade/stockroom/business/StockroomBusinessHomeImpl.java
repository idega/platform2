package com.idega.block.trade.stockroom.business;


public class StockroomBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements StockroomBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return StockroomBusiness.class;
 }


 public StockroomBusiness create() throws javax.ejb.CreateException{
  return (StockroomBusiness) super.createIBO();
 }



}