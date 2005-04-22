/*
 * Created on 2005-apr-21
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.school.data;



import com.idega.data.IDOEntity;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
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
