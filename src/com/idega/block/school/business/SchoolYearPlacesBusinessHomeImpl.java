package com.idega.block.school.business;


public class SchoolYearPlacesBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolYearPlacesBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolYearPlacesBusiness.class;
 }


 public SchoolYearPlacesBusiness create() throws javax.ejb.CreateException{
  return (SchoolYearPlacesBusiness) super.createIBO();
 }



}