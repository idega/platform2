package com.idega.block.trade.stockroom.data;


public interface SupplierStaffGroupHome extends com.idega.data.IDOHome
{
 public SupplierStaffGroup create() throws javax.ejb.CreateException;
 public SupplierStaffGroup createLegacy();
 public SupplierStaffGroup findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public SupplierStaffGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public SupplierStaffGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}