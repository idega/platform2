package com.idega.block.school.business;


public class SchoolClassMemberBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolClassMemberBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolClassMemberBusiness.class;
 }


 public SchoolClassMemberBusiness create() throws javax.ejb.CreateException{
  return (SchoolClassMemberBusiness) super.createIBO();
 }



}