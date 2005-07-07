/*
 * $Id: AdultEducationSessionHome.java,v 1.8 2005/07/07 08:41:42 laddi Exp $
 * Created on Jul 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2005/07/07 08:41:42 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.8 $
 */
public interface AdultEducationSessionHome extends IBOHome {

	public AdultEducationSession create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
