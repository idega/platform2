/*
 * Created on 1.6.2004
 */
package com.idega.style;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * @author laddi
 */
public class StyleAttributeFamily {

	private String name;
	private Collection attributes;
	
	public StyleAttributeFamily(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addStyleAttribute(StyleAttribute attribute) {
		if (attributes == null) {
			attributes = new ArrayList();
		}
		attributes.add(attribute);
	}
	
	public StyleAttribute getStyleAttribute(String attributeName) {
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
	
	public Iterator iterator() {
		if (attributes != null) {
			return attributes.iterator();
		}
		return null;
	}
}