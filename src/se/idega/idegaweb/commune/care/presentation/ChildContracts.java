/*
 * $Id: ChildContracts.java,v 1.1 2004/10/14 17:32:26 thomas Exp $
 * Created on Oct 7, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.repository.data.ImplementorPlaceholder;


/**
 * 
 *  Last modified: $Date: 2004/10/14 17:32:26 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public interface ChildContracts extends ImplementorPlaceholder {
	
	PresentationObject getPresentation();
	
	Class getWindowClass();
	
	String getParameterChildID();
	
	void storeChildInSession(int childID, IWContext iwc);
}
