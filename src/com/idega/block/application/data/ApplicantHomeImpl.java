package com.idega.block.application.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import java.util.Collection;

public class ApplicantHomeImpl extends IDOFactory implements ApplicantHome {
	public Class getEntityInterfaceClass() {
		return Applicant.class;
	}

	public Applicant create() throws CreateException {
		return (Applicant) super.createIDO();
	}

	public Applicant findByPrimaryKey(Object pk) throws FinderException {
		return (Applicant) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findBySSN(String SSN) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApplicantBMPBean) entity).ejbFindBySSN(SSN);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByApplicationStatusOrderedBy(String status,
			String order) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApplicantBMPBean) entity)
				.ejbFindByApplicationStatusOrderedBy(status, order);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySQL(String sql) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApplicantBMPBean) entity).ejbFindBySQL(sql);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}