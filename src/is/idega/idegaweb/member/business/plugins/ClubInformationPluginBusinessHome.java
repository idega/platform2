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

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date$ by $Author$
 * 
 * @author <a href="mailto:palli@idega.com">palli</a>
 * @version $Revision$
 */
public interface ClubInformationPluginBusinessHome extends IBOHome {

	public ClubInformationPluginBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
