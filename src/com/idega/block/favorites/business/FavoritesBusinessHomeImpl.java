/*
 * Created on Oct 26, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.favorites.business;



import com.idega.business.IBOHomeImpl;

/**
 * @author Anna
 */
public class FavoritesBusinessHomeImpl extends IBOHomeImpl implements
        FavoritesBusinessHome {
    protected Class getBeanInterfaceClass() {
        return FavoritesBusiness.class;
    }

    public FavoritesBusiness create() throws javax.ejb.CreateException {
        return (FavoritesBusiness) super.createIBO();
    }

}
