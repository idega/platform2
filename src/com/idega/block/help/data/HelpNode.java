/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.help.data;

import com.idega.core.data.ICTreeNode;
import com.idega.idegaweb.IWApplicationContext;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class HelpNode implements ICTreeNode {
	protected int _key = -1;
	protected String _bundle = null;
	protected String _name = null;
	protected ICTreeNode _parent = null;
	protected List _children = null;
	protected int _order = -1;

	public HelpNode(int key, String bundle, String name) {
		this(key,bundle,name,-1);
	}
	
	public HelpNode(int key, String bundle, String name, int order) {
		this._key = key;
		this._bundle = bundle;
		this._name = name;	
		this._order = order;		
	}

	/**
	 * @see com.idega.core.ICTreeNode#getChildrenIterator()
	 */
	public Iterator getChildrenIterator() {
	    Iterator it = null;
	    Collection children = getChildren();
	    if (children != null) {
	        it = children.iterator();
	    }
	    return it;
	}

	/**
	 * @see com.idega.core.ICTreeNode#getChildren()
	 */
	public Collection getChildren() {
		if (this._children != null) {
			return this._children;
		}
		else {
			return null;
		}
	}

	/**
	 * @see com.idega.core.ICTreeNode#getAllowsChildren()
	 */
	public boolean getAllowsChildren() {
		return true;
	}

	/**
	 * @see com.idega.core.ICTreeNode#getChildAtIndex(int)
	 */
	public ICTreeNode getChildAtIndex(int childIndex) {
		if (this._children != null) {
			return (HelpNode)this._children.get(childIndex);
		}
		else {
			return null;
		}
	}

	/**
	 * @see com.idega.core.ICTreeNode#getChildCount()
	 */
	public int getChildCount() {
		if (this._children != null) {
			return this._children.size();
		}
		else {
			return 0;
		}
	}

	/**
	 * @see com.idega.core.ICTreeNode#getIndex(ICTreeNode)
	 */
	public int getIndex(ICTreeNode node) {
		if (this._children != null) {
			return this._children.indexOf(node);
		}
		else {
			return -1;
		}
	}

	/**
	 * @see com.idega.core.ICTreeNode#getParentNode()
	 */
	public ICTreeNode getParentNode() {
		return this._parent;
	}

	/**
	 * @see com.idega.core.ICTreeNode#isLeaf()
	 */
	public boolean isLeaf() {
		return true;
	}

	/**
	 * @see com.idega.core.ICTreeNode#getNodeName()
	 */
	public String getNodeName() {
		return this._name;
	}
	
	public String getNodeName(Locale locale) {
		return getNodeName();
	}
	
	public String getNodeName(Locale locale, IWApplicationContext iwac){
		return getNodeName(locale);
	}

	/**
	 * @see com.idega.core.ICTreeNode#getNodeID()
	 */
	public int getNodeID() {
		return this._key;
	}

	/**
	 * @see com.idega.core.ICTreeNode#getSiblingCount()
	 */
	public int getSiblingCount() {
		if (this._parent != null) {
			return (this._parent.getChildCount() - 1);
		}
		else {
			return 0;
		}
	}
	
	/**
	 * @see com.idega.core.ICTreeNode#getNodeType()
	 */
	public int getNodeType(){
		return -1;
	}
	
	public void setNodeID(int id) {
		this._key = id;	
	}
	
	public void setNodeName(String name) {
		this._name = name;	
	}
	
	public String getBundleName() {
		return this._bundle;	
	}
	
	public void setBundleName(String bundle) {
		this._bundle = bundle;	
	}
	
	public void addChild(ICTreeNode child) {
		if (this._children == null) {
			this._children = new Vector();
		}
			
		this._children.add(child);	
		((HelpNode)child).setParent(this);
	}
	
	protected void setParent(ICTreeNode parent) {
		this._parent = parent;
	}
}