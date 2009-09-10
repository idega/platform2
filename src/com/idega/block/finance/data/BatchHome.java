package com.idega.block.finance.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface BatchHome extends IDOHome {
	public Batch create() throws CreateException;

	public Batch findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public Batch findUnsent() throws FinderException;

	public Collection findAllNewestFirst() throws FinderException;
}