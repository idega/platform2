/*
 * $Id: Favorites.java,v 1.1 2004/11/05 13:26:10 laddi Exp $
 * Created on 5.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.presentation;

import com.idega.presentation.IWContext;


/**
 * Last modified: 5.11.2004 10:24:47 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class Favorites extends FavoriteBlock {

	/* (non-Javadoc)
	 * @see com.idega.block.favorites.presentation.FavoriteBlock#present(com.idega.presentation.IWContext)
	 */
	protected void present(IWContext iwc) {
		add(new FavoriteTypeNavigator());
		add("<br><br>");
		add(new FavoriteList());
		add("<br><br>");
		add(new FavoriteAddEditText());
		add("<br><br>");
		add(new FavoriteEditor());
	}

}
