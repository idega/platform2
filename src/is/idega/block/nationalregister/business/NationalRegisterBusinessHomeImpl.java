/*
 * $Id$
 * Created on 14.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.nationalregister.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date$ by $Author$
 * 
 * @author <a href="mailto:Joakim@idega.com">Joakim</a>
 * @version $Revision$
 */
public class NationalRegisterBusinessHomeImpl extends IBOHomeImpl implements NationalRegisterBusinessHome {

	protected Class getBeanInterfaceClass() {
		return NationalRegisterBusiness.class;
	}

	public NationalRegisterBusiness create() throws javax.ejb.CreateException {
		return (NationalRegisterBusiness) super.createIBO();
	}
}
