/*
 * Created on Oct 21, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.favorites.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.favorites.business.FavoritesBusiness;
import com.idega.block.favorites.data.Favorite;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;

/**
 * @author Anna
 */
public class FavoritesEditor extends Block {
    
    private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.favorites";
    
    IWResourceBundle iwrb;
    User user;
    private String action;
    
    private static final String PARAMETER_ACTION = "action";
    private static final String PARAMETER_PRIMARY_KEY = "pk";
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_URL = "url";
    private static final String PARAMETER_QUICK_LINK = "quick_link";

    private static final String ACTION_DELETE = "delete";
    private static final String ACTION_EDIT = "edit";
    private static final String ACTION_SAVE = "save";
    private static final String ACTION_VIEW = "view";    
    
    private FavoritesBusiness getBusiness(IWApplicationContext iwc) {
        try {
            return (FavoritesBusiness) IBOLookup.getServiceInstance(iwc, FavoritesBusiness.class);
        }
        catch (IBOLookupException ible) {
            throw new IBORuntimeException(ible);
        }
    }
			
    public void main(IWContext iwc) throws Exception {
        action = iwc.getParameter("action");
        if (action == null) {
            action = ACTION_VIEW;
        }
        user = iwc.getCurrentUser();
        iwrb = getResourceBundle(iwc);

        if(action.equals(ACTION_DELETE)) {
            deleteLink(iwc);
        }
        if(action.equals(ACTION_SAVE)) {
            saveLink(iwc);
        }

        add(getFavoritesTable(iwc));
        add(getEditTable(iwc));
    }
    //shows a list of favorites, with the url, title, and a possibility to change or delete a favorite
    private Table getFavoritesTable(IWContext iwc) {
        Collection favoriteCollection = null;
        try {
            favoriteCollection = getFavoritesBusiness(iwc).getFavorites(user);
        }
        catch(FinderException fe) {
            log(fe);
        }
        catch (RemoteException re) {
            log(re);
        }
                    
        Table table = new Table();
    		table.setWidth("100%");
    		table.setCellpadding(2);
    		table.setCellspacing(0);
    		table.setBorder(1);
    		int row = 1;
    		
    		Link edit;
    		Link delete;
    		Link intranet;
    		Link internet;
    		Link documents;
    		
    		intranet = new Link(iwrb.getLocalizedString("favorite.intranet","Intranet favorites"));
    		internet = new Link(iwrb.getLocalizedString("favorite.internet","Internet favorites"));
    		documents = new Link(iwrb.getLocalizedString("favorite.documents","Favorite documents"));
    		
    		table.mergeCells(1, row, 4, row);
    		table.add(intranet,1,row);
    		table.add(" | ", 1, row);
    		table.add(internet,1,row);
    		table.add(" | ", 1, row);
    		table.add(documents,1,row++);
    		
    		table.add(iwrb.getLocalizedString("favorite.title","Title"),1,row);
    		table.add(iwrb.getLocalizedString("favorite.url","Url"),2,row++);
    		
    		
    		if (favoriteCollection != null) {
					Iterator iter = favoriteCollection.iterator();
					while (iter.hasNext()) {
			        Favorite element = (Favorite) iter.next();
			        edit = new Link(iwrb.getLocalizedString("favorite.edit","Edit"));
			        edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);
			        edit.addParameter(PARAMETER_PRIMARY_KEY, element.getPrimaryKey().toString());
			        
			        delete = new Link(iwrb.getLocalizedString("favorite.delete","Delete"));
			        delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);
			        delete.addParameter(PARAMETER_PRIMARY_KEY, element.getPrimaryKey().toString());

			        table.add(element.getName(), 1, row);
			        table.add(element.getURL(), 2, row);	
			        table.add(edit, 3, row);
			        table.add(delete, 4, row++);
	        }
    		}
    		return table; 
    }
    //shows the form where you can edit a link, its title and choose if it's a quick link or not. 
    private Form getEditTable(IWContext iwc) {
        Form form = new Form();
        if (action.equals(ACTION_EDIT) && iwc.isParameterSet(PARAMETER_PRIMARY_KEY)) {
            form.addParameter(PARAMETER_PRIMARY_KEY, iwc.getParameter(PARAMETER_PRIMARY_KEY));
        }
        form.addParameter(PARAMETER_ACTION, ACTION_SAVE);
        form.setMethod("get");
        
        Favorite favorite = null;
        if (action.equals(ACTION_EDIT) && iwc.isParameterSet(PARAMETER_PRIMARY_KEY)) {
	        try {
	            favorite = getBusiness(iwc).getFavorite(new Integer(iwc.getParameter(PARAMETER_PRIMARY_KEY)));
	        }
	        catch (FinderException fe) {
	            //Nothing found
	        }
	        catch (RemoteException re) {
	            log(re);
	        }
        }
        
        TextInput linkName = new TextInput(PARAMETER_NAME);
        if (favorite != null) {
            linkName.setContent(favorite.getName());
        }
        TextInput URL = new TextInput(PARAMETER_URL);
        if(favorite != null) {
            URL.setContent(favorite.getURL());
        }
        CheckBox quickLink = new CheckBox(PARAMETER_QUICK_LINK, "true");
        if(favorite != null) {
            quickLink.setChecked(favorite.isQuickLink());
        }     
        Link save = new Link(iwrb.getLocalizedString("favorite.save","Save"));
        save.setToFormSubmit(form);
        
        Table editTable = new Table();
        
        editTable.setWidth("30%");//er þetta lélegt???
        editTable.setCellpadding(2);
        editTable.setCellspacing(0);
    		int row = 1;
    		if(action.equals(ACTION_EDIT)) {
    		    editTable.add(iwrb.getLocalizedString("favorite.edit_favorites","Edit favorites"),1,row++);
    		}
    		else {
    		    editTable.add(iwrb.getLocalizedString("favorite.add_favorites","Add favorites"),1,row++);
    		}
    		
    		editTable.add(iwrb.getLocalizedString("favorite.link_name","Title") ,1,row);
    		editTable.add(linkName ,2,row++);
    		editTable.add(iwrb.getLocalizedString("favorite.url","Url") ,1,row);
    		editTable.add(URL ,2,row++);
    		editTable.add(quickLink ,1,row);
    		editTable.add(iwrb.getLocalizedString("favorite.quick_link","Quick link") ,2,row);
    		editTable.add(save ,3,row);
    		
        form.add(editTable);
    		return form;
    }
    //böggur  - þegar nýbúið að skrá inn link og ýtt á go eða reload, þá vistast 2 í viðbót af linknum - ac
    private void saveLink(IWContext iwc) {
        String name = iwc.getParameter(PARAMETER_NAME);
        String URL = iwc.getParameter(PARAMETER_URL);
        boolean quickLink = false;
        if (iwc.isParameterSet(PARAMETER_QUICK_LINK)) {
            quickLink = true;
        }
        Object pk = iwc.getParameter(PARAMETER_PRIMARY_KEY);
        
        try {
            getBusiness(iwc).storeFavorite(pk, user, URL, name, quickLink);
        }
        catch (RemoteException re) {
            log(re);
        }
        catch (CreateException ce) {
            log(ce);
        }
    }
    //hér er böggur - það þarf að ýta tvisvar á þennan takka til að valið favorite eyðist - ac
    private void deleteLink(IWContext iwc) {
        try {
            Favorite favorite = getBusiness(iwc).getFavorite(new Integer(iwc.getParameter(PARAMETER_PRIMARY_KEY)));
            getBusiness(iwc).deleteFavorite(favorite);
        }
        catch (FinderException fe) {
            //Nothing found
        }
        catch (RemoteException re) {
            log(re);
        }
        catch (RemoveException re) {
            log(re);
        }
    }
    
    private FavoritesBusiness getFavoritesBusiness(IWApplicationContext iwc) {
        try {
            return (FavoritesBusiness)IBOLookup.getServiceInstance(iwc, FavoritesBusiness.class);
        }
        catch (IBOLookupException ile) {
            throw new IBORuntimeException(ile);
        }
    }

    public String getBundleIdentifier() {
        return IW_BUNDLE_IDENTIFIER;
    }
}