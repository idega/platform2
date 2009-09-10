package com.idega.block.trade.stockroom.data;

import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORemoveRelationshipException;


/**
 * @author gimmi
 */
public interface SupplyPool extends IDOEntity {

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolBMPBean#setDescription
	 */
	public void setDescription(String description);

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolBMPBean#getDescription
	 */
	public String getDescription();

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolBMPBean#setSupplier
	 */
	public void setSupplier(Supplier supplier);

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolBMPBean#setSupplierID
	 */
	public void setSupplierID(int supplierID);

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolBMPBean#getSupplier
	 */
	public Supplier getSupplier();

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolBMPBean#getSupplierID
	 */
	public int getSupplierID();

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolBMPBean#addProduct
	 */
	public void addProduct(Product product) throws IDOAddRelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolBMPBean#removeProduct
	 */
	public void removeProduct(Product product) throws IDORemoveRelationshipException;
}
