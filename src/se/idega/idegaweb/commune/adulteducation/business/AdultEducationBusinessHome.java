/*
 * $Id: AdultEducationBusinessHome.java,v 1.14 2005/05/30 10:01:43 laddi Exp $
 * Created on May 30, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2005/05/30 10:01:43 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.14 $
 */
public interface AdultEducationBusinessHome extends IBOHome {

	public AdultEducationBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
