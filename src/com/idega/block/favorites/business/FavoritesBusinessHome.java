/*
 * Created on Oct 26, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.favorites.business;



import com.idega.business.IBOHome;

/**
 * @author Anna
 */
public interface FavoritesBusinessHome extends IBOHome {
    public FavoritesBusiness create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}
