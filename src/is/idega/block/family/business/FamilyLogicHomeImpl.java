/*
 * $Id: FamilyLogicHomeImpl.java,v 1.1 2004/08/27 16:15:24 joakim Exp $
 * Created on 27.8.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2004/08/27 16:15:24 $ by $Author: joakim $
 * 
 * @author <a href="mailto:Joakim@idega.com">Joakim</a>
 * @version $Revision: 1.1 $
 */
public class FamilyLogicHomeImpl extends IBOHomeImpl implements FamilyLogicHome {

	protected Class getBeanInterfaceClass() {
		return FamilyLogic.class;
	}

	public FamilyLogic create() throws javax.ejb.CreateException {
		return (FamilyLogic) super.createIBO();
	}
}
