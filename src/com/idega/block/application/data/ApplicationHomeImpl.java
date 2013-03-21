package com.idega.block.application.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import java.util.Collection;

public class ApplicationHomeImpl extends IDOFactory implements ApplicationHome {
	public Class getEntityInterfaceClass() {
		return Application.class;
	}

	public Application create() throws CreateException {
		return (Application) super.createIDO();
	}

	public Application findByPrimaryKey(Object pk) throws FinderException {
		return (Application) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApplicationBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByApplicantID(Integer ID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApplicationBMPBean) entity).ejbFindByApplicantID(ID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByApplicantAndStatus(Integer ID, String status)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApplicationBMPBean) entity)
				.ejbFindByApplicantAndStatus(ID, status);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySubjectAndStatus(Integer subjectID, String status)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApplicationBMPBean) entity)
				.ejbFindBySubjectAndStatus(subjectID, status);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByStatus(String status) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApplicationBMPBean) entity).ejbFindByStatus(status);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySubject(Integer subjectID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApplicationBMPBean) entity)
				.ejbFindBySubject(subjectID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySQL(String sql) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApplicationBMPBean) entity).ejbFindBySQL(sql);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}