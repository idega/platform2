/*
 * $Id: MealBusinessHomeImpl.java,v 1.2 2005/08/12 19:29:50 gimmi Exp $
 * Created on Aug 12, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2005/08/12 19:29:50 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.2 $
 */
public class MealBusinessHomeImpl extends IBOHomeImpl implements MealBusinessHome {

	protected Class getBeanInterfaceClass() {
		return MealBusiness.class;
	}

	public MealBusiness create() throws javax.ejb.CreateException {
		return (MealBusiness) super.createIBO();
	}
}