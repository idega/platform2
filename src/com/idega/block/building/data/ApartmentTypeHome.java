package com.idega.block.building.data;


public interface ApartmentTypeHome extends com.idega.data.IDOHome
{
 public ApartmentType create() throws javax.ejb.CreateException;
 public ApartmentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}