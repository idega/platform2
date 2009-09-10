package com.idega.block.help.business;


public class HelpBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements HelpBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return HelpBusiness.class;
 }


 public HelpBusiness create() throws javax.ejb.CreateException{
  return (HelpBusiness) super.createIBO();
 }



}