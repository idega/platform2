/*
 * $Id: FavoriteHome.java,v 1.1 2004/10/08 07:08:34 laddi Exp $
 * Created on 7.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.data;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2004/10/08 07:08:34 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface FavoriteHome extends IDOHome {

	public Favorite create() throws CreateException;
	public Favorite findByPrimaryKey(Object pk) throws FinderException;
	
	public Favorite findByUserAndURL(User user, String URL) throws FinderException;
	public Favorite findByUserAndName(User user, String name) throws FinderException;
	
	public Collection findAllByUser(User user) throws FinderException;
	public Collection findAllQuickLinksByUser(User user) throws FinderException;
	
}