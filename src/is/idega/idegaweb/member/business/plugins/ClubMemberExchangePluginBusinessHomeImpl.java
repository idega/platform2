/*
 * $Id: ClubMemberExchangePluginBusinessHomeImpl.java,v 1.2 2004/12/07 15:58:29 eiki Exp $
 * Created on Dec 7, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.business.plugins;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2004/12/07 15:58:29 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.2 $
 */
public class ClubMemberExchangePluginBusinessHomeImpl extends IBOHomeImpl implements
		ClubMemberExchangePluginBusinessHome {

	protected Class getBeanInterfaceClass() {
		return ClubMemberExchangePluginBusiness.class;
	}

	public ClubMemberExchangePluginBusiness create() throws javax.ejb.CreateException {
		return (ClubMemberExchangePluginBusiness) super.createIBO();
	}
}
