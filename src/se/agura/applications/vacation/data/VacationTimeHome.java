/*
 * $Id: VacationTimeHome.java,v 1.1 2004/11/25 14:22:35 anna Exp $
 * Created on 16.11.2004
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
 * Last modified: 16.11.2004 11:27:19 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public interface VacationTimeHome extends IDOHome {

	public VacationTime create() throws javax.ejb.CreateException;

	public VacationTime findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.agura.applications.vacation.data.VacationTimeBMPBean#ejbFindAllByVacationRequest
	 */
	public Collection findAllByVacationRequest(VacationRequest request) throws FinderException;
}
