package com.idega.block.school.data;


public interface SchoolCategoryHome extends com.idega.data.IDOHome
{
 public SchoolCategory create() throws javax.ejb.CreateException;
 public SchoolCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllCategories()throws javax.ejb.FinderException;

}