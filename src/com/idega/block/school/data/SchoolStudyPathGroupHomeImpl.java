/*
 * $Id: SchoolStudyPathGroupHomeImpl.java,v 1.2 2005/05/11 07:14:19 laddi Exp $
 * Created on 28.4.2005
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
 * Last modified: $Date: 2005/05/11 07:14:19 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class SchoolStudyPathGroupHomeImpl extends IDOFactory implements SchoolStudyPathGroupHome {

	protected Class getEntityInterfaceClass() {
		return SchoolStudyPathGroup.class;
	}

	public SchoolStudyPathGroup create() throws javax.ejb.CreateException {
		return (SchoolStudyPathGroup) super.createIDO();
	}

	public SchoolStudyPathGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (SchoolStudyPathGroup) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllStudyPathGroups() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathGroupBMPBean) entity).ejbFindAllStudyPathGroups();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolStudyPathGroup findByGroupName(String name) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolStudyPathGroupBMPBean) entity).ejbFindByGroupName(name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}
