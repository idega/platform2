/*
 * $Id: FavoriteList.java,v 1.1 2004/11/05 13:26:10 laddi Exp $
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
 * @version $Revision: 1.1 $
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
			table.setWidth(iWidth);
			table.setCellpadding(iCellpadding);
			table.setCellspacing(0);
			int row = 1;
	
			Text title = new Text(getResourceBundle().getLocalizedString("favorite.title", "Title"));
			title.setBold();
			Text URL = new Text(getResourceBundle().getLocalizedString("favorite.url", "Url"));
			URL.setBold();
			
			Text text = new Text(Text.NON_BREAKING_SPACE + "|" + Text.NON_BREAKING_SPACE);
			
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
					
					link = new Link(element.getName());
					if (element.getURL() != null) {
						link.setURL(element.getURL());
					}
					
					edit = new Link(getResourceBundle().getLocalizedString("favorite.edit", "Edit"));
					edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);
					edit.addParameter(PARAMETER_PRIMARY_KEY, element.getPrimaryKey().toString());
	
					delete = new Link(getResourceBundle().getLocalizedString("favorite.delete", "Delete"));
					delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);
					delete.addParameter(PARAMETER_PRIMARY_KEY, element.getPrimaryKey().toString());
					delete.setEventListener(getClass());
	
					table.add(link, 1, row);
					table.add(element.getURL(), 2, row);
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
		iWidth = width;
	}
	
	/**
	 * Sets the spacing between the columns in the favorites list.
	 * @param cellpadding The cellpadding to set.
	 */
	public void setCellpadding(int cellpadding) {
		iCellpadding = cellpadding;
	}
}