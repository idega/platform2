/*
 * $Id: NwNewsHome.java,v 1.3 2004/09/13 14:05:21 aron Exp $
 * Created on 11.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.news.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * 
 *  Last modified: $Date: 2004/09/13 14:05:21 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.3 $
 */
public interface NwNewsHome extends IDOHome {
    public NwNews create() throws javax.ejb.CreateException;

    public NwNews findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

    public NwNews findByPrimaryKey(int id) throws javax.ejb.FinderException;

    public NwNews findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#ejbFindPublishedByCategoriesAndLocale
     */
    public Collection findPublishedByCategoriesAndLocale(int[] newsCategoryIds,
            int iLocaleId, boolean ignorePublishingDates)
            throws FinderException;

    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#ejbFindPublishedByCategory
     */
    public Collection findPublishedByCategory(int newsCategoryId,
            boolean ignorePublishingDates) throws FinderException;

}
