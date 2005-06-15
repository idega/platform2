/*
 * $Id: SupplierBrowser.java,v 1.6 2005/06/15 11:51:35 gimmi Exp $
 * Created on 19.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;


public class SupplierBrowser extends TravelBlock {

	private static final String ACTION = "sb_a";
	private static final String ACTION_VIEW_SUPPLIERS = "sb_a_vs";
	private static final String ACTION_VIEW_PRODUCTS = "sb_a_vp";
	private static final String PARAMETER_SUPPLIER_MANAGER = "sb_sm";
	public static final String PARAMETER_POSTAL_CODES = "sb_pc";
	private static final String PARAMETER_SUPPLIER_ID = "sb_sid";
	private String[][] postalCodes = null;
	private Group supplierManager = null;
	private int supplierManagerId = -1;
	
	private SupplierBrowserPlugin plugin = null;
	private String pluginClassName = null;
	
	private String textStyleClass = null;
	private String headerStyleClass = null;
	private String linkStyleClass = null;
	private String imageStyleClass = null;
	private String interfaceObjectStyleClass = null;
	private String width = null;
	private String imageWidth = "70";
	
	public SupplierBrowser() {
		
	}
	
	public static void main(String[] args) {
	}
	
	public void main(IWContext iwc) throws Exception {
		super.initializer(iwc);
		init(iwc);
		
		Form form = new Form();

		if (plugin == null) {
			form.add(getText(getResourceBundle().getLocalizedString("plugin_not_defined", "Plugin not defined")));
		} else if (supplierManager == null) {
			form.add(getText(getResourceBundle().getLocalizedString("supplier_manager_not_defined", "SupplierManager not defined")));
		} else {
			form.maintainParameter(PARAMETER_POSTAL_CODES);
			form.maintainParameter(PARAMETER_SUPPLIER_MANAGER);
			form.maintainParameter(PARAMETER_SUPPLIER_ID);
			form.maintainParameter(ACTION);
			String[] params = plugin.getParameters();
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					form.maintainParameter(params[i]);
				}
			}

			String action = iwc.getParameter(ACTION);
			if (action == null) {
				action = ACTION_VIEW_SUPPLIERS;
			}
			if (action.equals(ACTION_VIEW_SUPPLIERS)) {
				if (plugin.displaySupplierResults()) {
					listSuppliers(iwc, form);
				} else {
					listProducts(iwc, form);
				}
			} else if (action.equals(ACTION_VIEW_PRODUCTS)) {
				listProducts(iwc, form);
			}
		}
		
		add(form);
	}
	
	private void init(IWContext iwc) {
		// SupplierManager check
		String suppMan = iwc.getParameter(PARAMETER_SUPPLIER_MANAGER);
		if (supplierManagerId > 0) {
			suppMan = Integer.toString(supplierManagerId);
		}
		if (suppMan != null) {
			try {
				GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class, getSupplierHome().getDatasource());
				supplierManager = gHome.findByPrimaryKey(new Integer(suppMan));
			}
			catch (IDOLookupException e) {
				e.printStackTrace();
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}
		// Checking the postal codes
		String pcs = iwc.getParameter(PARAMETER_POSTAL_CODES);
		postalCodes = new String[2][0];
		if (pcs != null) {
			StringTokenizer tok = new StringTokenizer(pcs, ",");
			postalCodes = new String[2][tok.countTokens()];
			int i = 0;
			while (tok.hasMoreElements()) {
				String token = tok.nextToken().trim();
				StringTokenizer dashTok = new StringTokenizer(token, "-");
				String first = dashTok.nextToken().trim();
				String second = first;
				if (dashTok.hasMoreTokens()) {
					second = dashTok.nextToken().trim();
				}
				postalCodes[0][i] = first;
				postalCodes[1][i] = second;
				++i;
			}
			
		}
		
		if (pluginClassName != null) {
			try {
				plugin = (SupplierBrowserPlugin) Class.forName(pluginClassName).newInstance();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void listProducts(IWContext iwc, Form form) throws RemoteException {
		Table table = new Table();
		if (width != null) {
			table.setWidth(width);
		}
		table.setColumnWidth(1, imageWidth);
//		table.setBorder(1);
		int row = 1;

		Collection coll = getProducts(iwc);
		
		Collection[] inputs = plugin.getProductSearchInputs(iwc, super.getResourceBundle());
		Table searchTable = getSearchFrom(inputs);
		if (searchTable != null) {
//			searchTable.setBorder(1);
			form.add(searchTable);
		}
		
		int localeID = iwc.getCurrentLocaleId();
		if (coll != null) {
			Iterator iter = coll.iterator();
			Product product;
			Image image = null;
			while (iter.hasNext()) {
				product = (Product) iter.next();
				try {
					ICFile file = product.getFile();
					if (file != null) {
						image = new Image(Integer.parseInt(file.getPrimaryKey().toString()));
						if (imageStyleClass != null) {
							image.setStyleClass(imageStyleClass);
							image.setDatasource(product.getDatasource());
						}
					}
				} catch (SQLException sql) {
					sql.printStackTrace();
				}
				int startRow = row;
				if (image != null) {
					table.add(image, 1, row);
				}
				table.add(getText(product.getProductName(localeID), headerStyleClass), 2, row++);
				table.add(getText(product.getProductDescription(localeID)), 2, row);
				if (plugin.isProductSearchCompleted(iwc)) {
					table.add(getText("(Book)"), 3, startRow);
				}
				table.mergeCells(1, startRow, 1, row);
				++row;
			}
		} else {
			table.add(getText(getResourceBundle().getLocalizedString("no_products", "No products")), 1, row++);
		}
	
		
		form.add(table);
	}
	
	private void listSuppliers(IWContext iwc, Form form) throws RemoteException {
		Table table = new Table();
		if (width != null) {
			table.setWidth(width);
		}
		table.setColumnWidth(1, imageWidth);
		
//		table.setBorder(1);
		int row = 1;
		
		Collection coll = null;
		try {
			coll = getSuppliers(iwc);
		}
		catch (IDOCompositePrimaryKeyException e) {
			e.printStackTrace();
		}
		
		Collection[] inputs = plugin.getSupplierSearchInputs(iwc, super.getResourceBundle());
		Table searchF = getSearchFrom(inputs);
		if (searchF != null) {
			form.add(searchF);
		}
		
		if (coll != null) {
			
			Iterator iter = coll.iterator();
			Supplier supplier;
			ICFile file = null;
			Image image = null;
			while (iter.hasNext()) {
				supplier = (Supplier) iter.next();
				file = supplier.getICFile();
				if (file != null) {
					try {
						image = new Image(((Integer) file.getPrimaryKey()).intValue());
						image.setMaxImageWidth(Integer.parseInt(imageWidth));
						image.setDatasource(supplier.getDatasource());
						table.add(image, 1, row);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				table.add(getText(supplier.getName(), headerStyleClass), 2, row);
				table.add(getText(Text.BREAK), 2, row);
				table.add(getText(supplier.getDescription()), 2, row);
				table.add(getDetailLink(supplier, iwc), 3, row);
				
				table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
				table.setVerticalAlignment(2, row, Table.VERTICAL_ALIGN_TOP);
				table.setVerticalAlignment(3, row, Table.VERTICAL_ALIGN_TOP);
//				table.setVerticalAlignment(4, row, Table.VERTICAL_ALIGN_TOP);
				row++;
			}
			
		} else {
			table.add("No suppliers found");
		}
		
		form.add(table);
	}

	/**
	 * @param inputs
	 * @throws RemoteException
	 */
	private Table getSearchFrom(Collection[] inputs) throws RemoteException {
		if (inputs != null && inputs[0] != null && inputs[1] != null && !inputs[0].isEmpty() && !inputs[1].isEmpty()) {
			Table searchTable = new Table();
			searchTable.setWidth(width);
			int stRow = 1;
			Collection strings = inputs[0];
			Collection ios = inputs[1];
			int stringCount = strings.size();
			int iosCount = ios.size();
			if (stringCount == iosCount) {
				Iterator siter = strings.iterator();
				Iterator iiter = ios.iterator();
				PresentationObject io;
				for (int i = 0; i < iosCount; i++) {
					String s = (String) siter.next();
					io = (PresentationObject) iiter.next();
					if (interfaceObjectStyleClass != null) {
						io.setStyleClass(interfaceObjectStyleClass);
					}
					searchTable.add(getText(s), 1, stRow);
					searchTable.setAlignment(2, stRow, Table.HORIZONTAL_ALIGN_RIGHT);
					searchTable.add(io, 2, stRow++);
				}
				SubmitButton search = new SubmitButton(super.getResourceBundle().getLocalizedString("search", "Search"));
				searchTable.setAlignment(2, stRow, Table.HORIZONTAL_ALIGN_RIGHT);
				searchTable.add(search, 2, stRow++);
			} else {
				System.out.println("IO stuff error yes (SupplierBrowser)");
			}
			return searchTable;
		}
		return null;
	}
	
	private Link getDetailLink(Supplier supplier, IWContext iwc) throws RemoteException {
		Link link = new Link(getText(getResourceBundle().getLocalizedString("details", "Details"), linkStyleClass));
		link.addParameter(ACTION, ACTION_VIEW_PRODUCTS);
		link.maintainParameter(PARAMETER_POSTAL_CODES, iwc);
		link.maintainParameter(PARAMETER_SUPPLIER_MANAGER, iwc);
		link.addParameter(PARAMETER_SUPPLIER_ID, supplier.getPrimaryKey().toString());
		String[] params = plugin.getParameters();
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				link.maintainParameter(params[i], iwc);
			}
		}
		return link;
	}
	
	private Text getText(String content) {
		return getText(content, textStyleClass);
	}
	
	private Text getText(String content, String styleClass) {
		Text text = new Text(content);
		if (styleClass != null) {
			text.setStyleClass(styleClass);
		}
		return text;
	}
	
	
	
	// Move to a better location later... when possible
	private Collection getSuppliers(IWContext iwc) throws IDOCompositePrimaryKeyException {
		try {
			return getSupplierHome().findByPostalCodes(supplierManager, postalCodes[0], postalCodes[1], plugin.getSuppplierSearchCriterias(iwc));
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Collection getProducts(IWContext iwc) throws RemoteException {
		try {
			Supplier supplier = null;
			if (iwc.isParameterSet(PARAMETER_SUPPLIER_ID)) {
				supplier = getSupplierHome().findByPrimaryKey(Integer.parseInt(iwc.getParameter(PARAMETER_SUPPLIER_ID)));
			}
			return plugin.getProducts(supplier, supplierManager, iwc, postalCodes);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setSupplierManager(int id) {
		this.supplierManagerId = id;
	}
	
	public void setTextStyleClass(String styleClass) {
		this.textStyleClass = styleClass;
	}
	
	public void setHeaderStyleClass(String styleClass) {
		this.headerStyleClass = styleClass;
	}
		
	public void setLinkStyleClass(String styleClass) {
		this.linkStyleClass = styleClass;
	}
	
	public void setImageStyleClass(String styleClass) {
		this.imageStyleClass = styleClass;
	}
	
	public void setInterfaceObjectStyleClass(String styleClass) {
		this.interfaceObjectStyleClass = styleClass;
	}
	
	public void setPlugin(String pluginClassName) {
		this.pluginClassName = pluginClassName;
	}
	
	public void setWidth(String width) {
		this.width = width;
	}
	
	private SupplierHome getSupplierHome() {
		try {
			return (SupplierHome) IDOLookup.getHome(Supplier.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}
	
}
