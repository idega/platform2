/*
 * $Id: GolfUserPluginBusinessHome.java,v 1.4.4.1 2006/03/30 11:16:06 palli Exp $
 * Created on Mar 28, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.business.plugin;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2006/03/30 11:16:06 $ by $Author: palli $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.4.4.1 $
 */
public interface GolfUserPluginBusinessHome extends IBOHome {

	public GolfUserPluginBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
