/*
 * $Id: MessageContentValue.java,v 1.1 2004/10/11 13:35:41 aron Exp $
 * Created on 7.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.business;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 
 *  Last modified: $Date: 2004/10/11 13:35:41 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class MessageContentValue {
    
    public static final String PARAMETER_TYPE = "msgctp";
    public static final String PARAMETER_LOCALE = "msgctl";
    public static final String PARAMETER_ID = "msgctid";
    
    public Integer ID;
    public String type;
    public String name;
    public String body;
    public Integer creatorID;
    public Date updated;
    public Date created;
    public Locale locale;
    
    /**
     * Gets a String:String map of parameters neccessary to delete a MessageContent
     * @param value
     * @return
     */
    public Map getDeleteParameters() {
        HashMap map = new HashMap(2);
        map.put(PARAMETER_ID,ID.toString());
        map.put(PARAMETER_TYPE,type);
        return map;
    }
    
    /**
     * Gets a String:String map of parameters neccessary to edit a MessageContent
     * @param value
     * @return
     */
    public Map getEditParameters(){
        HashMap map = new HashMap(2);
        map.put(PARAMETER_ID,ID.toString());
        map.put(PARAMETER_TYPE,type);
        return map;
    }
    
    /**
     * Gets a String:String map of parameters neccessary to create a MessageContent
     * @param value
     * @return
     */
    public Map getCreateParameters(){
        HashMap map = new HashMap(1);
        map.put(PARAMETER_TYPE,type);
        return map;
    }
}
