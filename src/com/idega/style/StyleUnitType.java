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
		if (types == null) {
			types = new ArrayList();
		}
		types.add(unit);
	}
	
	public boolean getMultivalued() {
		if (types != null) {
			return types.size() > 1;
		}
		return false;
	}
	
	public Iterator iterator() {
		if (types != null) {
			return types.iterator();
		}
		return null;
	}
	
	public String getName() {
		return this.name;
	}
}