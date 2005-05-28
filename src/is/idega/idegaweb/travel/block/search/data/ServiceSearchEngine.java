/*
 * $Id: ServiceSearchEngine.java,v 1.6 2005/05/28 00:30:05 gimmi Exp $
 * Created on 24.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.block.search.data;

import java.util.Collection;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.Group;


/**
 * 
 *  Last modified: $Date: 2005/05/28 00:30:05 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.6 $
 */
public interface ServiceSearchEngine extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#setCode
	 */
	public void setCode(String code);

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#setStaffGroupID
	 */
	public void setStaffGroupID(int groupID);

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#setUseBasket
	 */
	public void setUseBasket(boolean useBasket);

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getStaffGroupID
	 */
	public int getStaffGroupID();

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getCode
	 */
	public String getCode();

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getIsValid
	 */
	public boolean getIsValid();

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getUseBasket
	 */
	public boolean getUseBasket();

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#addSupplier
	 */
	public void addSupplier(Supplier supplier) throws IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#removeSupplier
	 */
	public void removeSupplier(Supplier supplier) throws IDORemoveRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#removeAllSuppliers
	 */
	public void removeAllSuppliers() throws IDORemoveRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getSupplierManagerID
	 */
	public int getSupplierManagerID();

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getSupplierManager
	 */
	public Group getSupplierManager();

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#setSupplierManager
	 */
	public void setSupplierManager(Group group);

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#setSupplierManagerPK
	 */
	public void setSupplierManagerPK(Object pk);

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#setURL
	 */
	public void setURL(String URL);

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getURL
	 */
	public String getURL();

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getCountries
	 */
	public Collection getCountries() throws IDORelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#setCountries
	 */
	public void setCountries(Collection countries) throws IDORemoveRelationshipException, IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getSuppliers
	 */
	public Collection getSuppliers() throws IDORelationshipException;
}
