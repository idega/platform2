/*
 * $Id: ForumDataHome.java,v 1.4 2005/04/18 11:30:40 gummi Exp $
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
import com.idega.data.IDOHome;


/**
 * 
 *  Last modified: $Date: 2005/04/18 11:30:40 $ by $Author: gummi $
 * 
 * @author <a href="mailto:gummi@idega.com">Gudmundur Agust Saemundsson</a>
 * @version $Revision: 1.4 $
 */
public interface ForumDataHome extends IDOHome {

	public ForumData create() throws javax.ejb.CreateException;

	public ForumData findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	public ForumData findByPrimaryKey(int id) throws javax.ejb.FinderException;

	public ForumData findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#ejbFindAllThreads
	 */
	public Collection findAllThreads(ICCategory category) throws FinderException;

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#ejbFindAllThreads
	 */
	public Collection findAllThreads(ICCategory category, int numberOfReturns) throws FinderException;

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#ejbHomeGetNumberOfThreads
	 */
	public int getNumberOfThreads(ICCategory category) throws EJBException;

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#ejbFindNewestThread
	 */
	public Collection findNewestThread(ICCategory category) throws FinderException;

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#ejbFindThreadsInCategories
	 */
	public Collection findThreadsInCategories(Collection categories, int numberOfReturns) throws FinderException;

	/**
	 * @see com.idega.block.forum.data.ForumDataBMPBean#ejbFindAllTopLevelThreads
	 */
	public Collection findAllTopLevelThreads() throws FinderException;
}
