/*
 * $Id: MealBusinessHomeImpl.java,v 1.5 2005/10/02 22:12:23 laddi Exp $
 * Created on Oct 2, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.business;

import com.idega.business.IBOHomeImpl;


/**
 * Last modified: $Date: 2005/10/02 22:12:23 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.5 $
 */
public class MealBusinessHomeImpl extends IBOHomeImpl implements MealBusinessHome {

	protected Class getBeanInterfaceClass() {
		return MealBusiness.class;
	}

	public MealBusiness create() throws javax.ejb.CreateException {
		return (MealBusiness) super.createIBO();
	}
}
