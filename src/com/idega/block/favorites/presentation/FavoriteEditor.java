/*
 * Created on Oct 21, 2004
 */
package com.idega.block.favorites.presentation;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.favorites.data.Favorite;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.TextInput;

/**
 * @author Anna
 */
public class FavoriteEditor extends FavoriteBlock implements IWPageEventListener {

	private String iWidth = Table.HUNDRED_PERCENT;
	private int iCellpadding = 3;

	public void present(IWContext iwc) {
		if (iwc.isLoggedOn()) {
			String action = iwc.getParameter(PARAMETER_ACTION);
			if (action == null) {
				action = "";
			}
			
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
			
			if (canEdit || action.equals(ACTION_EDIT)) {
				Form form = new Form();
				if (action.equals(ACTION_EDIT) && iwc.isParameterSet(PARAMETER_PRIMARY_KEY)) {
					form.addParameter(PARAMETER_PRIMARY_KEY, iwc.getParameter(PARAMETER_PRIMARY_KEY));
				}
				form.addParameter(PARAMETER_ACTION, ACTION_SAVE);
				form.addParameter(PARAMETER_FAVORITE_TYPE, favoriteType);
				form.setEventListener(getClass());
				//form.setMethod("get");
		
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
		
				TextInput linkName = (TextInput) getInput(new TextInput(PARAMETER_NAME));
				if (favorite != null) {
					linkName.setContent(favorite.getName());
				}
				
				TextInput URL = (TextInput) getInput(new TextInput(PARAMETER_URL));
				if (favorite != null) {
					URL.setContent(favorite.getURL());
					if (!canEdit) {
						form.addParameter(PARAMETER_URL, favorite.getURL());
					}
				}
				URL.setDisabled(!canEdit);
				
				CheckBox quickLink = getCheckBox(PARAMETER_QUICK_LINK, "true");
				if (favorite != null) {
					quickLink.setChecked(favorite.isQuickLink());
				}
				
				Link save = getLink(getResourceBundle().getLocalizedString("favorite.save", "Save"));
				save.setToFormSubmit(form);
				save.setToolTip(getResourceBundle().getLocalizedString("favorite.save_tooltip", "Saves the current information."));
		
				Table editTable = new Table(4, 2);
				editTable.setWidth(iWidth);
				editTable.setWidth(4, Table.HUNDRED_PERCENT);
				editTable.setAlignment(4, 2, Table.HORIZONTAL_ALIGN_RIGHT);
				editTable.setCellpadding(iCellpadding);
				editTable.setCellspacing(0);
				int row = 1;
		
				editTable.setCellpaddingLeft(1, row, 0);
				editTable.add(getHeader(getResourceBundle().getLocalizedString("favorite.link_name", "Title")), 1, row);
				editTable.add(linkName, 2, row++);
	
				editTable.setCellpaddingLeft(1, row, 0);
				editTable.setCellpaddingRight(4, row, 0);
				editTable.add(getHeader(getResourceBundle().getLocalizedString("favorite.url", "Url")), 1, row);
				editTable.add(URL, 2, row);
				editTable.add(quickLink, 3, row);
				editTable.add(Text.getNonBrakingSpace(), 3, row);
				editTable.add(getText(getResourceBundle().getLocalizedString("favorite.quick_link", "Quick link")), 3, row);
				editTable.setNoWrap(3, row);
				
				if (canEdit || action.equals(ACTION_EDIT)) {
					editTable.add(save, 4, row);
				}
		
				form.add(editTable);
				add(form);
			}
		}
		else {
			add("No user logged on...");
		}
	}

	/**
	 * Sets the width of the favorites editor.
	 * @param width The width to set.
	 */
	public void setWidth(String width) {
		iWidth = width;
	}
	
	/**
	 * Sets the spacing between the columns in the favorites editor.
	 * @param cellpadding The cellpadding to set.
	 */
	public void setCellpadding(int cellpadding) {
		iCellpadding = cellpadding;
	}

	/* (non-Javadoc)
	 * @see com.idega.event.IWEventListener#actionPerformed(com.idega.event.IWPresentationEvent)
	 */
	public boolean actionPerformed(IWContext iwc) throws IWException {
		String name = iwc.getParameter(PARAMETER_NAME);
		String URL = iwc.getParameter(PARAMETER_URL);
		boolean quickLink = false;
		if (iwc.isParameterSet(PARAMETER_QUICK_LINK)) {
			quickLink = true;
		}
		Object pk = iwc.getParameter(PARAMETER_PRIMARY_KEY);

		try {
			getBusiness(iwc).storeFavorite(pk, iwc.getCurrentUser(), URL, name, getSession(iwc).getFavoriteType(), quickLink);
			return true;
		}
		catch (RemoteException re) {
			log(re);
		}
		catch (CreateException ce) {
			log(ce);
		}
		return false;
	}
}