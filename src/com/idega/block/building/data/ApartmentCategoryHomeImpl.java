package com.idega.block.building.data;


public class ApartmentCategoryHomeImpl extends com.idega.data.IDOFactory implements ApartmentCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return ApartmentCategory.class;
 }


 public ApartmentCategory create() throws javax.ejb.CreateException{
  return (ApartmentCategory) super.createIDO();
 }


 public ApartmentCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApartmentCategory) super.findByPrimaryKeyIDO(pk);
 }



}