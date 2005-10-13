/*
 * $Id: ManagementTypeHome.java,v 1.2 2005/10/13 08:09:37 palli Exp $
 * Created on Oct 11, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.regulations.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;


/**
 * 
 *  Last modified: $Date: 2005/10/13 08:09:37 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.2 $
 */
public interface ManagementTypeHome extends IDOHome {

	public ManagementType create() throws javax.ejb.CreateException;

	public ManagementType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ManagementTypeBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

}
