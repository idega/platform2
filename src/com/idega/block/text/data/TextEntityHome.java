package com.idega.block.text.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface TextEntityHome extends IDOHome {
	public TextEntity create() throws CreateException;

	public TextEntity findByPrimaryKey(Object pk) throws FinderException;
}