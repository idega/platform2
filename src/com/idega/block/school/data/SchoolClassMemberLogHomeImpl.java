/*
 * $Id: SchoolClassMemberLogHomeImpl.java,v 1.2 2005/01/10 14:05:36 laddi Exp $
 * Created on 10.1.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;


/**
 * Last modified: $Date: 2005/01/10 14:05:36 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
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

	public Collection findAllBySchoolClassMember(SchoolClassMember member) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberLogBMPBean) entity).ejbFindAllBySchoolClassMember(member);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolClassMemberLog findOpenLogByUser(SchoolClassMember member) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberLogBMPBean) entity).ejbFindOpenLogByUser(member);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMemberLog findOpenLogByUserAndSchoolClass(SchoolClassMember member, SchoolClass schoolClass) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberLogBMPBean) entity).ejbFindOpenLogByUserAndSchoolClass(member, schoolClass);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMemberLog findLatestLogByUser(SchoolClassMember member) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberLogBMPBean) entity).ejbFindLatestLogByUser(member);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
