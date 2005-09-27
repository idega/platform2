/*
 * $Id: GolfUserPluginBusinessHome.java,v 1.4 2005/09/27 17:33:58 eiki Exp $
 * Created on Sep 27, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.business.plugin;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2005/09/27 17:33:58 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.4 $
 */
public interface GolfUserPluginBusinessHome extends IBOHome {

	public GolfUserPluginBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
