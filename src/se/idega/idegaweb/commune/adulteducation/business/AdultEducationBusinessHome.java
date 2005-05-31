/*
 * $Id: AdultEducationBusinessHome.java,v 1.15 2005/05/31 12:08:41 laddi Exp $
 * Created on May 31, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2005/05/31 12:08:41 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.15 $
 */
public interface AdultEducationBusinessHome extends IBOHome {

	public AdultEducationBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
