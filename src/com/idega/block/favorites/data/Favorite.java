/*
 * $Id: Favorite.java,v 1.1 2004/10/08 07:08:34 laddi Exp $
 * Created on 7.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.data;

import java.sql.Timestamp;

import com.idega.data.IDOEntity;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2004/10/08 07:08:34 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface Favorite extends IDOEntity {

	public User getOwner();
	public Timestamp getTimestamp();
	public String getURL();
	public String getName();
	
	public boolean isQuickLink();
	public boolean isDeleted();
	
	public void setOwner(User user);
	public void setTimestamp(Timestamp timestamp);
	public void setURL(String URL);
	public void setName(String name);
	public void setAsQuickLink(boolean asQuickLink);
	public void setDeleted(boolean deleted);
	
}