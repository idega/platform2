/*
 * $Id: FavoriteAddEditText.java,v 1.2 2004/11/26 08:41:56 laddi Exp $
 * Created on 5.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.presentation;

import java.rmi.RemoteException;

import com.idega.block.favorites.business.FavoritesBusiness;
import com.idega.block.favorites.business.FavoritesSession;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;


/**
 * Last modified: 5.11.2004 11:09:46 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class FavoriteAddEditText extends Text {

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		boolean canEdit = false;
		String favoriteType = null;
		try {
			favoriteType = getSession(iwc).getFavoriteType();
			if (favoriteType.equals(getBusiness(iwc).getFavoriteTypeInternet())) {
				canEdit = true;
			}
		}
		catch (RemoteException re) {
			log(re);
		}
		
		String action = iwc.getParameter(FavoriteBlock.PARAMETER_ACTION);
		if (action == null) {
			action = "";
		}
		if (canEdit || action.equals(FavoriteBlock.ACTION_EDIT)) {
			IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(FavoriteBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
			
			setText(iwrb.getLocalizedString("add_favorite", "Add favorite"));
			if (action.equals(FavoriteBlock.ACTION_EDIT)) {
				setText(iwrb.getLocalizedString("edit_favorite", "Edit favorite"));
			}
		}
		else {
			setText("");
		}
	}

	protected FavoritesBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (FavoritesBusiness) IBOLookup.getServiceInstance(iwac, FavoritesBusiness.class);
		}
		catch (IBOLookupException ible) {
			throw new IBORuntimeException(ible);
		}
	}

	protected FavoritesSession getSession(IWUserContext iwuc) {
		try {
			return (FavoritesSession) IBOLookup.getSessionInstance(iwuc, FavoritesSession.class);
		}
		catch (IBOLookupException ible) {
			throw new IBORuntimeException(ible);
		}
	}
}