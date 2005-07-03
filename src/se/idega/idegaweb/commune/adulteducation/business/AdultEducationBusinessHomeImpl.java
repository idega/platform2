/*
 * $Id: AdultEducationBusinessHomeImpl.java,v 1.24 2005/07/03 14:37:08 laddi Exp $
 * Created on Jul 3, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import com.idega.business.IBOHomeImpl;


/**
 * Last modified: $Date: 2005/07/03 14:37:08 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.24 $
 */
public class AdultEducationBusinessHomeImpl extends IBOHomeImpl implements AdultEducationBusinessHome {

	protected Class getBeanInterfaceClass() {
		return AdultEducationBusiness.class;
	}

	public AdultEducationBusiness create() throws javax.ejb.CreateException {
		return (AdultEducationBusiness) super.createIBO();
	}
}
