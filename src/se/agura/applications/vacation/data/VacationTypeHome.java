/*
 * $Id: VacationTypeHome.java,v 1.3 2005/01/11 09:29:15 laddi Exp $
 * Created on 11.1.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.vacation.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;


/**
 * Last modified: $Date: 2005/01/11 09:29:15 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public interface VacationTypeHome extends IDOHome {

	public VacationType create() throws javax.ejb.CreateException;

	public VacationType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.agura.applications.vacation.data.VacationTypeBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see se.agura.applications.vacation.data.VacationTypeBMPBean#ejbFindByName
	 */
	public VacationType findByName(String name) throws FinderException;

}
