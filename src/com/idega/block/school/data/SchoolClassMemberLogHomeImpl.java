/*
 * $Id: SchoolClassMemberLogHomeImpl.java,v 1.3 2005/01/19 09:39:51 anders Exp $
 * Created on 10.1.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;


/**
 * Last modified: $Date: 2005/01/19 09:39:51 $ by $Author: anders $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
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

	public SchoolClassMemberLog findByPlacementAndDate(SchoolClassMember member, Date date) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberLogBMPBean) entity).ejbFindByPlacementAndDate(member, date);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}