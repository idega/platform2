package com.idega.block.building.data;


public interface BuildingHome extends com.idega.data.IDOHome
{
 public Building create() throws javax.ejb.CreateException;
 public Building findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

 public java.util.Collection getImageFilesByComplex(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByComplex(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByComplex(com.idega.block.building.data.Complex p0)throws javax.ejb.FinderException;


}