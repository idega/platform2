package com.idega.block.school.data;



public interface SchoolSubAreaHome extends com.idega.data.IDOHome
{
 public SchoolSubArea create() throws javax.ejb.CreateException;
 public SchoolSubArea findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByArea(SchoolArea p0)throws javax.ejb.FinderException;

}