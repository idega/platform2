/*
 * $Id: GolfUserPluginBusinessHomeImpl.java,v 1.4.4.1 2006/03/30 11:16:06 palli Exp $
 * Created on Mar 28, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.business.plugin;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2006/03/30 11:16:06 $ by $Author: palli $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.4.4.1 $
 */
public class GolfUserPluginBusinessHomeImpl extends IBOHomeImpl implements GolfUserPluginBusinessHome {

	protected Class getBeanInterfaceClass() {
		return GolfUserPluginBusiness.class;
	}

	public GolfUserPluginBusiness create() throws javax.ejb.CreateException {
		return (GolfUserPluginBusiness) super.createIBO();
	}
}
