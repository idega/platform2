/*
 * $Id: MessageContentServiceBean.java,v 1.2 2004/10/12 06:57:51 laddi Exp $
 * Created on 7.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.message.data.MessageContent;
import se.idega.idegaweb.commune.message.data.MessageContentHome;

import com.idega.business.IBOServiceBean;
import com.idega.util.IWTimestamp;

/**
 * 
 *  Last modified: $Date: 2004/10/12 06:57:51 $ by $Author: laddi $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.2 $
 */
public class MessageContentServiceBean extends IBOServiceBean  implements MessageContentService{
    
    public Collection getValues(MessageContentValue value) throws RemoteException, FinderException{
        Collection contents = getHome().findByValue(value);
        ArrayList values = new ArrayList(contents.size());
        for (Iterator iter = contents.iterator(); iter.hasNext();) {
            values.add(getValue((MessageContent)iter.next()));
            
        }
        return values;
    }
    
    public MessageContentValue storeValue(MessageContentValue value) throws FinderException, RemoteException, CreateException{
        MessageContent content;
        if(value.ID!=null)
            content = getHome().findByPrimaryKey(value.ID);
        else{
            content = getHome().create();
            content.setCreated(IWTimestamp.getTimestampRightNow());
        }
        content.setContentName(value.name);
        content.setContentBody(value.body);
        content.setCategory(value.type);
        content.setUpdated(IWTimestamp.getTimestampRightNow());
        content.setCreator(value.creatorID);
        content.setLocaleId(value.locale);
        content.store();
        return getValue(content);
    }
    
    public void removeValue(Integer ID) throws RemoteException, RemoveException, FinderException{
        getHome().findByPrimaryKey(ID).remove();
    }
    
    public MessageContentValue getValue(Integer valueID) throws RemoteException, FinderException{
        return getValue(getHome().findByPrimaryKey(valueID));
    }
    
    protected MessageContentValue getValue(MessageContent content) {
        MessageContentValue value = new MessageContentValue();
        value.ID = (Integer)content.getPrimaryKey();
        value.name = content.getContentName();
        value.body = content.getContentBody();
        value.locale = content.getLocale();
        value.created = content.getCreated();
        value.updated = content.getUpdated();
        value.creatorID = content.getCreatorID();
        value.type = content.getCategory();
        return value;
    }
    
    public MessageContentHome getHome() throws RemoteException{
        return (MessageContentHome)getIDOHome(MessageContent.class);
    }
    
}
