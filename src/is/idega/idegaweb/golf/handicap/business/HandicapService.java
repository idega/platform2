/*
 * $Id: HandicapService.java,v 1.1 2005/05/19 07:32:43 laddi Exp $
 * Created on May 19, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.handicap.business;

import com.idega.business.IBOService;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/05/19 07:32:43 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface HandicapService extends IBOService {

	/**
	 * @see is.idega.idegaweb.golf.handicap.business.HandicapServiceBean#updateAllHandicaps
	 */
	public void updateAllHandicaps() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.handicap.business.HandicapServiceBean#updateAllHandicaps
	 */
	public void updateAllHandicaps(IWTimestamp fromDate) throws java.rmi.RemoteException;
}
