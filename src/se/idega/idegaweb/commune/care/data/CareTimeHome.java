/*
 * $Id: CareTimeHome.java,v 1.1 2004/12/02 12:39:08 laddi Exp $
 * Created on 1.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOHome;
import com.idega.data.IDOLookup;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;


/**
 * Last modified: $Date: 2004/12/02 12:39:08 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface CareTimeHome extends IDOHome {

	public CareTime create() throws javax.ejb.CreateException;

	public CareTime findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.CareTimeBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

}
