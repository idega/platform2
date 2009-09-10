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
public class StyleUnitType {

	private String name;
	private Collection types;
	
	public StyleUnitType(String name) {
		this.name = name;
	}
	
	public void addStyleUnit(StyleUnit unit) {
		if (this.types == null) {
			this.types = new ArrayList();
		}
		this.types.add(unit);
	}
	
	public boolean getMultivalued() {
		if (this.types != null) {
			return this.types.size() > 1;
		}
		return false;
	}
	
	public Iterator iterator() {
		if (this.types != null) {
			return this.types.iterator();
		}
		return null;
	}
	
	public String getName() {
		return this.name;
	}
}