/*
 * $Id: AdultEducationSessionHome.java,v 1.6 2005/06/02 07:50:05 laddi Exp $
 * Created on Jun 2, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2005/06/02 07:50:05 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.6 $
 */
public interface AdultEducationSessionHome extends IBOHome {

	public AdultEducationSession create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
