/*
 * $Id: VacationTypeHomeImpl.java,v 1.2 2004/12/13 14:44:20 anna Exp $
 * Created on 8.12.2004
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
 * Last modified: 8.12.2004 14:05:32 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.2 $
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
