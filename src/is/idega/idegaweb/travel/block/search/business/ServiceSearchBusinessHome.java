/*
 * $Id: ServiceSearchBusinessHome.java,v 1.3 2005/07/05 22:43:10 gimmi Exp $
 * Created on Jul 4, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.block.search.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2005/07/05 22:43:10 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.3 $
 */
public interface ServiceSearchBusinessHome extends IBOHome {

	public ServiceSearchBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
