/*
 * $Id: MessageContentBMPBean.java,v 1.1 2004/10/11 13:35:42 aron Exp $
 * Created on 5.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.data;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Locale;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.message.business.MessageContentValue;

import com.idega.core.localisation.data.ICLocale;
import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2004/10/11 13:35:42 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class MessageContentBMPBean extends GenericEntity   implements MessageContent{
    
    private final static String TABLE_NAME = "MSG_CONTENT";
    private final static String CONTENT_NAME = "CONTENT_NAME";
    private final static String CONTENT_BODY = "CONTENT_BODY";
    private final static String CREATED = "CREATED";
    private final static String UPDATED = "UPDATED";
    private final static String CREATOR = "CREATOR";
    private final static String LOCALE = "LOCALE_ID";
    private final static String CATEGORY = "CATEGORY";

    /* (non-Javadoc)
     * @see com.idega.data.GenericEntity#getEntityName()
     */
    public String getEntityName() {
       return TABLE_NAME;
    }

    /* (non-Javadoc)
     * @see com.idega.data.GenericEntity#initializeAttributes()
     */
    public void initializeAttributes() {
	  addAttribute(getIDColumnName());
	  addAttribute(CONTENT_NAME, "Name", true, true, String.class);
	  addAttribute(CONTENT_BODY, "Body", true, true, String.class,30000);
	  addAttribute(CATEGORY,"Category",true,true,String.class,50);
	  addAttribute(CREATED, "Created", true, true, Timestamp.class);
      addAttribute(UPDATED, "Updated", true, true, Timestamp.class);
      addAttribute(CREATOR,"creator",true,true,Integer.class,MANY_TO_ONE,User.class);
      addAttribute(LOCALE, "Locale", true, true, java.lang.String.class);
	}
    
    public void setCategory(String category){
        setColumn(CATEGORY,category);
    }
    
    public String getCategory(){
        return getStringColumnValue(CATEGORY);
    }
    
    public void setContentName(String name){
        setColumn(CONTENT_NAME,name);
    }
    
    public String getContentName(){
        return getStringColumnValue(CONTENT_NAME);
    }
    
    public void setContentBody(String body){
        setColumn(CONTENT_BODY,body);
    }
    
    public String getContentBody(){
        return getStringColumnValue(CONTENT_BODY);
    }
    
    public void setCreated(Timestamp stamp){
        setColumn(CREATED,stamp);
    }
    
    public Timestamp getCreated(){
        return getTimestampColumnValue(CREATED);
    }
    
    public void setUpdated(Timestamp stamp){
        setColumn(UPDATED,stamp);
    }
    
    public Timestamp getUpdated(){
        return getTimestampColumnValue(UPDATED);
    }
    
    public void setCreator(User user){
        setColumn(CREATOR,user);
    	}
    
    public void setCreator(Integer userID){
        	setColumn(CREATOR,userID);
    }
    
    public Integer getCreatorID(){
        return getIntegerColumnValue(CREATOR);
    }
    
    public User getCreator(){
        return (User) getColumnValue(CREATOR);
    }
    
    public void setLocale(ICLocale locale){
        setColumn(LOCALE,locale);
    }
    
    public void setLocaleId(Locale locale){
        setColumn(LOCALE,locale);
    }
    
    public String getLocaleString(){
        return getStringColumnValue(LOCALE);
    }
    
    public Locale getLocale(){
        String loc = getStringColumnValue(LOCALE);
        if(loc!=null)
            return new Locale(loc);
        return null;
    }
    
    public Collection ejbFindAll()throws FinderException{
        Table table = new Table(this);
        SelectQuery query = new SelectQuery(table);
        query.addColumn(new WildCardColumn());
        return idoFindPKsByQuery(query);
    }
    
    public Collection ejbFindByCategory(String category)throws FinderException{
        Table table = new Table(this);
        SelectQuery query = new SelectQuery(table);
        query.addColumn(new WildCardColumn());
        query.addCriteria(new MatchCriteria(table,CATEGORY,MatchCriteria.EQUALS,category,true));
        return idoFindPKsByQuery(query);
    }
    
    public Collection ejbFindByCategoryAndLocale(String category,Integer locale)throws FinderException{
        Table table = new Table(this);
        SelectQuery query = new SelectQuery(table);
        query.addColumn(new WildCardColumn());
        query.addCriteria(new MatchCriteria(table,CATEGORY,MatchCriteria.EQUALS,category,true));
        query.addCriteria(new MatchCriteria(table,LOCALE,MatchCriteria.EQUALS,locale));
        return idoFindPKsByQuery(query);
    }
    
    public Collection ejbFindByValue(MessageContentValue value)throws FinderException{
        return idoFindPKsByQuery(ejbHomeGetFindQuery(value));
    }
    
    
    public SelectQuery ejbHomeGetFindQuery(MessageContentValue value){
        Table table = new Table(this);
        SelectQuery query = new SelectQuery(table);
        query.addColumn(new WildCardColumn());
        if(value.type!=null)
            query.addCriteria(new MatchCriteria(table,CATEGORY,MatchCriteria.EQUALS,value.type,true));
        if(value.name!=null)
            query.addCriteria(new MatchCriteria(table,CONTENT_NAME,MatchCriteria.EQUALS,value.name,true));
        if(value.creatorID!=null)
            query.addCriteria(new MatchCriteria(table,CREATOR,MatchCriteria.EQUALS,value.creatorID));
        if(value.locale!=null)
            query.addCriteria(new MatchCriteria(table,LOCALE,MatchCriteria.EQUALS,value.locale.toString(),true));
        
        return query;
    } 
}
