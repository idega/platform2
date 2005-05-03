/*
 * Created on 2005-maj-02
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface SchoolCategoryHome extends IDOHome {
	public SchoolCategory create() throws javax.ejb.CreateException;

	public SchoolCategory findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolCategoryBMPBean#ejbFindAllCategories
	 */
	public Collection findAllCategories() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolCategoryBMPBean#ejbFindByLocalizedKey
	 */
	public SchoolCategory findByLocalizedKey(String key) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolCategoryBMPBean#ejbFindChildcareCategory
	 */
	public SchoolCategory findChildcareCategory() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolCategoryBMPBean#ejbFindElementarySchoolCategory
	 */
	public SchoolCategory findElementarySchoolCategory() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolCategoryBMPBean#ejbFindHighSchoolCategory
	 */
	public SchoolCategory findHighSchoolCategory() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolCategoryBMPBean#ejbFindCollegeCategory
	 */
	public SchoolCategory findCollegeCategory() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolCategoryBMPBean#ejbFindUniversityCategory
	 */
	public SchoolCategory findUniversityCategory() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolCategoryBMPBean#ejbFindAdultEducationCategory
	 */
	public SchoolCategory findAdultEducationCategory() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolCategoryBMPBean#ejbFindMusicSchoolCategory
	 */
	public SchoolCategory findMusicSchoolCategory() throws FinderException;

}
