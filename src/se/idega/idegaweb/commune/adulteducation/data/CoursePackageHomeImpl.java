/*
 * $Id: CoursePackageHomeImpl.java,v 1.1 2005/07/07 08:41:42 laddi Exp $
 * Created on Jul 6, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;


/**
 * Last modified: $Date: 2005/07/07 08:41:42 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class CoursePackageHomeImpl extends IDOFactory implements CoursePackageHome {

	protected Class getEntityInterfaceClass() {
		return CoursePackage.class;
	}

	public CoursePackage create() throws javax.ejb.CreateException {
		return (CoursePackage) super.createIDO();
	}

	public CoursePackage findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (CoursePackage) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CoursePackageBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
