/*
 * Created on 26.5.2004
 */
package com.idega.style;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author laddi
 */
public class StyleValue {
	
	private String name;
	private String value;
	private StyleUnit unit;
	
	private Collection options;
	private StyleUnitType unitType;
	private String type;
	private boolean fixed;
	
	public StyleValue(String value) {
		this(null, value, null);
	}
	
	public StyleValue(String name, String value) {
		this(name, value, null);
	}
	
	public StyleValue(String value, StyleUnit unit) {
		this(null, value, unit);
	}
	
	public StyleValue(String name, String value, StyleUnit unit) {
		this.name = name;
		this.value = value;
		this.unit = unit;
	}
	
	protected void addOption(Object option) {
		if (options == null) {
			options = new ArrayList();
		}
		options.add(option);
	}
	
	public Iterator iterator() {
		if (options != null) {
			return options.iterator();
		}
		return null;
	}
	
	protected void setUnitType(StyleUnitType type) {
		this.unitType = type;
	}
	
	public StyleUnitType getUnitType() {
		return this.unitType;
	}
	
	protected void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	protected void setFixedValue(boolean fixedValue) {
		this.fixed = fixedValue;
	}
	
	public boolean getFixedValue() {
		return this.fixed;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public StyleUnit getUnit() {
		return unit;
	}
	
	public void setUnit(StyleUnit unit) {
		this.unit = unit;
	}
	
	public String toString() {
		if (getUnit() != null) {
			return getUnit().toString(this);
		}
		else {
			return getValue();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if (object instanceof StyleValue) {
			try {
				return this.getName().equals(((StyleValue) object).getName());
			}
			catch (NullPointerException npe) {
				return true;
			}
		}
		return false;
	}
}