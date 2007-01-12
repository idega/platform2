/*
 * $Id: FavoriteTypeNavigator.java,v 1.3.2.1 2007/01/12 19:32:21 idegaweb Exp $
 * Created on 5.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.presentation;

import java.rmi.RemoteException;

import com.idega.business.IBORuntimeException;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;


/**
 * Last modified: 5.11.2004 09:37:06 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3.2.1 $
 */
public class FavoriteTypeNavigator extends FavoriteBlock implements IWPageEventListener {

	private boolean iShowDocumentFavorites = false;
	
	/* (non-Javadoc)
	 * @see com.idega.block.favorites.presentation.FavoriteBlock#present(com.idega.presentation.IWContext)
	 */
	protected void present(IWContext iwc) {
		Text text = getText(Text.NON_BREAKING_SPACE + "|" + Text.NON_BREAKING_SPACE);
		try {
			add(getObject(iwc, getResourceBundle().getLocalizedString("favorite.intranet", "Intranet favorites"), getBusiness(iwc).getFavoriteTypeIntranet()));
			add(text);
			add(getObject(iwc, getResourceBundle().getLocalizedString("favorite.internet", "Internet favorites"), getBusiness(iwc).getFavoriteTypeInternet()));
			if (this.iShowDocumentFavorites) {
				add(text);
				add(getObject(iwc, getResourceBundle().getLocalizedString("favorite.documents", "Document favorites"), getBusiness(iwc).getFavoriteTypeDocument()));
			}
		}
		catch (RemoteException re) {
			log(re);
		}
	}
	
	private PresentationObject getObject(IWContext iwc, String name, String favoriteType) {
		try {
			if (getSession(iwc).getFavoriteType().equals(favoriteType)) {
				return new Text(name);
			}
			else {
				Link link = getLink(name);
				link.setEventListener(FavoriteTypeNavigator.class);
				link.addParameter(PARAMETER_FAVORITE_TYPE, favoriteType);
				return link;
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public boolean actionPerformed(IWContext iwc) throws IWException {
		try {
			getSession(iwc).setFavoriteType(iwc.getParameter(PARAMETER_FAVORITE_TYPE));
			return true;
		}
		catch (RemoteException re) {
			log(re);
		}
		return false;
	}
	
	public void setShowDocumentFavorites(boolean show) {
		this.iShowDocumentFavorites = show;
	}
}