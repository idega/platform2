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

import com.idega.block.favorites.business.FavoritesBusiness;
import com.idega.block.favorites.data.Favorite;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.text.Link;
import com.idega.user.data.User;

/**
 * @author Anna
 */
public class FavoriteLink extends Block {
    IWResourceBundle iwrb;
    IWBundle iwb;
    User currentUser;
    User favoriteUser;
    private String action;
    Link favoriteLink;
  
    private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.favorites";
    private static final String PARAMETER_URL = "url";
    private static final String PARAMETER_ACTION = "action";
    private static final String ACTION_SAVE = "save";
    private static final String ACTION_VIEW = "view"; 
    String currentName = null;
    String favoriteName = null;
    Favorite favorite = null;
    
    
    public void main(IWContext iwc)  {
        
        action = iwc.getParameter("action");
        
         if (action == null) {
             action = ACTION_VIEW;
         }
         if(action.equals(ACTION_SAVE)) {
             saveLink(iwc);
         }
        
        try {
            ICPage currentPage = getBuilderService(iwc).getCurrentPage(iwc);
            currentName = currentPage.getName();
            currentUser = iwc.getCurrentUser();
            favorite = getBusiness(iwc).getFavorite(currentUser,currentName);
            if(favorite != null) {
	               getImage();
	            }                  
        }
        catch (FinderException fe) {
            //not found!    
            getLink();
            
        }
        catch (RemoteException re) {
            log(re);
        }       
    }
    
    private Image getImage() {
        Image image = new Image("imageName.gif??????????????"); 
        return image;
    }
    private Link getLink() {      
        favoriteLink = new Link(iwb.getImage("imageName.gif???????"));
        favoriteLink.addParameter(PARAMETER_ACTION, ACTION_SAVE);
        return favoriteLink;
    }
    //hér inn vantar eiginlega url-ið, ekkert gagn í að geyma þetta án þess - kemur seinna - ac
    private void saveLink(IWContext iwc) {
        try {
        getBusiness(iwc).storeFavorite(currentUser, null, currentName, false);
        }
        catch(RemoteException re) {
            log(re);
        }
        catch(CreateException ce) {
            log(ce);
        }
    }
    
    private FavoritesBusiness getBusiness(IWApplicationContext iwc) {
        try {
            return (FavoritesBusiness) IBOLookup.getServiceInstance(iwc, FavoritesBusiness.class);
        }
        catch (IBOLookupException ible) {
            throw new IBORuntimeException(ible);
        }
    }
    
    public String getBundleIdentifier() {
        return IW_BUNDLE_IDENTIFIER;
    }
}