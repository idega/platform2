/*
 * $Id: MessageContentHome.java,v 1.1 2004/10/11 13:35:42 aron Exp $
 * Created on 8.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.data;

import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.message.business.MessageContentValue;

import com.idega.data.IDOHome;
import com.idega.data.query.SelectQuery;

/**
 * 
 *  Last modified: $Date: 2004/10/11 13:35:42 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface MessageContentHome extends IDOHome {
    public MessageContent create() throws javax.ejb.CreateException;

    public MessageContent findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#ejbFindAll
     */
    public Collection findAll() throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#ejbFindByCategory
     */
    public Collection findByCategory(String category) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#ejbFindByCategoryAndLocale
     */
    public Collection findByCategoryAndLocale(String category, Integer locale)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#ejbFindByValue
     */
    public Collection findByValue(MessageContentValue value)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#ejbHomeGetFindQuery
     */
    public SelectQuery getFindQuery(MessageContentValue value);

}
