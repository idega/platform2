/*
 * Created on 1.6.2004
 */
package com.idega.style;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.idega.idegaweb.IWMainApplication;
import com.idega.util.FileUtil;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLException;
import com.idega.xml.XMLParser;


/**
 * @author laddi
 */
public class StyleManager {

	private static final String XML_UNITS = "units";
	private static final String XML_UNIT_TYPE = "unit-type";
	private static final String XML_UNIT = "unit";
	private static final String XML_ATTRIBUTE_FAMILY = "attribute-family";
	private static final String XML_ATTRIBUTE = "attribute";
	private static final String XML_VALUES = "values";
	private static final String XML_VALUE = "value";
	private static final String XML_OPTIONS = "options";
	private static final String XML_OPTION = "option";
	private static final String XML_FIXED = "fixed";
	private static final String XML_TYPE = "type";
	private static final String XML_NAME = "name";
	private static final String XML_SEPERATOR = "seperator";
	private static final String XML_PREFIX = "prefix";
	private static final String XML_POSTFIX = "postfix";
	
	private static Map attributeFamilies;
	private static Map unitTypeMap;
	private static Map unitMap;
	
	private static StyleManager instance;
	
	public StyleManager() {
	}
	
	public static StyleManager getStaticInstance() {
		if (instance == null) {
			instance = new StyleManager();
		}
		return instance;
	}
	
	public void parseXML(IWMainApplication iwma) {
		String URL = iwma.getApplicationRealPath() + FileUtil.getFileSeparator() + "idegaweb" + FileUtil.getFileSeparator() + "style" + FileUtil.getFileSeparator() + "style.xml";
		System.out.println(URL);
		try {
			File file = FileUtil.getFileAndCreateRecursiveIfNotExists(URL);
			
			XMLParser parser = new XMLParser();
			XMLDocument document = parser.parse(file);
			
			XMLElement element = document.getRootElement();
			List units = element.getChild(XML_UNITS).getChildren();
			List attributes = element.getChildren(XML_ATTRIBUTE_FAMILY);
			
			attributeFamilies = new LinkedHashMap();
			StyleAttributeFamily family;
			StyleAttribute attribute;
			StyleValue value;
			StyleUnit unit;
			StyleUnitType unitType;
			unitTypeMap = new LinkedHashMap();
			unitMap = new HashMap();
			
			Iterator iter = units.iterator();
			while (iter.hasNext()) {
				XMLElement unitTypes = (XMLElement) iter.next();
				XMLElement name = unitTypes.getChild(XML_NAME);
				unitType = new StyleUnitType(name.getText());
				
				List xmlUnits = unitTypes.getChildren(XML_UNIT);
				Iterator iterator = xmlUnits.iterator();
				while (iterator.hasNext()) {
					XMLElement xmlUnit = (XMLElement) iterator.next();
					XMLElement unitName = xmlUnit.getChild(XML_NAME);
					XMLElement prefix = xmlUnit.getChild(XML_PREFIX);
					XMLElement postfix = xmlUnit.getChild(XML_POSTFIX);
					
					unit = new StyleUnit();
					if (unitName != null) {
						unit.setName(unitName.getText());
					}
					if (prefix != null) {
						unit.setPrefix(prefix.getText());
					}
					if (postfix != null) {
						unit.setPostfix(postfix.getText());
					}
					unitType.addStyleUnit(unit);
					unitMap.put(unit.getName(), unit);
				}
				unitTypeMap.put(name.getText(), unitType);
			}

			iter = attributes.iterator();
			while (iter.hasNext()) {
				XMLElement attributeFamily = (XMLElement) iter.next();
				XMLElement name = attributeFamily.getChild(XML_NAME);
				family = new StyleAttributeFamily(name.getText());
				
				List att = attributeFamily.getChildren(XML_ATTRIBUTE);
				Iterator iterator = att.iterator();
				while (iterator.hasNext()) {
					XMLElement xmlAttribute = (XMLElement) iterator.next();
					name = xmlAttribute.getChild(XML_NAME);
					attribute = new StyleAttribute(name.getText());
					family.addStyleAttribute(attribute);
					
					XMLElement values = xmlAttribute.getChild(XML_VALUES);
					XMLElement seperator = values.getChild(XML_SEPERATOR);
					if (seperator != null) {
						attribute.setValueSeperator(seperator.getText());
					}
					
					List valueList = values.getChildren(XML_VALUE);
					if (valueList.size() > 1) {
						attribute.setMultivalued(true);
					}
					Iterator iterator2 = valueList.iterator();
					while (iterator2.hasNext()) {
						XMLElement xmlValue = (XMLElement) iterator2.next();
						
						XMLElement xmlValueName = xmlValue.getChild(XML_NAME);
						String valueName = null;
						if (xmlValueName != null) {
							valueName = xmlValueName.getText();
						}
						
						XMLElement xmlFixed = xmlValue.getChild(XML_FIXED);
						boolean fixed = false;
						if (xmlFixed != null) {
							fixed = Boolean.valueOf(xmlFixed.getText()).booleanValue();
						}
						
						XMLElement xmlType = xmlValue.getChild(XML_TYPE);
						String type = null;
						if (xmlType != null) {
							type = xmlType.getText();
						}
						
						XMLElement xmlUnitType = xmlValue.getChild(XML_UNIT_TYPE);
						StyleUnitType unitTypes = null;
						if (xmlUnitType != null) {
							unitTypes = (StyleUnitType) unitTypeMap.get(xmlUnitType.getText());
						}
						
						value = new StyleValue(valueName, null, null);
						value.setType(type);
						value.setFixedValue(fixed);
						value.setUnitType(unitTypes);
						
						if (fixed) {
							XMLElement xmlOptions = xmlValue.getChild(XML_OPTIONS);
							List options = xmlOptions.getChildren();
							Iterator iterator3 = options.iterator();
							while (iterator3.hasNext()) {
								XMLElement option = (XMLElement) iterator3.next();
								value.addOption(option.getText());
							}
						}
						attribute.add(value);
					}
				}
				
				attributeFamilies.put(family.getName(), family);
			}
			
			/*Iterator iterator = styles.iterator();
			while (iterator.hasNext()) {
				StyleAttributeFamily attFam = (StyleAttributeFamily) iterator.next();
				System.out.println("Attribute family: " + attFam.getName());
				System.out.println("-----------------------------------------------------------");
				
				Iterator attributeIterator = attFam.iterator();
				while (attributeIterator.hasNext()) {
					StyleAttribute att = (StyleAttribute) attributeIterator.next();
					System.out.println("   Attribute: " + att.getName());
					System.out.println("   - Multivalued: " + att.getMultivalued());
					
					Iterator attIterator = att.iterator();
					while (attIterator.hasNext()) {
						StyleValue attValue = (StyleValue) attIterator.next();
						if (att.getMultivalued()) {
							System.out.println("   - Value name: " + attValue.getName());
						}
						System.out.println("     - Fixed value: " + attValue.getFixedValue());
						System.out.println("     - Type: " + attValue.getType());
						
						StyleUnitType attUnitType = attValue.getUnitType();
						if (attUnitType != null) {
							System.out.println("     - Value unit type: " + attUnitType.getName());
						}
						
						Iterator valueIter = attValue.iterator();
						if (valueIter != null) {
							System.out.println("     - Value options: ");
							while (valueIter.hasNext()) {
								System.out.println("       - " + valueIter.next());
							}
						}
						System.out.println("");
					}
					System.out.println("");
				}
				System.out.println("----------------------------------");
			}*/
		}
		catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		catch (XMLException xe) {
			throw new RuntimeException(xe.getMessage());
		}
	}
	
	public Iterator attributeFamilyIterator() {
		return attributeFamilies.values().iterator();
	}
	
	public StyleAttributeFamily getAttributeFamily(String attributeFamily) {
		return (StyleAttributeFamily) attributeFamilies.get(attributeFamily);
	}
	
	public StyleUnitType getUnitType(String unitTypeName) {
		return (StyleUnitType) unitTypeMap.get(unitTypeName);
	}

	public StyleUnit getUnit(String unitName) {
		return (StyleUnit) unitMap.get(unitName);
	}
}