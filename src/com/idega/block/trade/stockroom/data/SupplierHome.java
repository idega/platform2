/*
 * $Id: SupplierHome.java,v 1.8 2005/06/01 09:28:33 gimmi Exp $
 * Created on 1.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;
import com.idega.user.data.Group;


/**
 * 
 *  Last modified: $Date: 2005/06/01 09:28:33 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.8 $
 */
public interface SupplierHome extends IDOHome {

	public Supplier create() throws javax.ejb.CreateException;

	public Supplier findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	public Supplier findByPrimaryKey(int id) throws javax.ejb.FinderException;

	public Supplier findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#ejbFindAll
	 */
	public Collection findAll(Group supplierManager) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#ejbFindWithTPosMerchant
	 */
	public Collection findWithTPosMerchant() throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#ejbFindAllByGroupID
	 */
	public Collection findAllByGroupID(int groupID) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#ejbFindByPostalCodes
	 */
	public Collection findByPostalCodes(Group supplierManager, String[] from, String[] to, Collection criterias)
			throws IDORelationshipException, FinderException;
}
