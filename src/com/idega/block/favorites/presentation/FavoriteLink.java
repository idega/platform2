 /*
 * Created on Oct 21, 2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.favorites.presentation;

import java.rmi.RemoteException;
import java.text.MessageFormat;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.favorites.data.Favorite;
import com.idega.core.builder.data.ICPage;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.text.Link;
import com.idega.user.data.User;

/**
 * @author Anna
 */
public class FavoriteLink extends FavoriteBlock implements IWPageEventListener {

	private static final String PARAMETER_ADD_PAGE = "fav_add_page";
	
	String currentName = null;
	String currentURL = null;
	User currentUser;

	public void present(IWContext iwc) {
		parse(iwc);
		
		try {
			Favorite favorite = getBusiness(iwc).getFavoriteByName(currentUser, currentName, getBusiness(iwc).getFavoriteTypeIntranet());
			if (favorite != null) {
				add(getImage());
			}
		}
		catch (FinderException fe) {
			add(getLink());

		}
		catch (RemoteException re) {
			log(re);
		}
	}
	
	private void parse(IWContext iwc) {
		ICPage currentPage = null;
		try {
			currentPage = getBuilderService(iwc).getCurrentPage(iwc);
			currentURL = getBuilderService(iwc).getCurrentPageURI(iwc);
		}
		catch (RemoteException re) {
			log(re);
		}
		currentName = currentPage.getName();
		currentUser = iwc.getCurrentUser();
		
		if (iwc.isParameterSet(PARAMETER_ADD_PAGE)) {
			Object[] arguments = { currentName };
			getParentPage().setAlertOnLoad(MessageFormat.format(getResourceBundle().getLocalizedString("added_page_to_favorites", "Added page {0}Êto your intranet favorites"), arguments));
		}
	}

	private Image getImage() {
		return getBundle().getImage("disabled.gif");
	}

	private Link getLink() {
		Link favoriteLink = new Link(getBundle().getImage("enabled.jpg"));
		favoriteLink.addParameter(PARAMETER_ADD_PAGE, "true");
		favoriteLink.setEventListener(this.getClass());
		return favoriteLink;
	}

	/* (non-Javadoc)
	 * @see com.idega.event.IWEventListener#actionPerformed(com.idega.event.IWPresentationEvent)
	 */
	public boolean actionPerformed(IWContext iwc) throws IWException {
		try {
			parse(iwc);
			getBusiness(iwc).storeFavorite(currentUser, currentURL, currentName, getBusiness(iwc).getFavoriteTypeIntranet(), false);
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