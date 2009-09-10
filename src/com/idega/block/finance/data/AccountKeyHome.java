package com.idega.block.finance.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface AccountKeyHome extends IDOHome {
	public AccountKey create() throws CreateException;

	public AccountKey findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findBySQL(String sql) throws FinderException;

	public Collection findByCategory(Integer categoryID) throws FinderException;

	public Collection findByPrimaryKeys(String[] keys) throws FinderException;
}