/*
 * $Id$
 * Created on Jan 4, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.business.plugins;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date$ by $Author$
 * 
 * @author <a href="mailto:palli@idega.com">palli</a>
 * @version $Revision$
 */
public class ClubInformationPluginBusinessHomeImpl extends IBOHomeImpl implements ClubInformationPluginBusinessHome {

	protected Class getBeanInterfaceClass() {
		return ClubInformationPluginBusiness.class;
	}

	public ClubInformationPluginBusiness create() throws javax.ejb.CreateException {
		return (ClubInformationPluginBusiness) super.createIBO();
	}
}
