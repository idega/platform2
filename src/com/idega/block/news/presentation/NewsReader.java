/*
 * $Id: NewsReader.java,v 1.138 2004/11/03 10:05:20 gimmi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.news.presentation;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.data.ICCategory;
import com.idega.block.category.presentation.CategoryBlock;
import com.idega.block.news.business.NewsBusiness;
import com.idega.block.news.business.NewsFinder;
import com.idega.block.news.business.NewsFormatter;
import com.idega.block.news.business.NewsHelper;
import com.idega.block.news.business.NewsLayoutHandler;
import com.idega.block.news.data.NwNews;
import com.idega.block.text.business.ContentHelper;
import com.idega.block.text.data.Content;
import com.idega.block.text.data.LocalizedText;
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
 * Title: Description: Copyright: Copyright (c) 2001 Company: idega.is
 * 
 * @author 2000 - idega team -<br>
 *         <a href="mailto:aron@idega.is">Aron Birkir </a> <br>
 * @version 1.0
 */

public class NewsReader extends CategoryBlock implements Builderaware {

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.news";
	public final static String CACHE_KEY = "nw_news";
	private boolean hasEdit = false, hasAdd = false, hasInfo = false, hasEditExisting = false;
	private int iCategoryId = -1;
	private String attributeName = null;
	private int attributeId = -1;
	private User eUser = null;

	private boolean showNewsCollectionButton = false;
	private int categoryId = 0;

	private Table outerTable = new Table(1, 1);

	private int numberOfLetters = 273;
	private int numberOfHeadlineLetters = -1;
	private int numberOfDisplayedNews = 5;
	private int numberOfExpandedNews = 3;
	private int numberOfCollectionNews = 30;
	private int iSpaceBetween = 1;
	private int iSpaceBetweenNews = 20;
	private int iSpaceBetweenNewsAndBody = 5;
	private int cellPadding = 0;
	private int cellSpacing = 0;
	private int viewPageId = -1;
	private int textSize = 2;
	private int firstImageWidth = 200;
	private int ImageWidth = 100;
	private int ImageBorder = 1;
	private int dateWidth = 60;
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
	private boolean limitNumberOfNews = false;
	private boolean enableDelete = true;
	private boolean viewNews = true;
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

	private static String prmFromPage = "nwr_from_page";
	private static String prmDelete = "nwr_delete";
	private static String prmEdit = "nwr_edit";
	private static String prmNew = "nwr_new";
	private static String prmMore = "nwr_more";
	private static String prmCollection = "nwr_collection";
	private static String prmObjIns = "nwr_instance_id";

	public static String prmListCategory = "nwr_newscategoryid";
	public static String prmNewsCategoryId = "nwr_listcategory";

	private static String AddPermisson = "add";
	private static String InfoPermission = "info";
	private static String EditExistingPermission = "edit_existing";

	private IWBundle iwb;
	private IWResourceBundle iwrb;

	public static final int SINGLE_FILE_LAYOUT = NewsLayoutHandler.SINGLE_FILE_LAYOUT;
	public static final int NEWS_SITE_LAYOUT = NewsLayoutHandler.NEWS_SITE_LAYOUT;
	public static final int NEWS_PAPER_LAYOUT = NewsLayoutHandler.NEWS_PAPER_LAYOUT;
	public static final int SINGLE_LINE_LAYOUT = NewsLayoutHandler.SINGLE_LINE_LAYOUT;
	public static final int COLLECTION_LAYOUT = NewsLayoutHandler.COLLECTION_LAYOUT;

	private int iLayout = SINGLE_FILE_LAYOUT;
	private int newsCount = 0;

	private int visibleNewsRangeStart = 0;
	private int visibleNewsRangeEnd = Integer.MAX_VALUE;
	private boolean setHeadlineLinktToCategoryMainViewerPage = false;
	private boolean showCategoryInSingleLineView = false;
	private String moreAndBackStyleClass;

	public NewsReader() {
		setCacheable(getCacheKey(), 999999999);//cache indefinately
		init();
		showAll = true;
	}

	public NewsReader(int iCategoryId) {
		this();
		this.iCategoryId = iCategoryId;
		this.showAll = false;
	}

	public void registerPermissionKeys() {
		registerPermissionKey(AddPermisson);
		registerPermissionKey(InfoPermission);
		registerPermissionKey(EditExistingPermission);
	}

	public boolean getMultible() {
		return true;
	}

	public String getCategoryType() {
		return "news";
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
		if (viewPageId > 0) link.addParameter(getFromPageParameter());
	}

	private void control(IWContext iwc) {

		if (moreImage == null) moreImage = iwrb.getImage("more.gif");
		if (backImage == null) backImage = iwrb.getImage("back.gif");
		if (collectionImage == null) collectionImage = iwrb.getImage("collection.gif");

		Locale locale = iwc.getCurrentLocale();
		String sNewsId = null;
		boolean beInvisible = false;
		if (viewNews) {
			if(visibleNewsRangeStart>0) {
				Enumeration enumer = iwc.getParameterNames();
				while (enumer.hasMoreElements()) {
					String pName = (String) enumer.nextElement();
					if(pName.startsWith(prmMore)) {
						if(visibleNewsRangeStart==1) {
							sNewsId = iwc.getParameter(pName);
						} else {
							beInvisible=true;
						}
						break;
					}
				}
			} else {
				sNewsId = iwc.getParameter(prmMore + getInstanceIDString(iwc));
			}
		}
		
		if(!beInvisible) {
			ICCategory newsCategory = null;
			String prm = prmListCategory + getInstanceIDString(iwc);
			boolean info = false;
			if (iwc.isParameterSet(prm)) {
				if (iwc.getParameter(prm).equalsIgnoreCase("true"))
					info = true;
				else
					info = false;
			}
	
			if (iCategoryId <= 0) {
				String sCategoryId = iwc.getParameter(prmNewsCategoryId);
				if (sCategoryId != null)
					iCategoryId = Integer.parseInt(sCategoryId);
				else {
					//if(getICObjectInstanceID() > 0){
					//		  iCategoryId =
					// NewsFinder.getObjectInstanceCategoryId(getICObjectInstanceID(),true);
					iCategoryId = getCategoryId();
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
				newsCategory = CategoryFinder.getInstance().getCategory(iCategoryId);
				if (newsCategory != null) {
					if (sNewsId != null) {
						int id = Integer.parseInt(sNewsId);
						NewsHelper nh = NewsFinder.getNewsHelper(id);
						T.add(getNewsTable(nh, locale, true, false, iwc, true), 1, 1);
					}
					else if (info) {
						T.add(getCategoryList(locale, iwc), 1, 1);
					}
					else {
						String cprm = prmCollection + getInstanceIDString(iwc);
						T.add(publishNews(iwc, locale, iwc.isParameterSet(cprm)), 1, 1);
					}
				}
			}
			else {
				T.add(new Text(iwrb.getLocalizedString("no_news_category", "No news category")));
			}
			super.add(T);
		}
	}

	private PresentationObject getAdminPart(int iCategoryId, boolean enableDelete, boolean newObjInst, boolean info, IWContext iwc) {
		Table T = new Table(3, 1);
		T.setCellpadding(2);
		T.setCellspacing(2);
		T.setBorder(0);

		IWBundle core = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		if (iCategoryId > 0) {
			if (hasEdit || hasAdd || hasInfo) {
				Link ne = new Link(core.getImage("/shared/create.gif"));
				ne.setWindowToOpen(NewsEditorWindow.class);
				ne.addParameter(NewsEditorWindow.prmCategory, iCategoryId);
				ne.addParameter(NewsEditorWindow.prmObjInstId, getICObjectInstanceID());
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
				delete.setWindowToOpen(NewsEditorWindow.class);
				delete.addParameter(NewsEditorWindow.prmDelete, iCategoryId);
				T.add(delete, 3, 1);
			}
		}
		if (hasEdit && newObjInst) {
			Link newLink = getCategoryLink();
			newLink.setImage(core.getImage("/shared/detach.gif"));
			//Link newLink = new Link(core.getImage("/shared/create.gif"));
			//newLink.setWindowToOpen(NewsEditorWindow.class);
			//if(newObjInst)
			//newLink.addParameter(NewsEditorWindow.prmObjInstId,getICObjectInstanceID());

			T.add(newLink, 2, 1);
		}
		T.setWidth("100%");
		return T;
	}

	private PresentationObject getCategoryList(Locale locale, IWContext iwc) {
		List L = NewsFinder.listOfAllNewsHelpersInCategory(getCategoryIds(), 50, locale);
		Table T = new Table();
		int row = 1;
		if (L != null) {
			Iterator I = L.iterator();
			NewsHelper newsHelper;
			while (I.hasNext()) {
				newsHelper = (NewsHelper) I.next();
				T.add(getNewsOverViewTable(newsHelper, locale, iwc), 1, row++);
			}
		}
		else {
			// T.add(new Text(iwrb.getLocalizedString("no_news","No News")));
		}
		return T;
	}

	private PresentationObject getNewsOverViewTable(NewsHelper newsHelper, Locale locale, IWContext iwc) {
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		T.setWidth("100%");
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
		ContentHelper contentHelper = newsHelper.getContentHelper();
		NwNews news = newsHelper.getNwNews();
		LocalizedText locText = contentHelper.getLocalizedText(locale);
		Text newsInfo = getInfoText(news, newsHelper.getContentHelper().getContent(), locale, showOnlyDates, showTime, showTimeFirst, showUpdatedDate);

		String sNewsBody = "";
		String sHeadline = "";

		if (locText != null) {
			sHeadline = locText.getHeadline();
			sNewsBody = locText.getBody();
		}

		int letterCount = sNewsBody.length();
		int fileCount = 0;

		List files = newsHelper.getContentHelper().getFiles();
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
		IWTimestamp from = new IWTimestamp(newsHelper.getContentHelper().getContent().getPublishFrom());
		IWTimestamp to = new IWTimestamp(newsHelper.getContentHelper().getContent().getPublishTo());
		IWTimestamp created = new IWTimestamp(newsHelper.getContentHelper().getContent().getCreated());
		IWTimestamp updated = new IWTimestamp(newsHelper.getContentHelper().getContent().getLastUpdated());

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
		if (newsInfo != null) newsInfo = setInformationAttributes(newsInfo);
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
		if (showInfo) T.add(newsInfo, 1, row++);
		T.add(headLine, 1, row++);
		T.add(infoTable, 1, row++);

		T.setHeight(row++, String.valueOf(iSpaceBetweenNewsAndBody));

		if (showMoreButton) {
			T.add(getMoreLink(moreImage, news.getID(), iwc), 1, row);
			T.add(Text.getNonBrakingSpace(), 1, row);
		}
		if (showMoreText) {
			Text tMore = new Text(iwrb.getLocalizedString("more", "More"));
			tMore = setMoreAttributes(tMore);
			T.add(getMoreLink(tMore, news.getID(), iwc), 1, row);
		}
		row++;
		int ownerId = newsHelper.getContentHelper().getContent().getUserId();
		if (hasEdit || hasEditExisting || (hasAdd && (ownerId == iwc.getUserId()))) {
			T.add(getNewsAdminPart(news, iwc), 1, row);
		}
		return T;
	}

	private Text formatText(String text, boolean bold) {
		Text T = new Text(text);
		T.setFontSize(2);
		T.setBold(bold);
		return T;
	}

	private PresentationObject publishNews(IWContext iwc, Locale locale, boolean collection) {
		List L = null;
		if (iLayout == COLLECTION_LAYOUT || collection) {
			L = NewsFinder.listOfAllNewsHelpersInCategory(getCategoryIds(), numberOfCollectionNews, locale);
		}
		else {
			L = NewsFinder.listOfNewsHelpersInCategory(getCategoryIds(), numberOfDisplayedNews, locale);
		}
		NewsTable T = new NewsTable(NewsTable.NEWS_SITE_LAYOUT, cellPadding, cellSpacing, firstTableColor, secondTableColor);

		//int count = NewsFinder.countNewsInCategory(newsCategory.getID());
		//System.err.println(" news count "+count);
		boolean useDividedTable = iLayout == NEWS_SITE_LAYOUT ? true : false;
		if (L != null) {
			int len = Math.min(visibleNewsRangeEnd, L.size());
			Integer I;
			NewsHelper newsHelper;
			for (int i = Math.max(0, (visibleNewsRangeStart - 1)); i < len; i++) {
				if (numberOfExpandedNews == i) collection = true; // show the rest as
																													// collection
				newsHelper = (NewsHelper) L.get(i);
				I = new Integer(i);
				if (objectsBetween != null && objectsBetween.containsKey(I)) {
					Table t = new Table(1, 1);
					t.setCellpadding(4);
					t.add((PresentationObject) objectsBetween.get(I));
					T.add(t, sObjectAlign);
					objectsBetween.remove(I);
				}
				T.add(getNewsTable(newsHelper, locale, false, collection, iwc, (i + 1) == len), useDividedTable, "left");
			}
			// news collection
			if (showNewsCollectionButton) {
				if (!collection) {
					// adds collectionButton only if one category bound to instance:
					//if(getCategoryIds().length == 1)
					T.add(getCollectionTable(iwc, getCategoryIds()[0]));
				}
				else if (collection && isFromCollectionLink(iwc)) {
					T.add(getBackTable(iwc));
				}
				else if (collection && !isFromCollectionLink(iwc)) {
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
		}
		else {
			if (hasEdit || hasInfo) {
				T.add(new Text(iwrb.getLocalizedString("no_news", "No News")));
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
		collectionLink.addParameter(prmNewsCategoryId, iCategoryId);
		collectionLink.addParameter(prmCollection + getInstanceIDString(iwc), "true");
		if (viewPageId > 0) collectionLink.setPage(viewPageId);
		return collectionLink;
	}

	private boolean isFromCollectionLink(IWContext iwc) {
		return iwc.isParameterSet(prmCollection + getInstanceIDString(iwc));
	}

	// Make a table around each news
	private PresentationObject getNewsTable(NewsHelper newsHelper, Locale locale, boolean showAll, boolean collection, IWContext iwc, boolean isLastNews) {

		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		T.setWidth("100%");
		int row = 1;
		ContentHelper contentHelper = newsHelper.getContentHelper();
		NwNews news = newsHelper.getNwNews();
		LocalizedText locText = contentHelper.getLocalizedText(locale);

		if (iLayout == SINGLE_LINE_LAYOUT) showOnlyDates = true;

		String sNewsBody = "";
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

		Text newsInfo = getInfoText(news, newsHelper.getContentHelper().getContent(), locale, showOnlyDates, showTime, showTimeFirst, showUpdatedDate);
		if (newsInfo != null) newsInfo = setInformationAttributes(newsInfo);
		headLine = setHeadlineAttributes(headLine);
		teaser = setTextAttributes(teaser);

		// Check if using single_line_layout
		if (iLayout != SINGLE_LINE_LAYOUT) {
			if (newsInfo != null) {
				T.add(newsInfo, 1, row);
				T.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
				row++;
			}

			//////// HEADLINE PART ////////////////

			if (alignWithHeadline) {
				if (headlineImage != null) {
					headlineImage.setHorizontalSpacing(3);
					T.add(getMoreLink(headlineImage, news.getID(), iwc), 1, row);
				}
				if (headlineImageURL != null) T.add(getMoreLink(iwb.getImage(headlineImageURL), news.getID(), iwc), 1, row);
			}

			if (headlineAsLink) {
				if (setHeadlineLinktToCategoryMainViewerPage) {
					T.add(getLinkToCategoryMainViewerPage(headLine, news, iwc), 1, row);
				}
				else {
					T.add(getMoreLink(headLine, news.getID(), iwc), 1, row);
				}
			}
			else {
				T.add(headLine, 1, row);
			}
			row++;
			T.setHeight(row, String.valueOf(iSpaceBetweenNewsAndBody));
			row++;
			/////////// BODY PART //////////
			if (showTeaserText && sTeaser.length() > 0 && !showAll) {
				if (showImages && showImagesInOverview) {
					T.add(getNewsImage(newsHelper, sHeadline), 1, row);
					//if (news.getImageId()!= -1 && showImages &&
					// news.getIncludeImage()){
				}
				T.add(teaser, 1, row);
				needMoreButton = true;
			}
			else if (locText != null && !collection) {
				// counting news
				newsCount++;
				sNewsBody = locText.getBody();
				sNewsBody = sNewsBody == null ? "" : sNewsBody;

				// shortening newstext
				if (!showAll && sNewsBody.length() >= numberOfLetters) {
					sNewsBody = sNewsBody.substring(0, numberOfLetters) + "...";
					needMoreButton = true;
				}

				sNewsBody = TextSoap.formatText(sNewsBody);

				Text newsBody = new Text(sNewsBody);
				newsBody = setTextAttributes(newsBody);

				//////////// IMAGE PART ///////////
				if (showImages) {
					if (!showAll && showImagesInOverview)
						T.add(getNewsImage(newsHelper, sHeadline), 1, row);
					else if (showAll) T.add(getNewsImage(newsHelper, sHeadline), 1, row);
					//if (news.getImageId()!= -1 && showImages &&
					// news.getIncludeImage()){
				}

				T.add(newsBody, 1, row);
			}
			row++;

			///////// BACK LINK ////////////////

			if (showAll) {
				T.setHeight(row++, String.valueOf(iSpaceBetweenNewsAndBody));
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
				T.setHeight(row++, String.valueOf(iSpaceBetweenNewsAndBody));
				if (showMoreButton) {
					T.add(getMoreLink(moreImage, news.getID(), iwc), 1, row);
					T.add(Text.getNonBrakingSpace(), 1, row);
				}
				if (showMoreText) {
					Text tMore = new Text(iwrb.getLocalizedString("more", "More"));
					tMore = setMoreAttributes(tMore);
					T.add(getMoreLink(tMore, news.getID(), iwc), 1, row);
				}
			}

			//////////// ADMIN PART /////////////////////
			int ownerId = newsHelper.getContentHelper().getContent().getUserId();
			if (hasEdit || hasEditExisting || (hasAdd && (ownerId == iwc.getUserId()))) {
				T.add(getNewsAdminPart(news, iwc), 1, row);
			}
			if (!isLastNews) {
				row++;
				T.setHeight(row++, String.valueOf(iSpaceBetweenNews));
			}
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
				if (headlineImageURL != null) T.add(iwb.getImage(headlineImageURL), dateCol, 1);
			}

			if (showInfo) {
				T.setWidth(dateCol, 1, dateWidth);
				T.setVerticalAlignment(dateCol, 1, Table.VERTICAL_ALIGN_TOP);
				T.add(newsInfo, dateCol, 1);
			}
			if (spacerImage == null) {
				spacerImage = T.getTransparentCell(iwc);
				spacerImage.setWidth(iSpaceBetweenNewsAndBody);
				spacerImage.setHeight(1);
			}
			T.setAlignment(headlineCol, 1, "left");
			T.setAlignment(4, 1, "right");
			T.setWidth(headlineCol, 1, "100%");
			T.setWidth(dateCol, 1, "45");
			T.add(spacerImage, 2, 1);
			//T.add(Text.getNonBrakingSpace(2),2,1);
			if (headlineAsLink) {
				if (setHeadlineLinktToCategoryMainViewerPage) {
					T.add(getLinkToCategoryMainViewerPage(headLine, news, iwc), headlineCol, 1);
				}
				else {
					T.add(getMoreLink(headLine, news.getID(), iwc), headlineCol, 1);
				}
			}
			else {
				T.add(headLine, headlineCol, 1);
			}
			int ownerId = newsHelper.getContentHelper().getContent().getUserId();
			if (hasEdit || hasEditExisting || (hasAdd && (ownerId == iwc.getUserId()))) {
				T.add(getNewsAdminPart(news, iwc), 4, 1);
			}
			if (iSpaceBetweenNews > 0 && !isLastNews) {
				T.setHeight(2, iSpaceBetweenNews);
			}
		}
		//T.setBorder(1);
		return T;
	}

	private Link getMoreLink(PresentationObject obj, int newsId, IWContext iwc) {
		Link moreLink = new Link(obj);
		if (moreAndBackStyleClass != null) {
			moreLink.setStyle(moreAndBackStyleClass);
		}
		checkFromPage(moreLink);
		moreLink.addParameter(prmMore + getInstanceIDString(iwc), newsId);
		if (viewPageId > 0) moreLink.setPage(viewPageId);
		return moreLink;
	}

	private Text getLinkToCategoryMainViewerPage(Text obj, NwNews news, IWContext iwc) {
		Link categoryPageLink = new Link(obj);
		checkFromPage(categoryPageLink);

		try {
			String pageKey = news.getNewsCategory().getMetaData(METADATAKEY_CATEGORY_MAIN_VIEWER_PAGE);
			if (pageKey != null) {
				try {
					categoryPageLink.setPage(Integer.parseInt(pageKey));
				}
				catch (NumberFormatException e) {
					System.out.println("NewsReader.getLinkToCategoryMainViewerPage - NumberFormatException parsing pageKey");
					return obj;
				}
			}
			else {
				return obj;
			}
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			return obj;
		}
		return categoryPageLink;
	}

	private Link getBackLink(PresentationObject obj) {
		Link backLink = new Link(obj);
		if (moreAndBackStyleClass != null) {
			backLink.setStyle(moreAndBackStyleClass);
		}
		backLink.setAsBackLink(1);
		return backLink;
	}

	private PresentationObject getNewsAdminPart(NwNews news, IWContext iwc) {
		Table links = new Table(3, 1);
		Link newsEdit = new Link(iwb.getImage("/shared/edit.gif"));
		newsEdit.setWindowToOpen(NewsEditorWindow.class);
		newsEdit.addParameter(NewsEditorWindow.prmNwNewsId, news.getID());
		newsEdit.addParameter(NewsEditorWindow.prmObjInstId, getICObjectInstanceID());

		Link newsDelete = new Link(iwb.getImage("/shared/delete.gif"));
		newsDelete.setWindowToOpen(NewsEditorWindow.class);
		newsDelete.addParameter(NewsEditorWindow.prmDelete, news.getID());

		//links.setAlignment(1,1,"left");
		//links.setAlignment(2,1,"right");
		links.setCellpadding(0);
		links.setCellspacing(0);
		links.add(newsEdit, 1, 1);
		links.add(links.getTransparentCell(iwc), 2, 1);
		links.add(newsDelete, 3, 1);
		return links;
	}

	private Text getInfoText(NwNews nwNews, Content content, Locale locale, boolean ifUseOnlyDates, boolean ifShowTime, boolean ifShowTimeFirst, boolean showUpdatedDate) {
		if (showInfo) {
			String categoryName = "";
			try {
				if (showCategoryInSingleLineView) categoryName = nwNews.getNewsCategory().getName(locale);
			}
			catch (RuntimeException e) {
				System.out.println("Error in NewsReader#getInfoText(...)");
				//
			}
			return new Text(NewsFormatter.getInfoText(nwNews, content, categoryName, locale, ifUseOnlyDates, ifShowTime, ifShowTimeFirst, showUpdatedDate));
		}
		else
			return null;
	}

	public void main(IWContext iwc) throws Exception {
		hasEdit = iwc.hasEditPermission(this);
		hasAdd = iwc.hasPermission(AddPermisson, this);
		hasInfo = iwc.hasPermission(InfoPermission, this);
		hasEditExisting = iwc.hasPermission(EditExistingPermission, this);

		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		control(iwc);
	}

	public boolean deleteBlock(int instanceid) {
		return NewsBusiness.disconnectBlock(instanceid);
	}

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

	public void setInformationFontStyleClass(String styleName) {
		getInformationProxy().removeMarkupAttribute("style");
		getInformationProxy().setStyleClass(styleName);
	}

	public void setMoreAndBackFontStyle(String style) {
		getMoreProxy().setFontStyle(style);
	}

	public void setMoreAndBackFontStyleClass(String styleName) {
		getMoreProxy().removeMarkupAttribute("style");
		getMoreProxy().setStyleClass(styleName);
	}

	public void setHeadlineFontStyle(String face) {
		getHeadlineProxy().setFontStyle(face);
	}

	public void setHeadlineStyleClass(String styleName) {
		getHeadlineProxy().removeMarkupAttribute("style");
		getHeadlineProxy().setStyleClass(styleName);
	}

	public void setTextFontFontStyle(String face) {
		getTextProxy().setFontStyle(face);
	}

	public void setTextFontStyleClass(String styleName) {
		getTextProxy().removeMarkupAttribute("style");
		getTextProxy().setStyleClass(styleName);
	}

	public void setNumberOfLetters(int numberOfLetters) {
		this.numberOfLetters = Math.abs(numberOfLetters);
	}

	public void setNumberOfHeadlineLetters(int numberOfLetters) {
		this.numberOfHeadlineLetters = Math.abs(numberOfLetters);
	}

	//debug this changes the number of news displayed..that is the date alone is
	// failing
	public void setNumberOfDisplayedNews(int numberOfDisplayedNews) {
		this.limitNumberOfNews = true;
		this.numberOfDisplayedNews = Math.abs(numberOfDisplayedNews);
	}

	public void setNumberOfCollectionNews(int numberOfCollectionNews) {
		this.limitNumberOfNews = true;
		this.numberOfCollectionNews = Math.abs(numberOfCollectionNews);
	}

	public void setToViewNews(boolean viewNews) {
		this.viewNews = viewNews;
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

	public void showNewsCollectionButton(boolean showNewsCollectionButton) {
		this.showNewsCollectionButton = showNewsCollectionButton;
	}

	public void setNumberOfExpandedNews(int numberOfExpandedNews) {
		this.numberOfExpandedNews = Math.abs(numberOfExpandedNews);
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

	public void setSpaceBetweenNews(int pixels) {
		this.iSpaceBetweenNews = pixels;
	}

	public void setSpaceBetweenTitleAndBody(int pixels) {
		this.iSpaceBetweenNewsAndBody = pixels;
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
		if (objectsBetween == null) objectsBetween = new Hashtable();
		objectsBetween.put(new Integer(spaceNumber), object);
	}

	// overriding super class method
	public void add(PresentationObject MO) {
		addObjectBetween(MO, iSpaceBetween);
		if (iLayout == NEWS_SITE_LAYOUT) {
			iSpaceBetween += 2;
		}
		else
			iSpaceBetween++;
	}

	public synchronized Object clone() {
		NewsReader obj = null;
		try {
			obj = (NewsReader) super.clone();

			// integers :
			obj.numberOfLetters = numberOfLetters;
			obj.numberOfHeadlineLetters = numberOfHeadlineLetters;
			obj.numberOfDisplayedNews = numberOfDisplayedNews;
			obj.numberOfExpandedNews = numberOfExpandedNews;
			obj.numberOfCollectionNews = numberOfCollectionNews;
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
			obj.setHeadlineLinktToCategoryMainViewerPage = setHeadlineLinktToCategoryMainViewerPage;
			obj.showHeadlineImage = showHeadlineImage;
			obj.showMoreButton = showMoreButton;
			obj.alignWithHeadline = alignWithHeadline;
			obj.limitNumberOfNews = limitNumberOfNews;
			obj.enableDelete = enableDelete;
			obj.viewNews = viewNews;
			obj.newobjinst = newobjinst;
			obj.showBackText = showBackText;
			obj.showMoreText = showMoreText;
			obj.showTeaserText = showTeaserText;
			// Strings :
			obj.outerTableWidth = outerTableWidth;
			obj.sObjectAlign = sObjectAlign;
			obj.headlineImageURL = headlineImageURL;
			obj.dateAlign = dateAlign;

			if (headlineImage != null) obj.headlineImage = headlineImage;
			if (backImage != null) obj.backImage = backImage;
			if (moreImage != null) obj.moreImage = moreImage;
			if (collectionImage != null) obj.collectionImage = collectionImage;

			// Nullable :
			if (firstTableColor != null) obj.firstTableColor = firstTableColor;
			if (secondTableColor != null) obj.secondTableColor = secondTableColor;
			if (objectsBetween != null) obj.objectsBetween = objectsBetween;
			if (spacerImage != null) obj.spacerImage = spacerImage;

			// Text proxies :
			obj.textProxy = textProxy;
			obj.headlineProxy = headlineProxy;
			obj.informationProxy = informationProxy;

		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}

	/**@todo finish this for all states**/
	protected String getCacheState(IWContext iwc, String cacheStatePrefix) {
		String returnString = "";
		String parName = prmMore + getInstanceIDString(iwc);
		if (iwc.isParameterSet(parName)) {
			returnString += parName+"="+iwc.getParameter(parName);
		} else {
			if (viewNews) {
				if(visibleNewsRangeStart>0) {
					Enumeration enumer = iwc.getParameterNames();
					while (enumer.hasMoreElements()) {
						String pName = (String) enumer.nextElement();
						if(pName.startsWith(prmMore)) {
							returnString += pName+"="+iwc.getParameter(pName);
							break;
						}
					}
				}
			}
		}
		parName = prmListCategory + getInstanceIDString(iwc);
		if (iwc.isParameterSet(parName)) returnString += parName + "=" + iwc.getParameter(parName);
		parName = prmNewsCategoryId;
		if (iwc.isParameterSet(parName)) {
			returnString += parName + "=" + iwc.getParameter(parName);
			parName = prmCollection + getInstanceIDString(iwc);
			if (iwc.isParameterSet(parName)) returnString += parName + "=" + iwc.getParameter(parName);
		}
		return cacheStatePrefix + returnString;
	}

	private PresentationObject getNewsImage(NewsHelper newsHelper, String headline) {
		List files = newsHelper.getContentHelper().getFiles();
		if (files != null && !files.isEmpty()) {
			try {
				//Table imageTable = new Table(1, 2);
				ICFile imagefile = (ICFile) files.get(0);
				int imid = ((Integer) imagefile.getPrimaryKey()).intValue();
				String att = imagefile.getMetaData(NewsEditorWindow.imageAttributeKey);

				Image newsImage = new Image(imid);
				if (att != null)
					newsImage.addMarkupAttributes(getAttributeMap(att));
				else {
					newsImage.setAlignment("right");
					newsImage.setBorder(ImageBorder);
				}
				// first news
				if (newsCount == 1) {
					if (newsImage.getWidth() == null || newsImage.getWidth().length() == 0) newsImage.setMaxImageWidth(ImageWidth);
					return newsImage;
				}
				// other news
				else {
					if (newsImage.getWidth() == null || newsImage.getWidth().length() == 0) newsImage.setMaxImageWidth(ImageWidth);
					Link L = new Link(newsImage);
					L.addParameter(ImageWindow.prmImageId, imid);
					if (addImageInfo) L.addParameter(ImageWindow.prmInfo, TextSoap.convertSpecialCharacters(headline));
					L.setWindowToOpen(ImageWindow.class);
					return L;
				}
			}
			catch (Exception ex) {
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

	public void setVisibleNewsRange(int start, int end) {
		if (end > 0 && start > end) {
			this.visibleNewsRangeStart = end;
			this.visibleNewsRangeEnd = start;
		}
		else {
			this.visibleNewsRangeStart = start;
			this.visibleNewsRangeEnd = (end < 1) ? Integer.MAX_VALUE : end;
		}
	}

	/**
	 * @param visibleNewsRangeEnd
	 *          The visibleNewsRangeEnd to set.
	 */
	public void setVisibleNewsRangeEnd(int visibleNewsRangeEnd) {
		this.visibleNewsRangeEnd = visibleNewsRangeEnd;
	}

	/**
	 * @param visibleNewsRangeStart
	 *          The visibleNewsRangeStart to set.
	 */
	public void setVisibleNewsRangeStart(int visibleNewsRangeStart) {
		this.visibleNewsRangeStart = visibleNewsRangeStart;
	}

	/**
	 * @return Returns the setHeadlineLinktToCategoryMainViewerPage.
	 */
	public boolean isHeadlineLinktSetToCategoryMainViewerPage() {
		return setHeadlineLinktToCategoryMainViewerPage;
	}

	/**
	 * @param value
	 *          The setHeadlineLinktToCategoryMainViewerPage to set.
	 */
	public void setHeadlineLinktToCategoryMainViewerPage(boolean value) {
		this.setHeadlineLinktToCategoryMainViewerPage = value;
	}

	/**
	 * @param value
	 *          The showCategoryInSingleLineView to set.
	 */
	public void setToShowCategoryInSingleLineView(boolean value) {
		this.showCategoryInSingleLineView = value;
	}
	/**
	 * @param dateWidth The dateWidth to set.
	 */
	public void setDateWidth(int dateWidth) {
		this.dateWidth = dateWidth;
	}
	/**
	 * @param moreAndBackStyleClass The moreAndBackStyleClass to set.
	 */
	public void setMoreAndBackStyleClass(String moreAndBackStyleClass) {
		this.moreAndBackStyleClass = moreAndBackStyleClass;
	}
}