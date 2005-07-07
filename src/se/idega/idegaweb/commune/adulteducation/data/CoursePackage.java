/*
 * $Id: CoursePackage.java,v 1.1 2005/07/07 08:41:42 laddi Exp $
 * Created on Jul 6, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.data;

import com.idega.data.IDOEntity;


/**
 * Last modified: $Date: 2005/07/07 08:41:42 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface CoursePackage extends IDOEntity {

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.CoursePackageBMPBean#getName
	 */
	public String getName();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.CoursePackageBMPBean#getLocalizedKey
	 */
	public String getLocalizedKey();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.CoursePackageBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.CoursePackageBMPBean#setLocalizedKey
	 */
	public void setLocalizedKey(String key);
}
