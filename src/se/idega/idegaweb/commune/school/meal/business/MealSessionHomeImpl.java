/*
 * $Id: MealSessionHomeImpl.java,v 1.2 2005/08/12 08:53:25 gimmi Exp $
 * Created on Aug 11, 2005
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
 *  Last modified: $Date: 2005/08/12 08:53:25 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.2 $
 */
public class MealSessionHomeImpl extends IBOHomeImpl implements MealSessionHome {

	protected Class getBeanInterfaceClass() {
		return MealSession.class;
	}

	public MealSession create() throws javax.ejb.CreateException {
		return (MealSession) super.createIBO();
	}
}
