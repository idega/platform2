package com.idega.block.trade.stockroom.business;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.category.business.CategoryFinder;
import com.idega.block.trade.business.CurrencyBusiness;
import com.idega.block.trade.business.CurrencyHolder;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.Image;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.SelectionBox;
import com.idega.util.IWTimestamp;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductEditorBusiness extends IBOServiceBean {
	private static ProductEditorBusiness peb;

	private ProductEditorBusiness() {
	}

	public static ProductEditorBusiness getInstance() {
		if (peb == null) {
			return new ProductEditorBusiness();
		}
		else {
			return peb;
		}
	}

	public void setCategories(Product product, String[] categoryIds) throws RemoteException, IDOException, FinderException {
		if (categoryIds != null) {
			int[] iCategoryIds = new int[categoryIds.length];
			for (int i = 0; i < categoryIds.length; i++) {
				iCategoryIds[i] = Integer.parseInt(categoryIds[i]);
			}

			product.setProductCategories(iCategoryIds);
		}
	}

	public void dropImage(Product product, boolean update) throws RemoteException {
		product.setFileId(null);
		if (update) {
			product.store();
		}
	}

	public void setThumbnail(Product product, int thumbnailId) throws RemoteException, FinderException, IDOException {
		try {
			boolean perform = true;
			if (thumbnailId != -1) {
				int newThumbId = thumbnailId;
				int oldThumbId = product.getFileId();

				if (newThumbId == product.getFileId()) {
					perform = false;
				}

				if (perform) {
					if (newThumbId == -1) {
						dropImage(product, true);
						//product.setFileId(null);
						//product.update();
					}
					else {
						product.setFileId(newThumbId);
						product.store();
						ICFileHome fileHome = (ICFileHome) IDOLookup.getHome(ICFile.class);
						ICFile file = fileHome.findByPrimaryKey(new Integer(newThumbId));
						product.removeICFile(file);
						//product.removeFrom(ICFile.class, newThumbId);
					}
				}

				if (perform) {
					if (oldThumbId != -1) {
						ICFileHome fileHome = (ICFileHome) IDOLookup.getHome(ICFile.class);
						ICFile file = fileHome.findByPrimaryKey(new Integer(oldThumbId));
						product.addICFile(file);
						//	    product.addTo(ICFile.class, oldThumbId);
					}
				}
			}
		}
		catch (IDOException ido) {
			ido.printStackTrace(System.err);
		}
	}

	public boolean setPrice(Product product, String price, String currencyId) throws RemoteException {
		if (price == null) {
			return false;
		}
		else {
			try {
				ProductPrice pPri = getStockroomBusiness().getPrice(product);
				int oldP = 0;
				int pCurrId = -1;
				if (pPri != null) {
					oldP = (int) pPri.getPrice();
					pCurrId = pPri.getID();
				}
				int newP = Integer.parseInt(price);
				if (oldP != newP || Integer.parseInt(currencyId) != pCurrId) {
					ProductPrice pPrice = ((com.idega.block.trade.stockroom.data.ProductPriceHome) com.idega.data.IDOLookup.getHomeLegacy(ProductPrice.class)).createLegacy();
					pPrice.setIsValid(true);
					pPrice.setPrice(Float.parseFloat(price));
					pPrice.setPriceType(com.idega.block.trade.stockroom.data.ProductPriceBMPBean.PRICETYPE_PRICE);
					pPrice.setProductId(product.getID());
					pPrice.setPriceDate(IWTimestamp.getTimestampRightNow());
					pPrice.setCurrencyId(Integer.parseInt(currencyId));
					pPrice.insert();
					return true;
				}
				else {
					return true;
				}
			}
			catch (Exception e) {
				return false;
			}
		}
	}

	public SelectionBox getCategorySelectionBox(Product product, String name, int productCatalogObjectInstanceId) throws RemoteException {
		SelectionBox catSel = new SelectionBox(name);
		List cats = CategoryFinder.getInstance().listOfCategoryForObjectInstanceId(productCatalogObjectInstanceId);
		if (product != null) {
			try {
				cats = getProductBusiness().getProductCategories(product);
			}
			catch (IDORelationshipException ido) {
				ido.printStackTrace(System.err);
			}
		}

		if (cats != null) {
			catSel.addMenuElements(cats);
			//      if ( product != null ) { Gimmi 17.08.02
			catSel.setAllSelected(true);
			//      }
		}

		return catSel;
	}

	public SelectionBox getSelectionBox(Product product, String name, int productCatalogObjectInstanceId) throws RemoteException {
		SelectionBox catSel = new SelectionBox(name);
		List cats = CategoryFinder.getInstance().listOfCategoryForObjectInstanceId(productCatalogObjectInstanceId);
		if (product != null) {
			try {
				cats = getProductBusiness().getProductCategories(product);
			}
			catch (IDORelationshipException ido) {
				ido.printStackTrace(System.err);
			}
		}

		if (cats != null) {
			catSel.addMenuElements(cats);
			if (product != null)
				catSel.setAllSelected(true);
		}

		return catSel;
	}

	public List getFiles(Product product) throws RemoteException {
		List files = new Vector();
		if (product == null) {
			return null;
		}
		else {
			try {
				Collection coll = product.getICFile();
				//	List list = EntityFinder.getInstance().findRelated(product, ICFile.class);
				if (coll != null)
					files = new Vector(coll);
			}
			catch (IDORelationshipException ido) {
				ido.printStackTrace(System.err);
			}
		}
		if (files != null) {
			int imageId = product.getFileId();
			if (imageId != -1) {
				try {
					if (!files.contains(((com.idega.core.file.data.ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class)).findByPrimaryKey(new Integer(imageId)))) {
						files.add(0, ((com.idega.core.file.data.ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class)).findByPrimaryKey(new Integer(imageId)));
					}
				}
				catch (IDOLookupException e) {
					e.printStackTrace();
				}
				catch (FinderException e) {
					e.printStackTrace();
				}
			}
		}
		return files;
	}

	public void addImage(Product product, int imageId) throws RemoteException, FinderException {
		if (imageId != -1 && product != null) {
			try {
				ICFileHome fHome = (ICFileHome) IDOLookup.getHome(ICFile.class);
				product.addICFile(fHome.findByPrimaryKey(new Integer(imageId)));
				//	product.addTo(ICFile.class, imageId);
			}
			catch (IDOAddRelationshipException re) {
				//sql.printStackTrace(System.err);
			}
		}
	}

	public void removeImage(Product product, int imageId) throws RemoteException, FinderException {
		if (imageId != -1) {
			try {
				if (product.getFileId() == imageId) {
					product.setFileId(null);
					product.store();
				}
				else {
					ICFileHome fHome = (ICFileHome) IDOLookup.getHome(ICFile.class);
					product.removeICFile(fHome.findByPrimaryKey(new Integer(imageId)));
					//	  product.removeFrom(ICFile.class, imageId);
				}
			}
			catch (IDOException ido) {
				ido.printStackTrace(System.err);
			}
		}
	}

	public boolean deleteProduct(Product product) throws RemoteException {
		try {
			getProductBusiness().deleteProduct(product);
			return true;
		}
		catch (IDOException ido) {
			ido.printStackTrace(System.err);
			return false;
		}
	}

	public Image getImage(Object object) throws SQLException {
		if (object instanceof ICFile) {
			ICFile f = (ICFile) object;
			return new Image(((Integer) f.getPrimaryKey()).intValue());
		}
		else {
			return null;
		}
	}

	public DropdownMenu getCurrencyDropdown(String dropdownName, String defaultCurrency) {
		DropdownMenu _currencies = new DropdownMenu(dropdownName);
		List currencyList = CurrencyBusiness.getCurrencyList();
		if (currencyList != null) {
			Iterator iter = currencyList.iterator();
			while (iter.hasNext()) {
				CurrencyHolder holder = (CurrencyHolder) iter.next();
				_currencies.addMenuElement(holder.getCurrencyID(), holder.getCurrencyName());
			}
			if (defaultCurrency != null) {
				try {
					_currencies.setSelectedElement(Integer.toString(CurrencyBusiness.getCurrencyHolder(defaultCurrency).getCurrencyID()));
				}
				catch (Exception e) {
				}
			}
		}
		else {
			CurrencyHolder holder = CurrencyBusiness.getCurrencyHolder(defaultCurrency);
			if (holder != null)
				_currencies.addMenuElement(holder.getCurrencyID(), holder.getCurrencyName());
		}
		return _currencies;
	}

	private StockroomBusiness getStockroomBusiness() throws RemoteException {
		return (StockroomBusiness) IBOLookup.getServiceInstance(super.getIWApplicationContext(), StockroomBusiness.class);
	}

	private ProductBusiness getProductBusiness() throws RemoteException {
		return (ProductBusiness) IBOLookup.getServiceInstance(super.getIWApplicationContext(), ProductBusiness.class);
	}
}
