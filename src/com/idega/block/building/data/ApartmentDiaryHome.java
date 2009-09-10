package com.idega.block.building.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface ApartmentDiaryHome extends IDOHome {
	public ApartmentDiary create() throws CreateException;

	public ApartmentDiary findByPrimaryKey(Object pk) throws FinderException;
}