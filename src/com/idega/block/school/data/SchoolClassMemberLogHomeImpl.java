/*
 * $Id: SchoolClassMemberLogHomeImpl.java,v 1.1 2005/01/04 13:52:25 laddi Exp $
 * Created on 27.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/01/04 13:52:25 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class SchoolClassMemberLogHomeImpl extends IDOFactory implements SchoolClassMemberLogHome {

	protected Class getEntityInterfaceClass() {
		return SchoolClassMemberLog.class;
	}

	public SchoolClassMemberLog create() throws javax.ejb.CreateException {
		return (SchoolClassMemberLog) super.createIDO();
	}

	public SchoolClassMemberLog findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (SchoolClassMemberLog) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByUser(User user) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberLogBMPBean) entity).ejbFindAllByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolClassMemberLog findOpenLogByUser(User user) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberLogBMPBean) entity).ejbFindOpenLogByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMemberLog findOpenLogByUserAndSchoolClass(User user, SchoolClass schoolClass) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberLogBMPBean) entity).ejbFindOpenLogByUserAndSchoolClass(user, schoolClass);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMemberLog findLatestLogByUser(User user) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberLogBMPBean) entity).ejbFindLatestLogByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
