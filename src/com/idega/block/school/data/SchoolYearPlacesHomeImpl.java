package com.idega.block.school.data;


public class SchoolYearPlacesHomeImpl extends com.idega.data.IDOFactory implements SchoolYearPlacesHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolYearPlaces.class;
 }


 public SchoolYearPlaces create() throws javax.ejb.CreateException{
  return (SchoolYearPlaces) super.createIDO();
 }


 public SchoolYearPlaces findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolYearPlaces) super.findByPrimaryKeyIDO(pk);
 }



}