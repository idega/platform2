/*
 * $Id: AdultEducationSessionHome.java,v 1.7 2005/06/07 12:49:03 laddi Exp $
 * Created on Jun 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2005/06/07 12:49:03 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.7 $
 */
public interface AdultEducationSessionHome extends IBOHome {

	public AdultEducationSession create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
