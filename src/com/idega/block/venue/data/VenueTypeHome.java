package com.idega.block.venue.data;


public interface VenueTypeHome extends com.idega.data.IDOHome
{
 public VenueType create() throws javax.ejb.CreateException;
 public VenueType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}