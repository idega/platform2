/*
 * $Id$
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
 *  Last modified: $Date$ by $Author$
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision$
 */
public class GroupBoardPluginBusinessHomeImpl extends IBOHomeImpl implements GroupBoardPluginBusinessHome {

	protected Class getBeanInterfaceClass() {
		return GroupBoardPluginBusiness.class;
	}

	public GroupBoardPluginBusiness create() throws javax.ejb.CreateException {
		return (GroupBoardPluginBusiness) super.createIBO();
	}
}
