package com.idega.block.venue.data;


public interface VenueHome extends com.idega.data.IDOHome
{
 public Venue create() throws javax.ejb.CreateException;
 public Venue findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}