/*
 * $Id: NwNews.java,v 1.12 2004/09/13 14:05:21 aron Exp $
 * Created on 11.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.news.data;

import java.util.Collection;


import com.idega.block.category.data.ICCategory;
import com.idega.block.text.data.Content;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDORelationshipException;

/**
 * 
 *  Last modified: $Date: 2004/09/13 14:05:21 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.12 $
 */
public interface NwNews extends IDOLegacyEntity {
    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#getNewsCategoryId
     */
    public int getNewsCategoryId();

    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#getNewsCategory
     */
    public ICCategory getNewsCategory();

    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#setNewsCategoryId
     */
    public void setNewsCategoryId(Integer news_category_id);

    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#setNewsCategoryId
     */
    public void setNewsCategoryId(int news_category_id);

    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#getContentId
     */
    public int getContentId();

    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#setContentId
     */
    public void setContentId(int iContentId);

    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#setContentId
     */
    public void setContentId(Integer iContentId);

    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#getAuthor
     */
    public String getAuthor();

    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#setAuthor
     */
    public void setAuthor(String author);

    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#getSource
     */
    public String getSource();

    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#setSource
     */
    public void setSource(String source);

    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#getContent
     */
    public Content getContent();

    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#getRelatedFiles
     */
    public Collection getRelatedFiles() throws IDORelationshipException;

    /**
     * @see com.idega.block.news.data.NwNewsBMPBean#getLocalizedTexts
     */
    public Collection getLocalizedTexts() throws IDORelationshipException;

}
