/*
 * $Id: FamilyLogicHomeImpl.java,v 1.3 2004/09/06 10:54:11 joakim Exp $
 * Created on 6.9.2004
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
 *  Last modified: $Date: 2004/09/06 10:54:11 $ by $Author: joakim $
 * 
 * @author <a href="mailto:Joakim@idega.com">Joakim</a>
 * @version $Revision: 1.3 $
 */
public class FamilyLogicHomeImpl extends IBOHomeImpl implements FamilyLogicHome {

	protected Class getBeanInterfaceClass() {
		return FamilyLogic.class;
	}

	public FamilyLogic create() throws javax.ejb.CreateException {
		return (FamilyLogic) super.createIBO();
	}
}
