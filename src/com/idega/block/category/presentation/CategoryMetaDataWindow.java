/*
 * Created on 6.7.2003
 */
package com.idega.block.category.presentation;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.core.category.business.CategoryService;
import com.idega.core.category.data.ICCategory;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMetaDataConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author laddi
 */
public class CategoryMetaDataWindow extends IWAdminWindow {

	public static final String PARAMETER_CATEGORY_ID = "ic_category_id";
	public static final String PARAMETER_SAVE = "ic_category_save";
	public static final String PARAMETER_DELETE = "ic_category_delete";
	public static final String PARAMETER_NAME = "ic_category_name";
	public static final String PARAMETER_VALUE = "ic_category_value";
	public static final String PARAMETER_TYPE = "ic_category_type";
	public static final String PARAMETER_METADATA = "ic_metadata";
	
	protected IWResourceBundle iwrb;
	
	private ICCategory _category = null;
	private String _metaData = null;

	public CategoryMetaDataWindow() {
		setWidth(650);
		setHeight(400);
		setResizable(true);
		setScrollbar(true);
	}

	public void main(IWContext iwc) throws Exception {
		parseAction(iwc);
		
		iwrb = getResourceBundle(iwc);
		
		String title = iwrb.getLocalizedString("ic_category_metadata_editor", "Category Metadata Editor");
		setTitle(title);
		addTitle(title);
		
		if (_category != null)
			control(iwc);
	}
	
	private void control(IWContext iwc) throws RemoteException {
		Map superMetaData = getCategoryService(iwc).getInheritedMetaData(_category);
		Map superMetaDataTypes = getCategoryService(iwc).getInheritedMetaDataTypes(_category);
		Map metaData = _category.getMetaDataAttributes();
		Map metaDataTypes = _category.getMetaDataTypes();
		
		Table padder = new Table(1, 1);
		padder.setCellpadding(0);
		padder.setCellspacing(8);
		padder.setWidth(Table.HUNDRED_PERCENT);
		
		Form form = new Form();
		form.maintainParameter(PARAMETER_CATEGORY_ID);
		padder.add(form);
		
		Table table = new Table();
		table.setColumns(4);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setWidth(3, Table.HUNDRED_PERCENT);
		form.add(table);
		int column = 2;
		int row = 1;
		
		table.add(formatText(iwrb.getLocalizedString("name", "Name"), true), column++, row);
		table.add(formatText(iwrb.getLocalizedString("value", "Value"), true), column++, row);
		table.add(formatText(iwrb.getLocalizedString("type", "Type"), true), column++, row++);
		
		CheckBox deleteMetadata;

		TextInput metaDataKey = new TextInput(PARAMETER_NAME);
		setStyle(metaDataKey);

		TextInput metaDataValue = new TextInput(PARAMETER_VALUE);
		setStyle(metaDataValue);
		metaDataValue.setWidth(Table.HUNDRED_PERCENT);

		DropdownMenu metaDataType = getTypesMenu();
		setStyle(metaDataType);
		
		if (!superMetaData.isEmpty()) {
			deleteMetadata = new CheckBox("none");
			deleteMetadata.setDisabled(true);

			Iterator iter = superMetaData.keySet().iterator();
			while (iter.hasNext()) {
				column = 1;
				String key = (String) iter.next();
				String value = (String) superMetaData.get(key);
				
				table.add(deleteMetadata, column++, row);
				table.add(formatText(key, false), column++, row);
				table.add(formatText(value, false), column++, row);
				if (superMetaDataTypes.containsKey(key))
					table.add(formatText((String) superMetaDataTypes.get(key), false), column, row);
				row++;
			}
		}

		if (!metaData.isEmpty()) {
			Iterator iter = metaData.keySet().iterator();
			while (iter.hasNext()) {
				column = 1;
				String key = (String) iter.next();
				String value = (String) metaData.get(key);
				deleteMetadata = new CheckBox(PARAMETER_DELETE, key);
				
				if (_metaData != null && _metaData.equals(key)) {
					column++;
					metaDataKey.setContent(key);
					metaDataValue.setContent(value);
					if (metaDataTypes.containsKey(key))
						metaDataType.setSelectedElement((String) metaDataTypes.get(key));

					table.add(metaDataKey, column++, row);
					table.add(metaDataValue, column++, row);
					table.add(metaDataType, column, row++);
				}
				else {
					Link link = new Link(formatText(key));
					link.addParameter(PARAMETER_CATEGORY_ID, _category.getPrimaryKey().toString());
					link.addParameter(PARAMETER_METADATA, key);
					
					table.add(deleteMetadata, column++, row);
					table.add(link, column++, row);
					table.add(formatText(value), column++, row);
					if (metaDataTypes.containsKey(key))
						table.add(formatText((String) metaDataTypes.get(key)), column, row);
				}
				row++;
			}
		}
		
		if (_metaData == null) {
			column = 2;
			table.add(metaDataKey, column++, row);
			table.add(metaDataValue, column++, row);
			table.add(metaDataType, column, row++);
		}
		
		row++;
		table.mergeCells(1, row, table.getColumns(), row);
		
		SubmitButton save = new SubmitButton(iwrb.getLocalizedString("save", "Save"), PARAMETER_SAVE, "true");
		save.setAsImageButton(true);
		CloseButton close = new CloseButton(iwrb.getLocalizedString("close", "Close"));
		close.setAsImageButton(true);
		
		table.add(save, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(close, 1, row);
		
		add(padder);
	}
	
	private DropdownMenu getTypesMenu() {
		DropdownMenu menu = new DropdownMenu(PARAMETER_TYPE);
		
		String[] types = IWMetaDataConstants.getMetaDataTypes();
		for (int i = 0; i < types.length; i++) {
			String type = types[i];
			menu.addMenuElement(type, iwrb.getLocalizedString("metadata_"+type, type));
		}
		
		return menu;
	}
	
	private void parseAction(IWContext iwc) throws RemoteException {
		if (iwc.isParameterSet(PARAMETER_CATEGORY_ID)) {
			try {
				_category = getCategoryService(iwc).getCategoryHome().findByPrimaryKey(iwc.getParameter(PARAMETER_CATEGORY_ID));
			}
			catch (FinderException e) {
				getParentPage().close();
				return;	
			}
		}
		
		if (iwc.isParameterSet(PARAMETER_METADATA))
			_metaData = iwc.getParameter(PARAMETER_METADATA);
		
		if (iwc.isParameterSet(PARAMETER_SAVE)) {
			if (iwc.isParameterSet(PARAMETER_NAME) && iwc.isParameterSet(PARAMETER_VALUE) && iwc.isParameterSet(PARAMETER_TYPE)) {
				_category.addMetaData(iwc.getParameter(PARAMETER_NAME), iwc.getParameter(PARAMETER_VALUE), iwc.getParameter(PARAMETER_TYPE));
			}
			
			if (iwc.isParameterSet(PARAMETER_DELETE)) {
				String[] toDelete = iwc.getParameterValues(PARAMETER_DELETE);
				for (int i = 0; i < toDelete.length; i++) {
					_category.removeMetaData(toDelete[i]);
				}
			}
			_category.store();
		}
	}
	
	private CategoryService getCategoryService(IWApplicationContext iwc) throws RemoteException {
		return (CategoryService) IBOLookup.getServiceInstance(iwc,CategoryService.class);
	}
}