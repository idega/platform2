/*
 * $Id: SchoolStudyPathGroup.java,v 1.2 2005/05/11 07:14:19 laddi Exp $
 * Created on 28.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import com.idega.data.IDOEntity;


/**
 * Last modified: $Date: 2005/05/11 07:14:19 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface SchoolStudyPathGroup extends IDOEntity {

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathGroupBMPBean#getGroupName
	 */
	public String getGroupName();

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathGroupBMPBean#setGroupName
	 */
	public void setGroupName(String groupname);

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathGroupBMPBean#getLocalizationKey
	 */
	public String getLocalizationKey();

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathGroupBMPBean#setLocalizationKey
	 */
	public void setLocalizationKey(String localizedkey);
}
