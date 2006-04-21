/*
 * $Id: AfterSchoolBusinessHomeImpl.java,v 1.3.2.7 2006/04/21 14:52:32 igors Exp $
 * Created on Apr 6, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;




import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO laddi Describe Type AfterSchoolBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2006/04/21 14:52:32 $ by $Author: igors $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3.2.7 $
 */
public class AfterSchoolBusinessHomeImpl extends IBOHomeImpl implements AfterSchoolBusinessHome {

	protected Class getBeanInterfaceClass() {
		return AfterSchoolBusiness.class;
	}

	public AfterSchoolBusiness create() throws javax.ejb.CreateException {
		return (AfterSchoolBusiness) super.createIBO();
	}

}
