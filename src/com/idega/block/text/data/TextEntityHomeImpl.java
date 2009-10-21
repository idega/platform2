package com.idega.block.text.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

public class TextEntityHomeImpl extends IDOFactory implements TextEntityHome {
	public Class getEntityInterfaceClass() {
		return TextEntity.class;
	}

	public TextEntity create() throws CreateException {
		return (TextEntity) super.createIDO();
	}

	public TextEntity findByPrimaryKey(Object pk) throws FinderException {
		return (TextEntity) super.findByPrimaryKeyIDO(pk);
	}
}