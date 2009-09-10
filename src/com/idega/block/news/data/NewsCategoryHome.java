package com.idega.block.news.data;


public interface NewsCategoryHome extends com.idega.data.IDOHome
{
 public NewsCategory create() throws javax.ejb.CreateException;
 public NewsCategory createLegacy();
 public NewsCategory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public NewsCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public NewsCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}