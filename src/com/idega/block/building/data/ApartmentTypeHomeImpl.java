package com.idega.block.building.data;


public class ApartmentTypeHomeImpl extends com.idega.data.IDOFactory implements ApartmentTypeHome
{
 protected Class getEntityInterfaceClass(){
  return ApartmentType.class;
 }


 public ApartmentType create() throws javax.ejb.CreateException{
  return (ApartmentType) super.createIDO();
 }


 public ApartmentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApartmentType) super.findByPrimaryKeyIDO(pk);
 }



}