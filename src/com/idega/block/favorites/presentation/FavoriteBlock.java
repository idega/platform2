/*
 * $Id: FavoriteBlock.java,v 1.1 2004/11/05 13:26:10 laddi Exp $
 * Created on 4.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.presentation;

import com.idega.block.favorites.business.FavoritesBusiness;
import com.idega.block.favorites.business.FavoritesSession;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;


/**
 * Last modified: 4.11.2004 15:53:08 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public abstract class FavoriteBlock extends Block {

	protected static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.favorites";

	protected static final String PARAMETER_ACTION = "fav_action";
	protected static final String PARAMETER_PRIMARY_KEY = "fav_pk";
	protected static final String PARAMETER_NAME = "fav_name";
	protected static final String PARAMETER_URL = "fav_url";
	protected static final String PARAMETER_QUICK_LINK = "fav_quick_link";
	protected static final String PARAMETER_FAVORITE_TYPE = "fav_favorite_type";

	protected static final String ACTION_DELETE = "delete";
	protected static final String ACTION_EDIT = "edit";
	protected static final String ACTION_SAVE = "save";
	protected static final String ACTION_VIEW = "view";

	private IWBundle iwb;
	private IWResourceBundle iwrb;
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		
		present(iwc);
	}
	
	protected abstract void present(IWContext iwc);
	
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

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	/**
	 * @return Returns the iwb.
	 */
	protected IWBundle getBundle() {
		return iwb;
	}
	
	/**
	 * @return Returns the iwrb.
	 */
	protected IWResourceBundle getResourceBundle() {
		return iwrb;
	}
}