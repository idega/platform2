/*
 * $Id: ProductBusiness.java,v 1.44 2005/06/16 21:04:35 gimmi Exp $
 * Created on 12.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.stockroom.business;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import com.idega.block.category.data.ICCategory;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductCategory;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.ProductPriceHome;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.business.IBOService;
import com.idega.core.location.data.Address;
import com.idega.data.IDOException;
import com.idega.data.IDOFinderException;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.IWTimestamp;


/**
 * 
 *  Last modified: $Date: 2005/06/16 21:04:35 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.44 $
 */
public interface ProductBusiness extends IBOService {

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#updateProduct
	 */
	public int updateProduct(int productId, int supplierId, Integer fileId, String productName, String number,
			String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception,
			java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#updateProduct
	 */
	public int updateProduct(int productId, Integer fileId, String productName, String number,
			String productDescription, boolean isValid, int localeId) throws Exception, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#createProduct
	 */
	public int createProduct(Integer fileId, String productName, String number, String productDescription,
			boolean isValid, int localeId) throws Exception, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#createProduct
	 */
	public int createProduct(Integer fileId, String productName, String number, String productDescription,
			boolean isValid) throws Exception, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#createProduct
	 */
	public int createProduct(int supplierId, Integer fileId, String productName, String number,
			String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception,
			java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#createProduct
	 */
	public int createProduct(int productId, int supplierId, Integer fileId, String productName, String number,
			String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception,
			java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#createProduct
	 */
	public int createProduct(int productId, int supplierId, Integer fileId, String productName, String number,
			String productDescription, boolean isValid, int[] addressIds, int discountTypeId, int localeId)
			throws Exception, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProductIdParameter
	 */
	public String getProductIdParameter() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getParameterLocaleDrop
	 */
	public String getParameterLocaleDrop() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProduct
	 */
	public Product getProduct(Integer productId) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProduct
	 */
	public Product getProduct(int productId) throws RemoteException, FinderException;
    public Collection getProduct(int supplierId, int firstEntity, int lastEntity) throws FinderException, RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#deleteProduct
	 */
	public void deleteProduct(Product product) throws RemoteException, IDOException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#updateProduct
	 */
	public Product updateProduct(Product product) throws RemoteException, FinderException, IDOException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProductCategory
	 */
	public ProductCategory getProductCategory(int categoryID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProductNameWithNumber
	 */
	public String getProductNameWithNumber(Product product) throws RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProductNameWithNumber
	 */
	public String getProductNameWithNumber(Product product, int localeID) throws RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProductNameWithNumber
	 */
	public String getProductNameWithNumber(Product product, boolean numberInFront) throws RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProductNameWithNumber
	 */
	public String getProductNameWithNumber(Product product, boolean numberInFront, int localeID) throws RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getSelectedLocaleId
	 */
	public int getSelectedLocaleId(IWContext iwc) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getLocaleDropDown
	 */
	public DropdownMenu getLocaleDropDown(IWContext iwc) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#clearProductCache
	 */
	public void clearProductCache(int supplierId) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProducts
	 */
	public List getProducts(IWContext iwc, int supplierId) throws RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProducts
	 */
	public List getProducts(int supplierId) throws RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProducts
	 */
	public List getProducts() throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProducts
	 */
	public List getProducts(List productCategories) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProducts
	 */
	public List getProducts(ICCategory category) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProducts
	 */
	public List getProducts(ProductCategory productCategory) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProducts
	 */
	public List getProducts(IWTimestamp stamp) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProducts
	 */
	public List getProducts(IWTimestamp fromStamp, IWTimestamp toStamp) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProducts
	 */
	public List getProducts(int supplierId, IWTimestamp stamp) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProducts
	 */
	public List getProducts(int supplierId, IWTimestamp from, IWTimestamp to) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProducts
	 */
	public List getProducts(int supplierId, int productCategoryId, IWTimestamp from, IWTimestamp to)
			throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getTimeframe
	 */
	public Timeframe getTimeframe(Product product, IWTimestamp stamp) throws RemoteException, EJBException,
			FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getTimeframe
	 */
	public Timeframe getTimeframe(Product product, IWTimestamp stamp, int travelAddressId) throws RemoteException,
			EJBException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getTimeframe
	 */
	public Timeframe getTimeframe(Product product, Timeframe[] timeframes, IWTimestamp stamp, int travelAddressId)
			throws RemoteException, EJBException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getDepartureAddresses
	 */
	public List getDepartureAddresses(Product product, IWTimestamp stamp, boolean ordered) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getDepartureAddresses
	 */
	public List getDepartureAddresses(Product product, IWTimestamp stamp, boolean ordered, String key)
			throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getDepartureAddresses
	 */
	public List getDepartureAddresses(Product product, IWTimestamp stamp, boolean ordered, String key,
			Timeframe[] timeframes) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getDepartureAddresses
	 */
	public List getDepartureAddresses(Product product, boolean ordered) throws RemoteException, IDOFinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getDepartureAddress
	 */
	public TravelAddress getDepartureAddress(Product product) throws RemoteException, IDOFinderException, SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getArrivalAddresses
	 */
	public Address[] getArrivalAddresses(Product product) throws RemoteException, IDOFinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getArrivalAddress
	 */
	public Address getArrivalAddress(Product product) throws RemoteException, IDOFinderException, SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getDropdownMenuWithProducts
	 */
	public DropdownMenu getDropdownMenuWithProducts(IWContext iwc, int supplierId) throws RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getDropdownMenuWithProducts
	 */
	public DropdownMenu getDropdownMenuWithProducts(IWContext iwc, int supplierId, String parameterName)
			throws RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getDropdownMenuWithProducts
	 */
	public DropdownMenu getDropdownMenuWithProducts(IWContext iwc, List products, String parameterName)
			throws RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProductCategories
	 */
	public List getProductCategories() throws IDOFinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProductCategories
	 */
	public List getProductCategories(Product product) throws RemoteException, IDORelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProductHome
	 */
	public ProductHome getProductHome() throws RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductBusinessBean#getProductPriceHome
	 */
	public ProductPriceHome getProductPriceHome() throws RemoteException;
}
