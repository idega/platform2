/*
 * Created on Oct 26, 2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.favorites.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.favorites.data.Favorite;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.user.data.User;

/**
 * @author Anna
 */
public class FavoriteQuickLink extends FavoriteBlock {

	private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.favorites";
	private static final String PARAMETER_QUICK_LINK = "quick_link_url";
	
	private int iSpaceBetween = -1;
	private int iMaxLength = -1;

	private User user;

	public void present(IWContext iwc) {
		user = iwc.getCurrentUser();

		add(getQuickLinks(iwc));
	}

	private Form getQuickLinks(IWContext iwc) {
		Collection quickLinkList = null;
		try {
			quickLinkList = getBusiness(iwc).getQuickLinkFavorites(user);
		}
		catch (FinderException fe) {
			log(fe);
		}
		catch (RemoteException re) {
			log(re);
		}

    getParentPage().getAssociatedScript().addFunction("navHandler", getScriptSource());
		
		Form form = new Form();
    Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		form.add(table);
		int column = 1;

		DropdownMenu quickLinks = (DropdownMenu) getInput(new DropdownMenu(PARAMETER_QUICK_LINK));
		Link go = getLink(getResourceBundle().getLocalizedString("go", "Go!"));
		go.setURL("javascript:" + getScriptCaller(PARAMETER_QUICK_LINK));

		Iterator iter = quickLinkList.iterator();
		while (iter.hasNext()) {
			Favorite element = (Favorite) iter.next();
			String URI = element.getURL();
			String name = element.getName();
			if (iMaxLength > 0 && name.length() > iMaxLength) {
				name = name.substring(0, iMaxLength + 1) + "...";
			}
			
			if (URI != null) {
				try {
					if (element.getFavoriteType().equals(getBusiness(iwc).getFavoriteTypeInternet()) && URI.indexOf("http://") == -1) {
						URI = "http://" + URI;
					}
				}
				catch (RemoteException re) {
					log(re);
				}
			}
			quickLinks.addMenuElement(URI, name);
		}

		table.add(quickLinks, column++, 1);
		if (iSpaceBetween > 0) {
			table.setWidth(column++, iSpaceBetween);
		}
		table.add(go, column, 1);

		return form;
	}

	public String getScriptCaller(String dropDownName) {
		return "navQuickLink(findObj('" + dropDownName + "'))";
	}

	public String getScriptSource() {
		StringBuffer s = new StringBuffer();
		s.append("\n function navQuickLink(input){");
		s.append("\n\t var URL = input.options[input.selectedIndex].value;");
		s.append("\n\t window.location.href = URL;");
		s.append("\n }");
		return s.toString();
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	/**
	 * Sets the space between the dropdown and the Go! link.
	 * @param spaceBetween The spaceBetween to set.
	 */
	public void setSpaceBetween(int spaceBetween) {
		iSpaceBetween = spaceBetween;
	}

	/**
	 * Sets the maximum length of the names of the favorites in the dropdown.
	 * @param maxLength The maximum length to set.
	 */
	public void setMaximumLength(int maxLength) {
		iMaxLength = maxLength;
	}
}