/*
 * $Id: ManagementTypeHome.java,v 1.1 2005/09/07 11:39:46 palli Exp $
 * Created on Sep 7, 2005
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
 *  Last modified: $Date: 2005/09/07 11:39:46 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.1 $
 */
public interface ManagementTypeHome extends IDOHome {

	public ManagementType create() throws javax.ejb.CreateException;

	public ManagementType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ManagementTypeBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

}
