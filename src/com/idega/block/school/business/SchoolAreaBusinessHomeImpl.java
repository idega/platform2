package com.idega.block.school.business;


public class SchoolAreaBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolAreaBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolAreaBusiness.class;
 }


 public SchoolAreaBusiness create() throws javax.ejb.CreateException{
  return (SchoolAreaBusiness) super.createIBO();
 }



}