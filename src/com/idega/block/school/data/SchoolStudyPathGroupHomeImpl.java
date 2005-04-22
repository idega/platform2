/*
 * Created on 2005-apr-21
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SchoolStudyPathGroupHomeImpl extends IDOFactory implements
		SchoolStudyPathGroupHome {
	protected Class getEntityInterfaceClass() {
		return SchoolStudyPathGroup.class;
	}

	public SchoolStudyPathGroup create() throws javax.ejb.CreateException {
		return (SchoolStudyPathGroup) super.createIDO();
	}

	public SchoolStudyPathGroup findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (SchoolStudyPathGroup) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllStudyPathGroups() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathGroupBMPBean) entity)
				.ejbFindAllStudyPathGroups();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
