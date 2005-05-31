/*
 * $Id: TravelSessionManagerHomeImpl.java,v 1.3 2005/05/31 19:15:20 gimmi Exp $
 * Created on 10.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2005/05/31 19:15:20 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.3 $
 */
public class TravelSessionManagerHomeImpl extends IBOHomeImpl implements TravelSessionManagerHome {

	protected Class getBeanInterfaceClass() {
		return TravelSessionManager.class;
	}

	public TravelSessionManager create() throws javax.ejb.CreateException {
		return (TravelSessionManager) super.createIBO();
	}
}
