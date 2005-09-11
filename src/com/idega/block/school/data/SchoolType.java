/*
 * Created on 2005-sep-09
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
public interface SchoolType extends IDOEntity {
	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#setSchoolTypeName
	 */
	public void setSchoolTypeName(String name);

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#getSchoolTypeName
	 */
	public String getSchoolTypeName();

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#setSchoolTypeInfo
	 */
	public void setSchoolTypeInfo(String info);

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#getSchoolTypeInfo
	 */
	public String getSchoolTypeInfo();

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#getCategory
	 */
	public SchoolCategory getCategory();

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#getSchoolCategory
	 */
	public String getSchoolCategory();

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#setCategory
	 */
	public void setCategory(SchoolCategory category);

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#setSchoolCategory
	 */
	public void setSchoolCategory(String category);

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#getLocalizationKey
	 */
	public String getLocalizationKey();

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#setLocalizationKey
	 */
	public void setLocalizationKey(String key);

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#getMaxSchoolAge
	 */
	public int getMaxSchoolAge();

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#setMaxSchoolAge
	 */
	public void setMaxSchoolAge(int maxAge);

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#getIsFreetimeType
	 */
	public boolean getIsFreetimeType();

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#setIsFreetimeType
	 */
	public void setIsFreetimeType(boolean isFreetimeType);

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#isSelectable
	 */
	public boolean isSelectable();

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#setSelectable
	 */
	public void setSelectable(boolean isSelectable);

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#getIsFamilyFreetimeType
	 */
	public boolean getIsFamilyFreetimeType();

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#setIsFamilyFreetimeType
	 */
	public void setIsFamilyFreetimeType(boolean isFamilyFreetimeType);

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#getOrder
	 */
	public int getOrder();

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#setOrder
	 */
	public void setOrder(int order);

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#getTypeStringId
	 */
	public String getTypeStringId();

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#setTypeStringId
	 */
	public void setTypeStringId(String typeStringId);

}
