package com.idega.block.building.data;


public interface ApartmentViewHome extends com.idega.data.IDOHome
{
 public ApartmentView create() throws javax.ejb.CreateException;
 public ApartmentView findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findByApartmentName(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findByBuilding(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByCategory(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByComplex(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByFloor(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByType(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByTypeAndComplex(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException;

}