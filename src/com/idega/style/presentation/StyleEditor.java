/*
 * $Id: StyleEditor.java,v 1.1 2004/09/16 10:25:24 laddi Exp $
 * Created on 10.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.style.presentation;

import java.util.Iterator;

import com.idega.builder.presentation.IBColorChooser;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.FloatInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.style.Style;
import com.idega.style.StyleAttribute;
import com.idega.style.StyleAttributeFamily;
import com.idega.style.StyleManager;
import com.idega.style.StyleMultivaluedException;
import com.idega.style.StyleUnit;
import com.idega.style.StyleUnitType;
import com.idega.style.StyleValue;


/**
 * Last modified: $Date: 2004/09/16 10:25:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class StyleEditor extends Block {

	private static final String PARAMETER_STYLE_SESSION = "session_style";

	private static final String PARAMETER_ATTRIBUTE_FAMILY = "attribute_family";
	private static final String PARAMETER_CURRENT_ATTRIBUTE_FAMILY = "current_attribute_family";
	
	private String attributeFamily;
	
	private Style style;
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return super.getBundleIdentifier();
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		StyleManager manager = StyleManager.getStaticInstance();
		style = (Style) iwc.getSessionAttribute(PARAMETER_STYLE_SESSION);
		parse(iwc, manager);

		Form form = new Form();
		
		Table table = new Table();
		form.add(table);
		
		DropdownMenu menu = new DropdownMenu(PARAMETER_ATTRIBUTE_FAMILY);
		menu.setToSubmit();
		menu.keepStatusOnAction();
		
		Iterator iter = manager.attributeFamilyIterator();
		while (iter.hasNext()) {
			StyleAttributeFamily family = (StyleAttributeFamily) iter.next();
			menu.addMenuElement(family.getName(), family.getName());
			
			if (attributeFamily == null) {
				attributeFamily = family.getName();
			}
		}
		form.addParameter(PARAMETER_CURRENT_ATTRIBUTE_FAMILY, attributeFamily);
		
		table.add(menu, 1, 1);
		table.add(getAttributeTable(manager), 1, 3);
		table.add(getPreviewTable(style), 1, 5);
		
		add(form);
	}
	
	private Table getAttributeTable(StyleManager manager) {
		Table table = new Table();
		int row = 1;
		
		StyleAttributeFamily family = manager.getAttributeFamily(attributeFamily);
		Iterator iter = family.iterator();
		while (iter.hasNext()) {
			StyleAttribute attribute = (StyleAttribute) iter.next();
			table.add(attribute.getName() + ":", 1, row);
			
			Iterator iterator = attribute.iterator();
			while (iterator.hasNext()) {
				StyleValue value = (StyleValue) iterator.next();
				if (attribute.getMultivalued()) {
					table.add(value.getName(), 2, row);
				}
				if (value.getFixedValue()) {
					table.add(getOptions(attribute.getName() + "_" + value.getName(), value.iterator(), getSelectedValue(style, attribute, value)), 3, row);
				}
				else {
					table.add(getValueObject(value.getType(), attribute.getName() + "_" + value.getName(), getSelectedValue(style, attribute, value)), 3, row);
					
					StyleUnitType unitType = value.getUnitType();
					if (unitType != null) {
						table.add(getUnits(attribute.getName() + "_" + value.getName() + "_unit", unitType.iterator(), unitType.getMultivalued(), getSelectedValue(style, attribute, value)), 3, row);
					}
				}
				row++;
			}
		}
		
		return table;
	}
	
	private Table getPreviewTable(Style style) {
		Table table = new Table();
		
		Text text = new Text("This is an example of the style you've chosen...");
		if (style != null) {
			text.setFontStyle(style.toString());
		}
		table.add(text, 1, 1);
		
		table.add(new SubmitButton("Preview"), 1, 3);
		
		return table;
	}
	
	private PresentationObject getValueObject(String type, String name, StyleValue value) {
		if (type.equals("com.idega.util.IWColor")) {
			IBColorChooser color = new IBColorChooser(name);
			if (value != null) {
				color.setSelected(value.getValue());
			}
			return color;
		}
		else if (type.equals("java.lang.Float")) {
			FloatInput input = new FloatInput(name);
			input.setLength(5);
			if (value != null) {
				input.setValue(value.getValue());
			}
			return input;
		}
		else {
			TextInput input = new TextInput(name);
			input.setLength(16);
			if (value != null) {
				input.setValue(value.getValue());
			}
			return input;
		}
	}
	
	private DropdownMenu getOptions(String name, Iterator options, StyleValue value) {
		DropdownMenu menu = new DropdownMenu(name);
		menu.addMenuElementFirst("", "");
		
		if (options != null) {
			while (options.hasNext()) {
				Object element = (Object) options.next();
				menu.addMenuElement(element.toString(), element.toString());
			}
		}
		
		if (value != null) {
			menu.setSelectedElement(value.getValue());
		}
		
		return menu;
	}
	
	private InterfaceObject getUnits(String name, Iterator units, boolean multivalued, StyleValue value) {
		if (multivalued) {
			DropdownMenu menu = new DropdownMenu(name);
			menu.addMenuElementFirst("", "");
			
			if (units != null) {
				while (units.hasNext()) {
					StyleUnit unit = (StyleUnit) units.next();
					menu.addMenuElement(unit.getName(), unit.getName());
				}
			}
			
			if (value != null) {
				StyleUnit unit = value.getUnit();
				if (unit != null) {
					menu.setSelectedElement(unit.getName());
				}
			}
			
			return menu;
		}
		else {
			if (units != null) {
				while (units.hasNext()) {
					StyleUnit unit = (StyleUnit) units.next();
					return new HiddenInput(name, unit.getName());
				}
			}
		}
		return null;
	}
	
	private StyleValue getSelectedValue(Style style, StyleAttribute attribute, StyleValue value) {
		if (style != null) {
			StyleAttribute styleAttribute = style.get(attribute.getName());
			if (styleAttribute != null) {
				StyleValue styleValue = null;
				if (attribute.getMultivalued()) {
					styleValue = styleAttribute.getValue(value.getName());
					
				}
				else {
					try {
						styleValue = styleAttribute.getValue();
					}
					catch (StyleMultivaluedException sme) {
						log(sme);
					}
				}
				
				return styleValue;
			}
		}
		return null;
	}
	
	private void parse(IWContext iwc, StyleManager manager) {
		if (iwc.isParameterSet(PARAMETER_ATTRIBUTE_FAMILY)) {
			attributeFamily = iwc.getParameter(PARAMETER_ATTRIBUTE_FAMILY);
		}
		
		if (iwc.isParameterSet(PARAMETER_CURRENT_ATTRIBUTE_FAMILY)) {
			if (style == null) {
				style = new Style();
			}
			
			String currentAttributeFamily = iwc.getParameter(PARAMETER_CURRENT_ATTRIBUTE_FAMILY);
			StyleAttributeFamily family = manager.getAttributeFamily(currentAttributeFamily);
			Iterator iter = family.iterator();
			while (iter.hasNext()) {
				StyleAttribute attribute = (StyleAttribute) iter.next();
				StyleAttribute newAttribute = new StyleAttribute(attribute.getName());
				boolean addAttribute = true;
				
				Iterator iterator = attribute.iterator();
				while (iterator.hasNext()) {
					StyleValue value = (StyleValue) iterator.next();
					boolean addValue = false;
					if (iwc.isParameterSet(attribute.getName() + "_" + value.getName())) {
						addValue = true;
						value.setValue(iwc.getParameter(attribute.getName() + "_" + value.getName()));
						if (value.getUnitType() != null && value.getUnitType().getMultivalued()) {
							if (iwc.isParameterSet(attribute.getName() + "_" + value.getName() + "_unit")) {
								StyleUnit unit = manager.getUnit(iwc.getParameter(attribute.getName() + "_" + value.getName() + "_unit"));
								value.setUnit(unit);
							}
							else {
								addValue = false;
								addAttribute = false;
							}
						}
					}
					else {
						addAttribute = false;
					}
					
					if (addValue) {
						newAttribute.add(value);
					}
				}
				
				if (addAttribute) {
					style.add(newAttribute);
				}
				else {
					style.remove(attribute);
				}
			}
			
			iwc.setSessionAttribute(PARAMETER_STYLE_SESSION, style);
		}
	}
}