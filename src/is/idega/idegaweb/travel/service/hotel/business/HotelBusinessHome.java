/*
 * $Id: HotelBusinessHome.java,v 1.4 2005/08/24 13:23:40 gimmi Exp $
 * Created on Aug 12, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.service.hotel.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2005/08/24 13:23:40 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.4 $
 */
public interface HotelBusinessHome extends IBOHome {

	public HotelBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
