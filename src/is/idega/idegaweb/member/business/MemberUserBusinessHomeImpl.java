/*
 * $Id: MemberUserBusinessHomeImpl.java,v 1.2 2005/01/04 15:44:23 palli Exp $
 * Created on Jan 4, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2005/01/04 15:44:23 $ by $Author: palli $
 * 
 * @author <a href="mailto:palli@idega.com">palli</a>
 * @version $Revision: 1.2 $
 */
public class MemberUserBusinessHomeImpl extends IBOHomeImpl implements MemberUserBusinessHome {

	protected Class getBeanInterfaceClass() {
		return MemberUserBusiness.class;
	}

	public MemberUserBusiness create() throws javax.ejb.CreateException {
		return (MemberUserBusiness) super.createIBO();
	}
}
