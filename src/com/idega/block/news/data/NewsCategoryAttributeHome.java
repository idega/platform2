package com.idega.block.news.data;


public interface NewsCategoryAttributeHome extends com.idega.data.IDOHome
{
 public NewsCategoryAttribute create() throws javax.ejb.CreateException;
 public NewsCategoryAttribute createLegacy();
 public NewsCategoryAttribute findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public NewsCategoryAttribute findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public NewsCategoryAttribute findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}