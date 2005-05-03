/*
 * Created on 2005-maj-02
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
public class SchoolCategoryHomeImpl extends IDOFactory implements
		SchoolCategoryHome {
	protected Class getEntityInterfaceClass() {
		return SchoolCategory.class;
	}

	public SchoolCategory create() throws javax.ejb.CreateException {
		return (SchoolCategory) super.createIDO();
	}

	public SchoolCategory findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (SchoolCategory) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllCategories() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolCategoryBMPBean) entity)
				.ejbFindAllCategories();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolCategory findByLocalizedKey(String key) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolCategoryBMPBean) entity).ejbFindByLocalizedKey(key);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolCategory findChildcareCategory() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolCategoryBMPBean) entity).ejbFindChildcareCategory();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolCategory findElementarySchoolCategory() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolCategoryBMPBean) entity)
				.ejbFindElementarySchoolCategory();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolCategory findHighSchoolCategory() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolCategoryBMPBean) entity)
				.ejbFindHighSchoolCategory();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolCategory findCollegeCategory() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolCategoryBMPBean) entity).ejbFindCollegeCategory();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolCategory findUniversityCategory() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolCategoryBMPBean) entity)
				.ejbFindUniversityCategory();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolCategory findAdultEducationCategory() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolCategoryBMPBean) entity)
				.ejbFindAdultEducationCategory();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolCategory findMusicSchoolCategory() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolCategoryBMPBean) entity)
				.ejbFindMusicSchoolCategory();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
