package com.idega.block.category.presentation;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.business.CategoryService;
import com.idega.block.category.data.Category;
import com.idega.block.category.data.ICCategory;
import com.idega.block.category.data.ICCategoryTranslation;
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
public class CategoryWindow extends IWAdminWindow {
	private int iCategoryId = -1;
	protected int iObjectInstanceId = -1;
	protected ICObjectInstance objectInstance;
	protected String sType = "no_type";
	protected String sCacheKey = null;
	protected boolean multi = false;
	protected boolean allowOrdering = false;
	public static final String prmCategoryId = "iccat_categoryid";
	public static final String prmObjInstId = "iccat_obinstid";
	public final static String prmCategoryType = "iccat_type";
	public final static String prmMulti = "iccat_multi";
	public final static String prmOrder = "iccat_order";
	public static final String prmCacheClearKey = "iccat_cache_clear";
	public static final String prmParentID = "iccat_parent";
	public final static String prmLocale = "iccat_localedrp";
	public static final String prmBundleIdentifier = "iccat_bundleIdent";
	protected static final String actDelete = "iccat_del";
	protected static final String actSave = "iccat_save";
	protected static final String actClose = "iccat_close";
	protected static final String actForm = "iccat_form";
	protected Image tree_image_M,tree_image_L,tree_image_T;
	protected IWResourceBundle iwrb;
	protected IWBundle iwb, core;
	private int iObjInsId = -1;
	private int iUserId = -1;
	protected boolean formAdded = false;
	protected int row = 1;
	protected CategoryService catServ = null;
	protected int iLocaleId = -1;
	int iSaveLocaleId = -1;
	private String bundleIdentifier = null;
	
	public CategoryWindow() {
		setWidth(600);
		setHeight(400);
		setResizable(true);
		setUnMerged();
		setScrollbar(true);
	}
	protected void clearCache(IWContext iwc) {
		if (getCacheKey(iwc) != null) {
			if (iwc.getIWMainApplication().getIWCacheManager().isCacheValid(getCacheKey(iwc))) {
				iwc.getIWMainApplication().getIWCacheManager().invalidateCache(getCacheKey(iwc));
			}
		}
	}
	protected String getCacheKey(IWContext iwc) {
		if (sCacheKey == null) {
			sCacheKey = iwc.getParameter(prmCacheClearKey);
		}
		return sCacheKey;
	}
	protected void maintainClearCacheKeyInForm(IWContext iwc) {
		if (getCacheKey(iwc) != null) {
			this.addHiddenInput(new HiddenInput(prmCacheClearKey, getCacheKey(iwc)));
		}
		else {
		}
	}
	protected void control(IWContext iwc) throws Exception {
	
	    if(iwc.isParameterSet(prmLocale)){
	      iLocaleId = Integer.parseInt(iwc.getParameter(prmLocale));
	    }
	    else{
	      iLocaleId = ICLocaleBusiness.getLocaleId( iwc.getCurrentLocale());
	    }
	    
	    iSaveLocaleId = ICLocaleBusiness.getLocaleId(iwc.getApplicationSettings().getDefaultLocale());
	    if(iSaveLocaleId == iLocaleId)
	     	iSaveLocaleId = -1;
	    else
	    	iSaveLocaleId = iLocaleId;
	    
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		if (iCategoryId <= 0 && iwc.isParameterSet(prmCategoryId)) {
			iCategoryId = Integer.parseInt(iwc.getParameter(prmCategoryId));
		}
		if (iObjectInstanceId <= 0 && iwc.isParameterSet(prmObjInstId)) {
			iObjectInstanceId = Integer.parseInt(iwc.getParameter(prmObjInstId));
			objectInstance =
				((ICObjectInstanceHome) IDOLookup.getHome(ICObjectInstance.class)).findByPrimaryKey(iObjectInstanceId);
		}
		if (iwc.isParameterSet(prmCategoryType)) {
			sType = iwc.getParameter(prmCategoryType);
		}
		clearCache(iwc);
		multi = iwc.isParameterSet(prmMulti);
		allowOrdering = iwc.isParameterSet(prmOrder);
		/**
		 * @todo We need some authication here ,
		 *  permissions from underlying window ???
		 */
		if (true) {
			if (iwc.isParameterSet(actForm)) {
				processCategoryForm(iwc);
			}
			//addCategoryFields(CategoryFinder.getCategory(iCategoryId));
			getCategoryFields(iwc, iCategoryId );
		}
		else {
			add(formatText(iwrb.getLocalizedString("access_denied", "Access denied")));
		}
	}
	protected void processCategoryForm(IWContext iwc)throws RemoteException{
		// saving :
		if (iwc.isParameterSet(actSave) || iwc.isParameterSet(actSave + ".x")) {
			String sName = iwc.getParameter("name");
			String sDesc = iwc.getParameter("info");
			String sOrder = iwc.getParameter("order");
			int parent = iwc.isParameterSet(prmParentID)?Integer.parseInt(iwc.getParameter(prmParentID)):-1;
			if (sOrder == null || sOrder.equals("")) {
				sOrder = "0";
			}
			String sType = iwc.getParameter(prmCategoryType);
			if (sName != null && sType != null) {
				if (iCategoryId <= 0 && sName.length() > 0) {
					try{
						iCategoryId =catServ.storeCategory(	iCategoryId,sName,	sDesc,Integer.parseInt(sOrder),	iObjectInstanceId,sType,	multi).getID();
						catServ.storeCategoryTranslation(iCategoryId,sName,sDesc,iSaveLocaleId);
						
						if(parent>0 && iCategoryId >0)
							catServ.storeCategoryToParent(iCategoryId,parent);

						postSave(iwc, iCategoryId);
					}
					catch(java.rmi.RemoteException ex){
						ex.printStackTrace();
					}
				}
				else {
					String[] sids = iwc.getParameterValues("id_box");
					int[] savedids = new int[0];
					if (sids != null)
						savedids = new int[sids.length];
					for (int i = 0; i < savedids.length; i++) {
						savedids[i] = Integer.parseInt(sids[i]);
						//  System.err.println("save id "+savedids[i]);
					}
					if (iCategoryId > 0)
						catServ.updateCategory(iCategoryId,	sName,sDesc,	Integer.parseInt(sOrder),iObjectInstanceId,iSaveLocaleId);
						catServ.storeRelatedCategories(iObjectInstanceId, savedids);
						postSave(iwc, iCategoryId);
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
				catServ.removeCategory(iCategoryId);
				iCategoryId = -1;
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	protected void postSave(IWContext iwc, int iCategoryId) throws RemoteException{
		
	}

	protected void getCategoryFields(IWContext iwc, int iCategoryId) throws RemoteException{
		int parent = iwc.isParameterSet(prmParentID)?Integer.parseInt(iwc.getParameter(prmParentID)):-1;
		
		Link newLink = new Link(core.getImage("/shared/create.gif"));
		newLink.addParameter(prmCategoryId, -1);
		newLink.addParameter(prmObjInstId, iObjectInstanceId);
		newLink.addParameter(actForm, "true");
		/** @todo  permission handling */
		//List L = CategoryFinder.getInstance().listOfCategories(sType);
		Collection L = null;
		try{
			L = catServ.getCategoryHome().findRootsByType(sType);
		}
		catch(Exception ex) {}
		if (L != null) { // Gimmi 17.08.2002
			/** @todo laga comparatorinn */
			//Collections.sort(L, new CategoryComparator());
		}
		Collection coll = CategoryFinder.getInstance().collectCategoryIntegerIds(iObjectInstanceId);
		int chosenId = iCategoryId;
		
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		row = 1;
		DropdownMenu LocaleDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(prmLocale);
	    LocaleDrop.setToSubmit();
	    LocaleDrop.setSelectedElement(Integer.toString(iLocaleId));
	    T.add(LocaleDrop,1,row);
	    T.mergeCells(1,row,3,row);
	    row++;
	    T.add(Text.getBreak(),1,row);
		T.add(formatText(iwrb.getLocalizedString("use", "Use")), 1, row);
		T.add(formatText(iwrb.getLocalizedString("name", "Name")), 2, row);
		T.add(formatText(iwrb.getLocalizedString("info", "Info")), 3, row);
		if (allowOrdering) {
			T.add(formatText("  "+iwrb.getLocalizedString("order", "Order")), 4, row);
		}
		T.add(formatText("  "+iwrb.getLocalizedString("add_child", "Add child")+"  "),5,row);
		T.add(formatText("  "+iwrb.getLocalizedString("delete", "Delete")+"  "),6,row);
		T.add(formatText("  "+iwrb.getLocalizedString("metadata", "Metadata")+"  "),7,row);
		row++;
		TextInput name = new TextInput("name");
		TextInput info = new TextInput("info");
		TextInput order = new TextInput("order");
		order.setSize(3);
		setStyle(name);
		setStyle(info);
		setStyle(order);
		formAdded = false;
		if(L!=null)
			fillTable(L.iterator(),T,chosenId,coll,name,info,order,0);
		if (!formAdded) {
			T.add(Text.getBreak(),1,row++);
			T.mergeCells(2,row,6,row);
			if(parent>0){
				ICCategory cat = CategoryFinder.getInstance().getCategory(parent);
				T.add(formatText(iwrb.getLocalizedString("create_child_category_under","Create child under")+" "+cat.getName()),2,row);;
				
			}
			else{
				T.add(formatText(iwrb.getLocalizedString("create_root_category","Create new root category")),2,row);
			}
			row++;
			T.add(name, 2, row);
			T.add(info, 3, row);
		}
		else {
			Link li = new Link(iwrb.getLocalizedImageButton("new", "New"));
			addParametersToLink(li);
			T.add(Text.getBreak(),2,row);
			T.add(li, 2, row);
		}
		addLeft(iwrb.getLocalizedString("categories", "Categories"), T, true, false);
		addBreak();
		
		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), actSave);
		SubmitButton close = new SubmitButton(iwrb.getLocalizedImageButton("close", "Close"), actClose);
		addSubmitButton(save);
		addSubmitButton(close);
		addHiddenInput(new HiddenInput(prmCategoryType, sType));
		addHiddenInput(new HiddenInput(prmObjInstId, String.valueOf(iObjectInstanceId)));
		addHiddenInput(new HiddenInput(prmParentID, String.valueOf(parent) ));
		addHiddenInput(new HiddenInput(actForm, "true"));
		if (allowOrdering) {
			addHiddenInput(new HiddenInput(prmOrder, "true"));
		}
		if(multi){
			addHiddenInput(new HiddenInput(prmMulti, "true"));
		}
		this.maintainClearCacheKeyInForm(iwc);
		
		T.setColumnAlignment(4,T.HORIZONTAL_ALIGN_CENTER);
		T.setAlignment(4,1,T.HORIZONTAL_ALIGN_LEFT);
		T.setColumnAlignment(5,T.HORIZONTAL_ALIGN_CENTER);
		T.setAlignment(5,1,T.HORIZONTAL_ALIGN_LEFT);
		T.setColumnAlignment(6,T.HORIZONTAL_ALIGN_CENTER);
		T.setAlignment(6,1,T.HORIZONTAL_ALIGN_LEFT);
		
	}
	
	protected void fillTable(Iterator iter,Table T,int chosenId,Collection coll,TextInput name,TextInput info,TextInput order,int level)throws RemoteException{
		if (iter != null) {
			
			ICCategory cat;
			ICCategoryTranslation trans = null;
			String catName,catInfo;
			CheckBox box;
			RadioButton rad;
			Link deleteLink;
			Link metadataLink;
			int id;
			int iOrder = 0;
			while (iter.hasNext()) {
				cat = (ICCategory) iter.next();
				id = ((Integer)cat.getPrimaryKey()).intValue();	
				try{
				trans = catServ.getCategoryTranslationHome().findByCategoryAndLocale(id,iLocaleId);
				}catch(FinderException ex){}
				if(trans!=null){
					catName = trans.getName();
					catInfo = trans.getDescription();
				}
				else{
					catName = cat.getName();
					catInfo = cat.getDescription();
				}
				if (allowOrdering) {
					try {
						iOrder = CategoryFinder.getInstance().getCategoryOrderNumber(cat, this.objectInstance);
					}
					catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
				if(level > 0){
					for (int i = 0; i < level; i++) {
						T.add(tree_image_T,2,row);
					}
					if(iter.hasNext())
					T.add(tree_image_M,2,row);
					else
					T.add(tree_image_L,2,row);
				}
				if (id == chosenId ) {
					
					name.setContent(catName);
					if (catInfo != null)
						info.setContent(catInfo);
					T.add(name, 2, row);
					T.add(info, 3, row);
					if (allowOrdering) {
						T.add(order, 4, row);
						order.setContent(Integer.toString(iOrder));
					}
					T.add(new HiddenInput(prmCategoryId, String.valueOf(id)));
					formAdded = true;
				}
				else if (id > 0) {
					Link Li = new Link(formatText(catName));
					Li.addParameter(prmCategoryId, id);
					Li.addParameter("edit","true");
					T.add(Li, 2, row);
					T.add(formatText(catInfo), 3, row);
					addParametersToLink(Li);
				} 
				
				Link childLink = new Link(core.getImage("/shared/create.gif"));
				childLink.addParameter(prmParentID,id);
				deleteLink = new Link(core.getImage("/shared/delete.gif"));
				deleteLink.addParameter(actDelete, "true");
				deleteLink.addParameter(prmCategoryId, id);
				deleteLink.addParameter(actForm, "true");
				metadataLink = new Link(core.getImage("/shared/edit.gif"));
				metadataLink.setWindowToOpen(CategoryMetaDataWindow.class);
				metadataLink.addParameter(CategoryMetaDataWindow.PARAMETER_CATEGORY_ID, id);
				if (bundleIdentifier != null) {
					metadataLink.addParameter(prmBundleIdentifier, bundleIdentifier);
				}
				addParametersToLink(childLink);
				addParametersToLink(deleteLink);
				if (allowOrdering) {
					T.add(formatText(Integer.toString(iOrder)), 4, row);					
				}				
				T.add(childLink,5,row);
				T.add(deleteLink, 6, row);
				T.add(metadataLink, 7, row);
				
				if (multi) {
					box = new CheckBox("id_box", String.valueOf(cat.getID()));
					box.setChecked(coll != null && coll.contains(new Integer(cat.getID())));
					//setStyle(box);
					T.add(box, 1, row);
				}
				else {
					rad = new RadioButton("id_box", String.valueOf(cat.getID()));
					if (coll != null && coll.contains(new Integer(cat.getID())))
						rad.setSelected();
					//setStyle(rad);
					T.add(rad, 1, row);
				}
				row++;
				if(cat.getChildCount()>0)
					fillTable(cat.getChildren(), T,chosenId,coll,name,info,order,level+1);
			}
			trans = null;
		}
	}
	
	
	protected void addParametersToLink(Link L){
		if (this.sCacheKey != null) 
			L.addParameter(this.prmCacheClearKey, this.sCacheKey);
		if (allowOrdering)
			L.addParameter(prmOrder, "true");
		if (multi) 
			L.addParameter(prmMulti, "true");
		L.addParameter(prmCategoryType, sType);
		L.addParameter(prmObjInstId, String.valueOf(iObjectInstanceId));
		L.addParameter(prmLocale,String.valueOf(iLocaleId));

	}
	/**
	 * @deprecated
	 */
	public static Link getWindowLink(int iCategoryId, int iInstanceId, String type, boolean multible) {
		return getWindowLink(iCategoryId, iInstanceId, type, multible, false);
	}
	
	public static Link getWindowLink(
		int iCategoryId,
		int iInstanceId,
		String type,
		boolean multible,
		boolean allowOrdering) {
		return getWindowLink(iCategoryId, iInstanceId, type, multible, allowOrdering, null);
	}
	public static Link getWindowLink(
		int iCategoryId,
		int iInstanceId,
		String type,
		boolean multible,
		boolean allowOrdering,
		String cacheKey) {
		Link L = new Link();
		L.addParameter(CategoryWindow.prmCategoryId, iCategoryId);
		L.addParameter(CategoryWindow.prmObjInstId, iInstanceId);
		L.addParameter(CategoryWindow.prmCategoryType, type);
		if (multible) {
			L.addParameter(CategoryWindow.prmMulti, "true");
		}
		if (allowOrdering) {
			L.addParameter(CategoryWindow.prmOrder, "true");
		}
		if (cacheKey != null) {
			L.addParameter(prmCacheClearKey, cacheKey);
		}
		L.setWindowToOpen(CategoryWindow.class);
		return L;
	}
	public PresentationObject getNameInput(Category node) {
		TextInput name = new TextInput("name");
		if (node != null) {
			name.setContent(node.getName());
		}
		return name;
	}
	public void main(IWContext iwc) throws Exception {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		bundleIdentifier = iwc.getParameter(prmBundleIdentifier);
		core = iwc.getIWMainApplication().getCoreBundle();
		catServ = (CategoryService) IBOLookup.getServiceInstance(iwc,CategoryService.class);
		String title = iwrb.getLocalizedString("ic_category_editor", "Category Editor");
		tree_image_M = core.getImage("/treeviewer/ui/win/treeviewer_M_line.gif");
		tree_image_L = core.getImage("/treeviewer/ui/win/treeviewer_L_line.gif");
		tree_image_T = core.getImage("treeviewer/ui/win/treeviewer_trancparent.gif");
		setTitle(title);
		addTitle(title);
		control(iwc);
	}
}
