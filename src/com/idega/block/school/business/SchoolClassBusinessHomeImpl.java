package com.idega.block.school.business;


public class SchoolClassBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolClassBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolClassBusiness.class;
 }


 public SchoolClassBusiness create() throws javax.ejb.CreateException{
  return (SchoolClassBusiness) super.createIBO();
 }



}