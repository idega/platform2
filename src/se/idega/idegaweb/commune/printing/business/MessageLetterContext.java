/*
 * $Id: MessageLetterContext.java,v 1.1 2004/11/04 20:34:48 aron Exp $
 * Created on 15.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.printing.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import se.idega.idegaweb.commune.business.Constants;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.business.NoUserAddressException;
import se.idega.idegaweb.commune.message.data.Message;

import com.idega.block.pdf.business.PrintingContextImpl;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.location.data.Address;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.user.data.User;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLOutput;

/**
 * 
 *  Last modified: $Date: 2004/11/04 20:34:48 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class MessageLetterContext extends PrintingContextImpl {
    
    protected IWBundle iwb;
    protected IWResourceBundle iwrb;
    
    public MessageLetterContext(IWUserContext iwuc ,Message msg){
        init(iwuc,msg);
    }
    
    private void init(IWUserContext iwuc,Message msg){
        Map props = new HashMap();
        
        props.put("iwuc",iwuc);
        props.put("iwb",getBundle(iwuc));
        props.put("iwrb",getResourceBundle(iwuc));
        
        User user = msg.getOwner();
        props.put("user",user);
        Address address = null;
        try {
            CommuneUserBusiness userBuiz = getUserService(iwuc.getApplicationContext());
            address = userBuiz.getPostalAddress(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NoUserAddressException e) {
            e.printStackTrace();
        }
        props.put("address",address);
        props.put("msg",msg);
        addDocumentProperties(props);
        setResourceDirectory(new File(getResourcRealPath(getBundle(iwuc),iwuc.getCurrentLocale())));
    }
    
    protected IWBundle getBundle(IWUserContext iwuc){
        if(iwb==null)
            iwb = iwuc.getApplicationContext().getIWMainApplication().getBundle(getBundleIdentifier());
        return iwb;
    }
    
    protected IWResourceBundle getResourceBundle(IWUserContext iwuc){
        if(iwrb==null)
            getBundle(iwuc).getResourceBundle(iwuc.getCurrentLocale());
        return iwrb;
    }
    
    protected String getTemplateUrl(IWBundle iwb, Locale locale, String name) {
        return getResourcRealPath(iwb,locale)+name;
	}
    
    protected String getResourceUrl(IWBundle iwb, Locale locale) {
        return getResourcRealPath(iwb,locale);
	}
    
    private String getResourcRealPath(IWBundle iwb,Locale locale){
        if(locale!=null)
            return iwb.getResourcesRealPath(locale)+"/print/";
        else
            return iwb.getResourcesRealPath()+"/print/";
    }
    
    protected FileInputStream getTemplateUrlAsStream(IWBundle iwb, Locale locale, String name,boolean createIfNotExists) throws IOException{
        File template = new File(getTemplateUrl(iwb,locale,name));
        if(!template.exists() && createIfNotExists)
            createTemplateFile(template);
        return new FileInputStream(template);
    }
    
    private void createTemplateFile(File file) throws IOException{
        
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		
		XMLOutput xmlOutput = new XMLOutput("  ", true);
		xmlOutput.setLineSeparator(System.getProperty("line.separator"));
		xmlOutput.setTextNormalize(true);
		xmlOutput.setEncoding("iso-8859-1");
		XMLDocument doc = getTemplateXMLDocument();
		xmlOutput.output(doc, fos);
		fos.close();
		
        
    }
        
    protected XMLDocument getTemplateXMLDocument(){
       XMLDocument doc = getBasicXMLDocument();
       XMLElement document = doc.getRootElement();
       XMLElement subject = new XMLElement("paragraph");
       subject.addContent("${msg.subject}");
       document.addContent(subject);
       XMLElement body = new XMLElement("paragraph");
       body.setAttribute("halign","justified");
       body.addContent("${msg.body}");
       document.addContent(body);
       return doc;
       
    }
    
    protected XMLDocument getBasicXMLDocument(){
        XMLElement document = new XMLElement("document");
        document.setAttribute("size", "A4");
        document.setAttribute("margin-left", "25");
        document.setAttribute("margin-right", "25");
        document.setAttribute("margin-top", "25");
        document.setAttribute("margin-bottom", "25");
        XMLDocument doc = new XMLDocument(document);
		
        return doc;
    }
    
    protected String getBundleIdentifier(){
        return Constants.IW_BUNDLE_IDENTIFIER;
    }
    
    protected CommuneUserBusiness getUserService(IWApplicationContext iwac) throws IBOLookupException{
        return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwac,CommuneUserBusiness.class);
    }
    
    
}
