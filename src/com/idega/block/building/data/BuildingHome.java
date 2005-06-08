/*
 * $Id: BuildingHome.java,v 1.4 2005/06/08 11:42:06 palli Exp $
 * Created on Jun 6, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;


/**
 * 
 *  Last modified: $Date: 2005/06/08 11:42:06 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.4 $
 */
public interface BuildingHome extends IDOHome {

	public Building create() throws javax.ejb.CreateException;

	public Building findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#ejbFindByComplex
	 */
	public Collection findByComplex(Integer complexID) throws FinderException;

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#ejbHomeGetImageFilesByComplex
	 */
	public Collection getImageFilesByComplex(Integer complexID) throws FinderException;

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#ejbFindByComplex
	 */
	public Collection findByComplex(Complex complex) throws FinderException;

}
