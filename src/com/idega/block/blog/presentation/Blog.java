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
		showAll = true;
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
		headlineProxy.setBold();
		informationProxy.setFontColor("#666666");
		textProxy.setFontSize(1);
		informationProxy.setFontSize(1);
	}

	private void checkCategories() {

	}

	/** @todo take out when instanceId handler is used */
	private String getInstanceIDString(IWContext iwc) {
		if (viewPageId > 0 || iwc.isParameterSet(prmFromPage))
			return "";
		else
			return String.valueOf(getICObjectInstanceID());
	}

	private Parameter getFromPageParameter() {
		return new Parameter(prmFromPage, "true");
	}

	private void checkFromPage(Link link) {
		if (viewPageId > 0)
			link.addParameter(getFromPageParameter());
	}

	private void control(IWContext iwc) throws Exception {

		if (moreImage == null)
			moreImage = iwrb.getImage("more.gif");
		if (backImage == null)
			backImage = iwrb.getImage("back.gif");
		if (collectionImage == null)
			collectionImage = iwrb.getImage("collection.gif");

		Locale locale = iwc.getCurrentLocale();
		String sBlogId = null;
		if (viewBlog)
			sBlogId = iwc.getParameter(prmMore + getInstanceIDString(iwc));
		InformationFolder blogCategory = null;
		String prm = prmListCategory + getInstanceIDString(iwc);
		boolean info = false;
		if (iwc.isParameterSet(prm)) {
			if (iwc.getParameter(prm).equalsIgnoreCase("true"))
				info = true;
			else
				info = false;
		}

		if (iCategoryId <= 0) {
			String sCategoryId = iwc.getParameter(prmBlogCategoryId);
			if (sCategoryId != null)
				iCategoryId = Integer.parseInt(sCategoryId);
			else {
				//if(getICObjectInstanceID() > 0){
				//		  iCategoryId =
				// BlogFinder.getObjectInstanceCategoryId(getICObjectInstanceID(),true);
				iCategoryId = this.getWorkFolder().getID();
				if (iCategoryId <= 0) {
					newobjinst = true;
				}
			}
		}
		Table T = new Table(1, 1);
		T.setCellpadding(0);
		T.setCellpadding(0);
		T.setWidth("100%");
		if (hasEdit || hasAdd || hasInfo) {
			T.add(getAdminPart(iCategoryId, false, newobjinst, info, iwc), 1, 1);
		}
		if (iCategoryId > 0) {
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
			T.add(new Text(iwrb.getLocalizedString("no_blog_category", "No blog category")));
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
			if (hasEdit || hasAdd || hasInfo) {				
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
			if (hasEdit || hasInfo) {
				Link list = new Link(iwb.getImage("/shared/info.gif"));
				checkFromPage(list);
				if (!info)
					list.addParameter(prmListCategory + getInstanceIDString(iwc), "true");
				else
					list.addParameter(prmListCategory + getInstanceIDString(iwc), "false");
				T.add(list, 1, 1);
			}

			if (hasEdit) {
				Link change = getCategoryLink();
				change.setImage(core.getImage("/shared/detach.gif"));
				T.add(change, 1, 1);
			}

			if (hasEdit && enableDelete) {
				T.add(T.getTransparentCell(iwc), 1, 1);
				Link delete = new Link(core.getImage("/shared/delete.gif"));
				delete.setWindowToOpen(BlogEditorWindow.class);
				delete.addParameter(BlogEditorWindow.prmDelete, iWorkFolderId);
				T.add(delete, 3, 1);
			}
		}
		if (hasEdit && newObjInst) {
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
		Text blogInfo = getInfoText(blog, blogHelper.getContentHelper().getContent(), locale, showOnlyDates, showTime, showTimeFirst, showUpdatedDate);

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

		Text hLetters = formatText(iwrb.getLocalizedString("letters", "Letters") + " : ", true);
		Text hFiles = formatText(iwrb.getLocalizedString("files", "Files") + " : ", true);
		Text hFrom = formatText(iwrb.getLocalizedString("publish_from", "Publish from") + " : ", true);
		Text hTo = formatText(iwrb.getLocalizedString("publish_to", "Publish to") + " : ", true);
		Text hCreated = formatText(iwrb.getLocalizedString("created", "Created") + " : ", true);
		Text hUpdated = formatText(iwrb.getLocalizedString("updated", "Updated") + " : ", true);
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
		if (blogInfo != null)
			blogInfo = setInformationAttributes(blogInfo);
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
		if (showInfo)
			T.add(blogInfo, 1, row++);
		T.add(headLine, 1, row++);
		T.add(infoTable, 1, row++);

		T.setHeight(row++, String.valueOf(iSpaceBetweenBlogAndBody));

		if (showMoreButton) {
			T.add(getMoreLink(moreImage, blog.getID(), iwc), 1, row);
			T.add(Text.getNonBrakingSpace(), 1, row);
		}
		if (showMoreText) {
			Text tMore = new Text(iwrb.getLocalizedString("more", "More"));
			tMore = setMoreAttributes(tMore);
			T.add(getMoreLink(tMore, blog.getID(), iwc), 1, row);
		}
		row++;
		int ownerId = blogHelper.getContentHelper().getContent().getUserId();
		if (hasEdit || (hasAdd && (ownerId == iwc.getUserId()))) {
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
		if (iLayout == COLLECTION_LAYOUT || collection) {
			L = BlogFinder.listOfAllBlogHelpersInCategory(getCategoryIds(), numberOfCollectionBlog, locale);
		} else {
			L = BlogFinder.listOfBlogHelpersInCategory(getCategoryIds(), numberOfDisplayedBlog, locale);
		}
		BlogTable T = new BlogTable(BlogTable.BLOG_SITE_LAYOUT, cellPadding, cellSpacing, firstTableColor, secondTableColor);

		//int count = BlogFinder.countBlogInCategory(blogCategory.getID());
		//System.err.println(" blog count "+count);
		boolean useDividedTable = iLayout == BLOG_SITE_LAYOUT ? true : false;
		if (L != null) {
			int len = L.size();
			Integer I;
			BlogHelper blogHelper;
			for (int i = 0; i < len; i++) {
				if (numberOfExpandedBlog == i)
					collection = true; // show the rest as collection
				blogHelper = (BlogHelper) L.get(i);
				I = new Integer(i);
				if (objectsBetween != null && objectsBetween.containsKey(I)) {
					Table t = new Table(1, 1);
					t.setCellpadding(4);
					t.add((PresentationObject) objectsBetween.get(I));
					T.add(t, sObjectAlign);
					objectsBetween.remove(I);
				}
				T.add(getBlogTable(blogHelper, locale, false, collection, iwc), useDividedTable, "left");
			}
			// blog collection
			if (showBlogCollectionButton) {
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
			if (objectsBetween != null && objectsBetween.size() > 0) {
				Vector V = new Vector(objectsBetween.values());
				Collections.reverse(V);
				Iterator iter = V.iterator();
				while (iter.hasNext()) {
					T.add((PresentationObject) iter.next(), sObjectAlign);
				}
			}
		} else {
			if (hasEdit || hasInfo) {
				T.add(new Text(iwrb.getLocalizedString("no_blog", "No Blog")));
			}
		}
		return (T);
	}

	private Table getCollectionTable(IWContext iwc, int iCollectionCategoryId) {
		Table smallTable = new Table(1, 1);
		smallTable.setCellpadding(0);
		smallTable.setCellspacing(0);
		if (collectionImage != null) {
			smallTable.add(getCollectionLink(collectionImage, iCollectionCategoryId, iwc), 1, 1);
		}
		if (showCollectionText) {
			Text collText = new Text(iwrb.getLocalizedString("collection", "Collection"));
			collText = setInformationAttributes(collText);
			smallTable.add(getCollectionLink(collText, iCollectionCategoryId, iwc), 1, 1);
		}
		return smallTable;
	}

	public Table getBackTable(IWContext iwc) {
		Table smallTable = new Table(1, 1);
		smallTable.setCellpadding(0);
		smallTable.setCellspacing(0);
		if (showBackButton) {
			smallTable.add(getBackLink(backImage), 1, 1);
			smallTable.add(Text.getNonBrakingSpace(), 1, 1);
		}
		if (showBackText) {
			Text tBack = new Text(iwrb.getLocalizedString("back", "Back"));
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
		if (viewPageId > 0)
			collectionLink.setPage(viewPageId);
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

		if (iLayout == SINGLE_LINE_LAYOUT)
			showOnlyDates = true;

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
		if (!showAll && numberOfHeadlineLetters > -1 && sHeadline.length() >= numberOfHeadlineLetters) {
			sHeadline = sHeadline.substring(0, numberOfHeadlineLetters) + "...";
			needMoreButton = true;
		}

		Text headLine = new Text(sHeadline);
		Text teaser = new Text(sTeaser);

		Text blogInfo = getInfoText(blog, blogHelper.getContentHelper().getContent(), locale, showOnlyDates, showTime, showTimeFirst, showUpdatedDate);
		if (blogInfo != null)
			blogInfo = setInformationAttributes(blogInfo);
		headLine = setHeadlineAttributes(headLine);
		teaser = setTextAttributes(teaser);

		// Check if using single_line_layout
		if (iLayout != SINGLE_LINE_LAYOUT) {
			if (blogInfo != null) {
				T.add(blogInfo, 1, row);
				row++;
			}

			//////// HEADLINE PART ////////////////

			if (alignWithHeadline) {
				if (headlineImage != null) {
					headlineImage.setHorizontalSpacing(3);
					T.add(getMoreLink(headlineImage, blog.getID(), iwc), 1, row);
				}
				if (headlineImageURL != null)
					T.add(getMoreLink(iwb.getImage(headlineImageURL), blog.getID(), iwc), 1, row);
			}

			if (headlineAsLink) {
				T.add(getMoreLink(headLine, blog.getID(), iwc), 1, row);
			} else {
				T.add(headLine, 1, row);
			}
			row++;
			T.setHeight(row, String.valueOf(iSpaceBetweenBlogAndBody));
			row++;
			/////////// BODY PART //////////
			if (showTeaserText && sTeaser.length() > 0 && !showAll) {
				if (showImages && showImagesInOverview) {
					T.add(getBlogImage(blogHelper, sHeadline), 1, row);
					//if (blog.getImageId()!= -1 && showImages &&
					// blog.getIncludeImage()){
				}
				T.add(teaser, 1, row);
				needMoreButton = true;
			} else if (locText != null && !collection) {
				// counting blog
				blogCount++;
				sBlogBody = locText.getBody();
				sBlogBody = sBlogBody == null ? "" : sBlogBody;

				// shortening blogtext
				if (!showAll && sBlogBody.length() >= numberOfLetters) {
					sBlogBody = sBlogBody.substring(0, numberOfLetters) + "...";
					needMoreButton = true;
				}

				sBlogBody = TextSoap.formatText(sBlogBody);

				Text blogBody = new Text(sBlogBody);
				blogBody = setTextAttributes(blogBody);

				//////////// IMAGE PART ///////////
				if (showImages) {
					if (!showAll && showImagesInOverview)
						T.add(getBlogImage(blogHelper, sHeadline), 1, row);
					else if (showAll)
						T.add(getBlogImage(blogHelper, sHeadline), 1, row);
					//if (blog.getImageId()!= -1 && showImages &&
					// blog.getIncludeImage()){
				}

				T.add(blogBody, 1, row);
			}
			row++;

			///////// BACK LINK ////////////////

			if (showAll) {
				T.setHeight(row++, String.valueOf(iSpaceBetweenBlogAndBody));
				if (showBackButton) {
					T.add(getBackLink(backImage), 1, row);
					T.add(Text.getNonBrakingSpace(), 1, row);
				}
				if (showBackText) {
					Text tBack = new Text(iwrb.getLocalizedString("back", "Back"));
					tBack = setMoreAttributes(tBack);
					T.add(getBackLink(tBack), 1, row);
				}
			}

			////////// MORE LINK ///////////////

			if (!showAll && needMoreButton) {
				T.setHeight(row++, String.valueOf(iSpaceBetweenBlogAndBody));
				if (showMoreButton) {
					T.add(getMoreLink(moreImage, blog.getID(), iwc), 1, row);
					T.add(Text.getNonBrakingSpace(), 1, row);
				}
				if (showMoreText) {
					Text tMore = new Text(iwrb.getLocalizedString("more", "More"));
					tMore = setMoreAttributes(tMore);
					T.add(getMoreLink(tMore, blog.getID(), iwc), 1, row);
				}
			}

			//////////// ADMIN PART /////////////////////
			int ownerId = blogHelper.getContentHelper().getContent().getUserId();
			if (hasEdit || (hasAdd && (ownerId == iwc.getUserId()))) {
				T.add(getBlogAdminPart(blog, iwc), 1, row);
			}
			row++;
			T.setHeight(row++, String.valueOf(iSpaceBetweenBlog));
		}
		//////////// SINGLE LINE VIEW ///////////////
		// if single line view
		else {
			int headlineCol = 3;
			int dateCol = 1;
			if (dateAlign.toLowerCase().equals("right")) {
				headlineCol = 1;
				dateCol = 3;
			}

			if (alignWithHeadline) {
				if (headlineImage != null) {
					headlineImage.setHorizontalSpacing(3);
					T.add(headlineImage, dateCol, 1);
				}
				if (headlineImageURL != null)
					T.add(iwb.getImage(headlineImageURL), dateCol, 1);
			}

			if (showInfo) {
				T.add(blogInfo, dateCol, 1);
			}
			if (spacerImage == null) {
				spacerImage = T.getTransparentCell(iwc);
				spacerImage.setWidth(iSpaceBetweenBlogAndBody);
				spacerImage.setHeight(1);
			}
			T.setAlignment(headlineCol, 1, "left");
			T.setAlignment(4, 1, "right");
			T.setWidth(headlineCol, 1, "100%");
			T.setWidth(dateCol, 1, "45");
			T.add(spacerImage, 2, 1);
			//T.add(Text.getNonBrakingSpace(2),2,1);
			if (headlineAsLink) {
				Link headlineLink = new Link(headLine);
				checkFromPage(headlineLink);
				headlineLink.addParameter(prmMore + getInstanceIDString(iwc), blog.getID());
				if (viewPageId > 0)
					headlineLink.setPage(viewPageId);
				T.add(headlineLink, headlineCol, 1);
			} else {
				T.add(headLine, headlineCol, 1);
			}
			int ownerId = blogHelper.getContentHelper().getContent().getUserId();
			if (hasEdit || (hasAdd && (ownerId == iwc.getUserId()))) {
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
		if (viewPageId > 0)
			moreLink.setPage(viewPageId);
		return moreLink;
	}

	private Link getBackLink(PresentationObject obj) {
		Link backLink = new Link(obj);
		backLink.setAsBackLink(1);
		return backLink;
	}

	private PresentationObject getBlogAdminPart(BlogEntity blog, IWContext iwc) {
		Table links = new Table(3, 1);
		Link blogEdit = new Link(iwb.getImage("/shared/edit.gif"));
		blogEdit.setWindowToOpen(BlogEditorWindow.class);
		blogEdit.addParameter(BlogEditorWindow.prmBlogEntityId, blog.getID());
		blogEdit.addParameter(BlogEditorWindow.prmObjInstId, getICObjectInstanceID());

		Link blogDelete = new Link(iwb.getImage("/shared/delete.gif"));
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
		if (showInfo)
			return new Text(BlogFormatter.getInfoText(bgBlog, content, "", locale, ifUseOnlyDates, ifShowTime, ifShowTimeFirst, showUpdatedDate));
		else
			return null;
	}

	public void main(IWContext iwc) throws Exception {
		hasEdit = iwc.hasEditPermission(this);
		hasAdd = iwc.hasPermission(AddPermisson, this);
		hasInfo = iwc.hasPermission(InfoPermission, this);

		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		
		
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
		return textProxy;
	}
	public Text getHeadlineProxy() {
		return headlineProxy;
	}
	public Text getInformationProxy() {
		return informationProxy;
	}
	public Text getMoreProxy() {
		return moreProxy;
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
		Text tempText = (Text) textProxy.clone();
		tempText.setText(realText.getText());
		return tempText;
	}
	private Text setHeadlineAttributes(Text realText) {
		Text tempText = (Text) headlineProxy.clone();
		tempText.setText(realText.getText());
		return tempText;
	}
	private Text setInformationAttributes(Text realText) {
		Text tempText = (Text) informationProxy.clone();
		tempText.setText(realText.getText());
		return tempText;
	}
	private Text setMoreAttributes(Text realText) {
		Text tempText = (Text) moreProxy.clone();
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
		firstTableColor = color;
	}
	public void setZebraColored(String firstColor, String secondColor) {
		firstTableColor = firstColor;
		secondTableColor = secondColor;
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
		firstImageWidth = imageWith;
	}

	public void setImageWidth(int imagewidth) {
		ImageWidth = imagewidth;
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
		viewPageId = page.getID();
	}

	public void setShowTime(boolean showTime) {
		this.showTime = showOnlyDates;
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
		return sObjectAlign;
	}
	public void setObjectAligment(String sAlign) {
		sObjectAlign = sAlign;
	}
	public void addObjectBetween(PresentationObject object, int spaceNumber) {
		if (objectsBetween == null)
			objectsBetween = new Hashtable();
		objectsBetween.put(new Integer(spaceNumber), object);
	}
	// overriding super class method
	public void add(PresentationObject MO) {
		addObjectBetween(MO, iSpaceBetween);
		if (iLayout == BLOG_SITE_LAYOUT) {
			iSpaceBetween += 2;
		} else
			iSpaceBetween++;
	}

	public synchronized Object clone() {
		Blog obj = null;
		try {
			obj = (Blog) super.clone();

			// integers :
			obj.numberOfLetters = numberOfLetters;
			obj.numberOfHeadlineLetters = numberOfHeadlineLetters;
			obj.numberOfDisplayedBlog = numberOfDisplayedBlog;
			obj.numberOfExpandedBlog = numberOfExpandedBlog;
			obj.numberOfCollectionBlog = numberOfCollectionBlog;
			obj.iSpaceBetween = iSpaceBetween;
			obj.cellPadding = cellPadding;
			obj.cellSpacing = cellSpacing;
			obj.viewPageId = viewPageId;
			obj.textSize = textSize;

			// booleans:
			obj.showBackButton = showBackButton;
			obj.showAll = showAll;
			obj.showImages = showImages;
			obj.showOnlyDates = showOnlyDates;
			obj.showTime = showTime;
			obj.showInfo = showInfo;
			obj.showTimeFirst = showTimeFirst;
			obj.headlineAsLink = headlineAsLink;
			obj.showHeadlineImage = showHeadlineImage;
			obj.showMoreButton = showMoreButton;
			obj.alignWithHeadline = alignWithHeadline;
			obj.limitNumberOfBlog = limitNumberOfBlog;
			obj.enableDelete = enableDelete;
			obj.viewBlog = viewBlog;
			obj.newobjinst = newobjinst;
			obj.showBackText = showBackText;
			obj.showMoreText = showMoreText;
			obj.showTeaserText = showTeaserText;
			// Strings :
			obj.outerTableWidth = outerTableWidth;
			obj.sObjectAlign = sObjectAlign;
			obj.headlineImageURL = headlineImageURL;
			obj.dateAlign = dateAlign;

			if (headlineImage != null)
				obj.headlineImage = headlineImage;
			if (backImage != null)
				obj.backImage = backImage;
			if (moreImage != null)
				obj.moreImage = moreImage;
			if (collectionImage != null)
				obj.collectionImage = collectionImage;

			// Nullable :
			if (firstTableColor != null)
				obj.firstTableColor = firstTableColor;
			if (secondTableColor != null)
				obj.secondTableColor = secondTableColor;
			if (objectsBetween != null)
				obj.objectsBetween = objectsBetween;
			if (spacerImage != null)
				obj.spacerImage = spacerImage;

			// Text proxies :
			obj.textProxy = textProxy;
			obj.headlineProxy = headlineProxy;
			obj.informationProxy = informationProxy;

		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}

	/** @todo finish this for all states* */
	protected String getCacheState(IWContext iwc, String cacheStatePrefix) {
		String returnString = "";
		String parName = prmMore + getInstanceIDString(iwc);
		if (iwc.isParameterSet(parName))
			returnString += iwc.getParameter(parName);
		parName = prmListCategory + getInstanceIDString(iwc);
		if (iwc.isParameterSet(parName))
			returnString += parName + "=" + iwc.getParameter(parName);
		parName = prmBlogCategoryId;
		if (iwc.isParameterSet(parName)) {
			returnString += parName + "=" + iwc.getParameter(parName);
			parName = prmCollection + getInstanceIDString(iwc);
			if (iwc.isParameterSet(parName))
				returnString += parName + "=" + iwc.getParameter(parName);
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
				if (att != null)
					blogImage.addMarkupAttributes(getAttributeMap(att));
				else {
					blogImage.setAlignment("right");
					blogImage.setBorder(ImageBorder);
				}
				// first blog
				if (blogCount == 1) {
					if (blogImage.getWidth() == null || blogImage.getWidth().length() == 0)
						blogImage.setMaxImageWidth(ImageWidth);
					return blogImage;
				}
				// other blog
				else {
					if (blogImage.getWidth() == null || blogImage.getWidth().length() == 0)
						blogImage.setMaxImageWidth(ImageWidth);
					Link L = new Link(blogImage);
					L.addParameter(ImageWindow.prmImageId, imid);
					if (addImageInfo)
						L.addParameter(ImageWindow.prmInfo, TextSoap.convertSpecialCharacters(headline));
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
		addImageInfo = showImageInfo;
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
