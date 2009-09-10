package com.idega.block.building.data;


import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;

public class ApartmentDiaryHomeImpl extends IDOFactory implements ApartmentDiaryHome {
	public Class getEntityInterfaceClass() {
		return ApartmentDiary.class;
	}

	public ApartmentDiary create() throws CreateException {
		return (ApartmentDiary) super.createIDO();
	}

	public ApartmentDiary findByPrimaryKey(Object pk) throws FinderException {
		return (ApartmentDiary) super.findByPrimaryKeyIDO(pk);
	}
}