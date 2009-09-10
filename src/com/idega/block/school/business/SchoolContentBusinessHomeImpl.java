package com.idega.block.school.business;


public class SchoolContentBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolContentBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolContentBusiness.class;
 }


 public SchoolContentBusiness create() throws javax.ejb.CreateException{
  return (SchoolContentBusiness) super.createIBO();
 }



}