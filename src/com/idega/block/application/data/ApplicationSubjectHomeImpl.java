package com.idega.block.application.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class ApplicationSubjectHomeImpl extends IDOFactory implements
		ApplicationSubjectHome {
	public Class getEntityInterfaceClass() {
		return ApplicationSubject.class;
	}

	public ApplicationSubject create() throws CreateException {
		return (ApplicationSubject) super.createIDO();
	}

	public ApplicationSubject findByPrimaryKey(Object pk)
			throws FinderException {
		return (ApplicationSubject) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApplicationSubjectBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findNonExpired() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApplicationSubjectBMPBean) entity)
				.ejbFindNonExpired();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}