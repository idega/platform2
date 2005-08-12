/*
 * $Id: MealSessionHome.java,v 1.2 2005/08/12 08:53:25 gimmi Exp $
 * Created on Aug 11, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2005/08/12 08:53:25 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.2 $
 */
public interface MealSessionHome extends IBOHome {

	public MealSession create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
