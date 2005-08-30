/*
 * $Id: BookerHomeImpl.java,v 1.3 2005/08/30 02:08:32 gimmi Exp $
 * Created on Aug 30, 2005
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
 *  Last modified: $Date: 2005/08/30 02:08:32 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.3 $
 */
public class BookerHomeImpl extends IBOHomeImpl implements BookerHome {

	protected Class getBeanInterfaceClass() {
		return Booker.class;
	}

	public Booker create() throws javax.ejb.CreateException {
		return (Booker) super.createIBO();
	}
}
