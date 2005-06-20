/*
 * $Id: SchoolClassMemberGradeHomeImpl.java,v 1.1 2005/06/20 12:55:48 laddi Exp $
 * Created on Jun 20, 2005
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
 * Last modified: $Date: 2005/06/20 12:55:48 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class SchoolClassMemberGradeHomeImpl extends IDOFactory implements SchoolClassMemberGradeHome {

	protected Class getEntityInterfaceClass() {
		return SchoolClassMemberGrade.class;
	}

	public SchoolClassMemberGrade create() throws javax.ejb.CreateException {
		return (SchoolClassMemberGrade) super.createIDO();
	}

	public SchoolClassMemberGrade findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (SchoolClassMemberGrade) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByStudent(SchoolClassMember student) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberGradeBMPBean) entity).ejbFindAllByStudent(student);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolClassMemberGrade findByStudent(SchoolClassMember student) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberGradeBMPBean) entity).ejbFindByStudent(student);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}
