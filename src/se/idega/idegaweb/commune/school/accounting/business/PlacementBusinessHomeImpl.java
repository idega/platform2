/*
 * $Id: PlacementBusinessHomeImpl.java,v 1.2 2004/10/18 16:36:44 thomas Exp $
 * Created on Oct 18, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.accounting.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2004/10/18 16:36:44 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.2 $
 */
public class PlacementBusinessHomeImpl extends IBOHomeImpl implements PlacementBusinessHome {

	protected Class getBeanInterfaceClass() {
		return PlacementBusiness.class;
	}

	public PlacementBusiness create() throws javax.ejb.CreateException {
		return (PlacementBusiness) super.createIBO();
	}
}
