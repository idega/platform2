/*
 * $Id: StandardMessageWindow.java,v 1.1 2004/10/11 13:35:42 aron Exp $
 * Created on 8.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.presentation;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.message.business.MessageContentService;
import se.idega.idegaweb.commune.message.business.MessageContentValue;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;


/**
 * 
 *  Last modified: $Date: 2004/10/11 13:35:42 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class StandardMessageWindow extends Window {
    
    public StandardMessageWindow(){
        this.setWidth(530);
		this.setHeight(400);
		this.setScrollbar(true);
		this.setResizable(true);	
		this.setAllMargins(0);
    }
    
    public void main(IWContext iwc){
        add(new StandardMessageEditor());
    }
    
    /* (non-Javadoc)
     * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
     */
    public String getBundleIdentifier() {
        return CommuneBlock.IW_BUNDLE_IDENTIFIER;
    }
    
    public class StandardMessageEditor extends CommuneBlock{
        
        private MessageContentValue value;
        
        public void main(IWContext iwc){
            add(getWindowHeader(getHeader(localize("standard_message.editor.window_header","Standard Message Manager"))));
            process(iwc);
            Form form = new Form();
            Layer windowBody = getWindowBody();
            windowBody.add(form);
            add(windowBody);
            
            form.add(getHeader(localize("standard_message.editor.name_of_message","Name of standard message")));
            form.add(new Break());
            TextInput nameInput = new TextInput("msg_name");
            nameInput.setLength(80);
            nameInput = (TextInput) getStyledInterface(nameInput);
            form.add(nameInput);
            form.add(new Break());
            form.add(new Break());
            TextArea bodyInput = new TextArea("msg_body");
            bodyInput.setColumns(80);
            bodyInput.setRows(20);
            bodyInput = (TextArea) getStyledInterface(bodyInput);
            form.add(getHeader(localize("standard_message.editor.body_of_message","Body of standard message")));
            form.add(new Break());
            form.add(bodyInput);
            form.add(new Break());
            form.add(new Break());
            form.add(getSaveButton());
            form.add(getCloseButton());
            
            if(value!=null){
                bodyInput.setContent(value.body!=null?value.body:"");
                nameInput.setContent(value.name!=null?value.name:"");
            }
            form.maintainParameter(MessageContentValue.PARAMETER_ID);
            form.maintainParameter(MessageContentValue.PARAMETER_LOCALE);
            form.maintainParameter(MessageContentValue.PARAMETER_TYPE);
        }
        
        private void process(IWContext iwc){
            value = new MessageContentValue();
            String ID = iwc.getParameter(MessageContentValue.PARAMETER_ID);
            
            if(ID!=null)
                value.ID = Integer.valueOf(ID);
            
            try {
                value = getService(iwc).getValue(value.ID);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            } catch (FinderException e1) {
                e1.printStackTrace();
            }
           
            
            if(iwc.isParameterSet(PARAM_CLOSE)){
                getParentPage().setParentToReload();
        		   getParentPage().close();	
            }
            else if(iwc.isParameterSet(PARAM_SAVE)){
                String type = iwc.getParameter(MessageContentValue.PARAMETER_TYPE);
                String loc  = iwc.getParameter(MessageContentValue.PARAMETER_LOCALE);
                value.type = type;
                if(loc!=null)
                    value.locale = new java.util.Locale(loc);
                else
                   value.locale = iwc.getCurrentLocale();
                String name = iwc.getParameter("msg_name");
                String body = iwc.getParameter("msg_body");
                
                value.name = name;
                value.body = body;
                value.creatorID = new Integer(iwc.getUserId());
                
                try {
                    getService(iwc).storeValue(value);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (FinderException e) {
                    e.printStackTrace();
                } catch (CreateException e) {
                    e.printStackTrace();
                }
            }
            else if(iwc.isParameterSet(PARAM_DELETE)){
                if(value.ID!=null)
                    try {
                        getService(iwc).removeValue(value.ID);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (RemoveException e) {
                        e.printStackTrace();
                    } catch (FinderException e) {
                        e.printStackTrace();
                    }
                getParentPage().setParentToReload();
     		   getParentPage().close();	
            }
        }
        
        private MessageContentService getService(IWContext iwc)throws RemoteException{
            return (MessageContentService)IBOLookup.getServiceInstance(iwc,MessageContentService.class);
        }
        
    }
}
