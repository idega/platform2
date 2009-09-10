/*
 * $Id: FavoritesBusinessHome.java,v 1.2 2004/11/05 13:26:10 laddi Exp $
 * Created on 3.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.business;



import com.idega.business.IBOHome;


/**
 * Last modified: 3.11.2004 10:34:54 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface FavoritesBusinessHome extends IBOHome {

	public FavoritesBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
