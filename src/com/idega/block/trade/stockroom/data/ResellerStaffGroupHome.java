package com.idega.block.trade.stockroom.data;


public interface ResellerStaffGroupHome extends com.idega.data.IDOHome
{
 public ResellerStaffGroup create() throws javax.ejb.CreateException;
 public ResellerStaffGroup createLegacy();
 public ResellerStaffGroup findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ResellerStaffGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ResellerStaffGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}