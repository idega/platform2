/*
 * $Id: FavoritesSessionHomeImpl.java,v 1.1 2004/11/05 13:26:10 laddi Exp $
 * Created on 4.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.business;


import com.idega.business.IBOHomeImpl;


/**
 * Last modified: 4.11.2004 15:55:33 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class FavoritesSessionHomeImpl extends IBOHomeImpl implements FavoritesSessionHome {

	protected Class getBeanInterfaceClass() {
		return FavoritesSession.class;
	}

	public FavoritesSession create() throws javax.ejb.CreateException {
		return (FavoritesSession) super.createIBO();
	}

}
