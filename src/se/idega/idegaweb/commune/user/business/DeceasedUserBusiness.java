/*
 * $Id: DeceasedUserBusiness.java,v 1.1 2004/10/05 18:40:38 thomas Exp $
 * Created on Oct 5, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.user.business;

import java.util.Date;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.repository.data.ImplementorPlaceholder;


/**
 * 
 *  Last modified: $Date: 2004/10/05 18:40:38 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public interface DeceasedUserBusiness extends ImplementorPlaceholder {
	
	boolean setUserAsDeceased(Integer userID,Date deceasedDate, IWApplicationContext iwac);
}
