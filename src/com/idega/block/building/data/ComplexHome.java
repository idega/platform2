package com.idega.block.building.data;


public interface ComplexHome extends com.idega.data.IDOHome
{
 public Complex create() throws javax.ejb.CreateException;
 public Complex findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}