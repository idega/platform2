/*
 * $Id: CalBusinessHome.java,v 1.2 2004/12/07 16:05:58 eiki Exp $
 * Created on Dec 7, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.cal.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2004/12/07 16:05:58 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.2 $
 */
public interface CalBusinessHome extends IBOHome {

	public CalBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
