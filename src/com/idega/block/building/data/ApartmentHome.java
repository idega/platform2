package com.idega.block.building.data;


public interface ApartmentHome extends com.idega.data.IDOHome
{
 public Apartment create() throws javax.ejb.CreateException;
 public Apartment findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByFloor(com.idega.block.building.data.Floor p0)throws javax.ejb.FinderException;
 public java.util.Collection findByFloor(java.lang.Integer p0)throws javax.ejb.FinderException;

}