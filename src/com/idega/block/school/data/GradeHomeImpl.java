/*
 * $Id: GradeHomeImpl.java,v 1.1 2005/06/20 12:55:48 laddi Exp $
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
public class GradeHomeImpl extends IDOFactory implements GradeHome {

	protected Class getEntityInterfaceClass() {
		return Grade.class;
	}

	public Grade create() throws javax.ejb.CreateException {
		return (Grade) super.createIDO();
	}

	public Grade findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Grade) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllBySchoolType(SchoolType type) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((GradeBMPBean) entity).ejbFindAllBySchoolType(type);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
