/*
 * $Id: VacationBusinessHomeImpl.java,v 1.2 2004/12/06 21:30:34 laddi Exp $
 * Created on 5.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.vacation.business;




import com.idega.business.IBOHomeImpl;


/**
 * Last modified: $Date: 2004/12/06 21:30:34 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class VacationBusinessHomeImpl extends IBOHomeImpl implements VacationBusinessHome {

	protected Class getBeanInterfaceClass() {
		return VacationBusiness.class;
	}

	public VacationBusiness create() throws javax.ejb.CreateException {
		return (VacationBusiness) super.createIBO();
	}

}
