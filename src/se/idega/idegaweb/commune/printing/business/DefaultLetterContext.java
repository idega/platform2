/*
 * $Id: DefaultLetterContext.java,v 1.1 2004/11/04 20:34:48 aron Exp $
 * Created on 15.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.printing.business;

import java.io.File;
import java.io.IOException;

import se.idega.idegaweb.commune.message.data.Message;

import com.idega.idegaweb.IWUserContext;

/**
 * 
 *  Last modified: $Date: 2004/11/04 20:34:48 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class DefaultLetterContext extends MessageLetterContext {
    
    public DefaultLetterContext(IWUserContext iwuc ,Message msg) {
        super(iwuc,msg);      
        init(iwuc,msg);
    }
    
    private void init(IWUserContext iwuc,Message msg){
        setResourceDirectory(new File(getResourceUrl(getBundle(iwuc),iwuc.getCurrentLocale())));
        try {
            setTemplateStream(getTemplateUrlAsStream(getBundle(iwuc),iwuc.getCurrentLocale(),"default_template.xml",true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}