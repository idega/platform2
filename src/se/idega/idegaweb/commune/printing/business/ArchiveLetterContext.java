/*
 * $Id: ArchiveLetterContext.java,v 1.1 2004/11/04 20:34:48 aron Exp $
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
public class ArchiveLetterContext extends MessageLetterContext {
    
    public ArchiveLetterContext(IWUserContext iwuc ,Message msg) {
        super(iwuc,msg);
        init(iwuc,msg);
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
            setTemplateStream(getTemplateUrlAsStream(getBundle(iwuc),iwuc.getCurrentLocale(),"archive_template.xml",true));
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
        
        XMLElement fontBold = new XMLElement("font-def");
        fontBold.setAttribute("name","header");
        fontBold.setAttribute("family","Helvetica");
        fontBold.setAttribute("size","12");
        fontBold.setAttribute("style","bold");
        root.addContent(fontBold);
        
        XMLElement fontNormal = new XMLElement("font-def");
        fontNormal.setAttribute("name","normal");
        fontNormal.setAttribute("family","Helvetica");
        fontNormal.setAttribute("size","12");
        fontNormal.setAttribute("style","normal");
        root.addContent(fontNormal);   
        
        XMLElement table = new XMLElement("table");
        table.setAttribute("widths","20,80");
        
        XMLElement cell = new XMLElement("cell");
        cell.addContent("");
        XMLElement phrase = new XMLElement("phrase");
        phrase.setAttribute("font","header");
        phrase.addContent("User");
        cell.addContent(phrase);
        table.addContent(cell);
        
        cell = new XMLElement("cell");
        cell.addContent("");
        phrase = new XMLElement("phrase");
        phrase.setAttribute("font","normal");
        phrase.addContent("${user.name}");
        cell.addContent(phrase);
        table.addContent(cell);
        
        cell = new XMLElement("cell");
        cell.addContent("");
        phrase = new XMLElement("phrase");
        phrase.setAttribute("font","header");
        phrase.addContent("Created");
        cell.addContent(phrase);
        table.addContent(cell);
        
        cell = new XMLElement("cell");
        cell.addContent("");
        phrase = new XMLElement("phrase");
        phrase.setAttribute("font","normal");
        phrase.addContent("${msg.created}");
        cell.addContent(phrase);
        table.addContent(cell);
        
        cell = new XMLElement("cell");
        cell.addContent("");
        phrase = new XMLElement("phrase");
        phrase.setAttribute("font","header");
        phrase.addContent("Subject");
        cell.addContent(phrase);
        table.addContent(cell);
        
        cell = new XMLElement("cell");
        cell.addContent("");
        phrase = new XMLElement("phrase");
        phrase.setAttribute("font","normal");
        phrase.addContent("${msg.subject}");
        cell.addContent(phrase);
        table.addContent(cell);
        
        cell = new XMLElement("cell");
        cell.addContent("");
        phrase = new XMLElement("phrase");
        phrase.setAttribute("font","header");
        phrase.addContent("Body");
        cell.addContent(phrase);
        table.addContent(cell);
        
        cell = new XMLElement("cell");
        cell.addContent("");
        phrase = new XMLElement("phrase");
        phrase.setAttribute("font","normal");
        phrase.addContent("${msg.body}");
        cell.addContent(phrase);
        table.addContent(cell);
        
        root.addContent(table);
        return doc;
    }
    
}
