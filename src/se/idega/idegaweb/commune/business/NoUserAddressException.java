/*
 * $Id: NoUserAddressException.java,v 1.1 2004/09/18 17:25:27 aron Exp $
 * Created on 18.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.business;

import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2004/09/18 17:25:27 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class NoUserAddressException extends Exception {
    NoUserAddressException(User user) {
		super("User :" + user.getPrimaryKey().toString() + " : " + user.getName() + " has no address ");
	}
}
