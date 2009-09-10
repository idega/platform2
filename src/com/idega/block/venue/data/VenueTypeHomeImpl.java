package com.idega.block.venue.data;


public class VenueTypeHomeImpl extends com.idega.data.IDOFactory implements VenueTypeHome
{
 protected Class getEntityInterfaceClass(){
  return VenueType.class;
 }


 public VenueType create() throws javax.ejb.CreateException{
  return (VenueType) super.createIDO();
 }


 public VenueType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (VenueType) super.findByPrimaryKeyIDO(pk);
 }



}