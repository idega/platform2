/*
 * $Id: NackaFixBusinessHome.java,v 1.3 2004/12/07 21:53:56 laddi Exp $
 * Created on 7.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.business;




import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2004/12/07 21:53:56 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public interface NackaFixBusinessHome extends IBOHome {

	public NackaFixBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
