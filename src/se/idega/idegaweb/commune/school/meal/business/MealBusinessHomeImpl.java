/*
 * $Id: MealBusinessHomeImpl.java,v 1.4 2005/10/02 18:41:15 laddi Exp $
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
 * <p>
 * TODO laddi Describe Type MealBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2005/10/02 18:41:15 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public class MealBusinessHomeImpl extends IBOHomeImpl implements MealBusinessHome {

	protected Class getBeanInterfaceClass() {
		return MealBusiness.class;
	}

	public MealBusiness create() throws javax.ejb.CreateException {
		return (MealBusiness) super.createIBO();
	}
}
