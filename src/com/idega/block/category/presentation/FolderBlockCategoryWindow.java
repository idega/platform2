package com.idega.block.category.presentation;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.category.business.FolderBlockBusiness;
import com.idega.block.category.data.ICInformationCategory;
import com.idega.block.category.data.ICInformationCategoryTranslation;
import com.idega.business.IBOLookup;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.component.data.ICObjectInstanceHome;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
	*@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

//TODO extend FolderBlockComponent and change name to FolderBlockCategoryEditor

public class FolderBlockCategoryWindow extends IWAdminWindow {
	private int iCategoryId = -1;
	protected int iObjectInstanceId = -1;
	protected int iObjectId = -1;
	protected int iWorkingFolder = -1;
	protected ICObjectInstance objectInstance;
	protected String sType = "no_type";
	protected String sCacheKey = null;
	protected boolean multi = false;
	protected boolean allowOrdering = false;
	public static final String prmCategoryId = "icinfcat_categoryid";
	public static final String prmObjInstId = "icinfcat_obinstid";
	public static final String prmObjId = "icinfcat_objid";
	public static final String prmWorkingFolder = "icinfcat_workf";
	public final static String prmCategoryType = "icinfcat_type";
	public final static String prmMulti = "icinfcat_multi";
	public final static String prmOrder = "icinfcat_order";
	public static final String prmCacheClearKey = "icinfcat_cache_clear";
	public static final String prmParentID = "icinfcat_parent";
	public final static String prmLocale = "icinfcat_localedrp";
	protected static final String actDelete = "icinfcat_del";
	protected static final String actSave = "icinfcat_save";
	protected static final String actClose = "icinfcat_close";
	protected static final String actForm = "icinfcat_form";
	protected Image tree_image_M, tree_image_L, tree_image_T;
	protected IWResourceBundle iwrb;
	protected IWBundle iwb, core;
	private int iObjInsId = -1;
	private int iUserId = -1;
	protected boolean formAdded = false;
	protected int row = 1;
	protected FolderBlockBusiness _folderblockBusiness = null;
	protected int iLocaleId = -1;

	public FolderBlockCategoryWindow() {
		setWidth(700);
		setHeight(400);
		setResizable(true);
		setUnMerged();
		setScrollbar(true);
	}

//	protected void clearCache(IWContext iwc) {
//		if (getCacheKey(iwc) != null) {
//			if (iwc.getApplication().getIWCacheManager().isCacheValid(getCacheKey(iwc))) {
//				iwc.getApplication().getIWCacheManager().invalidateCache(getCacheKey(iwc));
//			}
//		}
//	}
//	protected String getCacheKey(IWContext iwc) {
//		if (sCacheKey == null) {
//			sCacheKey = iwc.getParameter(prmCacheClearKey);
//		}
//		return sCacheKey;
//	}
//	protected void maintainClearCacheKeyInForm(IWContext iwc) {
//		if (getCacheKey(iwc) != null) {
//			this.addHiddenInput(new HiddenInput(prmCacheClearKey, getCacheKey(iwc)));
//		} else {
//		}
//	}
	protected void control(IWContext iwc) throws Exception {

		if (iwc.isParameterSet(prmLocale)) {
			this.iLocaleId = Integer.parseInt(iwc.getParameter(prmLocale));
		} else {
			this.iLocaleId = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());
		}

		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		if (this.iCategoryId <= 0 && iwc.isParameterSet(prmCategoryId)) {
			this.iCategoryId = Integer.parseInt(iwc.getParameter(prmCategoryId));
		}
		if (this.iObjectInstanceId <= 0 && iwc.isParameterSet(prmObjInstId)) {
			this.iObjectInstanceId = Integer.parseInt(iwc.getParameter(prmObjInstId));
			this.objectInstance = ((ICObjectInstanceHome)IDOLookup.getHome(ICObjectInstance.class)).findByPrimaryKey(this.iObjectInstanceId);
		}
		if (this.iObjectId <= 0 && iwc.isParameterSet(prmObjId)) {
			this.iObjectId = Integer.parseInt(iwc.getParameter(prmObjId));
		}

		if (this.iWorkingFolder <= 0 && iwc.isParameterSet(prmWorkingFolder)) {
			this.iWorkingFolder = Integer.parseInt(iwc.getParameter(prmWorkingFolder));
		}

		if (iwc.isParameterSet(prmCategoryType)) {
			this.sType = iwc.getParameter(prmCategoryType);
		}
//		clearCache(iwc);
		this.multi = iwc.isParameterSet(prmMulti);
		this.allowOrdering = iwc.isParameterSet(prmOrder);
		/**
		 * @todo We need some authication here ,
		 *  permissions from underlying window ???
		 */
			if (iwc.isParameterSet(actForm)) {
				processCategoryForm(iwc);
			}
			//addCategoryFields(CategoryFinder.getCategory(iCategoryId));
			getCategoryFields(iwc, this.iCategoryId);
	}
	protected void processCategoryForm(IWContext iwc) throws RemoteException {
		// saving :
		if (iwc.isParameterSet(actSave) || iwc.isParameterSet(actSave + ".x")) {
			String sName = iwc.getParameter("name");
			String sDesc = iwc.getParameter("info");
			String sOrder = iwc.getParameter("order");
			int parent = iwc.isParameterSet(prmParentID) ? Integer.parseInt(iwc.getParameter(prmParentID)) : -1;
			if (sOrder == null || sOrder.equals("")) {
				sOrder = "0";
			}
			String sType = iwc.getParameter(prmCategoryType);
			if (sName != null && sType != null) {
				if (this.iCategoryId <= 0 && sName.length() > 0) {
					try {
						ICInformationCategory newInfoCat = this._folderblockBusiness.createICInformationCategory(iwc, this.iLocaleId, sName, sDesc, sType, this.iObjectId, -1);
						this.iCategoryId = newInfoCat.getID();
						this._folderblockBusiness.createICInformationCategoryTranslation(this.iCategoryId, sName, sDesc, this.iLocaleId);

						if (parent > 0 && this.iCategoryId > 0) {
							this._folderblockBusiness.storeCategoryToParent(this.iCategoryId, parent);
						}
						postSave(iwc, this.iCategoryId);
					} catch (java.rmi.RemoteException ex) {
						ex.printStackTrace();
					}
				} else {
					String[] sids = iwc.getParameterValues("id_box");
					int[] savedids = new int[0];
					if (sids != null) {
						savedids = new int[sids.length];
					}
					for (int i = 0; i < savedids.length; i++) {
						savedids[i] = Integer.parseInt(sids[i]);
						//  System.err.println("save id "+savedids[i]);
					}

					if (this.iCategoryId > 0) {
						this._folderblockBusiness.updateCategory(iwc, this.iCategoryId, sName, sDesc, this.iLocaleId);
					}

					this._folderblockBusiness.storeInstanceCategories(this.iObjectInstanceId, savedids);
					postSave(iwc, this.iCategoryId);
				}
			}
		}
		if (iwc.isParameterSet(actClose) || iwc.isParameterSet(actClose + ".x")) {
			setParentToReload();
			close();
		}
		// deleting :
		else if (iwc.isParameterSet(actDelete) || iwc.isParameterSet(actDelete + ".x")) {
			try {
				this._folderblockBusiness.removeCategory(iwc, this.iCategoryId);
				System.out.println(this.getClass().getName() + ": should delete category");
				this.iCategoryId = -1;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	protected void postSave(IWContext iwc, int iCategoryId) throws RemoteException {

	}

	protected void getCategoryFields(IWContext iwc, int iCategoryId) throws RemoteException {
		int parent = iwc.isParameterSet(prmParentID) ? Integer.parseInt(iwc.getParameter(prmParentID)) : -1;

		int objID = iwc.isParameterSet(prmObjId) ? Integer.parseInt(iwc.getParameter(prmObjId)) : -1;
		int workFolderID = iwc.isParameterSet(prmWorkingFolder) ? Integer.parseInt(iwc.getParameter(prmWorkingFolder)) : -1;

		Link newLink = new Link(this.core.getImage("/shared/create.gif"));
		newLink.addParameter(prmCategoryId, -1);
		newLink.addParameter(prmObjInstId, this.iObjectInstanceId);
		newLink.addParameter(FolderBlockCategoryWindow.prmObjId, this.iObjectId);
		newLink.addParameter(FolderBlockCategoryWindow.prmWorkingFolder, this.iWorkingFolder);

		newLink.addParameter(actForm, "true");

		Collection L = null;
		try {
			/** @todo  permission handling */
			L = this._folderblockBusiness.getAvailableTopNodeCategories(objID, workFolderID);
		} catch (Exception ex) {

		}
		if (L != null) { // Gimmi 17.08.2002
			/** @todo laga comparatorinn */
			//Collections.sort(L, new CategoryComparator());
		}
		Collection coll = this._folderblockBusiness.collectCategoryIntegerIds(this.iObjectInstanceId);
		int chosenId = iCategoryId;

		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		this.row = 1;
		DropdownMenu LocaleDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(prmLocale);
		LocaleDrop.setToSubmit();
		LocaleDrop.setSelectedElement(Integer.toString(this.iLocaleId));
		T.add(LocaleDrop, 1, this.row);
		T.mergeCells(1, this.row, 3, this.row);
		this.row++;
		T.add(Text.getBreak(), 1, this.row);
		T.add(formatText(this.iwrb.getLocalizedString("use", "Use")), 1, this.row);
		T.add(formatText(this.iwrb.getLocalizedString("name", "Name")), 2, this.row);
		T.add(formatText(this.iwrb.getLocalizedString("info", "Info")), 3, this.row);
		if (this.allowOrdering) {
			T.add(formatText("  " + this.iwrb.getLocalizedString("order", "Order")), 4, this.row);
		}
		T.add(formatText("  " + this.iwrb.getLocalizedString("add_child", "Add child") + "  "), 5, this.row);
		T.add(formatText("  " + this.iwrb.getLocalizedString("delete", "Delete") + "  "), 6, this.row);
		this.row++;
		TextInput name = new TextInput("name");
		TextInput info = new TextInput("info");
		TextInput order = new TextInput("order");
		order.setSize(3);
		setStyle(name);
		setStyle(info);
		setStyle(order);
		this.formAdded = false;
		if (L != null) {
			fillTable(L.iterator(), T, chosenId, coll, name, info, order, 0);
		}
		if (!this.formAdded) {
			T.add(Text.getBreak(), 1, this.row++);
			T.mergeCells(2, this.row, 6, this.row);
			if (parent > 0) {
				ICInformationCategory cat = this._folderblockBusiness.getCategory(parent);
				T.add(formatText(this.iwrb.getLocalizedString("create_child_category_under", "Create child under") + " " + cat.getName()), 2, this.row);
				;

			} else {
				T.add(formatText(this.iwrb.getLocalizedString("create_root_category", "Create new root category")), 2, this.row);
			}
			this.row++;
			T.add(name, 2, this.row);
			T.add(info, 3, this.row);
		} else {
			Link li = new Link(this.iwrb.getLocalizedImageButton("new", "New"));
			addParametersToLink(li);
			T.add(Text.getBreak(), 2, this.row);
			T.add(li, 2, this.row);
		}
		addLeft(this.iwrb.getLocalizedString("categories", "Categories"), T, true, false);
		addBreak();

		SubmitButton save = new SubmitButton(this.iwrb.getLocalizedImageButton("save", "Save"), actSave);
		SubmitButton close = new SubmitButton(this.iwrb.getLocalizedImageButton("close", "Close"), actClose);
		addSubmitButton(save);
		addSubmitButton(close);
		addHiddenInput(new HiddenInput(prmCategoryType, this.sType));
		addHiddenInput(new HiddenInput(prmObjInstId, String.valueOf(this.iObjectInstanceId)));
		addHiddenInput(new HiddenInput(prmParentID, String.valueOf(parent)));
		addHiddenInput(new HiddenInput(FolderBlockCategoryWindow.prmObjId, String.valueOf(this.iObjectId)));
		addHiddenInput(new HiddenInput(FolderBlockCategoryWindow.prmWorkingFolder, String.valueOf(this.iWorkingFolder)));

		addHiddenInput(new HiddenInput(actForm, "true"));
		if (this.allowOrdering) {
			addHiddenInput(new HiddenInput(prmOrder, "true"));
		}
		if (this.multi) {
			addHiddenInput(new HiddenInput(prmMulti, "true"));
		}
//		this.maintainClearCacheKeyInForm(iwc);

		T.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
		T.setAlignment(4, 1, Table.HORIZONTAL_ALIGN_LEFT);
		T.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
		T.setAlignment(5, 1, Table.HORIZONTAL_ALIGN_LEFT);
		T.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
		T.setAlignment(6, 1, Table.HORIZONTAL_ALIGN_LEFT);

	}

	protected void fillTable(Iterator iter, Table T, int chosenId, Collection coll, TextInput name, TextInput info, TextInput order, int level) throws RemoteException {
		if (iter != null) {

			ICInformationCategory cat;
			ICInformationCategoryTranslation trans = null;
			String catName, catInfo;
			CheckBox box;
			RadioButton rad;
			Link deleteLink;
			int id;
			int iOrder = 0;
			while (iter.hasNext()) {
				cat = (ICInformationCategory)iter.next();
				id = ((Integer)cat.getPrimaryKey()).intValue();
				try {
					trans = this._folderblockBusiness.getCategoryTranslationHome().findByCategoryAndLocale(id, this.iLocaleId);
					catName = trans.getName();
					catInfo = trans.getDescription();
				} catch (FinderException ex) {
					catName = cat.getName();
					catInfo = cat.getDescription();
				}

				if (this.allowOrdering) {
					try {
						//TEMP iOrder = CategoryFinder.getInstance().getCategoryOrderNumber(cat, this.objectInstance);
						iOrder = 0;
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
				if (level > 0) {
					for (int i = 0; i < level; i++) {
						T.add(this.tree_image_T, 2, this.row);
					}
					if (iter.hasNext()) {
						T.add(this.tree_image_M, 2, this.row);
					}
					else {
						T.add(this.tree_image_L, 2, this.row);
					}
				}
				if (id == chosenId) {

					name.setContent(catName);
					if (catInfo != null) {
						info.setContent(catInfo);
					}
					T.add(name, 2, this.row);
					T.add(info, 3, this.row);
					if (this.allowOrdering) {
						T.add(order, 4, this.row);
						order.setContent(Integer.toString(iOrder));
					}
					T.add(new HiddenInput(prmCategoryId, String.valueOf(id)));
					this.formAdded = true;
				} else {
					Link Li = new Link(formatText(catName));
					Li.addParameter(prmCategoryId, id);
					Li.addParameter("edit", "true");
					T.add(Li, 2, this.row);
					T.add(formatText(catInfo), 3, this.row);
					Link childLink = new Link(this.core.getImage("/shared/create.gif"));
					childLink.addParameter(prmParentID, id);
					deleteLink = new Link(this.core.getImage("/shared/delete.gif"));
					deleteLink.addParameter(actDelete, "true");
					deleteLink.addParameter(prmCategoryId, id);
					deleteLink.addParameter(actForm, "true");
					addParametersToLink(childLink);
					addParametersToLink(deleteLink);
					addParametersToLink(Li);
					if (this.allowOrdering) {
						T.add(formatText(Integer.toString(iOrder)), 4, this.row);
					}
					T.add(childLink, 5, this.row);
					T.add(deleteLink, 6, this.row);

				}
				if (this.multi) {
					box = new CheckBox("id_box", String.valueOf(cat.getID()));
					box.setChecked(coll != null && coll.contains(new Integer(cat.getID())));
					//setStyle(box);
					T.add(box, 1, this.row);
				} else {
					rad = new RadioButton("id_box", String.valueOf(cat.getID()));
					if (coll != null && coll.contains(new Integer(cat.getID()))) {
						rad.setSelected();
					}
					//setStyle(rad);
					T.add(rad, 1, this.row);
				}
				this.row++;
				if (cat.getChildCount() > 0) {
					fillTable(cat.getChildrenIterator(), T, chosenId, coll, name, info, order, level + 1);
				}
			}
			trans = null;
		}
	}

	protected void addParametersToLink(Link L) {
		if (this.sCacheKey != null) {
			L.addParameter(FolderBlockCategoryWindow.prmCacheClearKey, this.sCacheKey);
		}
		if (this.allowOrdering) {
			L.addParameter(prmOrder, "true");
		}
		if (this.multi) {
			L.addParameter(prmMulti, "true");
		}
		L.addParameter(prmCategoryType, this.sType);
		L.addParameter(prmObjInstId, String.valueOf(this.iObjectInstanceId));
		L.addParameter(prmLocale, String.valueOf(this.iLocaleId));
		L.addParameter(FolderBlockCategoryWindow.prmObjId, this.iObjectId);
		L.addParameter(FolderBlockCategoryWindow.prmWorkingFolder, this.iWorkingFolder);

	}
	/**
	 * @deprecated
	 */
	public static Link getWindowLink(int iCategoryId, int iInstanceId, String type, boolean multible) {
		return getWindowLink(iCategoryId, iInstanceId, type, multible, false);
	}

	public static Link getWindowLink(int iCategoryId, int iInstanceId, String type, boolean multible, boolean allowOrdering) {
		return getWindowLink(iCategoryId, iInstanceId, type, multible, allowOrdering, null);
	}
	public static Link getWindowLink(int iCategoryId, int iInstanceId, String type, boolean multible, boolean allowOrdering, String cacheKey) {
		Link L = new Link();
		L.addParameter(FolderBlockCategoryWindow.prmCategoryId, iCategoryId);
		L.addParameter(FolderBlockCategoryWindow.prmObjInstId, iInstanceId);
		L.addParameter(FolderBlockCategoryWindow.prmCategoryType, type);
		if (multible) {
			L.addParameter(FolderBlockCategoryWindow.prmMulti, "true");
		}
		if (allowOrdering) {
			L.addParameter(FolderBlockCategoryWindow.prmOrder, "true");
		}
		if (cacheKey != null) {
			L.addParameter(prmCacheClearKey, cacheKey);
		}
		L.setWindowToOpen(FolderBlockCategoryWindow.class);
		return L;
	}
	public PresentationObject getNameInput(ICInformationCategory node) {
		TextInput name = new TextInput("name");
		if (node != null) {
			name.setContent(node.getName());
		}
		return name;
	}
	public void main(IWContext iwc) throws Exception {
		this.iwb = getBundle(iwc);
		this.iwrb = getResourceBundle(iwc);

		this.core = iwc.getIWMainApplication().getCoreBundle();
		this._folderblockBusiness = (FolderBlockBusiness)IBOLookup.getServiceInstance(iwc, FolderBlockBusiness.class);

		String title = this.iwrb.getLocalizedString("ic_category_editor", "Category Editor");
		this.tree_image_M = this.core.getImage("/treeviewer/ui/win/treeviewer_M_line.gif");
		this.tree_image_L = this.core.getImage("/treeviewer/ui/win/treeviewer_L_line.gif");
		this.tree_image_T = this.core.getImage("treeviewer/ui/win/treeviewer_trancparent.gif");
		setTitle(title);
		addTitle(title);
		control(iwc);
	}
}
