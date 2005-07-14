/*
 * $Id: AgeGenderPluginBusinessHome.java,v 1.4 2005/07/14 01:02:25 eiki Exp $
 * Created on Jul 3, 2005
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
 *  Last modified: $Date: 2005/07/14 01:02:25 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.4 $
 */
public interface AgeGenderPluginBusinessHome extends IBOHome {

	public AgeGenderPluginBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
