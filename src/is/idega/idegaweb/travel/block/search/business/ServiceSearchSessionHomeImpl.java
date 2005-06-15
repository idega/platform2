/*
 * $Id: ServiceSearchSessionHomeImpl.java,v 1.2 2005/06/15 16:38:33 gimmi Exp $
 * Created on 15.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.block.search.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2005/06/15 16:38:33 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.2 $
 */
public class ServiceSearchSessionHomeImpl extends IBOHomeImpl implements ServiceSearchSessionHome {

	protected Class getBeanInterfaceClass() {
		return ServiceSearchSession.class;
	}

	public ServiceSearchSession create() throws javax.ejb.CreateException {
		return (ServiceSearchSession) super.createIBO();
	}
}
