/*
 * Created on 1.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.staff.business;

import com.idega.business.IBOHomeImpl;

/**
 * @author laddi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StaffUserSessionHomeImpl extends IBOHomeImpl implements
		StaffUserSessionHome {
	protected Class getBeanInterfaceClass() {
		return StaffUserSession.class;
	}

	public StaffUserSession create() throws javax.ejb.CreateException {
		return (StaffUserSession) super.createIBO();
	}

}
