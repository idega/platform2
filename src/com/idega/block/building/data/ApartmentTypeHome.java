package com.idega.block.building.data;


public interface ApartmentTypeHome extends com.idega.data.IDOHome
{
 public ApartmentType create() throws javax.ejb.CreateException;
 public ApartmentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findByBuilding(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByCategory(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByComplex(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findFromSameComplex(com.idega.block.building.data.ApartmentType p0)throws javax.ejb.FinderException;

}