package com.idega.block.finance.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface TariffIndexHome extends IDOHome {
	public TariffIndex create() throws CreateException;

	public TariffIndex findByPrimaryKey(Object pk) throws FinderException;

	public TariffIndex findLastByType(String type) throws FinderException;

	public Collection findLastTypeGrouped() throws FinderException;
}