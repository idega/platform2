package com.idega.block.school.business;


public class SchoolSeasonBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolSeasonBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolSeasonBusiness.class;
 }


 public SchoolSeasonBusiness create() throws javax.ejb.CreateException{
  return (SchoolSeasonBusiness) super.createIBO();
 }



}