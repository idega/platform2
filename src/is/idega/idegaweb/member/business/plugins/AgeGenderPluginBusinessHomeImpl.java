/*
 * $Id: AgeGenderPluginBusinessHomeImpl.java,v 1.3 2005/07/03 19:01:45 eiki Exp $
 * Created on Jul 3, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.business.plugins;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2005/07/03 19:01:45 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.3 $
 */
public class AgeGenderPluginBusinessHomeImpl extends IBOHomeImpl implements AgeGenderPluginBusinessHome {

	protected Class getBeanInterfaceClass() {
		return AgeGenderPluginBusiness.class;
	}

	public AgeGenderPluginBusiness create() throws javax.ejb.CreateException {
		return (AgeGenderPluginBusiness) super.createIBO();
	}
}
