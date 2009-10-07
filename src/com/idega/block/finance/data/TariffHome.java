package com.idega.block.finance.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface TariffHome extends IDOHome {
	public Tariff create() throws CreateException;

	public Tariff findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAllByPrimaryKeyArray(String[] array)
			throws FinderException;

	public Collection findAllByColumnOrdered(String column, String value,
			String order) throws FinderException;

	public Collection findAllByColumnOrdered(String column, String value,
			String column2, String value2, String order) throws FinderException;

	public Collection findAllByColumn(String column, String value)
			throws FinderException;

	public Collection findAllByColumn(String column, int value)
			throws FinderException;

	public Collection findByTariffGroup(Integer groupId) throws FinderException;

	public Collection findByAttribute(String attribute) throws FinderException;
}