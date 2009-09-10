package com.idega.block.datareport.business;


public class JasperReportBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements JasperReportBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return JasperReportBusiness.class;
 }


 public JasperReportBusiness create() throws javax.ejb.CreateException{
  return (JasperReportBusiness) super.createIBO();
 }



}