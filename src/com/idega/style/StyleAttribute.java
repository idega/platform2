/*
 * Created on 26.5.2004
 */
package com.idega.style;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author laddi
 */
public class StyleAttribute {
	
	private String valueSeperator = Style.VALUE_SEPERATOR;
	
	private String name;
	private StyleUnit unit;
	private boolean multivalued = false;
	private List values;

	public StyleAttribute(String name) {
		this(name, null);
	}
	
	public StyleAttribute(String name, StyleUnit unit) {
		this.name = name;
		this.unit = unit;
	}
	
	protected void setMultivalued(boolean multivalued) {
		this.multivalued = multivalued;
	}
	
	protected void setValueSeperator(String valueSeperator) {
		this.valueSeperator = valueSeperator;
	}
	
	public boolean getMultivalued() {
		return this.multivalued;
	}
	
	protected String getValueSeperator() {
		return this.valueSeperator;
	}
	
	public void add(StyleValue value) {
		if (this.values == null) {
			this.values = new ArrayList();
		}
		this.values.add(value);
	}
	
	public Iterator iterator() {
		if (this.values != null) {
			return this.values.iterator();
		}
		return null;
	}
	
	public StyleValue getValue() throws StyleMultivaluedException {
		if (this.values != null) {
			if (!getMultivalued()) {
				return (StyleValue) this.values.get(0);
			}
			else {
				throw new StyleMultivaluedException();
			}
		}
		return null;
	}
	
	public StyleValue getValue(String name) {
		if (this.values != null) {
			Iterator iter = this.values.iterator();
			while (iter.hasNext()) {
				StyleValue element = (StyleValue) iter.next();
				if (element.getName().equals(name)) {
					return element;
				}
			}
		}
		return null;
	}
	
	protected boolean contains(StyleValue value) {
		if (this.values != null) {
			return this.values.contains(value);
		}
		return false;
	}
	
	public String getName() {
		return this.name;
	}
	
	public StyleUnit getUnit() {
		return this.unit;
	}
	
	public String toString() {
		return getName();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if (object instanceof StyleAttribute) {
			return this.getName().equals(((StyleAttribute) object).getName());
		}
		return false;
	}
}