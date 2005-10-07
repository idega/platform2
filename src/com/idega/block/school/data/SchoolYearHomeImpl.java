/*
 * $Id: SchoolYearHomeImpl.java,v 1.11 2005/10/07 13:23:39 laddi Exp $
 * Created on Oct 7, 2005
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
 * Last modified: $Date: 2005/10/07 13:23:39 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.11 $
 */
public class SchoolYearHomeImpl extends IDOFactory implements SchoolYearHome {

	protected Class getEntityInterfaceClass() {
		return SchoolYear.class;
	}

	public SchoolYear create() throws javax.ejb.CreateException {
		return (SchoolYear) super.createIDO();
	}

	public SchoolYear findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (SchoolYear) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllSchoolYears() throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolYearBMPBean) entity).ejbFindAllSchoolYears();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllSchoolYearBySchoolType(int schoolTypeId) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolYearBMPBean) entity).ejbFindAllSchoolYearBySchoolType(schoolTypeId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllSchoolYearBySchoolCategory(SchoolCategory schoolCategory) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolYearBMPBean) entity).ejbFindAllSchoolYearBySchoolCategory(schoolCategory);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllSchoolYearsBySchoolCategory(SchoolCategory schoolCategory, boolean showSelectable)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolYearBMPBean) entity).ejbFindAllSchoolYearsBySchoolCategory(schoolCategory,
				showSelectable);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySchoolAndSchoolCategory(School school, SchoolCategory schoolCategory,
			boolean showSelectable) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolYearBMPBean) entity).ejbFindAllBySchoolAndSchoolCategory(school, schoolCategory,
				showSelectable);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByAge(int age) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolYearBMPBean) entity).ejbFindAllByAge(age);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByAge(SchoolType schoolType, int age) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolYearBMPBean) entity).ejbFindAllByAge(schoolType, age);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolYear findByAge(SchoolCategory category, int age) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolYearBMPBean) entity).ejbFindByAge(category, age);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllByIDs(String[] schoolYearIDs) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolYearBMPBean) entity).ejbFindAllByIDs(schoolYearIDs);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolYear findByYearName(String name) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolYearBMPBean) entity).ejbFindByYearName(name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolYear findByYearName(SchoolType schoolType, String name) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolYearBMPBean) entity).ejbFindByYearName(schoolType, name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolYear findByYearName(SchoolCategory schoolCategory, String name) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolYearBMPBean) entity).ejbFindByYearName(schoolCategory, name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findBySchoolCategory(String schoolCategory) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolYearBMPBean) entity).ejbFindBySchoolCategory(schoolCategory);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolYear findPreviousSchoolYearFromAge(SchoolYear year) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolYearBMPBean) entity).ejbFindPreviousSchoolYearFromAge(year);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}
