package com.idega.block.trade.stockroom.data;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import javax.ejb.FinderException;
import com.idega.block.text.data.TxText;
import com.idega.core.file.data.ICFile;
import com.idega.core.location.data.Address;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.MetaDataCapable;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public interface Product extends IDOLegacyEntity, MetaDataCapable {

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#invalidate
	 */
	public void invalidate() throws IDOException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getID
	 */
	public int getID();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#setSupplierId
	 */
	public void setSupplierId(int id);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#setSupplierId
	 */
	public void setSupplierId(Integer id);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#setFileId
	 */
	public void setFileId(int id);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#setFileId
	 */
	public void setFileId(Integer id);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#setIsValid
	 */
	public void setIsValid(boolean valid);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#setDiscountTypeId
	 */
	public void setDiscountTypeId(int discountTypeId);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#setNumber
	 */
	public void setNumber(String number);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#setCreationDate
	 */
	public void setCreationDate(IWTimestamp timestamp);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#setCreationDate
	 */
	public void setCreationDate(Timestamp timestamp);
	
	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#setSaleOnHold
	 */
	public void setAuthorizationCheck(boolean saleOnHold);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getSupplierId
	 */
	public int getSupplierId();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getSupplier
	 */
	public Supplier getSupplier();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getFileId
	 */
	public int getFileId();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getFile
	 */
	public ICFile getFile();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getIsValid
	 */
	public boolean getIsValid();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getDiscountTypeId
	 */
	public int getDiscountTypeId();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getNumber
	 */
	public String getNumber();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getTimeframes
	 */
	public Timeframe[] getTimeframes() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getTimeframe
	 */
	public Timeframe getTimeframe() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getCreationDate
	 */
	public Timestamp getCreationDate();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getEditDate
	 */
	public Timestamp getEditDate();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getText
	 */
	public TxText getText() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getProductCategories
	 */
	public Collection getProductCategories() throws IDORelationshipException;
	
	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getSaleOnHold
	 */
	public boolean getAuthorizationCheck();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#setProductCategories
	 */
	public void setProductCategories(int[] categoryIds) throws RemoteException, FinderException,
			IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#addCategory
	 */
	public boolean addCategory(ProductCategory productCategory);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#removeCategory
	 */
	public void removeCategory(ProductCategory productCategory) throws IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#removeAllFrom
	 */
	public void removeAllFrom(Class entityInterface) throws IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#addTravelAddresses
	 */
	public void addTravelAddresses(int[] addressIds) throws RemoteException, FinderException,
			IDOAddRelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#addTravelAddress
	 */
	public void addTravelAddress(TravelAddress address);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#removeTravelAddress
	 */
	public void removeTravelAddress(TravelAddress address) throws IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getProductName
	 */
	public String getProductName(int localeId, String returnIfNull);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getProductName
	 */
	public String getProductName(int localeId);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getProductName
	 */
	public String getProductName(int localeId, int localeIDIfNull, String returnIfNull);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#setProductName
	 */
	public void setProductName(int localeId, String name);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getProductDescription
	 */
	public String getProductDescription(int localeId);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#setProductDescription
	 */
	public void setProductDescription(int localeId, String description);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getProductTeaser
	 */
	public String getProductTeaser(int localeId);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#setProductTeaser
	 */
	public void setProductTeaser(int localeId, String teaser);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getDepartureAddresses
	 */
	public List getDepartureAddresses(boolean ordered) throws IDOFinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#addArrivalAddress
	 */
	public void addArrivalAddress(Address address);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getArrivalAddresses
	 */
	public List getArrivalAddresses() throws IDOFinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getICFile
	 */
	public Collection getICFile() throws IDORelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#removeICFile
	 */
	public void removeICFile(ICFile file) throws IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#addICFile
	 */
	public void addICFile(ICFile file) throws IDOAddRelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#addTimeframe
	 */
	public void addTimeframe(Timeframe frame) throws IDOAddRelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#removeTimeframe
	 */
	public void removeTimeframe(Timeframe frame) throws IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#addText
	 */
	public void addText(TxText text) throws IDOAddRelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#setRefundable
	 */
	public void setRefundable(boolean refundable);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#getRefundable
	 */
	public boolean getRefundable();
}
