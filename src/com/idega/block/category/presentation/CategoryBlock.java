package com.idega.block.category.presentation;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.idega.block.category.business.CategoryBusiness;
import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.business.CategoryService;
import com.idega.block.category.data.ICCategory;
import com.idega.block.category.data.ICCategoryHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.builder.business.ICDynamicPageTriggerCopySession;
import com.idega.core.builder.business.ICDynamicPageTriggerInheritable;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWProperty;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 2.0
 */
public abstract class CategoryBlock extends Block implements ICDynamicPageTriggerInheritable {
	private ICCategory icCategory;
	private int icCategoryId = -1;
	private int[] icCategoryIds = new int[0];
	public final static String prmCategoryId = "catbl_catid";
	private boolean autocreate = true;
	protected boolean invalidateBlockCache = true;
	protected boolean orderManually = false;
	protected final static String METADATAKEY_CATEGORY_MAIN_VIEWER_PAGE = "category_main_viewer_page";
	
	
	public CategoryBlock(){
		IWContext iwc = IWContext.getInstance();
		if(iwc!=null){
			IWProperty property = iwc.getApplicationSettings().getIWProperty("autocreate_categories");
			if(property != null){
				try {
					this.autocreate = Boolean.getBoolean(property.getValue());
				}
				catch (RuntimeException e) {
					//Do nothing, keep old value;
				}
			}
		}
	}
	
	/**
	 *  Returns the first Category bound to this instance
	 */
	public int getCategoryId() {
		if (this.icCategoryId == -1 && this.icCategoryIds.length > 0) {
			this.icCategoryId = this.icCategoryIds[0];
		}
		return this.icCategoryId;
	}
	/**
	 *  Returns an array of Category ids from
	 */
	public int[] getCategoryIds() {
		return this.icCategoryIds;
	}
	/**
	 *  Sets the first categoryId
	 */
	public void setCategoryId(int iCategoryId) {
		this.icCategoryId = iCategoryId;
	}
	/**
	 * Sets the Category ids bound to this instance
	 */
	public void setCategoryIds(int[] iCategoryIds) {
		this.icCategoryIds = iCategoryIds;
	}
	/**
	 *  Turns Category autocreation on/off
	 */
	public void setAutoCreate(boolean autocreate) {
		this.autocreate = autocreate;
	}
	/**
	 * Turns Manual ordering fidus on/off
	 */
	public void setOrderManually(boolean orderManually) {
		this.orderManually = orderManually;
	}

	public boolean getOrderManually() {
		return this.orderManually;
	}
	/**
	 * Turns Manual ordering fidus on/off
	 */
	public void setInvalidateCache(boolean invalidateBlockCache) {
		this.invalidateBlockCache = invalidateBlockCache;
	}
	/**
	 *  Returns a collection of ICCategory objects bound to this instance
	 *  specified by default type
	 */
	public Collection getCategories(String type) {
		return CategoryFinder.getInstance().getCategories(this.icCategoryIds, type);
	}
	/**
	 *  Returns a collection of ICCategory objects bound to this instance
	 *  @returns Collection
	 */
	public Collection getCategories() {
		return CategoryFinder.getInstance().listOfCategoryForObjectInstanceId(getICObjectInstanceID(), this.orderManually);
		//      return CategoryFinder.getInstance().listOfCategoryForObjectInstanceId(getICObjectInstanceID());
	}

    /**
     *  Returns a collection of ICCategory objects bound to this instance, returns only root categories
     *  @returns Collection
     */
    public Collection getRootCategories() {
        return CategoryFinder.getInstance().listOfRootCategoryForObjectInstanceId(getICObjectInstanceID(), this.orderManually);
        //      return CategoryFinder.getInstance().listOfCategoryForObjectInstanceId(getICObjectInstanceID());
    }
    
    
	protected void initCategory(IWContext iwc) {
		//if (icCategoryId <= 0) {
			if (iwc.isParameterSet(prmCategoryId)) {
				this.icCategoryId = Integer.parseInt(iwc.getParameter(prmCategoryId));
				this.icCategory = CategoryFinder.getInstance().getCategory(this.icCategoryId);
				//System.err.println("getting category from parameter:"+prmCategoryId+" cat: "+icCategory+" "+this.getClassName());
			}
			else if (getICObjectInstanceID() > 0) {
				this.icCategoryIds = CategoryFinder.getInstance().getObjectInstanceCategoryIds(getICObjectInstanceID(), this.autocreate, getCategoryType());
				//System.err.println("getting category from instance: "+getICObjectInstanceID()+" cat: "+icCategory+" "+this.getClassName());
				//icCategoryId = CategoryFinder.getObjectInstanceCategoryId(getICObjectInstanceID(),autocreate,getCategoryType());
			}
		//}
	}
	public void initializeInMain(IWContext iwc) {
		initCategory(iwc);
	}
	public synchronized Object clone() {
		CategoryBlock obj = null;
		try {
			obj = (CategoryBlock) super.clone();
			obj.icCategory = this.icCategory;
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}
	/**
	 *  Returns a Link to CategoryWindow with specified type
	 *  for this intance
	 */
	public Link getCategoryLink(String type) {
		Link L = new Link();
		L.addParameter(CategoryWindow.prmCategoryId, getCategoryId());
		L.addParameter(CategoryWindow.prmObjInstId, getICObjectInstanceID());
		L.addParameter(CategoryWindow.prmCategoryType, type);
		L.addParameter(CategoryWindow.prmBundleIdentifier, getBundleIdentifier());
		if (getMultible()) {
			L.addParameter(CategoryWindow.prmMulti, "true");
		}
		if (this.orderManually) {
			L.addParameter(CategoryWindow.prmOrder, "true");
		}
		if (this.invalidateBlockCache && !getCacheKey().equals(IW_BLOCK_CACHE_KEY)) {
			//      L.addParameter(CategoryWindow.prmCategoryId,getCacheKey());
			//      L.addParameter(CategoryWindow.prmCacheClearKey ,getCacheKey());
			L.addParameter(CategoryWindow.prmCacheClearKey, super.getDerivedCacheKey());
		}
		L.setWindowToOpen(CategoryWindow.class);
		return L;
	}
	/**
	 *  returns a Link to the CategoryWindow for this instance
	 */
	public Link getCategoryLink() {
		return getCategoryLink(getCategoryType());
	}
	/**
	 *  Defines the type of categories this block handles
	 */
	public abstract String getCategoryType();
	/**
	 *  Defines if multiple categories can bound to this instance
	 */
	public abstract boolean getMultible();
	/**
	 * Defines if ordering is allowed
	 public abstract boolean getAllowOrdering();
	*/
	/**
	 *  Removes all categories bound to this instance
	 */
	public final boolean removeInstanceCategories() {
		return CategoryBusiness.getInstance().removeInstanceCategories(this.getICObjectInstanceID());
	}
	/**
	 *  Deletes this instance
	 */
	public boolean deleteBlock(int iObjectInstanceId) {
		return CategoryBusiness.getInstance().removeInstanceCategories(iObjectInstanceId);
	}
	public ICCategoryHome getCategoryHome() throws RemoteException {
		return (ICCategoryHome) IDOLookup.getHome(ICCategory.class);
	}
	
	public boolean copyICObjectInstance(String pageKey,int newInstanceID, ICDynamicPageTriggerCopySession copySession) {
		CategoryFinder finder = CategoryFinder.getInstance();
		List categories = finder.listOfCategoryForObjectInstanceId(getICObjectInstanceID());
		if(categories != null) {
			try {
				CategoryBusiness cb = CategoryBusiness.getInstance();
				CategoryService service = (CategoryService) IBOLookup.getServiceInstance(copySession.getIWApplicationContext(),CategoryService.class);
				int[] catIDs = new int[categories.size()];
				int catIDIndex = 0;
				for (Iterator iter = categories.iterator(); iter.hasNext();) {
					try {
						ICCategory category = (ICCategory) iter.next();
						ICCategory newCategory = (ICCategory)copySession.getNewValue(CategoryBlock.class,category);
						if(newCategory==null) {
							newCategory = cb.createCategory(newInstanceID,getCategoryType(),category.getName(),category.getDescription());
							copySession.setNewValue(CategoryBlock.class,category,newCategory);
							service.storeCategoryToParent(newCategory.getID(),category.getID());
							catIDs[catIDIndex++] = newCategory.getID();
							if(copySession.hasRootPage()) {
								newCategory.addMetaData(METADATAKEY_CATEGORY_MAIN_VIEWER_PAGE,pageKey);
								newCategory.store();
							}
						}else {
							catIDs[catIDIndex++] = newCategory.getID();
							service.storeCategoryToParent(newCategory.getID(),category.getID());
						}
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				cb.saveRelatedCategories(newInstanceID,catIDs);
			} catch (IBOLookupException e) {
				e.printStackTrace();
				return false;
			} catch (RemoteException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
}
