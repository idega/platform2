/*
 * $Id: MealSessionHome.java,v 1.3 2005/10/02 13:44:24 laddi Exp $
 * Created on Sep 30, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.business;

import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2005/10/02 13:44:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public interface MealSessionHome extends IBOHome {

	public MealSession create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
