/*
 * $Id: VacationTypeHomeImpl.java,v 1.1 2004/11/25 14:22:35 anna Exp $
 * Created on 25.11.2004
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
 * Last modified: 25.11.2004 14:19:45 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public class VacationTypeHomeImpl extends IDOFactory implements VacationTypeHome {

	protected Class getEntityInterfaceClass() {
		return VacationType.class;
	}

	public VacationType create() throws javax.ejb.CreateException {
		return (VacationType) super.createIDO();
	}

	public VacationType findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (VacationType) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((VacationTypeBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
