package com.idega.block.building.data;


public interface ApartmentCategoryHome extends com.idega.data.IDOHome
{
 public ApartmentCategory create() throws javax.ejb.CreateException;
 public ApartmentCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

 public java.util.Collection findAll()throws javax.ejb.FinderException;


}