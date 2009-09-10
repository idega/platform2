package com.idega.block.category.data;

import com.idega.data.IDOException;


public interface ICInformationCategoryHome extends com.idega.data.IDOHome
{
 public ICInformationCategory create() throws javax.ejb.CreateException;
 public ICInformationCategory createLegacy();
 public ICInformationCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ICInformationCategory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ICInformationCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findAvailableCategories(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findAvailableTopNodeCategories(int p0,int p1)throws javax.ejb.FinderException;
 public boolean hasAvailableCategory(int icObjectID) throws IDOException;
}