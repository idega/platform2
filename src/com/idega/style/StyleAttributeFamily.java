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
		return this.name;
	}
	
	public void addStyleAttribute(StyleAttribute attribute) {
		if (this.attributes == null) {
			this.attributes = new ArrayList();
		}
		this.attributes.add(attribute);
	}
	
	public StyleAttribute getStyleAttribute(String attributeName) {
		if (this.attributes != null) {
			Iterator iter = this.attributes.iterator();
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
		if (this.attributes != null) {
			return this.attributes.iterator();
		}
		return null;
	}
}