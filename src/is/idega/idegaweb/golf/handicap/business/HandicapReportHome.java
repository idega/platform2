/*
 * $Id: HandicapReportHome.java,v 1.1 2005/02/07 11:20:28 laddi Exp $
 * Created on 7.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.handicap.business;




import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2005/02/07 11:20:28 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface HandicapReportHome extends IBOHome {

	public HandicapReport create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
