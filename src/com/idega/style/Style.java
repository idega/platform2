/*
 * Created on 20.5.2004
 */
package com.idega.style;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author laddi
 */
public class Style {

	private List attributes;
	
	public static final String ATTRIBUTE_SEPERATOR = ";";
	public static final String VALUE_SEPERATOR = " ";
	public static final String STYLE_SEPERATOR = ":";
	
	public Style() {
		attributes = new ArrayList();
	}
	
	public void add(StyleAttribute attribute) {
		attributes.add(attribute);
	}
	
	public boolean contains(StyleAttribute attribute) {
		return attributes.contains(attribute);
	}
	
	public Iterator iterator() {
		return attributes.iterator();
	}
	
	public StyleAttribute get(String attributeName) {
		if (attributes != null) {
			Iterator iter = attributes.iterator();
			while (iter.hasNext()) {
				StyleAttribute attribute = (StyleAttribute) iter.next();
				if (attribute.getName().equals(attributeName)) {
					return attribute;
				}
			}
		}
		return null;
	}
	
	public boolean remove(StyleAttribute attribute) {
		if (attributes != null) {
			return attributes.remove(attribute);
		}
		return false;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		Iterator iter = attributes.iterator();
		while (iter.hasNext()) {
			StyleAttribute attribute = (StyleAttribute) iter.next();
			buffer.append(attribute.getName());
			buffer.append(STYLE_SEPERATOR).append(VALUE_SEPERATOR);
			Iterator iterator = attribute.iterator();
			while (iterator.hasNext()) {
				StyleValue value = (StyleValue) iterator.next();
				buffer.append(value.toString());
				
				if (iter.hasNext()) {
					buffer.append(VALUE_SEPERATOR);
				}
			}
			buffer.append(ATTRIBUTE_SEPERATOR);
		}

		return buffer.toString();
	}
	
	public boolean equals(Object object) {
		if (object instanceof Style) {
			Style styleObject = (Style) object;
			Iterator iter = styleObject.iterator();
			while (iter.hasNext()) {
				StyleAttribute attribute = (StyleAttribute) iter.next();
				if (contains(attribute)) {
					StyleAttribute att = get(attribute.getName());
					Iterator iterator = attribute.iterator();
					while (iterator.hasNext()) {
						StyleValue value = (StyleValue) iterator.next();
						if (!att.contains(value)) {
							return false;
						}
					}
				}
				else {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}