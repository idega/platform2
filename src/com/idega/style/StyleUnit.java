/*
 * Created on 20.5.2004
 */
package com.idega.style;

/**
 * @author laddi
 */
public class StyleUnit {
	
	private String prefix;
	private String postfix;
	private String name;
	
	public StyleUnit() {
		this(null, null);
	}
	
	protected StyleUnit(String name, String prefix, String postfix) {
		this(prefix, postfix);
		this.name = name;
	}
	
	public StyleUnit(String prefix, String postfix) {
		setPrefix(prefix);
		setPostfix(postfix);
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}
	
	public String getName() {
		return this.name;
	}
	
	protected void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		if (prefix != null) {
			return prefix;
		}
		return "";
	}

	public String getPostfix() {
		if (postfix != null) {
			return postfix;
		}
		return "";
	}
	
	public String toString() {
		return toString(null);
	}
	
	public String toString(StyleValue value) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getPrefix());
		if (value != null) {
			buffer.append(value.getValue());
		}
		else {
			buffer.append("xxx");
		}
		buffer.append(getPostfix());
		
		return buffer.toString();
	}
}