/*
 * Created on Oct 26, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.favorites.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.favorites.business.FavoritesBusiness;
import com.idega.block.favorites.data.Favorite;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;

/**
 * @author Anna
 */
public class FavoriteQuickLink extends Block {
    
    private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.favorites";
    private static final String PARAMETER_QUICK_LINK = "quick_link";
    User user;
    
    
    private FavoritesBusiness getBusiness(IWApplicationContext iwc) {
        try {
            return (FavoritesBusiness) IBOLookup.getServiceInstance(iwc, FavoritesBusiness.class);
        }
        catch (IBOLookupException ible) {
            throw new IBORuntimeException(ible);
        }
    }
    
    public void main(IWContext iwc) throws Exception {
        user = iwc.getCurrentUser();

        Form form = new Form();
        form.add(getQuickLinksTable(iwc));
        add(form);
    }
    
    private Table getQuickLinksTable(IWContext iwc) {
        Collection quickLinkList = null;
        try {
            quickLinkList = getBusiness(iwc).getQuickLinkFavorites(user);
        }
        catch(FinderException fe) {
            log(fe); //Hvernig skrifa ég meldingu sjálf???
        }
        catch(RemoteException re) {
            log(re);
        }
        
        Table table = new Table();
        DropdownMenu quickLinks = null;
        SubmitButton go = new SubmitButton("Go");
        table.setWidth("100%");
    		table.setCellpadding(2);
    		table.setCellspacing(0);
    		table.setBorder(2);
    		int row = 1;
 
        quickLinks = new DropdownMenu(PARAMETER_QUICK_LINK);//er þetta réttur parameter????
   
        Iterator iter = quickLinkList.iterator();
        while (iter.hasNext()) {
            Favorite element = (Favorite) iter.next();
            quickLinks.addMenuElement(element.getURL(), element.getName());
        }
        
        table.add(quickLinks, 1, row);
        table.add(go, 1, row);
       
        return table;
    }

    public String getBundleIdentifier() {
        return IW_BUNDLE_IDENTIFIER;
    }
}