package com.idega.block.trade.stockroom.presentation;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import com.idega.block.category.presentation.CategoryWindow;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductCategory;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.business.IBOLookup;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.LocalePresentationUtil;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SelectionDoubleBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.transaction.IdegaTransactionManager;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */
public class ProductCategoryEditor extends CategoryWindow {
	private IWBundle bundle = null;
	private IWResourceBundle iwrb = null;
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";
	public static final String SELECTED_CATEGORY = "pr_cat_seleted_cat";
	private static final String _action = "pr_cat_editor_action";
	private static final String _parameterSaveCategory = "pr_cat_view_cat";
	private static final String _parameterClose = "pr_cat_close";
	private static final String _parameterProductIn = "pr_cat_pr_in";
	private static final String _parameterProductOut = "pr_cat_pr_out";
	private static final String _parameterProductFilter = "pr_cat_filt";
	private static final String _parameterProductFilterUnusedOnly = "pr_cat_filt_unsd";
	private static final String _parameterProductFilterAllProducts = "pr_cat_filt_all";
	private static final String _parameterLocale = "pr_cat_loc";
	private static final String _parameterSelectedCategory = SELECTED_CATEGORY;
	private int maxWidth = 50;
	private int height = 10;
	int _selectedCategory = -1;
	ProductCategory _productCategory = null;
	List _categories = null;
	private int localeId = 1;
	public ProductCategoryEditor() {
		super.setWidth(600);
		super.setHeight(460);
		super.setUnMerged();
	}
	public void main(IWContext iwc) throws Exception {
		init(iwc);
		if (_selectedCategory != -1) {
			String action = iwc.getParameter(_action);
			if (action == null || "".equals(action)) {
				viewCategory(iwc);
			}
			else if (action.equals(_parameterClose)) {
				super.setParentToReload();
				super.close();
			}
			else {
				saveAssignment(iwc);
			}
		}
	}
	private void init(IWContext iwc) {
		bundle = getBundle(iwc);
		iwrb = bundle.getResourceBundle(iwc);
		localeId = iwc.getCurrentLocaleId();
		try {
			String sSelCat = iwc.getParameter(SELECTED_CATEGORY);
			if (sSelCat != null) {
				_selectedCategory = Integer.parseInt(sSelCat);
				_productCategory =
					(
						(com.idega.block.trade.stockroom.data.ProductCategoryHome) com.idega.data.IDOLookup.getHomeLegacy(
							ProductCategory.class)).findByPrimaryKeyLegacy(
						_selectedCategory);
			}
			_categories = getProductBusiness(iwc).getProductCategories();
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			_categories = new Vector();
		}
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	private void viewCategory(IWContext iwc) throws RemoteException {
		try {
			String sFilter = iwc.getParameter(this._parameterProductFilter);
			String sLocale = iwc.getParameter(this._parameterLocale);
			int iFilter = -1;
			int iLocale = -1;
			if (sFilter != null)
				iFilter = Integer.parseInt(sFilter);
			if (sLocale != null) {
				if (sLocale.equals("-1")) {
					iLocale = -1;
				}
				else {
					iLocale = ICLocaleBusiness.getLocaleId(ICLocaleBusiness.getLocaleFromLocaleString(sLocale));
				}
			} else {
			//if (iLocale == -1)
				iLocale = iwc.getCurrentLocaleId();
				sLocale = ICLocaleBusiness.getLocale(iLocale).toString();
			}

			List products = getProductBusiness(iwc).getProducts(_productCategory);
			Collection allProducts = ((ProductHome) IDOLookup.getHome(Product.class)).findProducts(-1, -1, null, null, null, iLocale, iFilter);

			allProducts.removeAll(products);
			SelectionDoubleBox sdb = new SelectionDoubleBox(this._parameterProductOut, this._parameterProductIn);
			/** @todo Sortera productin */
			Product product;
			Iterator iter = allProducts.iterator();
			while (iter.hasNext()) {
				product = (Product) iter.next();
				//        product = (Product) iter.next();
				sdb.getLeftBox().addMenuElement(((Integer) product.getPrimaryKey()).intValue(), product.getProductName(localeId));
			}
			iter = products.iterator();
			while (iter.hasNext()) {
				//        product = getProductHome().findByPrimaryKey(iter.next());
				product = (Product) iter.next();
				sdb.getRightBox().addMenuElement(((Integer) product.getPrimaryKey()).intValue(), product.getProductName(localeId));
			}
			sdb.getRightBox().selectAllOnSubmit();
			sdb.getLeftBox().setHeight(height);
			sdb.getRightBox().setHeight(height);
			sdb.getLeftBox().setWidth("130");
			sdb.getRightBox().setWidth("130");
			//sdb.getLeftBox().setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE + "width:130px");
			//sdb.getRightBox().setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE + "width:130px");
			SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), this._action, this._parameterSaveCategory);
			SubmitButton close = new SubmitButton(iwrb.getLocalizedImageButton("close", "Close"), this._action, this._parameterClose);
			DropdownMenu filter = new DropdownMenu(this._parameterProductFilter);
			filter.addMenuElement(-1, iwrb.getLocalizedString("trade.all_products", "All products"));
			filter.addMenuElement(
				getProductHome().getProductFilterNotConnectedToAnyProductCategory(),
				iwrb.getLocalizedString("trade.unused_products_only", "Unused products only"));
			if (sFilter != null) {
				filter.setSelectedElement(sFilter);
			}
			DropdownMenu loc = LocalePresentationUtil.getAvailableLocalesDropdown(iwc.getIWMainApplication(), _parameterLocale);
			loc.addMenuElementFirst("-1", iwrb.getLocalizedString("all_locales", "All locales"));
			loc.setToSubmit();
			loc.setSelectedElement(sLocale);
			//      super.addRight(iwrb.getLocalizedString("trade.product_filter","Product filter"), filter, false, true);
			super.addRight(iwrb.getLocalizedString("only_show_products_localized_in", "Only show products localized in")+" :", loc, true, true);
			this.addHiddenInput(new HiddenInput(this.SELECTED_CATEGORY, Integer.toString(this._selectedCategory)));
			super.maintainClearCacheKeyInForm(iwc);
			sdb.addToScripts(this.getParentPage().getAssociatedScript());
			GenericButton add = sdb.getRightButton();
			add.setOnClick("move( this.form." + sdb.getLeftBox().getName() + ", this.form." + sdb.getRightBox().getName() + " )");
			GenericButton remove = sdb.getLeftButton();
			remove.setOnClick("move( this.form." + sdb.getRightBox().getName() + ", this.form." + sdb.getLeftBox().getName() + " )");
			super.addLeft(iwrb.getLocalizedString("available_products", "Available products"), sdb.getLeftBox(), true);
			super.addLeft(iwrb.getLocalizedString("add_selected", "Add selected") + Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE, add, false);
			super.addLeft(
				iwrb.getLocalizedString("remove_selected", "Remove selected") + Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE,
				remove,
				false);
			super.addLeft(iwrb.getLocalizedString("selected_products", "Selected products"), sdb.getRightBox(), true);
			super.addSubmitButton(close);
			super.addSubmitButton(save);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	private void saveAssignment(IWContext iwc) throws RemoteException {
		String[] in = iwc.getParameterValues(this._parameterProductIn);
		TransactionManager tm = IdegaTransactionManager.getInstance();
		try {
			tm.begin();
			List products = EntityFinder.getInstance().findRelated(_productCategory, Product.class);
			_productCategory.removeProducts(products);
			if (in != null) {
				for (int i = 0; i < in.length; i++) {
					_productCategory.addTo(Product.class, Integer.parseInt(in[i]));
				}
			}
			tm.commit();
			iwc.getIWMainApplication().getIWCacheManager().invalidateCache(ProductCatalog.CACHE_KEY);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			try {
				tm.rollback();
			}
			catch (SystemException se) {
				se.printStackTrace(System.err);
			}
		}
		super.clearCache(iwc);
		viewCategory(iwc);
	}
	private Text getText(String content) {
		Text text = new Text(content);
		return text;
	}
	private ProductBusiness getProductBusiness(IWApplicationContext iwac) throws RemoteException {
		return (ProductBusiness) IBOLookup.getServiceInstance(iwac, ProductBusiness.class);
	}
	private ProductHome getProductHome() throws RemoteException {
		return (ProductHome) IDOLookup.getHome(Product.class);
	}
}
