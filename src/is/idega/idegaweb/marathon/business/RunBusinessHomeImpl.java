/*
 * $Id: RunBusinessHomeImpl.java,v 1.6 2005/05/27 12:55:30 laddi Exp $
 * Created on May 27, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.business;

import com.idega.business.IBOHomeImpl;


/**
 * Last modified: $Date: 2005/05/27 12:55:30 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.6 $
 */
public class RunBusinessHomeImpl extends IBOHomeImpl implements RunBusinessHome {

	protected Class getBeanInterfaceClass() {
		return RunBusiness.class;
	}

	public RunBusiness create() throws javax.ejb.CreateException {
		return (RunBusiness) super.createIBO();
	}
}
