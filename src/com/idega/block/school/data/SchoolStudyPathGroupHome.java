/*
 * $Id: SchoolStudyPathGroupHome.java,v 1.2 2005/05/11 07:14:19 laddi Exp $
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
import com.idega.data.IDOHome;


/**
 * Last modified: $Date: 2005/05/11 07:14:19 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface SchoolStudyPathGroupHome extends IDOHome {

	public SchoolStudyPathGroup create() throws javax.ejb.CreateException;

	public SchoolStudyPathGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathGroupBMPBean#ejbFindAllStudyPathGroups
	 */
	public Collection findAllStudyPathGroups() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathGroupBMPBean#ejbFindByGroupName
	 */
	public SchoolStudyPathGroup findByGroupName(String name) throws FinderException;
}
