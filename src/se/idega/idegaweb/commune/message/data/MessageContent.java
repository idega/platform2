/*
 * $Id: MessageContent.java,v 1.1 2004/10/11 13:35:42 aron Exp $
 * Created on 8.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.data;

import java.sql.Timestamp;
import java.util.Locale;



import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2004/10/11 13:35:42 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface MessageContent extends IDOEntity {
    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#setCategory
     */
    public void setCategory(String category);

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#getCategory
     */
    public String getCategory();

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#setContentName
     */
    public void setContentName(String name);

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#getContentName
     */
    public String getContentName();

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#setContentBody
     */
    public void setContentBody(String body);

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#getContentBody
     */
    public String getContentBody();

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#setCreated
     */
    public void setCreated(Timestamp stamp);

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#getCreated
     */
    public Timestamp getCreated();

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#setUpdated
     */
    public void setUpdated(Timestamp stamp);

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#getUpdated
     */
    public Timestamp getUpdated();

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#setCreator
     */
    public void setCreator(User user);

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#setCreator
     */
    public void setCreator(Integer userID);

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#getCreatorID
     */
    public Integer getCreatorID();

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#getCreator
     */
    public User getCreator();

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#setLocale
     */
    public void setLocale(ICLocale locale);

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#setLocaleId
     */
    public void setLocaleId(Locale locale);

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#getLocaleString
     */
    public String getLocaleString();

    /**
     * @see se.idega.idegaweb.commune.message.data.MessageContentBMPBean#getLocale
     */
    public Locale getLocale();

}
