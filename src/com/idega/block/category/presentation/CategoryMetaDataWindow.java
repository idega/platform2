/*
 * Created on 6.7.2003
 */
package com.idega.block.category.presentation;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.FinderException;
import com.idega.block.category.business.CategoryService;
import com.idega.block.category.data.ICCategory;
import com.idega.business.IBOLookup;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
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
import com.idega.presentation.ui.HiddenInput;
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
	public static final String PARAMETER_LOCALIZED_NAME = "ic_category_loc_name";
	public static final String PARAMETER_TYPE = "ic_category_type";
	public static final String PARAMETER_METADATA = "ic_metadata";
	public static final String PARAMETER_METADATA_ID = "ic_metadata_id";
	public static final String PARAMETER_LOCALE_ID = "ic_locale_id";
	private static final String METADATA = "metadata_";
	
	protected IWResourceBundle iwrb;
	protected IWResourceBundle categoryBlockResourceBundle; 
	protected String categoryBlockResourceBundleIdentifier; 
	
	private ICCategory _category = null;
	private String _metaData = null;
	private int localeID = -1;

	public CategoryMetaDataWindow() {
		setWidth(750);
		setHeight(400);
		setResizable(true);
		setScrollbar(true);
	}

	public void main(IWContext iwc) throws Exception {
		iwrb = getResourceBundle(iwc);
    if(iwc.isParameterSet(PARAMETER_LOCALE_ID)){
      localeID = Integer.parseInt(iwc.getParameter(PARAMETER_LOCALE_ID));
    }
    else{
      localeID = ICLocaleBusiness.getLocaleId( iwc.getCurrentLocale());
    }
    
    categoryBlockResourceBundleIdentifier = iwc.getParameter(CategoryWindow.prmBundleIdentifier);
    if (categoryBlockResourceBundleIdentifier != null) {
    		categoryBlockResourceBundle = iwc.getApplicationContext().getIWMainApplication().getBundle(categoryBlockResourceBundleIdentifier).getResourceBundle(ICLocaleBusiness.getLocale(localeID));
		}
    
		try {
			parseAction(iwc);
		} catch (Exception e) {
			e.printStackTrace();
			add(iwrb.getLocalizedString("category.update_failed", "Update failed"));
		}
		
		
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
		form.maintainParameter(CategoryWindow.prmBundleIdentifier);
		form.maintainParameter(PARAMETER_LOCALE_ID);
		padder.add(form);
		
		Table table = new Table();
		table.setColumns(4);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setWidth(3, Table.HUNDRED_PERCENT);
		form.add(table);
		int column = 1;
		int row = 1;
		DropdownMenu LocaleDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(PARAMETER_LOCALE_ID);
    LocaleDrop.setToSubmit();
    LocaleDrop.setSelectedElement(Integer.toString(localeID));

    table.add(LocaleDrop, 1, row);
    table.mergeCells(1, row, 4, row);
    table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
    ++row;
    
		table.setAlignment(column, row, Table.HORIZONTAL_ALIGN_CENTER);
		table.add(formatText(iwrb.getLocalizedString("delete", "Delete"), true), column++, row);
		table.add(formatText(iwrb.getLocalizedString("name", "Name"), true), column++, row);
		table.add(formatText(iwrb.getLocalizedString("value", "Value"), true), column++, row);
		table.add(formatText(iwrb.getLocalizedString("localized_name", "Localized name"), true), column++, row);
		table.add(formatText(iwrb.getLocalizedString("type", "Type"), true), column++, row++);
		
		CheckBox deleteMetadata;

		TextInput metaDataKey = new TextInput(PARAMETER_NAME);
		setStyle(metaDataKey);

		TextInput metaDataValue = new TextInput(PARAMETER_VALUE);
		setStyle(metaDataValue);
		
		TextInput metaDataLocalizedName = new TextInput(PARAMETER_LOCALIZED_NAME);
		setStyle(metaDataKey);
		
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
				String locName = categoryBlockResourceBundle.getLocalizedString(METADATA + key, key);
				
				table.setAlignment(column, row, Table.HORIZONTAL_ALIGN_CENTER);
				table.add(deleteMetadata, column++, row);
				table.add(formatText(key, false), column++, row);
				table.add(formatText(value, false), column++, row);
				table.add(formatText(locName, false), column++, row);
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
				String locName = categoryBlockResourceBundle.getLocalizedString(METADATA + key, key);
				deleteMetadata = new CheckBox(PARAMETER_DELETE, key);
				
				if (_metaData != null && _metaData.equals(key)) {
					column++;
					metaDataKey.setContent(key);
					metaDataValue.setContent(value);
					if (metaDataTypes.containsKey(key)) {
						metaDataType.setSelectedElement((String) metaDataTypes.get(key));
					}
					metaDataLocalizedName.setContent(locName);

					table.add(new HiddenInput(PARAMETER_METADATA_ID, _metaData), column, row);
					table.add(metaDataKey, column++, row);
					table.add(metaDataValue, column++, row);
					table.add(metaDataLocalizedName, column++, row);
					table.add(metaDataType, column, row++);
				}
				else {
					Link link = new Link(formatText(key));
					link.addParameter(PARAMETER_CATEGORY_ID, _category.getPrimaryKey().toString());
					link.addParameter(CategoryWindow.prmBundleIdentifier, categoryBlockResourceBundleIdentifier);
					link.addParameter(PARAMETER_LOCALE_ID, iwc.getParameter(PARAMETER_LOCALE_ID));
					link.addParameter(PARAMETER_METADATA, key);
					
					table.setAlignment(column, row, Table.HORIZONTAL_ALIGN_CENTER);
					table.add(deleteMetadata, column++, row);
					table.add(link, column++, row);
					table.add(formatText(value), column++, row);
					table.add(formatText(locName), column++, row);
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
			table.add(metaDataLocalizedName, column++, row);
			table.add(metaDataType, column, row++);
		}
		
		row++;
		//table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
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
		
		String updateName = null;
		if (iwc.isParameterSet(PARAMETER_METADATA)) {
			_metaData = iwc.getParameter(PARAMETER_METADATA);
		}
		if (iwc.isParameterSet(PARAMETER_METADATA_ID)) {
			updateName = iwc.getParameter(PARAMETER_METADATA_ID);
		}
	
		
		if (iwc.isParameterSet(PARAMETER_SAVE)) {
			String key = iwc.getParameter(PARAMETER_NAME);
			if (key != null && iwc.isParameterSet(PARAMETER_VALUE) && iwc.isParameterSet(PARAMETER_TYPE)) {
				if (updateName != null) {
					_category.renameMetaData(updateName, key);
				} else {
					_category.addMetaData(key, iwc.getParameter(PARAMETER_VALUE), iwc.getParameter(PARAMETER_TYPE));
				}
			}
			
			if (key != null && iwc.isParameterSet(PARAMETER_LOCALIZED_NAME)) {
				categoryBlockResourceBundle.setLocalizedString(METADATA + key, iwc.getParameter(PARAMETER_LOCALIZED_NAME));
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