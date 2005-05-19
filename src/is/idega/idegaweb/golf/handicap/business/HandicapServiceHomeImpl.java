/*
 * $Id: HandicapServiceHomeImpl.java,v 1.1 2005/05/19 07:32:43 laddi Exp $
 * Created on May 19, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.handicap.business;

import com.idega.business.IBOHomeImpl;


/**
 * Last modified: $Date: 2005/05/19 07:32:43 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class HandicapServiceHomeImpl extends IBOHomeImpl implements HandicapServiceHome {

	protected Class getBeanInterfaceClass() {
		return HandicapService.class;
	}

	public HandicapService create() throws javax.ejb.CreateException {
		return (HandicapService) super.createIBO();
	}
}
