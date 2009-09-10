package com.idega.block.blog.presentation;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import com.idega.block.blog.BlogBundle;
import com.idega.block.blog.business.BlogFinder;
import com.idega.block.blog.business.BlogFormatter;
import com.idega.block.blog.business.BlogHelper;
import com.idega.block.blog.business.BlogLayoutHandler;
import com.idega.block.blog.data.BlogEntity;
import com.idega.block.category.business.FolderBlockBusiness;
import com.idega.block.category.data.InformationFolder;
import com.idega.block.category.presentation.FolderBlock;
import com.idega.block.text.business.ContentHelper;
import com.idega.block.text.data.Content;
import com.idega.block.text.data.LocalizedText;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.builder.business.ICDynamicPageTriggerCopySession;
import com.idega.core.builder.business.ICDynamicPageTriggerInheritable;
import com.idega.core.file.data.ICFile;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.idegaweb.block.presentation.ImageWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Parameter;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * Title: Blog 
 * Description: 
 * Copyright: Copyright (c) 2003 
 * Company: idega Software
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @author 2004 - idega team -<br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.5b
 */

public class Blog extends FolderBlock implements Builderaware, ICDynamicPageTriggerInheritable {
	private final static String IW_BUNDLE_IDENTIFIER = BlogBundle.IW_BUNDLE_IDENTIFIER;
	public final static String CACHE_KEY = BlogBundle.CACHE_KEY;
	private boolean hasEdit = false, hasAdd = false, hasInfo = false;
	;
	private int iCategoryId = -1;
	private String attributeName = null;
	private int attributeId = -1;
	private User eUser = null;

	private boolean showBlogCollectionButton = false;
	private int categoryId = 0;

	private Table outerTable = new Table(1, 1);

	private int numberOfLetters = 273;
	private int numberOfHeadlineLetters = -1;
	private int numberOfDisplayedBlog = 5;
	private int numberOfExpandedBlog = 3;
	private int numberOfCollectionBlog = 30;
	private int iSpaceBetween = 1;
	private int iSpaceBetweenBlog = 20;
	private int iSpaceBetweenBlogAndBody = 5;
	private int cellPadding = 0;
	private int cellSpacing = 0;
	private int viewPageId = -1;
	private int textSize = 2;
	private int firstImageWidth = 200;
	private int ImageWidth = 100;
	private int ImageBorder = 1;
	private boolean showBackButton = false;
	private boolean showAll = false;
	private boolean showImages = true;
	private boolean showImagesInOverview = true;
	private boolean showOnlyDates = false;
	private boolean showTime = true;
	private boolean showInfo = true;
	private boolean showUpdatedDate = false;
	private boolean showTimeFirst = false;
	private boolean headlineAsLink = false;
	private boolean showHeadlineImage = false;
	private boolean showMoreButton = true;
	private boolean alignWithHeadline = false;
	private boolean limitNumberOfBlog = false;
	private boolean enableDelete = true;
	private boolean viewBlog = true;
	private boolean newobjinst = false;
	private boolean showBackText = false;
	private boolean showMoreText = false;
	private boolean showCollectionText = true;
	private boolean showTeaserText = true;
	private boolean addImageInfo = true;
	private String outerTableWidth = "100%";
	private String sObjectAlign = "center";
	private String headlineImageURL;
	private String firstTableColor = null;
	private String secondTableColor = null;
	private String dateAlign = "left";
	private Image headlineImage = null;
	private Image backImage = null;
	private Image moreImage = null;
	private Image collectionImage = null;

	private Hashtable objectsBetween = null;
	private Text textProxy = new Text();
	private Text headlineProxy = new Text();
	private Text informationProxy = new Text();
	private Text moreProxy = new Text();
	private Image spacerImage = null;

	private static String prmFromPage = "bgr_from_page";
	private static String prmDelete = "bgr_delete";
	private static String prmEdit = "bgr_edit";
	private static String prmNew = "bgr_new";
	private static String prmMore = "bgr_more";
	private static String prmCollection = "bgr_collection";
	private static String prmObjIns = "bgr_instance_id";

	public static String prmListCategory = "bgr_blogcategoryid";
	public static String prmBlogCategoryId = "bgr_listcategory";

	private static String AddPermisson = "add";
	private static String InfoPermission = "info";

	private IWBundle iwb;
	private IWResourceBundle iwrb;

	public static final int SINGLE_FILE_LAYOUT = BlogLayoutHandler.SINGLE_FILE_LAYOUT;
	public static final int BLOG_SITE_LAYOUT = BlogLayoutHandler.BLOG_SITE_LAYOUT;
	public static final int BLOG_PAPER_LAYOUT = BlogLayoutHandler.BLOG_PAPER_LAYOUT;
	public static final int SINGLE_LINE_LAYOUT = BlogLayoutHandler.SINGLE_LINE_LAYOUT;
	public static final int COLLECTION_LAYOUT = BlogLayoutHandler.COLLECTION_LAYOUT;

	private int iLayout = SINGLE_FILE_LAYOUT;
	private int blogCount = 0;

	private int icCategoryId = -1;
	
	public final static String PRM_MODE = "bg_mode";
	public final static String MODE_EDIT = "e";

	public Blog() {
		//setCacheable(getCacheKey(), 999999999); //cache indefinately
		init();
		this.showAll = true;
	}

	public Blog(int iCategoryId) {
		this();
		this.iCategoryId = iCategoryId;
		this.showAll = false;
	}

	public void registerPermissionKeys() {
		registerPermissionKey(AddPermisson);
		registerPermissionKey(InfoPermission);
	}

	public boolean getMultible() {
		return true;
	}

	public String getCategoryType() {
		return "blog";
	}

	private void init() {
		this.headlineProxy.setBold();
		this.informationProxy.setFontColor("#666666");
		this.textProxy.setFontSize(1);
		this.informationProxy.setFontSize(1);
	}

	private void checkCategories() {

	}

	/** @todo take out when instanceId handler is used */
	private String getInstanceIDString(IWContext iwc) {
		if (this.viewPageId > 0 || iwc.isParameterSet(prmFromPage)) {
			return "";
		}
		else {
			return String.valueOf(getICObjectInstanceID());
		}
	}

	private Parameter getFromPageParameter() {
		return new Parameter(prmFromPage, "true");
	}

	private void checkFromPage(Link link) {
		if (this.viewPageId > 0) {
			link.addParameter(getFromPageParameter());
		}
	}

	private void control(IWContext iwc) throws Exception {

		if (this.moreImage == null) {
			this.moreImage = this.iwrb.getImage("more.gif");
		}
		if (this.backImage == null) {
			this.backImage = this.iwrb.getImage("back.gif");
		}
		if (this.collectionImage == null) {
			this.collectionImage = this.iwrb.getImage("collection.gif");
		}

		Locale locale = iwc.getCurrentLocale();
		String sBlogId = null;
		if (this.viewBlog) {
			sBlogId = iwc.getParameter(prmMore + getInstanceIDString(iwc));
		}
		InformationFolder blogCategory = null;
		String prm = prmListCategory + getInstanceIDString(iwc);
		boolean info = false;
		if (iwc.isParameterSet(prm)) {
			if (iwc.getParameter(prm).equalsIgnoreCase("true")) {
				info = true;
			}
			else {
				info = false;
			}
		}

		if (this.iCategoryId <= 0) {
			String sCategoryId = iwc.getParameter(prmBlogCategoryId);
			if (sCategoryId != null) {
				this.iCategoryId = Integer.parseInt(sCategoryId);
			}
			else {
				//if(getICObjectInstanceID() > 0){
				//		  iCategoryId =
				// BlogFinder.getObjectInstanceCategoryId(getICObjectInstanceID(),true);
				this.iCategoryId = this.getWorkFolder().getID();
				if (this.iCategoryId <= 0) {
					this.newobjinst = true;
				}
			}
		}
		Table T = new Table(1, 1);
		T.setCellpadding(0);
		T.setCellpadding(0);
		T.setWidth("100%");
		if (this.hasEdit || this.hasAdd || this.hasInfo) {
			T.add(getAdminPart(this.iCategoryId, false, this.newobjinst, info, iwc), 1, 1);
		}
		if (this.iCategoryId > 0) {
			blogCategory = this.getWorkFolder(); //CategoryFinder.getInstance().getCategory(iCategoryId);
			if (blogCategory != null) {
				if (sBlogId != null) {
					int id = Integer.parseInt(sBlogId);
					BlogHelper nh = BlogFinder.getBlogHelper(id);
					T.add(getBlogTable(nh, locale, true, false, iwc), 1, 1);
				} else if (info) {
					T.add(getCategoryList(locale, iwc), 1, 1);
				} else {
					String cprm = prmCollection + getInstanceIDString(iwc);
					T.add(publishBlog(iwc, locale, iwc.isParameterSet(cprm)), 1, 1);
				}
			}
		} else {
			T.add(new Text(this.iwrb.getLocalizedString("no_blog_category", "No blog category")));
		}
		super.add(T);
	}

	private PresentationObject getAdminPart(int iWorkFolderId, boolean enableDelete, boolean newObjInst, boolean info, IWContext iwc) {
		Table T = new Table(3, 1);
		T.setCellpadding(2);
		T.setCellspacing(2);
		T.setBorder(0);

		IWBundle core = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		if (iWorkFolderId > 0) {
			if (this.hasEdit || this.hasAdd || this.hasInfo) {				
				Link ne = new Link(core.getImage("/shared/create.gif"));
				ne.addParameter(Blog.PRM_MODE, Blog.MODE_EDIT);
				//ne.setWindowToOpen(BlogEditorWindow.class,"570","620",true,true);
//				ne.addParameter(BlogEditorWindow.prmCategory, iCategoryId);
				ne.addParameter(BlogEditorWindow.prmWorkFolderPK, iWorkFolderId);			
				ne.addParameter(BlogEditorWindow.prmObjInstId, getICObjectInstanceID());
				ne.addParameter(BlogEditorWindow.prmObjId, getICObjectID());
				T.add(ne, 1, 1);
			}
			//T.add(T.getTransparentCell(iwc),1,1);
			if (this.hasEdit || this.hasInfo) {
				Link list = new Link(this.iwb.getImage("/shared/info.gif"));
				checkFromPage(list);
				if (!info) {
					list.addParameter(prmListCategory + getInstanceIDString(iwc), "true");
				}
				else {
					list.addParameter(prmListCategory + getInstanceIDString(iwc), "false");
				}
				T.add(list, 1, 1);
			}

			if (this.hasEdit) {
				Link change = getCategoryLink();
				change.setImage(core.getImage("/shared/detach.gif"));
				T.add(change, 1, 1);
			}

			if (this.hasEdit && enableDelete) {
				T.add(T.getTransparentCell(iwc), 1, 1);
				Link delete = new Link(core.getImage("/shared/delete.gif"));
				delete.setWindowToOpen(BlogEditorWindow.class);
				delete.addParameter(BlogEditorWindow.prmDelete, iWorkFolderId);
				T.add(delete, 3, 1);
			}
		}
		if (this.hasEdit && newObjInst) {
			Link newLink = getCategoryLink();
			newLink.setImage(core.getImage("/shared/detach.gif"));
			//Link newLink = new Link(core.getImage("/shared/create.gif"));
			//newLink.setWindowToOpen(BlogEditorWindow.class);
			//if(newObjInst)
			//newLink.addParameter(BlogEditorWindow.prmObjInstId,getICObjectInstanceID());

			T.add(newLink, 2, 1);
		}
		T.setWidth("100%");
		return T;
	}

	private PresentationObject getCategoryList(Locale locale, IWContext iwc) {
		List L = BlogFinder.listOfAllBlogHelpersInCategory(getCategoryIds(), 50, locale);
		Table T = new Table();
		int row = 1;
		if (L != null) {
			Iterator I = L.iterator();
			BlogHelper blogHelper;
			while (I.hasNext()) {
				blogHelper = (BlogHelper) I.next();
				T.add(getBlogOverViewTable(blogHelper, locale, iwc), 1, row++);
			}
		} else {
			// T.add(new Text(iwrb.getLocalizedString("no_blog","No Blog")));
		}
		return T;
	}

	private PresentationObject getBlogOverViewTable(BlogHelper blogHelper, Locale locale, IWContext iwc) {
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		T.setWidth("100%");
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
		ContentHelper contentHelper = blogHelper.getContentHelper();
		BlogEntity blog = blogHelper.getBlogEntity();
		LocalizedText locText = contentHelper.getLocalizedText(locale);
		Text blogInfo = getInfoText(blog, blogHelper.getContentHelper().getContent(), locale, this.showOnlyDates, this.showTime, this.showTimeFirst, this.showUpdatedDate);

		String sBlogBody = "";
		String sHeadline = "";

		if (locText != null) {
			sHeadline = locText.getHeadline();
			sBlogBody = locText.getBody();
		}

		int letterCount = sBlogBody.length();
		int fileCount = 0;

		List files = blogHelper.getContentHelper().getFiles();
		if (files != null) {
			fileCount = files.size();
		}

		Text hLetters = formatText(this.iwrb.getLocalizedString("letters", "Letters") + " : ", true);
		Text hFiles = formatText(this.iwrb.getLocalizedString("files", "Files") + " : ", true);
		Text hFrom = formatText(this.iwrb.getLocalizedString("publish_from", "Publish from") + " : ", true);
		Text hTo = formatText(this.iwrb.getLocalizedString("publish_to", "Publish to") + " : ", true);
		Text hCreated = formatText(this.iwrb.getLocalizedString("created", "Created") + " : ", true);
		Text hUpdated = formatText(this.iwrb.getLocalizedString("updated", "Updated") + " : ", true);
		Text tLetters = formatText(String.valueOf(letterCount), false);
		Text tFiles = formatText(String.valueOf(fileCount), false);

		IWTimestamp now = IWTimestamp.RightNow();
		IWTimestamp from = new IWTimestamp(blogHelper.getContentHelper().getContent().getPublishFrom());
		IWTimestamp to = new IWTimestamp(blogHelper.getContentHelper().getContent().getPublishTo());
		IWTimestamp created = new IWTimestamp(blogHelper.getContentHelper().getContent().getCreated());
		IWTimestamp updated = new IWTimestamp(blogHelper.getContentHelper().getContent().getLastUpdated());

		Text tFrom = formatText(df.format((java.util.Date) from.getTimestamp()), true);
		Text tTo = formatText(df.format((java.util.Date) to.getTimestamp()), true);
		Text tCreated = formatText(df.format((java.util.Date) created.getTimestamp()), false);
		Text tUpdated = formatText(df.format((java.util.Date) updated.getTimestamp()), false);

		// Unpublished
		if (from.isLaterThan(now)) {
			tFrom.setFontColor("#FFDE00");
			tTo.setFontColor("#FFDE00");
		}
		// Published
		else if (now.isLaterThan(to)) {
			tFrom.setFontColor("#CC3300");
			tTo.setFontColor("#CC3300");
		}
		// Publishing
		else if (now.isLaterThan(from) && to.isLaterThan(now)) {
			tFrom.setFontColor("#333399");
			tTo.setFontColor("#333399");
		}

		Text headLine = new Text(sHeadline);
		if (blogInfo != null) {
			blogInfo = setInformationAttributes(blogInfo);
		}
		headLine = setHeadlineAttributes(headLine);

		Table infoTable = new Table();
		infoTable.add(hLetters, 1, 1);
		infoTable.add(tLetters, 2, 1);
		infoTable.add(hFiles, 1, 2);
		infoTable.add(tFiles, 2, 2);
		infoTable.add(hFrom, 3, 1);
		infoTable.add(tFrom, 4, 1);
		infoTable.add(hTo, 3, 2);
		infoTable.add(tTo, 4, 2);
		infoTable.add(hCreated, 5, 1);
		infoTable.add(tCreated, 6, 1);
		infoTable.add(hUpdated, 5, 2);
		infoTable.add(tUpdated, 6, 2);

		int row = 1;
		if (this.showInfo) {
			T.add(blogInfo, 1, row++);
		}
		T.add(headLine, 1, row++);
		T.add(infoTable, 1, row++);

		T.setHeight(row++, String.valueOf(this.iSpaceBetweenBlogAndBody));

		if (this.showMoreButton) {
			T.add(getMoreLink(this.moreImage, blog.getID(), iwc), 1, row);
			T.add(Text.getNonBrakingSpace(), 1, row);
		}
		if (this.showMoreText) {
			Text tMore = new Text(this.iwrb.getLocalizedString("more", "More"));
			tMore = setMoreAttributes(tMore);
			T.add(getMoreLink(tMore, blog.getID(), iwc), 1, row);
		}
		row++;
		int ownerId = blogHelper.getContentHelper().getContent().getUserId();
		if (this.hasEdit || (this.hasAdd && (ownerId == iwc.getUserId()))) {
			T.add(getBlogAdminPart(blog, iwc), 1, row);
		}
		return T;
	}

	private Text formatText(String text, boolean bold) {
		Text T = new Text(text);
		T.setFontSize(2);
		T.setBold(bold);
		return T;
	}

	private PresentationObject publishBlog(IWContext iwc, Locale locale, boolean collection) {
		List L = null;
		if (this.iLayout == COLLECTION_LAYOUT || collection) {
			L = BlogFinder.listOfAllBlogHelpersInCategory(getCategoryIds(), this.numberOfCollectionBlog, locale);
		} else {
			L = BlogFinder.listOfBlogHelpersInCategory(getCategoryIds(), this.numberOfDisplayedBlog, locale);
		}
		BlogTable T = new BlogTable(BlogTable.BLOG_SITE_LAYOUT, this.cellPadding, this.cellSpacing, this.firstTableColor, this.secondTableColor);

		//int count = BlogFinder.countBlogInCategory(blogCategory.getID());
		//System.err.println(" blog count "+count);
		boolean useDividedTable = this.iLayout == BLOG_SITE_LAYOUT ? true : false;
		if (L != null) {
			int len = L.size();
			Integer I;
			BlogHelper blogHelper;
			for (int i = 0; i < len; i++) {
				if (this.numberOfExpandedBlog == i) {
					collection = true; // show the rest as collection
				}
				blogHelper = (BlogHelper) L.get(i);
				I = new Integer(i);
				if (this.objectsBetween != null && this.objectsBetween.containsKey(I)) {
					Table t = new Table(1, 1);
					t.setCellpadding(4);
					t.add((PresentationObject) this.objectsBetween.get(I));
					T.add(t, this.sObjectAlign);
					this.objectsBetween.remove(I);
				}
				T.add(getBlogTable(blogHelper, locale, false, collection, iwc), useDividedTable, "left");
			}
			// blog collection
			if (this.showBlogCollectionButton) {
				if (!collection) {
					// adds collectionButton only if one category bound to
					// instance:
					//if(getCategoryIds().length == 1)
					T.add(getCollectionTable(iwc, getCategoryIds()[0]));
				} else if (collection && isFromCollectionLink(iwc)) {
					T.add(getBackTable(iwc));
				} else if (collection && !isFromCollectionLink(iwc)) {
					T.add(getCollectionTable(iwc, getCategoryIds()[0]));
				}
			}
			// Finish objectsbetween
			if (this.objectsBetween != null && this.objectsBetween.size() > 0) {
				Vector V = new Vector(this.objectsBetween.values());
				Collections.reverse(V);
				Iterator iter = V.iterator();
				while (iter.hasNext()) {
					T.add((PresentationObject) iter.next(), this.sObjectAlign);
				}
			}
		} else {
			if (this.hasEdit || this.hasInfo) {
				T.add(new Text(this.iwrb.getLocalizedString("no_blog", "No Blog")));
			}
		}
		return (T);
	}

	private Table getCollectionTable(IWContext iwc, int iCollectionCategoryId) {
		Table smallTable = new Table(1, 1);
		smallTable.setCellpadding(0);
		smallTable.setCellspacing(0);
		if (this.collectionImage != null) {
			smallTable.add(getCollectionLink(this.collectionImage, iCollectionCategoryId, iwc), 1, 1);
		}
		if (this.showCollectionText) {
			Text collText = new Text(this.iwrb.getLocalizedString("collection", "Collection"));
			collText = setInformationAttributes(collText);
			smallTable.add(getCollectionLink(collText, iCollectionCategoryId, iwc), 1, 1);
		}
		return smallTable;
	}

	public Table getBackTable(IWContext iwc) {
		Table smallTable = new Table(1, 1);
		smallTable.setCellpadding(0);
		smallTable.setCellspacing(0);
		if (this.showBackButton) {
			smallTable.add(getBackLink(this.backImage), 1, 1);
			smallTable.add(Text.getNonBrakingSpace(), 1, 1);
		}
		if (this.showBackText) {
			Text tBack = new Text(this.iwrb.getLocalizedString("back", "Back"));
			tBack = setMoreAttributes(tBack);
			smallTable.add(getBackLink(tBack), 1, 1);
		}
		return smallTable;

	}

	private Link getCollectionLink(PresentationObject obj, int iCategoryId, IWContext iwc) {
		Link collectionLink = new Link(obj);
		checkFromPage(collectionLink);
		collectionLink.addParameter(prmBlogCategoryId, iCategoryId);
		collectionLink.addParameter(prmCollection + getInstanceIDString(iwc), "true");
		if (this.viewPageId > 0) {
			collectionLink.setPage(this.viewPageId);
		}
		return collectionLink;
	}

	private boolean isFromCollectionLink(IWContext iwc) {
		return iwc.isParameterSet(prmCollection + getInstanceIDString(iwc));
	}

	// Make a table around each blog
	private PresentationObject getBlogTable(BlogHelper blogHelper, Locale locale, boolean showAll, boolean collection, IWContext iwc) {

		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		T.setWidth("100%");
		int row = 1;
		ContentHelper contentHelper = blogHelper.getContentHelper();
		BlogEntity blog = blogHelper.getBlogEntity();
		LocalizedText locText = contentHelper.getLocalizedText(locale);

		if (this.iLayout == SINGLE_LINE_LAYOUT) {
			this.showOnlyDates = true;
		}

		String sBlogBody = "";
		String sHeadline = "";
		String sTeaser = "";

		if (locText != null) {
			sHeadline = locText.getHeadline();
			sHeadline = sHeadline == null ? "" : sHeadline;
			sTeaser = locText.getTitle();
			sTeaser = sTeaser == null ? "" : sTeaser;
		}
		// shortening headlinestext
		boolean needMoreButton = collection;
		if (!showAll && this.numberOfHeadlineLetters > -1 && sHeadline.length() >= this.numberOfHeadlineLetters) {
			sHeadline = sHeadline.substring(0, this.numberOfHeadlineLetters) + "...";
			needMoreButton = true;
		}

		Text headLine = new Text(sHeadline);
		Text teaser = new Text(sTeaser);

		Text blogInfo = getInfoText(blog, blogHelper.getContentHelper().getContent(), locale, this.showOnlyDates, this.showTime, this.showTimeFirst, this.showUpdatedDate);
		if (blogInfo != null) {
			blogInfo = setInformationAttributes(blogInfo);
		}
		headLine = setHeadlineAttributes(headLine);
		teaser = setTextAttributes(teaser);

		// Check if using single_line_layout
		if (this.iLayout != SINGLE_LINE_LAYOUT) {
			if (blogInfo != null) {
				T.add(blogInfo, 1, row);
				row++;
			}

			//////// HEADLINE PART ////////////////

			if (this.alignWithHeadline) {
				if (this.headlineImage != null) {
					this.headlineImage.setHorizontalSpacing(3);
					T.add(getMoreLink(this.headlineImage, blog.getID(), iwc), 1, row);
				}
				if (this.headlineImageURL != null) {
					T.add(getMoreLink(this.iwb.getImage(this.headlineImageURL), blog.getID(), iwc), 1, row);
				}
			}

			if (this.headlineAsLink) {
				T.add(getMoreLink(headLine, blog.getID(), iwc), 1, row);
			} else {
				T.add(headLine, 1, row);
			}
			row++;
			T.setHeight(row, String.valueOf(this.iSpaceBetweenBlogAndBody));
			row++;
			/////////// BODY PART //////////
			if (this.showTeaserText && sTeaser.length() > 0 && !showAll) {
				if (this.showImages && this.showImagesInOverview) {
					T.add(getBlogImage(blogHelper, sHeadline), 1, row);
					//if (blog.getImageId()!= -1 && showImages &&
					// blog.getIncludeImage()){
				}
				T.add(teaser, 1, row);
				needMoreButton = true;
			} else if (locText != null && !collection) {
				// counting blog
				this.blogCount++;
				sBlogBody = locText.getBody();
				sBlogBody = sBlogBody == null ? "" : sBlogBody;

				// shortening blogtext
				if (!showAll && sBlogBody.length() >= this.numberOfLetters) {
					sBlogBody = sBlogBody.substring(0, this.numberOfLetters) + "...";
					needMoreButton = true;
				}

				sBlogBody = TextSoap.formatText(sBlogBody);

				Text blogBody = new Text(sBlogBody);
				blogBody = setTextAttributes(blogBody);

				//////////// IMAGE PART ///////////
				if (this.showImages) {
					if (!showAll && this.showImagesInOverview) {
						T.add(getBlogImage(blogHelper, sHeadline), 1, row);
					}
					else if (showAll) {
						T.add(getBlogImage(blogHelper, sHeadline), 1, row);
					//if (blog.getImageId()!= -1 && showImages &&
					// blog.getIncludeImage()){
					}
				}

				T.add(blogBody, 1, row);
			}
			row++;

			///////// BACK LINK ////////////////

			if (showAll) {
				T.setHeight(row++, String.valueOf(this.iSpaceBetweenBlogAndBody));
				if (this.showBackButton) {
					T.add(getBackLink(this.backImage), 1, row);
					T.add(Text.getNonBrakingSpace(), 1, row);
				}
				if (this.showBackText) {
					Text tBack = new Text(this.iwrb.getLocalizedString("back", "Back"));
					tBack = setMoreAttributes(tBack);
					T.add(getBackLink(tBack), 1, row);
				}
			}

			////////// MORE LINK ///////////////

			if (!showAll && needMoreButton) {
				T.setHeight(row++, String.valueOf(this.iSpaceBetweenBlogAndBody));
				if (this.showMoreButton) {
					T.add(getMoreLink(this.moreImage, blog.getID(), iwc), 1, row);
					T.add(Text.getNonBrakingSpace(), 1, row);
				}
				if (this.showMoreText) {
					Text tMore = new Text(this.iwrb.getLocalizedString("more", "More"));
					tMore = setMoreAttributes(tMore);
					T.add(getMoreLink(tMore, blog.getID(), iwc), 1, row);
				}
			}

			//////////// ADMIN PART /////////////////////
			int ownerId = blogHelper.getContentHelper().getContent().getUserId();
			if (this.hasEdit || (this.hasAdd && (ownerId == iwc.getUserId()))) {
				T.add(getBlogAdminPart(blog, iwc), 1, row);
			}
			row++;
			T.setHeight(row++, String.valueOf(this.iSpaceBetweenBlog));
		}
		//////////// SINGLE LINE VIEW ///////////////
		// if single line view
		else {
			int headlineCol = 3;
			int dateCol = 1;
			if (this.dateAlign.toLowerCase().equals("right")) {
				headlineCol = 1;
				dateCol = 3;
			}

			if (this.alignWithHeadline) {
				if (this.headlineImage != null) {
					this.headlineImage.setHorizontalSpacing(3);
					T.add(this.headlineImage, dateCol, 1);
				}
				if (this.headlineImageURL != null) {
					T.add(this.iwb.getImage(this.headlineImageURL), dateCol, 1);
				}
			}

			if (this.showInfo) {
				T.add(blogInfo, dateCol, 1);
			}
			if (this.spacerImage == null) {
				this.spacerImage = T.getTransparentCell(iwc);
				this.spacerImage.setWidth(this.iSpaceBetweenBlogAndBody);
				this.spacerImage.setHeight(1);
			}
			T.setAlignment(headlineCol, 1, "left");
			T.setAlignment(4, 1, "right");
			T.setWidth(headlineCol, 1, "100%");
			T.setWidth(dateCol, 1, "45");
			T.add(this.spacerImage, 2, 1);
			//T.add(Text.getNonBrakingSpace(2),2,1);
			if (this.headlineAsLink) {
				Link headlineLink = new Link(headLine);
				checkFromPage(headlineLink);
				headlineLink.addParameter(prmMore + getInstanceIDString(iwc), blog.getID());
				if (this.viewPageId > 0) {
					headlineLink.setPage(this.viewPageId);
				}
				T.add(headlineLink, headlineCol, 1);
			} else {
				T.add(headLine, headlineCol, 1);
			}
			int ownerId = blogHelper.getContentHelper().getContent().getUserId();
			if (this.hasEdit || (this.hasAdd && (ownerId == iwc.getUserId()))) {
				T.add(getBlogAdminPart(blog, iwc), 4, 1);
			}
		}
		//T.setBorder(1);
		return T;
	}

	private Link getMoreLink(PresentationObject obj, int blogId, IWContext iwc) {
		Link moreLink = new Link(obj);
		checkFromPage(moreLink);
		moreLink.addParameter(prmMore + getInstanceIDString(iwc), blogId);
		if (this.viewPageId > 0) {
			moreLink.setPage(this.viewPageId);
		}
		return moreLink;
	}

	private Link getBackLink(PresentationObject obj) {
		Link backLink = new Link(obj);
		backLink.setAsBackLink(1);
		return backLink;
	}

	private PresentationObject getBlogAdminPart(BlogEntity blog, IWContext iwc) {
		Table links = new Table(3, 1);
		Link blogEdit = new Link(this.iwb.getImage("/shared/edit.gif"));
		blogEdit.setWindowToOpen(BlogEditorWindow.class);
		blogEdit.addParameter(BlogEditorWindow.prmBlogEntityId, blog.getID());
		blogEdit.addParameter(BlogEditorWindow.prmObjInstId, getICObjectInstanceID());

		Link blogDelete = new Link(this.iwb.getImage("/shared/delete.gif"));
		blogDelete.setWindowToOpen(BlogEditorWindow.class);
		blogDelete.addParameter(BlogEditorWindow.prmDelete, blog.getID());

		//links.setAlignment(1,1,"left");
		//links.setAlignment(2,1,"right");
		links.setCellpadding(0);
		links.setCellspacing(0);
		links.add(blogEdit, 1, 1);
		links.add(links.getTransparentCell(iwc), 2, 1);
		links.add(blogDelete, 3, 1);
		return links;
	}

	private Text getInfoText(BlogEntity bgBlog, Content content, Locale locale, boolean ifUseOnlyDates, boolean ifShowTime, boolean ifShowTimeFirst, boolean showUpdatedDate) {
		if (this.showInfo) {
			return new Text(BlogFormatter.getInfoText(bgBlog, content, "", locale, ifUseOnlyDates, ifShowTime, ifShowTimeFirst, showUpdatedDate));
		}
		else {
			return null;
		}
	}

	public void main(IWContext iwc) throws Exception {
		this.hasEdit = iwc.hasEditPermission(this);
		this.hasAdd = iwc.hasPermission(AddPermisson, this);
		this.hasInfo = iwc.hasPermission(InfoPermission, this);

		this.iwb = getBundle(iwc);
		this.iwrb = getResourceBundle(iwc);
		
		
		String mode = iwc.getParameter(Blog.PRM_MODE);
		if(Blog.MODE_EDIT.equals(mode)){
			super.add(new BlogEditorWindow());
		} else {
			control(iwc);
		}
	}

	//	public boolean deleteBlock(int instanceid){
	//		return BlogBusinessOLD.disconnectBlock(instanceid);
	//	}

	public void setConnectionAttributes(String attributeName, int attributeId) {
		this.attributeName = attributeName;
		this.attributeId = attributeId;
	}

	public void setConnectionAttributes(String attributeName, String attributeId) {
		this.attributeName = attributeName;
		this.attributeId = Integer.parseInt(attributeId);
	}

	/*
	 * * This method uses static layouts from this class *
	 */
	public void setLayout(int LAYOUT) {
		this.iLayout = LAYOUT;
	}

	/**
	 * 
	 * return a proxy for the main text. Use the standard set methods on this
	 * object such as .setFontSize(1) etc. and it will set the property for all
	 * texts.
	 */
	public Text getTextProxy() {
		return this.textProxy;
	}
	public Text getHeadlineProxy() {
		return this.headlineProxy;
	}
	public Text getInformationProxy() {
		return this.informationProxy;
	}
	public Text getMoreProxy() {
		return this.moreProxy;
	}
	public void setTextProxy(Text textProxy) {
		this.textProxy = textProxy;
	}
	public void setHeadlineProxy(Text headlineProxy) {
		this.headlineProxy = headlineProxy;
	}
	public void setInformationProxy(Text informationProxy) {
		this.informationProxy = informationProxy;
	}
	private Text setTextAttributes(Text realText) {
		Text tempText = (Text) this.textProxy.clone();
		tempText.setText(realText.getText());
		return tempText;
	}
	private Text setHeadlineAttributes(Text realText) {
		Text tempText = (Text) this.headlineProxy.clone();
		tempText.setText(realText.getText());
		return tempText;
	}
	private Text setInformationAttributes(Text realText) {
		Text tempText = (Text) this.informationProxy.clone();
		tempText.setText(realText.getText());
		return tempText;
	}
	private Text setMoreAttributes(Text realText) {
		Text tempText = (Text) this.moreProxy.clone();
		tempText.setText(realText.getText());
		return tempText;
	}

	public void setInformationFontSize(int size) {
		getInformationProxy().setFontSize(size);
	}

	public void setHeadlineFontSize(int size) {
		getHeadlineProxy().setFontSize(size);
	}

	public void setTextFontSize(int size) {
		getTextProxy().setFontSize(size);
	}

	public void setInformationFontColor(String color) {
		getInformationProxy().setFontColor(color);
	}

	public void setHeadlineFontColor(String color) {
		getHeadlineProxy().setFontColor(color);
	}

	public void setTextFontColor(String color) {
		getTextProxy().setFontColor(color);
	}

	public void setInformationFontFace(String face) {
		getInformationProxy().setFontFace(face);
	}

	public void setHeadlineFontFace(String face) {
		getHeadlineProxy().setFontFace(face);
	}

	public void setTextFontFontFace(String face) {
		getTextProxy().setFontFace(face);
	}

	public void setInformationFontStyle(String style) {
		getInformationProxy().setFontStyle(style);
	}

	public void setMoreAndBackFontStyle(String style) {
		getMoreProxy().setFontStyle(style);
	}

	public void setHeadlineFontStyle(String face) {
		getHeadlineProxy().setFontStyle(face);
	}

	public void setTextFontFontStyle(String face) {
		getTextProxy().setFontStyle(face);
	}

	public void setNumberOfLetters(int numberOfLetters) {
		this.numberOfLetters = Math.abs(numberOfLetters);
	}

	public void setNumberOfHeadlineLetters(int numberOfLetters) {
		this.numberOfHeadlineLetters = Math.abs(numberOfLetters);
	}
	//debug this changes the number of blog displayed..that is the date alone
	// is failing
	public void setNumberOfDisplayedBlog(int numberOfDisplayedBlog) {
		this.limitNumberOfBlog = true;
		this.numberOfDisplayedBlog = Math.abs(numberOfDisplayedBlog);
	}

	public void setNumberOfCollectionBlog(int numberOfCollectionBlog) {
		this.limitNumberOfBlog = true;
		this.numberOfCollectionBlog = Math.abs(numberOfCollectionBlog);
	}

	public void setToViewBlog(boolean viewBlog) {
		this.viewBlog = viewBlog;
	}

	public void setAdmin(boolean isAdmin) {
		this.hasEdit = isAdmin;
	}
	public void setWidth(int width) {
		setWidth(Integer.toString(width));
	}
	public void setWidth(String width) {
		this.outerTableWidth = width;
	}
	public void setBackgroundColor(String color) {
		this.firstTableColor = color;
	}
	public void setZebraColored(String firstColor, String secondColor) {
		this.firstTableColor = firstColor;
		this.secondTableColor = secondColor;
	}
	public void setCellPadding(int cellpad) {
		this.cellPadding = cellpad;
	}
	public void setCellSpacing(int cellspace) {
		this.cellSpacing = cellspace;
	}
	public void showBlogCollectionButton(boolean showBlogCollectionButton) {
		this.showBlogCollectionButton = showBlogCollectionButton;
	}
	public void setNumberOfExpandedBlog(int numberOfExpandedBlog) {
		this.numberOfExpandedBlog = Math.abs(numberOfExpandedBlog);
	}
	public void setShowTeaser(boolean showTeaser) {
		this.showTeaserText = showTeaser;
	}
	public void setShowBackButton(boolean showButton) {
		this.showBackButton = showButton;
	}
	public void setShowBackText(boolean showText) {
		this.showBackText = showText;
	}
	public void setShowImages(boolean showImages) {
		this.showImages = showImages;
	}
	public void setShowImagesInOverview(boolean showImages) {
		this.showImagesInOverview = showImages;
	}
	public void setShowMoreButton(boolean showMoreButton) {
		this.showMoreButton = showMoreButton;
	}
	public void setShowMoreText(boolean moreText) {
		this.showMoreText = moreText;
	}
	public void setShowHeadlineImage(boolean showHeadlineImage) {
		this.showHeadlineImage = showHeadlineImage;
	}
	public void alignImageWithHeadline() {
		this.alignWithHeadline = true;
	}
	public void setHeadlineAsLink(boolean headlineAsLink) {
		this.headlineAsLink = headlineAsLink;
		this.showHeadlineImage = true;
	}
	public void setHeadlineImage(Image image) {
		this.headlineImage = image;
		this.alignWithHeadline = true;
	}
	public void setBackImage(Image image) {
		this.backImage = image;
	}
	public void setMoreImage(Image image) {
		this.moreImage = image;
	}

	public void setFirstImageWidth(int imageWith) {
		this.firstImageWidth = imageWith;
	}

	public void setImageWidth(int imagewidth) {
		this.ImageWidth = imagewidth;
	}

	public void setCollectionImage(Image image) {
		this.collectionImage = image;
	}
	public void setHeadlineImageURL(String headlineImageURL) {
		this.headlineImageURL = headlineImageURL;
		this.alignWithHeadline = true;
	}
	public void setShowOnlyDates(boolean showOnlyDates) {
		this.showOnlyDates = showOnlyDates;
	}
	public void setDateAlign(String alignment) {
		this.dateAlign = alignment;
	}

	public void setViewPage(com.idega.core.builder.data.ICPage page) {
		this.viewPageId = page.getID();
	}

	public void setShowTime(boolean showTime) {
		this.showTime = this.showOnlyDates;
	}

	public void setSpaceBetweenBlog(int pixels) {
		this.iSpaceBetweenBlog = pixels;
	}

	public void setSpaceBetweenTitleAndBody(int pixels) {
		this.iSpaceBetweenBlogAndBody = pixels;
	}

	public void setShowTimeFirst(boolean showTimeFirst) {
		this.showTimeFirst = showTimeFirst;
	}

	public void setShowUpdatedDate(boolean showUpdatedDate) {
		this.showUpdatedDate = showUpdatedDate;
	}
	public void setShowInfo(boolean showInfo) {
		this.showInfo = showInfo;
	}
	public void setShowCollectionText(boolean showText) {
		this.showCollectionText = showText;
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public String getCacheKey() {
		return CACHE_KEY;
	}

	public String getObjectAlignment() {
		return this.sObjectAlign;
	}
	public void setObjectAligment(String sAlign) {
		this.sObjectAlign = sAlign;
	}
	public void addObjectBetween(PresentationObject object, int spaceNumber) {
		if (this.objectsBetween == null) {
			this.objectsBetween = new Hashtable();
		}
		this.objectsBetween.put(new Integer(spaceNumber), object);
	}
	// overriding super class method
	public void add(PresentationObject MO) {
		addObjectBetween(MO, this.iSpaceBetween);
		if (this.iLayout == BLOG_SITE_LAYOUT) {
			this.iSpaceBetween += 2;
		}
		else {
			this.iSpaceBetween++;
		}
	}

	public synchronized Object clone() {
		Blog obj = null;
		try {
			obj = (Blog) super.clone();

			// integers :
			obj.numberOfLetters = this.numberOfLetters;
			obj.numberOfHeadlineLetters = this.numberOfHeadlineLetters;
			obj.numberOfDisplayedBlog = this.numberOfDisplayedBlog;
			obj.numberOfExpandedBlog = this.numberOfExpandedBlog;
			obj.numberOfCollectionBlog = this.numberOfCollectionBlog;
			obj.iSpaceBetween = this.iSpaceBetween;
			obj.cellPadding = this.cellPadding;
			obj.cellSpacing = this.cellSpacing;
			obj.viewPageId = this.viewPageId;
			obj.textSize = this.textSize;

			// booleans:
			obj.showBackButton = this.showBackButton;
			obj.showAll = this.showAll;
			obj.showImages = this.showImages;
			obj.showOnlyDates = this.showOnlyDates;
			obj.showTime = this.showTime;
			obj.showInfo = this.showInfo;
			obj.showTimeFirst = this.showTimeFirst;
			obj.headlineAsLink = this.headlineAsLink;
			obj.showHeadlineImage = this.showHeadlineImage;
			obj.showMoreButton = this.showMoreButton;
			obj.alignWithHeadline = this.alignWithHeadline;
			obj.limitNumberOfBlog = this.limitNumberOfBlog;
			obj.enableDelete = this.enableDelete;
			obj.viewBlog = this.viewBlog;
			obj.newobjinst = this.newobjinst;
			obj.showBackText = this.showBackText;
			obj.showMoreText = this.showMoreText;
			obj.showTeaserText = this.showTeaserText;
			// Strings :
			obj.outerTableWidth = this.outerTableWidth;
			obj.sObjectAlign = this.sObjectAlign;
			obj.headlineImageURL = this.headlineImageURL;
			obj.dateAlign = this.dateAlign;

			if (this.headlineImage != null) {
				obj.headlineImage = this.headlineImage;
			}
			if (this.backImage != null) {
				obj.backImage = this.backImage;
			}
			if (this.moreImage != null) {
				obj.moreImage = this.moreImage;
			}
			if (this.collectionImage != null) {
				obj.collectionImage = this.collectionImage;
			}

			// Nullable :
			if (this.firstTableColor != null) {
				obj.firstTableColor = this.firstTableColor;
			}
			if (this.secondTableColor != null) {
				obj.secondTableColor = this.secondTableColor;
			}
			if (this.objectsBetween != null) {
				obj.objectsBetween = this.objectsBetween;
			}
			if (this.spacerImage != null) {
				obj.spacerImage = this.spacerImage;
			}

			// Text proxies :
			obj.textProxy = this.textProxy;
			obj.headlineProxy = this.headlineProxy;
			obj.informationProxy = this.informationProxy;

		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}

	/** @todo finish this for all states* */
	protected String getCacheState(IWContext iwc, String cacheStatePrefix) {
		String returnString = "";
		String parName = prmMore + getInstanceIDString(iwc);
		if (iwc.isParameterSet(parName)) {
			returnString += iwc.getParameter(parName);
		}
		parName = prmListCategory + getInstanceIDString(iwc);
		if (iwc.isParameterSet(parName)) {
			returnString += parName + "=" + iwc.getParameter(parName);
		}
		parName = prmBlogCategoryId;
		if (iwc.isParameterSet(parName)) {
			returnString += parName + "=" + iwc.getParameter(parName);
			parName = prmCollection + getInstanceIDString(iwc);
			if (iwc.isParameterSet(parName)) {
				returnString += parName + "=" + iwc.getParameter(parName);
			}
		}
		return cacheStatePrefix + returnString;
	}

	private PresentationObject getBlogImage(BlogHelper blogHelper, String headline) {
		List files = blogHelper.getContentHelper().getFiles();
		if (files != null && !files.isEmpty()) {
			try {
				//Table imageTable = new Table(1, 2);
				ICFile imagefile = (ICFile) files.get(0);
				int imid = ((Integer) imagefile.getPrimaryKey()).intValue();
				String att = imagefile.getMetaData(BlogEditorWindow.imageAttributeKey);

				Image blogImage = new Image(imid);
				if (att != null) {
					blogImage.addMarkupAttributes(getAttributeMap(att));
				}
				else {
					blogImage.setAlignment("right");
					blogImage.setBorder(this.ImageBorder);
				}
				// first blog
				if (this.blogCount == 1) {
					if (blogImage.getWidth() == null || blogImage.getWidth().length() == 0) {
						blogImage.setMaxImageWidth(this.ImageWidth);
					}
					return blogImage;
				}
				// other blog
				else {
					if (blogImage.getWidth() == null || blogImage.getWidth().length() == 0) {
						blogImage.setMaxImageWidth(this.ImageWidth);
					}
					Link L = new Link(blogImage);
					L.addParameter(ImageWindow.prmImageId, imid);
					if (this.addImageInfo) {
						L.addParameter(ImageWindow.prmInfo, TextSoap.convertSpecialCharacters(headline));
					}
					L.setWindowToOpen(ImageWindow.class);
					return L;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * @param addImageInfo
	 */
	public void setShowImageInfo(boolean showImageInfo) {
		this.addImageInfo = showImageInfo;
	}

	/**
	 * Returns an array of Category ids from
	 * 
	 * @deprecated replaced with #getCategoriesToView()
	 */
	public int[] getCategoryIds() {

		//TODO change to getViewFolders()

		int[] toReturn = { this.getWorkFolder().getID()};
		return toReturn;

		//		int[] categoryIds = new int[0];
		//		InformationCategory[] cats = this.getCategoriesToView();
		//		if(cats !=null && cats.length > 0){
		//			for (int i = 0; i < cats.length; i++) {
		//				categoryIds[i] = cats[i].getID();
		//			}
		//		}
		//		return categoryIds;
	}
	
	public boolean createDefaultCategories(IWContext iwc){
		try {
			this.getBlockBusinessInstance(iwc).createICInformationCategory(iwc,iwc.getCurrentLocaleId(),"Blog","Default category",null,getBlockObjectID(),-1);
			return true;
		} catch (IBOLookupException e) {
			e.printStackTrace();
			return false;
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean copyICObjectInstance(String pageKey,int newInstanceID, ICDynamicPageTriggerCopySession copySession) {
		try {
			return ((FolderBlockBusiness)IBOLookup.getServiceInstance(getIWApplicationContext(),FolderBlockBusiness.class)).copyCategoryAttachments(this.getBlockInstanceID(), newInstanceID);
		} catch (IBOLookupException e) {
			e.printStackTrace();
			return false;
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	
}
