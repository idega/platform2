package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.category.business.CategoryService;
import com.idega.block.category.data.ICCategory;
import com.idega.block.category.data.ICCategoryHome;
import com.idega.block.image.presentation.ImageAttributeSetter;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.business.ProductEditorBusiness;
import com.idega.block.trade.stockroom.business.StockroomBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.business.IBOLookup;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMetaDataConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.texteditor.TextEditor;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.RadioGroup;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductEditorWindow extends IWAdminWindow {
	private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";
	public static final String imageAttributeKey = "productimage";
	public static final String PRODUCT_ID = "prod_edit_prod_id";
	public static final String PARAMETER_CATEGORY_ID = "p_cat_id";
	public static final String PRODUCT_CATALOG_OBJECT_INSTANCE_ID = "prod_edit_prod_cat_inst_id";
	//  public static final String PRODUCT_ID = ProductBusiness.PRODUCT_ID;

	private static final String ACTION = "prod_edit_action";
	private static final String PAR_ADD_FILE = "prod_edit_add_file";
	private static final String PAR_CATEGORY = "prod_edit_category";
	private static final String PAR_CLOSE = "prod_edit_close";
	private static final String PAR_CURRENCY = "prod_edit_currency";
	private static final String PAR_DELETE = "prod_edit_del";
	private static final String PAR_DEL_VERIFIED = "prod_edit_del_verified";
	private static final String PAR_DEL_FILE = "prod_edit_del_file";
	private static final String PAR_DESCRIPTION = "prod_edit_description";
	private static final String PAR_IMAGE = "prod_edit_image";
	private static final String PAR_IMAGE_THUMBNAIL = "prod_edit_image_thumb";
	private static final String PAR_NAME = "prod_edit_name";
	private static final String PAR_NEW = "prod_edit_new";
	private static final String PAR_NUMBER = "prod_edit_number";
	private static final String PAR_PRICE = "prod_edit_price";
	private static final String PAR_SAVE = "prod_edit_save";
	private static final String PAR_TEASER = "prod_edit_teaser";
	
	private static final String METADATA = "metadata_";

	private IWResourceBundle iwrb;
	private IWBundle bundle;
	private IWBundle core;
	private ProductEditorBusiness _business = ProductEditorBusiness.getInstance();
	private Collection categories = new Vector();

	private Product _product = null;
	private DropdownMenu _currencies = null;
	private int _productId = -1;
	private int iLocaleID = -1;
	private Locale _locale;
//	private int _catalogICObjectInstanceId = -1;

	public ProductEditorWindow() {
		setUnMerged();
		setWidth(600);
		setHeight(650);
		setResizable(true);
		setTitle("Product Editor");
		setScrollbar(true);
	}

	public void main(IWContext iwc) throws RemoteException, FinderException {
		init(iwc);

		String action = iwc.getParameter(ACTION);

		if (action == null || action.equals("")) {
			String delImg = iwc.getParameter(PAR_DEL_FILE);
			if (delImg != null && !delImg.equals("")) {
				try {
					_business.removeImage(_product, Integer.parseInt(delImg));
				}
				catch (NumberFormatException nfe) {
				}
			}

			displayForm(iwc);
		}
		else if (action.equals(this.PAR_NEW)) {
			_product = null;
			_productId = -1;
			displayForm(iwc);
		}
		else if (action.equals(this.PAR_SAVE)) {
			if (saveProduct(iwc)) {
				displayForm(iwc);
			}
			else {

			}
		}
		else if (action.equals(this.PAR_DELETE)) {
			verifyDelete(iwc);
		}
		else if (action.equals(this.PAR_DEL_VERIFIED)) {
			if (_business.deleteProduct(_product)) {
				closeWindow();
			}
		}
		else if (action.equals(this.PAR_CLOSE)) {
			closeWindow();
		}
		else if (action.equals(this.PAR_ADD_FILE)) {
			String imageId = iwc.getParameter(PAR_IMAGE);
			if (imageId != null) {
				_business.addImage(_product, Integer.parseInt(imageId));
			}
			saveProduct(iwc);
			displayForm(iwc);
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	private void init(IWContext iwc) throws RemoteException {
		bundle = getBundle(iwc);
		iwrb = bundle.getResourceBundle(iwc);
		core = iwc.getIWMainApplication().getCoreBundle();

		addTitle(iwrb.getLocalizedString("product_editor", "Product Editor"));
		try {
			String sProductId = iwc.getParameter(this.PRODUCT_ID);
			_productId = Integer.parseInt(sProductId);
			if (_productId != -1) {
				_product = getProductBusiness(iwc).getProduct(_productId);
			}
		}
		catch (Exception e) {
			//e.printStackTrace(System.err);
			_productId = -1;
			_product = null;
		}
/*
		String pCatObjId = iwc.getParameter(this.PRODUCT_CATALOG_OBJECT_INSTANCE_ID);
		if (pCatObjId != null) {
			try {
				this._catalogICObjectInstanceId = Integer.parseInt(pCatObjId);
			}
			catch (NumberFormatException n) {
				n.printStackTrace(System.err);
			}
		}
*/
		setCurrencies(iwc);
		String currCurr = getBundle(iwc).getProperty("iw_default_currency", "USD");
		_currencies = _business.getCurrencyDropdown(this.PAR_CURRENCY, currCurr);
		
		String[] catIds = iwc.getParameterValues(PARAMETER_CATEGORY_ID);
		if (_product != null) {
			try {
				categories = getProductBusiness(iwc).getProductCategories(_product);
			}
			catch (IDORelationshipException e1) {
				e1.printStackTrace();
			}
		} else if (catIds != null) {
			ICCategoryHome catHome = (ICCategoryHome) IDOLookup.getHome(ICCategory.class);
			ICCategory cat;
			for (int i = 0; i < catIds.length; i++) {
				try {
					cat = catHome.findByPrimaryKey(new Integer(catIds[i]));
					categories.add(cat);
				}
				catch (NumberFormatException e1) {
					e1.printStackTrace();
				}
				catch (FinderException e1) {
					e1.printStackTrace();
				}
			}
		} 
	}

	private void setCurrencies(IWContext iwc) throws RemoteException {
		String sLocaleId = iwc.getParameter(getProductBusiness(iwc).getParameterLocaleDrop());
		if (sLocaleId != null) {
			_locale = ICLocaleBusiness.getLocale(Integer.parseInt(sLocaleId));;
		}
		else {
			_locale = iwc.getCurrentLocale();
		}
		iLocaleID = ICLocaleBusiness.getLocaleId(_locale);
	}

	public static Link getEditorLink(int productId) {
		Link link = new Link();
		link.setWindowToOpen(ProductEditorWindow.class);
		link.addParameter(ProductEditor.PRODUCT_ID, productId);
		return link;
	}

	private void displayForm(IWContext iwc) throws RemoteException {
		simpleForm(iwc);

	}

	private void simpleForm(IWContext iwc) throws RemoteException {
		TextInput number = new TextInput(PAR_NUMBER);
		TextInput name = new TextInput(PAR_NAME);
		//TextArea description = new TextArea(PAR_DESCRIPTION);
		TextEditor description = new TextEditor();
		description.setInputName(PAR_DESCRIPTION);

		TextArea teaser = new TextArea(PAR_TEASER);
		teaser.setWidth(Table.HUNDRED_PERCENT);
		teaser.setRows(3);
		TextInput price = new TextInput(PAR_PRICE);
		name.setSize(67);
		//description.setWidth(70);
		//description.setHeight(15);
		teaser.setWidth("70");
		teaser.setHeight("4");

		if (_product != null) {
			if (_product.getNumber() != null)
				number.setContent(_product.getNumber());
			name.setContent(_product.getProductName(iLocaleID));
			description.setContent(_product.getProductDescription(iLocaleID));
			teaser.setContent(_product.getProductTeaser(iLocaleID));
			ProductPrice pPrice = getStockroomBusiness(iwc).getPrice(_product);
			if (pPrice != null) {
				price.setContent(Integer.toString((int) pPrice.getPrice()));
				_currencies.setSelectedElement(Integer.toString(pPrice.getCurrencyId()));
			}
			else {
				price.setContent("0");
			}
		}

		DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(getProductBusiness(iwc).getParameterLocaleDrop());
		localeDrop.setToSubmit();
		localeDrop.setSelectedElement(Integer.toString(iLocaleID));
		super.addLeft(iwrb.getLocalizedString("locale", "Locale") + ": ", localeDrop, false);

		super.addHiddenInput(new HiddenInput(this.PRODUCT_ID, Integer.toString(_productId)));

		//    super.addLeft(iwrb.getLocalizedString("item_number","Item number"), number, true);

		Table topTable = new Table(5, 2);
		topTable.setCellpaddingAndCellspacing(0);
		super.setStyle(price);
		super.setStyle(_currencies);
		super.setStyle(number);

		topTable.add(super.formatText(iwrb.getLocalizedString("item_number", "Item number")), 1, 1);
		topTable.add(super.formatText(Text.NON_BREAKING_SPACE), 2, 1);
		topTable.add(number, 1, 2);
		topTable.add(super.formatText(iwrb.getLocalizedString("price", "Price")), 3, 1);
		topTable.add(price, 3, 2);
		topTable.add(super.formatText(Text.NON_BREAKING_SPACE), 4, 1);
		topTable.add(super.formatText(iwrb.getLocalizedString("currency", "Currency")), 5, 1);
		topTable.add(_currencies, 5, 2);

		super.addLeft(topTable, false);

		super.addLeft(iwrb.getLocalizedString("name", "Name"), name, true);
		
		//Adding metadata inputs
		if (!categories.isEmpty()) {
			boolean addTable = false;
			Table metaTable = new Table();
			metaTable.setColumns(3);
			metaTable.setCellpaddingAndCellspacing(0);
			metaTable.setWidth(2, 8);
			int row = 1;
			
			Hashtable metaData = new Hashtable();
			Hashtable metaDataTypes = new Hashtable();

			Iterator iter = categories.iterator();
			while (iter.hasNext()) {
				ICCategory element = (ICCategory) iter.next();
				metaData.putAll(getCategoryService(iwc).getInheritedMetaData(element.getMetaDataAttributes(), element));
				metaDataTypes.putAll(getCategoryService(iwc).getInheritedMetaDataTypes(element.getMetaDataTypes(), element));
				super.addHiddenInput(new HiddenInput(PARAMETER_CATEGORY_ID, element.getPrimaryKey().toString()));
			}

			Iterator iterator = metaData.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = (String) metaData.get(key);
				String type = (String) metaDataTypes.get(key);
				
				if (type.equals(IWMetaDataConstants.METADATA_TYPE_MULTIVALUED))
					metaTable.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
				metaTable.add(formatText(iwrb.getLocalizedString(METADATA + key, key)), 1, row);
				metaTable.add(getMetaDataObject(key, value, type, _product, _locale), 3, row++);
				metaTable.setHeight(row++, 6);
				addTable = true;
			}
			
			if (addTable)
				addLeft(metaTable, false);
		}
		//Done adding metadata inputs
		
		super.addLeft(iwrb.getLocalizedString("teaser", "Teaser"), teaser, true);
		super.addLeft(iwrb.getLocalizedString("description", "Description"), description, true);

		Table imageTable = getImageTable(iwc);
		super.addRight(iwrb.getLocalizedString("images", "Images"), imageTable, true, false);

		SubmitButton saveBtn = new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), this.ACTION, this.PAR_SAVE);
		SubmitButton deleteBtn = new SubmitButton(iwrb.getLocalizedImageButton("delete", "Delete"), this.ACTION, this.PAR_DELETE);
		SubmitButton closeBtn = new SubmitButton(iwrb.getLocalizedImageButton("close", "Close"), this.ACTION, this.PAR_CLOSE);
		SubmitButton newBtn = new SubmitButton(iwrb.getLocalizedImageButton("create_new", "Create new"), this.ACTION, this.PAR_NEW);

		
		SelectionBox catSel = new SelectionBox(PAR_CATEGORY);
		if (!categories.isEmpty()) {
			catSel.addMenuElements(categories);
			catSel.setAllSelected(true);
		}
		super.addLeft(iwrb.getLocalizedString("product_category", "Product category"), catSel, true);

/*		//if (this._catalogICObjectInstanceId != -1) {
			super.addHiddenInput(new HiddenInput(this.PRODUCT_CATALOG_OBJECT_INSTANCE_ID, Integer.toString(_catalogICObjectInstanceId)));
			SelectionBox catSel = _business.getCategorySelectionBox(_product, PAR_CATEGORY, _catalogICObjectInstanceId);
			super.addLeft(iwrb.getLocalizedString("product_category", "Product category"), catSel, true);
		}
		else {
			SelectionBox catSel = _business.getSelectionBox(_product, PAR_CATEGORY, _catalogICObjectInstanceId);
			super.addLeft(iwrb.getLocalizedString("product_category", "Product category"), catSel, true);
		}
*/
		Table table = new Table();
		table.add(newBtn);
		table.setAlignment(1, 1, "right");
		table.setWidth("100%");

		super.addLeft("", table, false, false);

		super.addSubmitButton(closeBtn);
		super.addSubmitButton(deleteBtn);
		super.addSubmitButton(saveBtn);
	}

	private boolean saveProduct(IWContext iwc) {
		String number = iwc.getParameter(PAR_NUMBER);
		String name = iwc.getParameter(PAR_NAME);
		String description = iwc.getParameter(PAR_DESCRIPTION);
		String teaser = iwc.getParameter(PAR_TEASER);
		String price = iwc.getParameter(PAR_PRICE);
		String currency = iwc.getParameter(PAR_CURRENCY);
		String thumbnailId = iwc.getParameter(PAR_IMAGE_THUMBNAIL);
		if (thumbnailId == null)
			thumbnailId = "-1";
		String[] categories = (String[]) iwc.getParameterValues(PAR_CATEGORY);

		boolean returner = false;

		if (!name.equals("")) {
			if (_product == null) {
				try {
					_productId = getProductBusiness(iwc).createProduct(null, name, number, description, true, iLocaleID);
					_product = getProductBusiness(iwc).getProduct(_productId);
					_business.setCategories(_product, categories);
					_product.setProductTeaser(iLocaleID, teaser);
					if (_business.setPrice(_product, price, currency)) {
					}
					else {
						System.out.println(iwrb.getLocalizedString("price_not_saved", "Price was not saved"));
					}
					returner = true;
				}
				catch (Exception e) {
					returner = false;
					e.printStackTrace(System.err);
				}
			}
			else {
				try {
					_product = getProductBusiness(iwc).getProduct(getProductBusiness(iwc).updateProduct(this._productId, null, name, number, description, true, iLocaleID));

					_business.setThumbnail(_product, Integer.parseInt(thumbnailId));
					_business.setCategories(_product, categories);
					_product.setProductTeaser(iLocaleID, teaser);
					
					saveMetaData(iwc, _product);
					
					if (_business.setPrice(_product, price, currency)) {
					}
					else {
						System.out.println(iwrb.getLocalizedString("price_not_saved", "Price was not saved"));
					}
					returner = true;
				}
				catch (Exception e) {
					returner = false;
					e.printStackTrace(System.err);
				}
			}
		}

		return returner;
	}
	
	private void saveMetaData(IWContext iwc, Product product) {
		try {
			Hashtable metaDataTypes = new Hashtable();

			List categories = getProductBusiness(iwc).getProductCategories(product);
			Iterator iter = categories.iterator();
			while (iter.hasNext()) {
				ICCategory element = (ICCategory) iter.next();
				metaDataTypes.putAll(getCategoryService(iwc).getInheritedMetaDataTypes(element.getMetaDataTypes(), element));
			}
			
			Iterator iterator = metaDataTypes.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String type = (String) metaDataTypes.get(key);
				
				if (iwc.isParameterSet(METADATA + key) && iwc.getParameter(METADATA + key).length() > 0) {
					if (type.equals(IWMetaDataConstants.METADATA_TYPE_MULTIVALUED)) {
						String[] values = iwc.getParameterValues(METADATA + key);
						if (values != null && values.length > 0) {
							StringBuffer buffer = new StringBuffer();
							for (int i = 0; i < values.length; i++) {
								buffer.append(values[i]);
								if ((i+1) < values.length)
									buffer.append(",");
							}
							product.setMetaData(METADATA + key, buffer.toString(), type);
						}
					}
					else {
						if (type.equals(IWMetaDataConstants.METADATA_TYPE_STRING))
							product.setMetaData(METADATA + key + "_" + _locale.toString(), iwc.getParameter(METADATA + key), type);
						else
							product.setMetaData(METADATA + key, iwc.getParameter(METADATA + key), type);
					}
				}
				else {
					if (type.equals(IWMetaDataConstants.METADATA_TYPE_STRING))
						product.removeMetaData(METADATA + key + "_" + _locale.toString());
					else
						product.removeMetaData(METADATA + key);
				}
			}
			product.store();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
	}

	private void saveFailed(IWContext iwc) {
		super.addLeft(iwrb.getLocalizedString("save_failed", "Save failed"), "");

		BackButton back = new BackButton(iwrb.getLocalizedImageButton("back", "Back"));

		super.addSubmitButton(back);
	}

	private void verifyDelete(IWContext iwc) throws RemoteException {
		super.addHiddenInput(new HiddenInput(this.PRODUCT_ID, Integer.toString(_productId)));
		StringBuffer text = new StringBuffer();
		text.append(iwrb.getLocalizedString("are_you_sure_you_want_to_delete", "Are you sure you want to delete this product")).append(" : ").append(_product.getProductName(iLocaleID));
		super.addLeft(iwrb.getLocalizedString("delete", "Delete"), text.toString());

		SubmitButton ok = new SubmitButton(iwrb.getLocalizedImageButton("ok", "OK"), this.ACTION, this.PAR_DEL_VERIFIED);
		SubmitButton cancel = new SubmitButton(iwrb.getLocalizedImageButton("cancel", "Cancel"), this.ACTION, "");

		super.addSubmitButton(ok);
		super.addSubmitButton(cancel);
	}

	private void closeWindow() {
		this.setParentToReload();
		this.close();
	}

	private Table getImageTable(IWContext iwc) throws RemoteException {
		ImageInserter imageInserter = new ImageInserter(PAR_IMAGE);
		imageInserter.setHasUseBox(false);
		SubmitButton addButton = new SubmitButton(core.getImage("/shared/create.gif", "Add to news"), ACTION, PAR_ADD_FILE);

		Table imageTable = new Table();
		int imgRow = 1;
		imageTable.mergeCells(1, imgRow, 4, imgRow);
		imageTable.add(imageInserter, 1, imgRow++);
		imageTable.mergeCells(1, imgRow, 4, imgRow);
		imageTable.add(addButton, 1, imgRow++);

		List files = _business.getFiles(_product);

		if (files != null && files.size() > 0) {
			RadioButton radio;
			int imageId = _product.getFileId();

			Iterator I = files.iterator();

			++imgRow;
			imageTable.add(formatText(iwrb.getLocalizedString("thumbnail", "Thumb")), 1, imgRow);
			imageTable.add(formatText(iwrb.getLocalizedString("image", "Image")), 2, imgRow);
			++imgRow;

			while (I.hasNext()) {
				try {
					Image immi = _business.getImage(I.next());
					int immiId = immi.getImageID(iwc);
					immi.setMaxImageWidth(50);

					Link edit = ImageAttributeSetter.getLink(core.getImage("/shared/edit.gif"), immiId, imageAttributeKey);
					SubmitButton delete = new SubmitButton(core.getImage("/shared/delete.gif"), PAR_DEL_FILE, Integer.toString(immiId));
					radio = new RadioButton(PAR_IMAGE_THUMBNAIL, Integer.toString(immiId));
					if (imageId == immiId) {
						radio.setSelected();
					}

					imageTable.add(radio, 1, imgRow);
					imageTable.add(immi, 2, imgRow);
					imageTable.add(edit, 3, imgRow);
					imageTable.add(delete, 4, imgRow);
					imgRow++;
				}
				catch (Exception ex) {
					ex.printStackTrace(System.err);
				}
			}
			radio = new RadioButton(PAR_IMAGE_THUMBNAIL, "-1");
			if (imageId == -1) {
				radio.setSelected();
			}
			imageTable.add(radio, 1, imgRow);
			imageTable.add(formatText(iwrb.getLocalizedString("no_thumbnail", "No thumbnail")), 2, imgRow);
			imageTable.mergeCells(2, imgRow, 4, imgRow);
			++imgRow;

		}

		return imageTable;
	}
	
	private PresentationObject getMetaDataObject(String key, String value, String type, Product product, Locale locale) {
		if (type.equalsIgnoreCase(IWMetaDataConstants.METADATA_TYPE_MULTIVALUED)) {
			SelectionBox box = new SelectionBox(METADATA + key);
			box.setHeight(4);
			setStyle(box);
			StringTokenizer token = new StringTokenizer(value, ",");
			while (token.hasMoreTokens()) {
				String string = token.nextToken();
				box.addMenuElement(string, iwrb.getLocalizedString(METADATA + "multi_" + string, string));
			}
			
			if (product != null) {
				String selected = product.getMetaData(METADATA + key);
				if (selected != null) {
					token = new StringTokenizer(selected, ",");
					while (token.hasMoreTokens()) {
						String string = token.nextToken();
						box.setSelectedElement(string);
					}
				}
			}
			
			return box;
		}
		else if (type.equalsIgnoreCase(IWMetaDataConstants.METADATA_TYPE_MULTIVALUED_SINGLE_SELECT)) {
			RadioGroup group = new RadioGroup(METADATA + key);
			group.setVertical(false);
			StringTokenizer token = new StringTokenizer(value, ",");
			while (token.hasMoreTokens()) {
				String string = token.nextToken();
				group.addRadioButton(string, formatText(iwrb.getLocalizedString(METADATA + "multi_" + string, string)), false);
				if (product!= null && product.getMetaData(METADATA + key) != null && product.getMetaData(METADATA + key).equals(string))
					group.setSelected(string);
			}
			return group;
		}
		else {
			TextInput textInput = new TextInput(METADATA + key);
			setStyle(textInput);
			if (type.equalsIgnoreCase(IWMetaDataConstants.METADATA_TYPE_STRING)) {
				if (product != null && product.getMetaData(METADATA + key + "_" + locale.toString()) != null)
					textInput.setContent(product.getMetaData(METADATA + key + "_" + locale.toString()));
			}
			else {
				if (product != null && product.getMetaData(METADATA + key) != null)
					textInput.setContent(product.getMetaData(METADATA + key));
			}
			return textInput;
		}
	}

	private StockroomBusiness getStockroomBusiness(IWApplicationContext iwac) throws RemoteException {
		return (StockroomBusiness) IBOLookup.getServiceInstance(iwac, StockroomBusiness.class);
	}

	private ProductBusiness getProductBusiness(IWApplicationContext iwac) throws RemoteException {
		return (ProductBusiness) IBOLookup.getServiceInstance(iwac, ProductBusiness.class);
	}

	private CategoryService getCategoryService(IWApplicationContext iwac) throws RemoteException {
		return (CategoryService) IBOLookup.getServiceInstance(iwac, CategoryService.class);
	}
}