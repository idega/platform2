/*
 * $Id: FavoriteList.java,v 1.3.2.1 2007/01/12 19:32:21 idegaweb Exp $
 * Created on 5.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.favorites.data.Favorite;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.User;


/**
 * Last modified: 5.11.2004 09:47:02 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3.2.1 $
 */
public class FavoriteList extends FavoriteBlock implements IWPageEventListener {
	
	private String iWidth = Table.HUNDRED_PERCENT;
	private int iCellpadding = 3;

	/* (non-Javadoc)
	 * @see com.idega.block.favorites.presentation.FavoriteBlock#present(com.idega.presentation.IWContext)
	 */
	protected void present(IWContext iwc) {
		if (iwc.isLoggedOn()) {
			User user = iwc.getCurrentUser();
			String favoriteType = null;
			
			Collection favoriteCollection = null;
			try {
				favoriteType = getSession(iwc).getFavoriteType();
				favoriteCollection = getBusiness(iwc).getFavorites(user, favoriteType);
			}
			catch (FinderException fe) {
				log(fe);
			}
			catch (RemoteException re) {
				log(re);
			}
	
			Table table = new Table();
			table.setWidth(this.iWidth);
			table.setCellpadding(this.iCellpadding);
			table.setCellspacing(0);
			int row = 1;
	
			Text title = getHeader(getResourceBundle().getLocalizedString("favorite.title", "Title"));
			Text URL = getHeader(getResourceBundle().getLocalizedString("favorite.url", "Url"));
			
			Text text = getText(Text.NON_BREAKING_SPACE + "|" + Text.NON_BREAKING_SPACE);
			
			table.add(title, 1, row);
			table.add(URL, 2, row);
			table.setCellpaddingLeft(1, row++, 0);
	
			Link edit;
			Link delete;
			Link link;
	
			if (favoriteCollection != null) {
				Iterator iter = favoriteCollection.iterator();
				while (iter.hasNext()) {
					Favorite element = (Favorite) iter.next();
					String URI = element.getURL();
					
					link = getLink(element.getName());
					if (URI != null) {
						try {
							if (favoriteType.equals(getBusiness(iwc).getFavoriteTypeInternet()) && URI.indexOf("http://") == -1) {
								URI = "http://" + URI;
							}
						}
						catch (RemoteException re) {
							log(re);
						}
						link.setURL(URI);
					}
					
					edit = getLink(getResourceBundle().getLocalizedString("favorite.edit", "Edit"));
					edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);
					edit.addParameter(PARAMETER_PRIMARY_KEY, element.getPrimaryKey().toString());
					edit.setToolTip(getResourceBundle().getLocalizedString("favorite.edit_tooltip", "Edit this favorite."));
	
					delete = getLink(getResourceBundle().getLocalizedString("favorite.delete", "Delete"));
					delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);
					delete.addParameter(PARAMETER_PRIMARY_KEY, element.getPrimaryKey().toString());
					delete.setEventListener(getClass());
					delete.setToolTip(getResourceBundle().getLocalizedString("favorite.delete_tooltip", "Delete this favorite."));
	
					table.add(link, 1, row);
					table.add(getText(element.getURL()), 2, row);
					table.setCellpaddingLeft(1, row, 0);
					table.setCellpaddingRight(3, row, 0);
					table.add(edit, 3, row);
					table.add(text, 3, row);
					table.add(delete, 3, row++);
				}
			}
			table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_RIGHT);
			
			add(table);
		}
		else {
			add("No user logged on...");
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.event.IWEventListener#actionPerformed(com.idega.event.IWPresentationEvent)
	 */
	public boolean actionPerformed(IWContext iwc) throws IWException {
		try {
			Favorite favorite = getBusiness(iwc).getFavorite(new Integer(iwc.getParameter(PARAMETER_PRIMARY_KEY)));
			getBusiness(iwc).deleteFavorite(favorite);
			return true;
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
		return false;
	}

	/**
	 * Sets the width of the favorites list.
	 * @param width The width to set.
	 */
	public void setWidth(String width) {
		this.iWidth = width;
	}
	
	/**
	 * Sets the spacing between the columns in the favorites list.
	 * @param cellpadding The cellpadding to set.
	 */
	public void setCellpadding(int cellpadding) {
		this.iCellpadding = cellpadding;
	}
}