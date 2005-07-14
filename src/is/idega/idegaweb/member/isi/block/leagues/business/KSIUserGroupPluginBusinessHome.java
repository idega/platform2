/*
 * $Id: KSIUserGroupPluginBusinessHome.java,v 1.1 2005/07/14 01:00:43 eiki Exp $
 * Created on Jul 13, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.leagues.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2005/07/14 01:00:43 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.1 $
 */
public interface KSIUserGroupPluginBusinessHome extends IBOHome {

	public KSIUserGroupPluginBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
