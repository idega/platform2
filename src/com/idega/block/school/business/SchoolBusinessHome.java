/*
 * $Id: SchoolBusinessHome.java,v 1.16 2005/10/20 01:08:09 palli Exp $
 * Created on Oct 20, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.business;



import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2005/10/20 01:08:09 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.16 $
 */
public interface SchoolBusinessHome extends IBOHome {

	public SchoolBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
