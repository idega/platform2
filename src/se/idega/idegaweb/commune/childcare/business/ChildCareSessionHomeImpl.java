/*
 * $Id: ChildCareSessionHomeImpl.java,v 1.2 2005/03/07 16:40:30 laddi Exp $
 * Created on 7.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import com.idega.business.IBOHomeImpl;


/**
 * Last modified: $Date: 2005/03/07 16:40:30 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class ChildCareSessionHomeImpl extends IBOHomeImpl implements ChildCareSessionHome {

	protected Class getBeanInterfaceClass() {
		return ChildCareSession.class;
	}

	public ChildCareSession create() throws javax.ejb.CreateException {
		return (ChildCareSession) super.createIBO();
	}
}
