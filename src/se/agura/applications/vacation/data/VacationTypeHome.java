/*
 * $Id: VacationTypeHome.java,v 1.2 2004/12/13 14:44:20 anna Exp $
 * Created on 8.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.vacation.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * Last modified: 8.12.2004 14:05:30 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.2 $
 */
public interface VacationTypeHome extends IDOHome {

	public VacationType create() throws javax.ejb.CreateException;

	public VacationType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.agura.applications.vacation.data.VacationTypeBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;
}
