/*
 * $Id$
 * Created on Dec 7, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.business.plugins;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date$ by $Author$
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision$
 */
public interface ClubInformationPluginBusinessHome extends IBOHome {

	public ClubInformationPluginBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
