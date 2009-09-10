package com.idega.block.datareport.business;


public class SimpleReportBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SimpleReportBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SimpleReportBusiness.class;
 }


 public SimpleReportBusiness create() throws javax.ejb.CreateException{
  return (SimpleReportBusiness) super.createIBO();
 }

}