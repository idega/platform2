package com.idega.block.websearch.presentation;

/**
* This class is a part of the websearch webcrawler and search engine block. <br>
* It is based on the <a href="http://lucene.apache.org">Lucene</a> java search engine from the Apache group and loosly <br>
* from the work of David Duddleston of i2a.com.<br>
*
* @copyright Idega Software 2002
* @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.block.websearch.business.*;
import com.idega.block.websearch.data.*;
import com.idega.core.builder.data.ICPage;

public class WebSearcher extends Block {

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.websearch";
	private final static String SEARCH_PARAM = "iw_bl_ws_search";
	private final static String CRAWL_PARAM = "iw_bl_ws_crawl";
	private final static String CRAWL_REPORT_PARAM = "iw_bl_ws_cr_re";
	private final static String HITS_PER_SET_PARAM = "iw_bl_ws_hi_p_s";
	private final static String EXACT_PHRASE_PARAM = "iw_bl_ws_ex_ph";
	private final static String PUBLISHED_FROM_PARAM = "iw_bl_ws_pb_fr";
	private final static String DETAILED_PARAM = "iw_bl_ws_de";
	private final static String DIRECTION_PARAM = "iw_bl_ws_set_dir";

	private final static String HITS_ITERATOR_SESSION_PARAM = "iw_bl_ws_hitsiterator";
	private final static String HITS_MAP_SESSION_PARAM = "iw_bl_ws_hitsmap";

	private final static String INDEX_NO_REPORT = "0";
	private final static String INDEX_MINOR_REPORT = "1";
	private final static String INDEX_NORMAL_REPORT = "2";
	private final static String INDEX_DETAILED_REPORT = "3";

	private final static String DIRECTION_NEXT = "next";
	private final static String DIRECTION_PREV = "prev";

	private WebSearchIndex index;
	private Crawler crawler;

	private boolean showResults = false;
	private boolean crawl = false;
	private boolean exact = false;
	private boolean detailed = false;
	private boolean canEdit = false;
	private boolean showOnlySearch = false;
	private boolean showButtonsAsLinks = false;

	private String queryString = null;
	private String direction = null;
	private Image searchImage = null;
	private String searchWidth = null;
	
	private Link titleLinkProto = new Link();
	private Text contentTextProto = new Text();
	private Text extraInfoTextProto = new Text();
	private String contentTextProtoStyle = null;
	private String extraInfoTextProtoStyle = null;
	private String titleLinkProtoStyle = null;

	private int hitsPerSet = 10;
	private int publishedFromDays = 0;
	private int report = 0;

	private IWBundle iwb = null;
	private IWResourceBundle iwrb = null;
	
	private static final int VERTICAL_LAYOUT = 1;
	private static final int HORIZONTAL_LAYOUT = 2;
	
	private int layout = HORIZONTAL_LAYOUT;
	private String searchStyleClass;
	private String arrowStyleClass;
	private ICPage submitPage;
	private String inputStyle;
	private String inputWidth;
	private int spaceBetween = 3;
	
	private Collection ignoreParameters;

	public WebSearcher() {
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	private void parseAction(IWContext iwc) {
		if (iwc.isParameterSet(SEARCH_PARAM)) {
			this.showResults = true;
			this.queryString = iwc.getParameter(SEARCH_PARAM);
			this.exact = iwc.isParameterSet(EXACT_PHRASE_PARAM);
			this.detailed = iwc.isParameterSet(DETAILED_PARAM);
			String fromDays = iwc.getParameter(PUBLISHED_FROM_PARAM);
			if (fromDays != null) {
				this.publishedFromDays = Integer.parseInt(fromDays);
			}
			String perSet = iwc.getParameter(HITS_PER_SET_PARAM);
			if (perSet != null) {
				this.hitsPerSet = Integer.parseInt(perSet);
			}
			this.direction = iwc.getParameter(DIRECTION_PARAM);
		}
		else if (iwc.isParameterSet(CRAWL_PARAM)) {
			this.crawl = true;
			String sReport = iwc.getParameter(CRAWL_REPORT_PARAM);
			if (sReport != null) {
				this.report = Integer.parseInt(sReport);
			}

		}
		
		if (this.showOnlySearch) {
			this.showResults = false;
		}

		this.canEdit = this.hasEditPermission();
	}

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		this.iwb = getBundle(iwc);
		this.iwrb = getResourceBundle(iwc);

		parseAction(iwc);

		if (this.showResults) {
			prepStyles();
			search(iwc);
		}
		else if (this.crawl) {
			iwc.removeSessionAttribute(HITS_MAP_SESSION_PARAM);
			crawl();
		}
		else {
			add(getSearchForm());
		}
	}

	private void crawl() throws Exception {
		add(this.iwrb.getLocalizedString("indexing", "Indexing..."));
		addBreak();

		this.index = WebSearchManager.getIndex("main"); //this should not be hard coded

		if (this.report > 0) {
			this.crawler = new Crawler(this.index, this.report); //change so that crawler return an arraylist and then print that or something
		}
		else { //no report
			this.crawler = new Crawler(this.index);
		}
		this.crawler.addIgnoreParameters(this.ignoreParameters);
		this.crawler.crawl();
		addBreak();
		add(this.iwrb.getLocalizedString("done", "Done!"));
		addBreak();
		add(new BackButton(this.iwrb.getLocalizedString("back", "back")));

	}

	private Form getSearchForm() {
		Form searchForm = new Form();
		if (this.submitPage != null) {
			searchForm.setPageToSubmitTo(this.submitPage);
		}
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		if (this.searchWidth != null) {
			table.setWidth(this.searchWidth);
		}
		TextInput search = new TextInput(SEARCH_PARAM);
		if (this.queryString != null) {
			search.setContent(this.queryString);
		}
		else {
			search.setContent(this.iwrb.getLocalizedString("search_entire_site", "Search entire site"));
			search.setOnFocus("this.value=''");
		}
		if (this.inputStyle != null) {
			search.setStyleAttribute(this.inputStyle);
		}
		if (this.inputWidth != null) {
			search.setWidth(this.inputWidth);
		}
		Link crawl = null;
		if (this.canEdit) {
			crawl = new Link(this.iwrb.getLocalizedString("index.this.site", "Index this site (done every 24h)"));
			crawl.addParameter(CRAWL_PARAM, "true");
			crawl.addParameter(CRAWL_REPORT_PARAM, INDEX_NORMAL_REPORT);
		}

		if (this.layout == HORIZONTAL_LAYOUT) {
			table.setCellpadding(3);
	
			table.add(search, 1, 1);
			if (!this.showButtonsAsLinks) {
				SubmitButton button = null;
				if (this.searchImage != null) {
					button = new SubmitButton(this.searchImage);
				}
				else {
					button = new SubmitButton(this.iwrb.getLocalizedString("search", "Search"));
				}
				table.add(button, 2, 1);
			}
			else {
				Link link = new Link(this.iwrb.getLocalizedString("search", "Search"));
				link.setToFormSubmit(searchForm);
				if (this.searchStyleClass != null) {
					link.setStyle(this.searchStyleClass);
				}
				table.add(link, 2, 1);
			}
			if (crawl != null) {
				table.mergeCells(1, 2, 2, 2);
				table.add(crawl, 2, 2);
			}
		}
		else if (this.layout == VERTICAL_LAYOUT) {
			table.add(search, 1, 1);
			table.setHeight(2, this.spaceBetween);
			
			Link link = new Link(this.iwrb.getLocalizedString("search", "Search"));
			link.setToFormSubmit(searchForm);
			if (this.searchStyleClass != null) {
				link.setStyle(this.searchStyleClass);
			}

			Link arrow = new Link("&gt;&gt;");
			arrow.setToFormSubmit(searchForm);
			if (this.arrowStyleClass != null) {
				arrow.setStyle(this.arrowStyleClass);
			}
			
			table.add(link, 1, 3);
			table.add(Text.NON_BREAKING_SPACE, 1, 3);
			table.add(arrow, 1, 3);
			
			if (crawl != null) {
				table.setHeight(4, 6);
				table.add(crawl, 1, 5);
			}
		}

		searchForm.add(table);


		return searchForm;

	}

	private void search(IWContext iwc) {
		add(getSearchForm());
		addBreak();

		WebSearchHitIterator hits = getHitsFromSession(iwc,HITS_ITERATOR_SESSION_PARAM + this.queryString);

		if (hits == null) {
			try {
				com.idega.block.websearch.business.WebSearcher searcher = new com.idega.block.websearch.business.WebSearcher(WebSearchManager.getIndex("main"));
				//hits per page
				searcher.setHitsPerSet(this.hitsPerSet);
				// exact phrase
				searcher.setPhraseSearch(this.exact);
				// from days
				if (this.publishedFromDays > 0) {
					searcher.setFromDays(this.publishedFromDays);
				}
				hits = searcher.search(this.queryString);
				
				setHitsToSession(iwc,HITS_ITERATOR_SESSION_PARAM + this.queryString, hits);
			
  			
			}
			catch (Exception e) {
				e.printStackTrace();
				add(this.iwrb.getLocalizedString("you.have.to.index.first", "You need to run the indexer first!"));
			}
			
			
		}
		
		if(hits!=null) {
			addBreak();
			add(getResultSetInfo(hits));
			addBreak();

			Table results = new Table();
			results.setWidth(Table.HUNDRED_PERCENT);
			//results.setHeight(Table.HUNDRED_PERCENT);
			results.setCellpaddingAndCellspacing(0);

			int row = 1;
			int row2 = 1;

			while (hits.hasNextInSet()) {
				WebSearchHit hit = hits.next();
				//String bgColor = (hit.getRank()%2==0)?"#BBBBBB":"#CDCDCD";
				Table hitTable = new Table(1, 3);
				hitTable.setWidth(Table.HUNDRED_PERCENT);
				hitTable.setHeight(50);
				hitTable.setCellpadding(0);
				hitTable.setCellspacing(1);

				//hitTable.setColor(bgColor);
				//if detailed ?
				//hitTable.add(new Text("Score: "+hit.getScore()),1,row++);
				//hitTable.add(new Text("Published: "+ hit.getPublishedFormated()),1,row++);
				//hitTable.add(new Text("Content Type: "+hit.getContentType()),1,row++);
				//hitTable.add(new Text("Keywords: "+hit.getKeywords()),1,row++); veit ekki afhverju thetta skilar alltaf null !?
				//hitTable.add(new Text("Categories: "+hit.getCategories()),1,row++);
				//hitTable.add(new Text("Description: "+hit.getDescription()),1,row++);
				Link title = (Link) this.titleLinkProto.clone();
				title.setURL(hit.getURL());

				String sTitle = hit.getTitle();
				if (sTitle == null) {
					sTitle = this.iwrb.getLocalizedString("websearch.untitled", "Untitled");
				}
				else if (sTitle.equals("null")) {
					sTitle = this.iwrb.getLocalizedString("websearch.untitled", "Untitled");
				}

				title.setText(sTitle);

				hitTable.add(title, 1, row++);
				String contents = hit.getContents(this.queryString); //could be heavy....

				if (contents != null) {
					contents = "..." + contents + "...";
					Text contentsText = (Text) this.contentTextProto.clone();
					contentsText.setText(contents);
					hitTable.add(contentsText, 1, row++);

				}

				String extraInfo = hit.getHREF() + " - " + hit.getContentType() + " - " + this.iwrb.getLocalizedString("rank", "rank") + ": " + hit.getRank();
				Text extraInfoText = (Text) this.extraInfoTextProto.clone();
				extraInfoText.setText(extraInfo);
				hitTable.add(extraInfoText, 1, row);
				row = 1;
				results.add(hitTable, 1, row2++);
				results.add(Text.getBreak(), 1, row2++);

			}

			add(results);
			
		}

	}

	private void prepStyles() {
		if (this.contentTextProtoStyle != null) {
			this.contentTextProto.setFontStyle(this.contentTextProtoStyle);
		}
		else {
			this.contentTextProto.setFontFace(Text.FONT_FACE_ARIAL);
			this.contentTextProto.setFontSize(Text.FONT_SIZE_10_HTML_2);

		}

		if (this.extraInfoTextProtoStyle != null) {
			this.extraInfoTextProto.setFontStyle(this.extraInfoTextProtoStyle);
		}
		else {
			this.extraInfoTextProto.setFontFace(Text.FONT_FACE_ARIAL);
			this.extraInfoTextProto.setFontSize(Text.FONT_SIZE_10_HTML_2);
			this.extraInfoTextProto.setFontColor("#008000");
		}

		if (this.titleLinkProtoStyle != null) {
			this.titleLinkProto.setFontStyle(this.titleLinkProtoStyle);
		}
		else {
			this.titleLinkProto.setFontSize(Text.FONT_SIZE_12_HTML_3);
			this.titleLinkProto.setFontColor("#0000CC");
		}
	}

	private Table getResultSetInfo(WebSearchHitIterator hits) {
		//temporary html tags inline and need to localize
		Table info = new Table();
		info.setWidth(Table.HUNDRED_PERCENT);
		info.setColor("#3366cc");

		// Get Set by direction
		if (this.direction != null && this.direction.equals(DIRECTION_NEXT)) {
			hits.nextSet();
		}
		if (this.direction != null && this.direction.equals(DIRECTION_PREV)) {
			hits.previousSet();
		}

		Text textProto = new Text();
		textProto.setFontColor("#FFFFFF");
		textProto.setFontFace(Text.FONT_FACE_ARIAL);
		textProto.setFontSize(Text.FONT_SIZE_10_HTML_2);

		Text text1 = (Text) textProto.clone();
		Text text2 = (Text) textProto.clone();
		Text text3 = (Text) textProto.clone();

		text1.setText(this.iwrb.getLocalizedString("searched.for", "Searched for") + " : <b>" + hits.getQuery() + "</b>. " + this.iwrb.getLocalizedString("results", "Results") + " ");
		info.add(text1, 1, 1);

		if (hits.hasPreviousSet()) {
			Link prev = new Link("<< ");
			prev.addParameter(DIRECTION_PARAM, DIRECTION_PREV);
			prev.addParameter(SEARCH_PARAM, this.queryString);
			prev.setFontColor("#FFFFFF");
			prev.setFontFace(Text.FONT_FACE_ARIAL);
			prev.setFontSize(Text.FONT_SIZE_10_HTML_2);
			prev.setBold();

			info.add(prev, 1, 1);
		}

		text2.setText("<b>" + hits.getSetStartPosition() + " - " + hits.getSetEndPosition() + "</b>");
		info.add(text2, 1, 1);

		if (hits.hasNextSet()) {
			Link next = new Link(" >> ");
			next.addParameter(DIRECTION_PARAM, DIRECTION_NEXT);
			next.addParameter(SEARCH_PARAM, this.queryString);
			next.setFontColor("#FFFFFF");
			next.setFontFace(Text.FONT_FACE_ARIAL);
			next.setFontSize(Text.FONT_SIZE_10_HTML_2);
			next.setBold();

			info.add(next, 1, 1);
		}

		text3.setText(" " + this.iwrb.getLocalizedString("of", "of") + " <b>" + hits.getTotalHits() + "</b>" + ".");
		info.add(text3, 1, 1);

		return info;

	}

	public void setContentTextStyle(String style) {
		this.contentTextProtoStyle = style;
	}

	public void setExtraInfoTextStyle(String style) {
		this.extraInfoTextProtoStyle = style;
	}

	public void setTitleLinkStyle(String style) {
		this.titleLinkProtoStyle = style;
	}

	private Map getSessionMap(IWContext iwc) {
		Map sessionMap = (Map) iwc.getSessionAttribute(HITS_MAP_SESSION_PARAM);
		if (sessionMap == null) {
			sessionMap = new HashMap();
			iwc.setSessionAttribute(HITS_MAP_SESSION_PARAM, sessionMap);
		}
		return sessionMap;
	}
	
	private WebSearchHitIterator getHitsFromSession(IWContext iwc, String value) {
		return (WebSearchHitIterator) getSessionMap(iwc).get(value);
	}
	
	private void setHitsToSession(IWContext iwc, String value, WebSearchHitIterator wshi) {
		getSessionMap(iwc).put(value,wshi);
	}

	/**
	 * @param showOnlySearch The showOnlySearch to set.
	 */
	public void setShowOnlySearch(boolean showOnlySearch) {
		this.showOnlySearch = showOnlySearch;
	}
	/**
	 * @param arrowStyleClass The arrowStyleClass to set.
	 */
	public void setArrowStyleClass(String arrowStyleClass) {
		this.arrowStyleClass = arrowStyleClass;
	}
	/**
	 * @param inputStyle The inputStyle to set.
	 */
	public void setInputStyle(String inputStyle) {
		this.inputStyle = inputStyle;
	}
	/**
	 * @param searchStyleClass The searchStyleClass to set.
	 */
	public void setSearchStyleClass(String searchStyleClass) {
		this.searchStyleClass = searchStyleClass;
	}
	/**
	 * @param submitPage The submitPage to set.
	 */
	public void setSubmitPage(ICPage submitPage) {
		this.submitPage = submitPage;
	}
	
	public void setVerticalLayout(boolean vertical) {
		if (vertical) {
			this.layout = VERTICAL_LAYOUT;
		}
		else {
			this.layout = HORIZONTAL_LAYOUT;
		}
	}
	/**
	 * @param inputWidth The inputWidth to set.
	 */
	public void setInputWidth(String inputWidth) {
		this.inputWidth = inputWidth;
	}
	/**
	 * @param spaceBetween The spaceBetween to set.
	 */
	public void setSpaceBetween(int spaceBetween) {
		this.spaceBetween = spaceBetween;
	}
	
	public void setParameterToIgnore(String parameter) {
		if (this.ignoreParameters == null) {
			this.ignoreParameters = new ArrayList();
		}
		this.ignoreParameters.add(parameter);
	}

	
	public void setShowButtonsAsLinks(boolean showButtonsAsLinks) {
		this.showButtonsAsLinks = showButtonsAsLinks;
	}

	
	public void setSearchImage(Image searchImage) {
		this.searchImage = searchImage;
	}

	
	public void setSearchWidth(String searchWidth) {
		this.searchWidth = searchWidth;
	}
}