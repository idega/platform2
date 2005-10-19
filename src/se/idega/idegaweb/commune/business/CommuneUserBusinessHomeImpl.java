/*
 * $Id: CommuneUserBusinessHomeImpl.java,v 1.4 2005/10/19 11:44:38 palli Exp $
 * Created on Oct 14, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.business;





import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2005/10/19 11:44:38 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.4 $
 */
public class CommuneUserBusinessHomeImpl extends IBOHomeImpl implements CommuneUserBusinessHome {

	protected Class getBeanInterfaceClass() {
		return CommuneUserBusiness.class;
	}

	public CommuneUserBusiness create() throws javax.ejb.CreateException {
		return (CommuneUserBusiness) super.createIBO();
	}

}
