package com.idega.block.school.business;


public class SchoolUserBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolUserBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolUserBusiness.class;
 }


 public SchoolUserBusiness create() throws javax.ejb.CreateException{
  return (SchoolUserBusiness) super.createIBO();
 }



}