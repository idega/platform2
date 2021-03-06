/*
 * $Id: PageTreeNode.java,v 1.22.2.1 2007/01/12 19:31:48 idegaweb Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.builder.business;

import com.idega.builder.data.IBPageName;
import com.idega.core.builder.data.ICPage;
import com.idega.core.data.ICTreeNode;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class PageTreeNode implements ICTreeNode {
	private static final String PAGE_TREE = "ib_page_node_tree";
	private static final String NAME_TREE = "ib_page_node_tree_names";

	protected int _id = -1;
	protected String _name = null;
	protected PageTreeNode _parent = null;
	protected List _children = null;
	protected Object _extra = null;
	protected int _order = -1;
	protected IWApplicationContext _iwac;
	protected boolean _isCategory = false;
	protected ICPage _page;

	protected PageTreeNode(int id, String name) {
		this(id, name, -1, false);
	}

	protected PageTreeNode(int id, String name, int order) {
		this(id, name, order, false);
	}

	protected PageTreeNode(int id, String name, boolean isCategory) {
		this(id, name, -1, isCategory);
	}

	protected PageTreeNode(int id, String name, int order, boolean isCategory) {
		this._id = id;
		this._name = name;
		this._parent = null;
		this._children = new Vector();
		this._extra = null;
		this._order = order;
		this._isCategory = isCategory;
	}

	/**
	 *
	 */
	public PageTreeNode(int id, IWApplicationContext iwc) {
		this._iwac=iwc;
		Map tree = PageTreeNode.getTree(iwc);
		PageTreeNode node = (PageTreeNode) tree.get(new Integer(id));
		if (node != null) {
			this._id = node._id;
			this._name = node._name;
			this._parent = node._parent;
			this._children = node._children;
			this._extra = node._extra;
		}
		else {
			this._id = id;
			this._children = new Vector();
		}
	}

	/**
	 *
	 */
	public void setNodeId(int id) {
		this._id = id;
	}

	/**
	 *
	 */
	public void setNodeName(String name) {
		this._name = name;
	}

	protected static Map getNamesFromDatabase() {
		Map names = new Hashtable();
		Collection col = TreeNodeFinder.getAllPageNames();		
		if (col != null) {
			Iterator it = col.iterator();
			while (it.hasNext()) {
				IBPageName nameEntry = (IBPageName)it.next();
				int pageId = nameEntry.getPageId();
				int localeId = nameEntry.getLocaleId();
								
				Locale loc = ICLocaleBusiness.getLocale(localeId);
				
				Integer locId = new Integer(pageId);
				Map localizedNames = (Map)names.get(locId);
				if (localizedNames == null) {
					localizedNames = new Hashtable();
					names.put(locId,localizedNames);					
				}
				
				StringBuffer localizedKey = new StringBuffer(loc.getLanguage());
				String country = loc.getCountry();
				if (country != null && !country.equals("")) {
					localizedKey.append("_");
					localizedKey.append(country);
				}
				
				localizedNames.put(localizedKey.toString(),nameEntry.getPageName());
			}		
		}
		
		return names;
	}

	protected static Map getTreeFromDatabase() {
		List page = null;
		List template = null;
		List rel = null;
		List rel2 = null;
		try {
			page = TreeNodeFinder.listOfAllPages();
			rel = TreeNodeFinder.listOfAllPageRelationships();
			template = TreeNodeFinder.listOfAllTemplates();
			rel2 = TreeNodeFinder.listOfAllTemplateRelationships();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		Map tree = new Hashtable();

		Iterator it = null;
		if (page != null) {
			it = page.iterator();
			while (it.hasNext()) {
				ICPage pages = (ICPage) it.next();
				PageTreeNode node = null;
				int order = pages.getTreeOrder();
				if (order == -1) {
					node = new PageTreeNode(pages.getID(), pages.getName(), pages.isCategory());
				}
				else {
					node = new PageTreeNode(pages.getID(), pages.getName(), order, pages.isCategory());
				}
				node.setPage(pages);
				tree.put(new Integer(node.getNodeID()), node);
			}
		}

		if (template != null) {
			it = template.iterator();
			while (it.hasNext()) {
				ICPage pages = (ICPage) it.next();
				PageTreeNode node = null;
				int order = pages.getTreeOrder();
				if (order == -1) {
					node = new PageTreeNode(pages.getID(), pages.getName());
				}
				else {
					node = new PageTreeNode(pages.getID(), pages.getName(), order);
				}
				node.setPage(pages);
				tree.put(new Integer(node.getNodeID()), node);
			}
		}

		if (rel != null) {
			it = rel.iterator();
			while (it.hasNext()) {
				Integer parentId = (Integer) it.next();
				Integer childId = (Integer) it.next();
				PageTreeNode parent = (PageTreeNode) tree.get(parentId);
				PageTreeNode child = (PageTreeNode) tree.get(childId);
				if (parent != null) {
					parent.addChild(child);
				}

				if (child != null) {
					child._parent = parent;
				}
			}
		}

		if (rel2 != null) {
			it = rel2.iterator();
			while (it.hasNext()) {
				Integer parentId = (Integer) it.next();
				Integer childId = (Integer) it.next();
				PageTreeNode parent = (PageTreeNode) tree.get(parentId);
				PageTreeNode child = (PageTreeNode) tree.get(childId);
				if (parent != null) {
					parent.addChild(child);
				}

				if (child != null) {
					child._parent = parent;
				}
			}
		}

		return tree;
	}

	/**
	 *
	 */
	public Collection getChildren() {
		return this._children;
	}

	/**
	 *
	 */
	public Iterator getChildrenIterator() {
	    if (this._children == null) {
	        return null;
	    }
		return this._children.iterator();
	}

	/**
	 *
	 */
	public boolean getAllowsChildren() {
		return true;
	}

	/**
	 *
	 */
	public ICTreeNode getChildAtIndex(int childIndex) {
		/**
		 * @todo fix this
		 */
		return null;
	}

	/**
	 *
	 */
	public int getChildCount() {
		return this._children.size();
	}

	/**
	 *
	 */
	public int getIndex(ICTreeNode node) {
		return 0;
	}

	/**
	 *
	 */
	public ICTreeNode getParentNode() {
		return this._parent;
	}
	
	public ICPage getPage() {
		return this._page;
	}
	
	public void setPage(ICPage page) {
		this._page = page;
	}

	/**
	 *
	 */
	public boolean isLeaf() {
		/*int children = getChildCount();
		if (children > 0) {
		  return false;
		}
		else {
		  return true;
		}*/
		return true;
	}

	/**
	 * Returns the node name for this node
	 */
	public String getNodeName() {
		return this._name;
	}

	/**
	 * Returns the Localized node name for this node
	 */
	public String getNodeName(Locale locale) {
		IWApplicationContext iwac = getIWApplicationContext();
		return getNodeName(locale,iwac);
	}
	
	public String getNodeName(Locale locale, IWApplicationContext iwac){
		return getLocalizedNodeName(iwac,locale);
	}
	
	public String getLocalizedNodeName(IWContext iwc) {
		Locale curr = iwc.getCurrentLocale();
		return getLocalizedNodeName(iwc,curr);
	}
	
	public String getLocalizedNodeName(IWApplicationContext iwc,Locale locale) {
		Hashtable names = (Hashtable)iwc.getApplicationAttribute(NAME_TREE);
		if (names == null) {
			return getNodeName();
		}
			
		Hashtable pageNames = (Hashtable)names.get(new Integer(getNodeID()));
		if (pageNames == null) {
			return getNodeName();
		}
	
		//Locale curr = iwc.getCurrentLocale();
		StringBuffer localeString = new StringBuffer(locale.getLanguage());
		String country = locale.getCountry();
		if (country != null && !country.equals("")) {
			localeString.append("_");
			localeString.append(country);
		}
			
		String localizedName = (String)pageNames.get(localeString.toString());
		if (localizedName != null && !localizedName.equals("")) {
			return localizedName;
		}
		
		return getNodeName();
	}	
	
	public void setLocalizedNodeName(String locale, String name, IWContext iwc) {
		Hashtable names = (Hashtable)iwc.getApplicationAttribute(NAME_TREE);
		if (names == null) {
			names = new Hashtable();
			iwc.setApplicationAttribute(NAME_TREE,names);
		}
		
		Integer nodeId = new Integer(getNodeID());
		Hashtable pageNames = (Hashtable)names.get(nodeId);
		if (pageNames == null) {
			pageNames = new Hashtable();
			names.put(nodeId,pageNames);
		}
		
		pageNames.put(locale,name);
	}

	/**
	 *
	 */
	public int getNodeID() {
		return this._id;
	}

	/**
	 *
	 */
	public int getSiblingCount() {
		return (0);
	}

	/**
	 *
	 */
	public boolean removeChild(PageTreeNode child) {
		int index = this._children.indexOf(child);
		if (index != -1) {
			this._children.remove(index);
			return true;
		}

		return (false);
	}

	/**
	 *
	 */
	public boolean addChild(PageTreeNode child) {
		child._parent = this;

		if (this._children.contains(child)) {
			int index = this._children.indexOf(child);
			this._children.add(index, child);
		}
		else {
			if (this._children.isEmpty()) {
				this._children.add(child);
			}
			else { //Check where in the tree this node should be (from the tree_order field)
				if (child._order < 0) {
					this._children.add(child);
				}
				else {
					ListIterator it = (new LinkedList(this._children)).listIterator();

					while (it.hasNext()) {
						PageTreeNode node = (PageTreeNode) it.next();
						if (node._order == -1 || node._order > child._order) {
							int i = it.previousIndex();
							this._children.add(i, child);
							return true;
						}
					}

					this._children.add(child);
				}
			}
		}

		return true;
	}

	/**
	 *
	 */
	public void setExtraInfo(Object extra) {
		this._extra = extra;
	}

	/**
	 *
	 */
	public Object getExtraInfo() {
		return this._extra;
	}

	/**
	 * Gets the tree and preloads it and stores in cache
	 */
	public static Map getTree(IWApplicationContext iwc) {
		Map tree = (Map) iwc.getApplicationAttribute(PageTreeNode.PAGE_TREE);

		if (tree == null) {
			tree = getTreeFromDatabase();
			iwc.setApplicationAttribute(PageTreeNode.PAGE_TREE, tree);
			Map names = getNamesFromDatabase();
			iwc.setApplicationAttribute(PageTreeNode.NAME_TREE, names);
		}

		return tree;
	}

	/**
	 * Clears the tree from cache
	 * @param iwc
	 */
	public static void clearTree(IWApplicationContext iwc){
		iwc.removeApplicationAttribute(PageTreeNode.PAGE_TREE);
		iwc.removeApplicationAttribute(PageTreeNode.NAME_TREE);
	}
	
	/**
	 *
	 */
	public boolean equals(Object obj) {
		if (obj instanceof PageTreeNode) {
			PageTreeNode node = (PageTreeNode) obj;
			if (node._id == this._id) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	protected IWApplicationContext getIWApplicationContext(){
		if(this._iwac==null){
			try{
				//Workaround solution if iwac is not set normally
				this._iwac = IWContext.getInstance().getApplicationContext();
			}
			catch(Exception e){
				System.err.println("PageTreeNode.getIWApplicationContext() : Tried to get IWApplicationContext from runtime but failed : "+e.getMessage());
			}
		}
		return this._iwac;
	}
	/**
	 * @return
	 */
	public boolean isCategory() {
		return this._isCategory;
	}

}
