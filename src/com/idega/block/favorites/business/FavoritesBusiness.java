/*
 * $Id: FavoritesBusiness.java,v 1.1 2004/10/08 07:08:34 laddi Exp $
 * Created on 7.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.business;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.favorites.data.Favorite;
import com.idega.business.IBOService;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2004/10/08 07:08:34 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface FavoritesBusiness extends IBOService {

	public Collection getFavorites(User user) throws FinderException, RemoteException;
	public Collection getQuickLinkFavorites(User user) throws FinderException, RemoteException;
	
	public void storeFavorite(User user, String URL, String name, boolean quickLink) throws CreateException, RemoteException;
	public void deleteFavorite(Favorite favorite) throws RemoveException, RemoteException;
	
}