/*
 * $Id: PasswordLetterContext.java,v 1.1 2004/11/04 20:34:48 aron Exp $
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
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import se.idega.idegaweb.commune.message.data.Message;

import com.idega.idegaweb.IWUserContext;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;

/**
 * 
 *  Last modified: $Date: 2004/11/04 20:34:48 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class PasswordLetterContext extends MessageLetterContext {
    
    public PasswordLetterContext(IWUserContext iwac ,Message msg){
        super(iwac,msg);
        init(iwac,msg);
    }
    
    private void init(IWUserContext iwuc,Message msg){
        Map props = new HashMap();
        if (msg.getBody().indexOf("|") > 0) {
			StringTokenizer tokenizer = new StringTokenizer(msg.getBody(), "|");
			
			if (tokenizer.hasMoreTokens()) {
			    props.put("username",tokenizer.nextToken());
			}
			if (tokenizer.hasMoreTokens()) {
			    props.put("password",tokenizer.nextToken());
			}

		}
        addDocumentProperties(props);
        setResourceDirectory(new File(getResourceUrl(getBundle(iwuc),iwuc.getCurrentLocale())));
        try {
            setTemplateStream(getTemplateUrlAsStream(getBundle(iwuc),iwuc.getCurrentLocale(),"password_template.xml",true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /* (non-Javadoc)
     * @see se.idega.idegaweb.commune.printing.business.MessageLetterContext#getTemplateXMLDocument()
     */
    protected XMLDocument getTemplateXMLDocument() {
        XMLDocument doc =  super.getBasicXMLDocument();
        XMLElement root = doc.getRootElement();
        XMLElement pass = new XMLElement("paragraph");
        pass.addContent("Username: \t<b>${username}</b> \n");
        pass.addContent("Password: \t<b>${password}</b> \n");
        root.addContent(pass);
        return doc;
    }
    
    
}
