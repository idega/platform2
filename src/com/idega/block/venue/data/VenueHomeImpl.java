package com.idega.block.venue.data;


public class VenueHomeImpl extends com.idega.data.IDOFactory implements VenueHome
{
 protected Class getEntityInterfaceClass(){
  return Venue.class;
 }


 public Venue create() throws javax.ejb.CreateException{
  return (Venue) super.createIDO();
 }


 public Venue findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Venue) super.findByPrimaryKeyIDO(pk);
 }



}