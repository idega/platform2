/*
 * $Id: StandardMessageArea.java,v 1.2 2004/10/11 17:15:25 aron Exp $
 * Created on 8.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.presentation;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import se.idega.idegaweb.commune.message.business.MessageContentService;
import se.idega.idegaweb.commune.message.business.MessageContentValue;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.GenericInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.TextArea;

/**
 * 
 *  Last modified: $Date: 2004/10/11 17:15:25 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.2 $
 */
public class StandardMessageArea extends CommuneBlock {
    
    private String textAreaName = "msgBody";
    private TextArea area;
    private Table quickLinks;
    private boolean hasParent = false;
    private String category = "STNDMSG";
    
    
    
    public void main(IWContext iwc) throws Exception {
        if(!hasParent){
            add(getTextArea(null));
            add(new Break());
            add(getMessageList(null));
        }
        if(quickLinks==null)
            quickLinks = new Table();
        
        MessageContentValue valueProxy = new MessageContentValue();
        valueProxy.locale = iwc.getCurrentLocale();
        valueProxy.type = getCategory();
        Collection messageValues = getService(iwc).getValues(valueProxy);
        int row = 1;
        for (Iterator iter = messageValues.iterator(); iter.hasNext();) {
            MessageContentValue value = (MessageContentValue) iter.next();
            quickLinks.add(getChoice(value),1,row );
            quickLinks.add(getText(value.name),2,row);
            quickLinks.add(getEditLink(value),3,row);
            quickLinks.add(getDeleteLink(value),4,row);
            row++;
        }
            
        
    }
    
    /**
     * Gets a delete link with the Delete icon to delete selected MessageContent
     * @param value
     * @return
     */
    private Link getDeleteLink(MessageContentValue value) {
        Link deleteLink = new Link(getDeleteIcon(localize("standard_message.tooltip.edit","Delete message content")));
        deleteLink.setParameter(value.getDeleteParameters());
        deleteLink.addParameter(PARAM_DELETE,"1");
        deleteLink.setWindowToOpen(StandardMessageWindow.class);
        return deleteLink;
    }

    /**
     * Gets a link with the Edit icon to edit selected MessageContent
     * @param value
     * @return
     */
    private Link getEditLink(MessageContentValue value) {
        Link editLink = new Link(getEditIcon(localize("standard_message.tooltip.edit","Edit message content")));
        editLink.setParameter(value.getEditParameters());
        editLink.addParameter(PARAM_EDIT,"1");
        editLink.setWindowToOpen(StandardMessageWindow.class);
        return editLink;
    }
    
    /**
     * Gets a map of parameters needed to create new MessageContent
     * @return
     */
    public Map getCreateParameters(){
        MessageContentValue val = new MessageContentValue();
        val.type = getCategory();
        return val.getCreateParameters();
    }
    
    /**
     * Gets the window class used to manage MessageContent
     * @param value
     * @return
     */
    public Class getManageWindowClass(){
        return StandardMessageWindow.class;
    }
    
    
    /**
     * @param value
     * @return
     */
    private GenericInput getChoice(MessageContentValue value) {
        RadioButton rad = getRadioButton("RB_"+value.type,value.ID.toString());
        StringReader reader = new StringReader(value.body);
        BufferedReader breader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer();
        try {
            String line;
            while((line = breader.readLine())!=null){
               buffer.append(line).append("\\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        rad.setOnClick("this.form."+getTextAreaName()+".value='" + buffer + "';");
        return rad;
    }

    public TextArea getTextArea(PresentationObject parent){
        if(parent!=null && !hasParent){
            this.setParent(parent);
            parent.addChild(this);
            hasParent = true;
        }
        if(area==null)
            area = new TextArea(getTextAreaName());
        return area;
    }
    
    public Table getMessageList(PresentationObject parent){
        if(parent!=null && !hasParent){
            this.setParent(parent);
            parent.addChild(this);
            hasParent = true;
        }
        if(quickLinks==null)
            quickLinks = new Table();
        return quickLinks;
    }
       
    public String getTextAreaName() {
        return textAreaName;
    }
    public void setTextAreaName(String textAreaName) {
        this.textAreaName = textAreaName;
    }
    
    public MessageContentService getService(IWContext iwc)throws RemoteException{
        return (MessageContentService) IBOLookup.getServiceInstance(iwc,MessageContentService.class);
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
