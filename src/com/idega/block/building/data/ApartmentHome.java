package com.idega.block.building.data;


public interface ApartmentHome extends com.idega.data.IDOHome
{
 public Apartment create() throws javax.ejb.CreateException;
 public Apartment findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findByFloor(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByName(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySQL(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySearch(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.Integer p4,boolean p5)throws javax.ejb.FinderException;
 public java.util.Collection findByType(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findeByTypeAndComplex(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException;
 public int getRentableCount()throws com.idega.data.IDOException;
 public int getTypeAndComplexCount(java.lang.Integer p0,java.lang.Integer p1)throws com.idega.data.IDOException;
 public java.util.Collection findByFloor(com.idega.block.building.data.Floor p0)throws javax.ejb.FinderException;


}