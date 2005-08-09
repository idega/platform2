/*
 * $Id: RunBusinessHomeImpl.java,v 1.10 2005/08/09 11:06:04 laddi Exp $
 * Created on Aug 9, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.business;

import com.idega.business.IBOHomeImpl;


/**
 * Last modified: $Date: 2005/08/09 11:06:04 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.10 $
 */
public class RunBusinessHomeImpl extends IBOHomeImpl implements RunBusinessHome {

	protected Class getBeanInterfaceClass() {
		return RunBusiness.class;
	}

	public RunBusiness create() throws javax.ejb.CreateException {
		return (RunBusiness) super.createIBO();
	}
}
