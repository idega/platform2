/*
 * Created on 7.7.2003
 */
package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

import com.idega.block.category.business.CategoryService;
import com.idega.block.category.data.ICCategory;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.business.IBOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMetaDataConstants;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;

/**
 * @author laddi
 */
public class ProductItemMetaData extends ProductItem {

	private static final String METADATA = "metadata_";

	public ProductItemMetaData() {
	}
	
	public ProductItemMetaData(int productId) throws RemoteException, FinderException {
		super(productId);
	}
	
	public ProductItemMetaData(Product product) throws RemoteException {
		super(product);
	}

	/**
	 *  Description of the Method
	 *
	 *@param  iwc  Description of the Parameter
	 */
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		drawObject(iwc);
	}

	/**
	 *  Description of the Method
	 */
	private void drawObject(IWContext iwc) {
		try {
			List categories = getProductBusiness(iwc).getProductCategories(_product);

			Table table = new Table();
			table.setCellpaddingAndCellspacing(0);
			
			Table metaTable = new Table();
			metaTable.setColumns(3);
			metaTable.setCellpaddingAndCellspacing(0);
			metaTable.setWidth(2, 8);
			int row = 4;
			
			Table multiTable = null;
			
			Hashtable metaData = new Hashtable();
			Hashtable metaDataTypes = new Hashtable();

			Iterator iter = categories.iterator();
			while (iter.hasNext()) {
				ICCategory element = (ICCategory) iter.next();
				metaData.putAll(getCategoryService(iwc).getInheritedMetaData(element.getMetaDataAttributes(), element));
				metaDataTypes.putAll(getCategoryService(iwc).getInheritedMetaDataTypes(element.getMetaDataTypes(), element));
			}

			Iterator iterator = metaData.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = (String) metaData.get(key);
				String type = (String) metaDataTypes.get(key);
				
				String meta = null;
				if (type.equalsIgnoreCase(IWMetaDataConstants.METADATA_TYPE_STRING))
					meta = _product.getMetaData(METADATA + key + "_" + _productLocale.toString());
				else
					meta = _product.getMetaData(METADATA + key);
					
				if (meta != null) {
					if (type.equals(IWMetaDataConstants.METADATA_TYPE_MULTIVALUED)) {
						multiTable = new Table(1,2);
						multiTable.setCellpaddingAndCellspacing(0);
						
						StringTokenizer tokens = new StringTokenizer(meta, ",");
						while (tokens.hasMoreTokens()) {
							String token = tokens.nextToken();
							Image image = _iwb.getImage("shared/"+token+".gif");
							image.setAlt(_productIWRB.getLocalizedString(METADATA + "multi_" + token, token));
							multiTable.add(image, 1, 2);
						}
						multiTable.add(getHeaderText(_productIWRB.getLocalizedString(METADATA + key, key)), 1, 1);
					}
					else if (type.equals(IWMetaDataConstants.METADATA_TYPE_LINK)) {
						metaTable.add(getHeaderText(_productIWRB.getLocalizedString(METADATA + key, key) + ":"), 1, row);
						metaTable.add(getLink(meta, meta), 3, row++);
						metaTable.setHeight(row++, 1);
					}
					else {
						metaTable.add(getHeaderText(_productIWRB.getLocalizedString(METADATA + key, key) + ":"), 1, row);
						if (type.equals(IWMetaDataConstants.METADATA_TYPE_MULTIVALUED_SINGLE_SELECT))
							meta = _productIWRB.getLocalizedString(METADATA + "multi_" + meta, meta);
						else if (type.equals(IWMetaDataConstants.METADATA_TYPE_INTEGER) || type.equals(IWMetaDataConstants.METADATA_TYPE_FLOAT)) {
							if (value.length() > 0)
								meta = meta + " " + value;
						}
						metaTable.add(getText(meta), 3, row++);
						metaTable.setHeight(row++, 1);
					}
				}
			}
			
			if (multiTable != null) {
				table.add(multiTable, 1, 1);
				table.setHeight(2, 8);
				table.add(metaTable, 1, 3);
				add(table);
			}
			else
				add(metaTable);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
	}

	private CategoryService getCategoryService(IWApplicationContext iwac) throws RemoteException {
		return (CategoryService) IBOLookup.getServiceInstance(iwac, CategoryService.class);
	}
}