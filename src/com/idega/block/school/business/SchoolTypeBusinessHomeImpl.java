package com.idega.block.school.business;


public class SchoolTypeBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolTypeBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolTypeBusiness.class;
 }


 public SchoolTypeBusiness create() throws javax.ejb.CreateException{
  return (SchoolTypeBusiness) super.createIBO();
 }



}