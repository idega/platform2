/*
 * $Id: ForumDataHomeImpl.java,v 1.4 2005/04/18 11:30:40 gummi Exp $
 * Created on 14.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.forum.data;

import java.util.Collection;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import com.idega.block.category.data.ICCategory;
import com.idega.data.IDOFactory;


/**
 * 
 *  Last modified: $Date: 2005/04/18 11:30:40 $ by $Author: gummi $
 * 
 * @author <a href="mailto:gummi@idega.com">Gudmundur Agust Saemundsson</a>
 * @version $Revision: 1.4 $
 */
public class ForumDataHomeImpl extends IDOFactory implements ForumDataHome {

	protected Class getEntityInterfaceClass() {
		return ForumData.class;
	}

	public ForumData create() throws javax.ejb.CreateException {
		return (ForumData) super.createIDO();
	}

	public ForumData findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (ForumData) super.findByPrimaryKeyIDO(pk);
	}

	public ForumData createLegacy() {
		try {
			return create();
		}
		catch (javax.ejb.CreateException ce) {
			throw new RuntimeException("CreateException:" + ce.getMessage());
		}
	}

	public ForumData findByPrimaryKey(int id) throws javax.ejb.FinderException {
		return (ForumData) super.findByPrimaryKeyIDO(id);
	}

	public ForumData findByPrimaryKeyLegacy(int id) throws java.sql.SQLException {
		try {
			return findByPrimaryKey(id);
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.sql.SQLException("FinderException:" + fe.getMessage());
		}
	}

	public Collection findAllThreads(ICCategory category) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ForumDataBMPBean) entity).ejbFindAllThreads(category);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllThreads(ICCategory category, int numberOfReturns) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ForumDataBMPBean) entity).ejbFindAllThreads(category, numberOfReturns);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getNumberOfThreads(ICCategory category) throws EJBException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ForumDataBMPBean) entity).ejbHomeGetNumberOfThreads(category);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findNewestThread(ICCategory category) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ForumDataBMPBean) entity).ejbFindNewestThread(category);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findThreadsInCategories(Collection categories, int numberOfReturns) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ForumDataBMPBean) entity).ejbFindThreadsInCategories(categories, numberOfReturns);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllTopLevelThreads() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ForumDataBMPBean) entity).ejbFindAllTopLevelThreads();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
