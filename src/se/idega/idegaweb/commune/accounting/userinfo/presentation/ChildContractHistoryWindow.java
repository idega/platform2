/*
 * $Id: ChildContractHistoryWindow.java,v 1.1 2004/10/07 18:11:40 thomas Exp $
 * Created on Oct 7, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.userinfo.presentation;

import com.idega.repository.data.ImplementorPlaceholder;


/**
 * 
 *  Last modified: $Date: 2004/10/07 18:11:40 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public interface ChildContractHistoryWindow extends ImplementorPlaceholder {
	
	String getParameterChildID();
}
