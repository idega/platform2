package com.idega.block.school.business;


public class SchoolYearBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolYearBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolYearBusiness.class;
 }


 public SchoolYearBusiness create() throws javax.ejb.CreateException{
  return (SchoolYearBusiness) super.createIBO();
 }



}