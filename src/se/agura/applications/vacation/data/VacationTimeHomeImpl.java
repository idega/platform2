/*
 * $Id: VacationTimeHomeImpl.java,v 1.1 2004/11/25 14:22:35 anna Exp $
 * Created on 16.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.vacation.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;


/**
 * Last modified: 16.11.2004 11:27:22 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public class VacationTimeHomeImpl extends IDOFactory implements VacationTimeHome {

	protected Class getEntityInterfaceClass() {
		return VacationTime.class;
	}

	public VacationTime create() throws javax.ejb.CreateException {
		return (VacationTime) super.createIDO();
	}

	public VacationTime findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (VacationTime) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByVacationRequest(VacationRequest request) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((VacationTimeBMPBean) entity).ejbFindAllByVacationRequest(request);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
