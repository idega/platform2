/*
 * $Id: NwNewsHomeImpl.java,v 1.3 2004/09/13 14:05:21 aron Exp $
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

import com.idega.data.IDOFactory;

/**
 * 
 *  Last modified: $Date: 2004/09/13 14:05:21 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.3 $
 */
public class NwNewsHomeImpl extends IDOFactory implements NwNewsHome {
    protected Class getEntityInterfaceClass() {
        return NwNews.class;
    }

    public NwNews create() throws javax.ejb.CreateException {
        return (NwNews) super.createIDO();
    }

    public NwNews findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
        return (NwNews) super.findByPrimaryKeyIDO(pk);
    }

    public NwNews createLegacy() {
        try {
            return create();
        } catch (javax.ejb.CreateException ce) {
            throw new RuntimeException("CreateException:" + ce.getMessage());
        }
    }

    public NwNews findByPrimaryKey(int id) throws javax.ejb.FinderException {
        return (NwNews) super.findByPrimaryKeyIDO(id);
    }

    public NwNews findByPrimaryKeyLegacy(int id) throws java.sql.SQLException {
        try {
            return findByPrimaryKey(id);
        } catch (javax.ejb.FinderException fe) {
            throw new java.sql.SQLException("FinderException:"
                    + fe.getMessage());
        }
    }

    public Collection findPublishedByCategoriesAndLocale(int[] newsCategoryIds,
            int iLocaleId, boolean ignorePublishingDates)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((NwNewsBMPBean) entity)
                .ejbFindPublishedByCategoriesAndLocale(newsCategoryIds,
                        iLocaleId, ignorePublishingDates);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findPublishedByCategory(int newsCategoryId,
            boolean ignorePublishingDates) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((NwNewsBMPBean) entity)
                .ejbFindPublishedByCategory(newsCategoryId,
                        ignorePublishingDates);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

}
