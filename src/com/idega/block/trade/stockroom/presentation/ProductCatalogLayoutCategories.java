package com.idega.block.trade.stockroom.presentation;
import java.util.Iterator;
import java.util.List;
import javax.ejb.FinderException;
import com.idega.block.category.business.CategoryService;
import com.idega.block.category.data.ICCategory;
import com.idega.block.category.data.ICCategoryTranslation;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */
public class ProductCatalogLayoutCategories extends AbstractProductCatalogLayout {
		
	CategoryService catServ;
	int localeID;
	
	public ProductCatalogLayoutCategories() {
	}
	
	public PresentationObject getCatalog(ProductCatalog productCatalog, IWContext iwc, List productCategories) {
		catServ = getCategoryService(iwc);
		localeID = iwc.getCurrentLocaleId();
		Table table = new Table();
		table.setWidth("100%");
		table.setCellpaddingAndCellspacing(0);
		Image spacer = Table.getTransparentCell(iwc);
		spacer.setWidth(5);
		int row = 1;
		int column = 1;
		Image spaceBetween = (Image) spacer.clone();
		spaceBetween.setHeight(productCatalog._spaceBetween);
		
		int level = 1;
		row = listCategories(productCatalog, productCategories.iterator(), table, spacer, row, column, spaceBetween, level);
		return table;
	}

	/**
	 * @param productCatalog
	 * @param productCategories
	 * @param table
	 * @param spacer
	 * @param row
	 * @param column
	 * @param spaceBetween
	 */
	private int listCategories(ProductCatalog productCatalog, Iterator productCategories, Table table, Image spacer, int row, int column, Image spaceBetween, int level) {
		ICCategory pCat;
		Link configCategory;
		String catName;
		
		while (productCategories != null && productCategories.hasNext()) {
		//for (int i = 0; i < productCategories.size(); i++) {
			try {
				if (productCatalog._spaceBetween > 0) {
					table.setCellpaddingBottom(1, row, productCatalog._spaceBetween);
					table.setCellpaddingTop(1, row, productCatalog._spaceBetween);
				}
				table.setCellpaddingLeft(1, row, productCatalog.getIndent(level));
				pCat = (ICCategory) productCategories.next();//get(i);
				try{
					ICCategoryTranslation trans = catServ.getCategoryTranslationHome().findByCategoryAndLocale(((Integer) pCat.getPrimaryKey()).intValue(),localeID);
					catName = trans.getName();
				}
				catch(FinderException ex){
					catName = pCat.getName();
				}
				if (productCatalog._iconImage != null) {
					Image iconImage = (Image) productCatalog._iconImage.clone();
					iconImage.setVerticalSpacing(productCatalog._iconSpacing);
					table.add(iconImage, column++, row);
					table.add(spacer, column++, row);
				}
				table.add(productCatalog.getCategoryLink(pCat, catName, level), column++, row);
				if (productCatalog._hasEditPermission) {
					configCategory = productCatalog.getProductCategoryEditorLink(pCat);
					table.add(configCategory, column, row);
				}
				String color = productCatalog.getColor(level);
				if (color != null) {
					table.setRowColor(row, color);
				}
				table.setRowVerticalAlignment(row++, Table.VERTICAL_ALIGN_TOP);
				column = 1;

				if (productCatalog.isCategoryExpanded(pCat)) {
					row = listCategories(productCatalog, pCat.getChildrenIterator(), table, spacer, row, column, spaceBetween, (level +1));
				}
			}
			catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
		return row;
	}
	
	public CategoryService getCategoryService(IWApplicationContext iwac) {
		try {
			return (CategoryService) IBOLookup.getServiceInstance(iwac, CategoryService.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}
