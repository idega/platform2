package com.idega.block.school.business;


public class SchoolBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolBusiness.class;
 }


 public SchoolBusiness create() throws javax.ejb.CreateException{
  return (SchoolBusiness) super.createIBO();
 }



}