/*
 * $Id: WebCrawlerSearchPlugin.java,v 1.2 2005/02/01 17:29:13 eiki Exp $
 * Created on Jan 17, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.websearch.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.queryParser.ParseException;
import com.idega.block.websearch.data.WebSearchHit;
import com.idega.block.websearch.data.WebSearchIndex;
import com.idega.core.search.business.Search;
import com.idega.core.search.business.SearchPlugin;
import com.idega.core.search.business.SearchQuery;
import com.idega.core.search.data.BasicSearch;
import com.idega.core.search.data.BasicSearchResult;
import com.idega.core.search.data.SimpleSearchQuery;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;


/**
 * 
 *  Last modified: $Date: 2005/02/01 17:29:13 $ by $Author: eiki $
 * This class implements the Searchplugin interface and can therefore be used in a Search block (com.idega.core.search)<br>
 * for searching the websites that are crawled and indexed by the websearch site crawler.
 * To use it simply register this class as a iw.searchable component in a bundle.
 * @author <a href="mailto:eiki@idega.com">Eirikur S. Hrafnsson</a>
 * @version $Revision: 1.2 $
 */
public class WebCrawlerSearchPlugin implements SearchPlugin {

	public static final String SEARCH_NAME_LOCALIZABLE_KEY = "webcrawler_search.name";
	public static final String SEARCH_DESCRIPTION_LOCALIZABLE_KEY = "webcrawler_search.description";
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.websearch";
	
	public static final String SEARCH_TYPE = "pages";

	private IWMainApplication iwma = null;
	
	public WebCrawlerSearchPlugin() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.search.business.SearchPlugin#getAdvancedSearchSupportedParameters()
	 */
	public List getAdvancedSearchSupportedParameters() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.core.search.business.SearchPlugin#getSupportsSimpleSearch()
	 */
	public boolean getSupportsSimpleSearch() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.idega.core.search.business.SearchPlugin#getSupportsAdvancedSearch()
	 */
	public boolean getSupportsAdvancedSearch() {
		return false;
	}
	

	/* (non-Javadoc)
	 * @see com.idega.core.search.business.SearchPlugin#initialize(com.idega.idegaweb.IWMainApplication)
	 */
	public boolean initialize(IWMainApplication iwma) {
		this.iwma = iwma;
		
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.idega.core.search.business.SearchPlugin#destroy(com.idega.idegaweb.IWMainApplication)
	 */
	public void destroy(IWMainApplication iwma) {
	}

	/* (non-Javadoc)
	 * @see com.idega.core.search.business.SearchPlugin#createSearch(com.idega.core.search.business.SearchQuery)
	 */
	public Search createSearch(SearchQuery searchQuery) {
		IWBundle bundle = iwma.getBundle(IW_BUNDLE_IDENTIFIER);
		IWResourceBundle iwrb =  bundle.getResourceBundle(IWContext.getInstance());
		String queryString = ((SimpleSearchQuery)searchQuery).getSimpleSearchQuery();
		List results = new ArrayList();
		BasicSearch search = new BasicSearch();
		search.setSearchName(getSearchName());
		search.setSearchType(SEARCH_TYPE);
		search.setSearchQuery(searchQuery);
		

		
		
		try {
//			todo use all indexes not just main
			WebSearchIndex index = WebSearchManager.getIndex("main");
			if(index!=null){
				WebSearcher searcher = new WebSearcher(index);
				WebSearchHitIterator hits = searcher.search(queryString);
				
				while (hits.hasNext()) {
					WebSearchHit hit = (WebSearchHit) hits.next();
					
					//String extraInfo = hit.getHREF() + " - " + hit.getContentType() + " - " + iwrb.getLocalizedString("rank", "rank") + ": " + hit.getRank();
					String extraInfo = hit.getContentType();
					String sTitle = hit.getTitle();
					if (sTitle == null){
						sTitle = iwrb.getLocalizedString("websearch.untitled", "Untitled");
					}
					else if (sTitle.equals("null")){
						sTitle = iwrb.getLocalizedString("websearch.untitled", "Untitled");
					}
					
					String contents = hit.getContents(queryString); //could be heavy....
	
					if (contents != null) {
						contents = "..." + contents + "...";
					}
					
					BasicSearchResult result = new BasicSearchResult();
					result.setSearchResultType(SEARCH_TYPE);
					result.setSearchResultName(sTitle);
					result.setSearchResultURI(hit.getHREF());
					result.setSearchResultAbstract(contents);
					result.setSearchResultExtraInformation(extraInfo);
					
					results.add(result);
				}
			}
			else{
				System.err.println("WebCrawlerSearchPlugin: index does not exist.");
			}				
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		
	
		        
		search.setSearchResults(results);

		
		return search;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.core.search.business.SearchPlugin#getSearchName()
	 */
	public String getSearchName() {
		IWBundle bundle = iwma.getBundle(IW_BUNDLE_IDENTIFIER);
		return bundle.getResourceBundle(IWContext.getInstance()).getLocalizedString(SEARCH_NAME_LOCALIZABLE_KEY,"Pages");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.core.search.business.SearchPlugin#getSearchDescription()
	 */
	public String getSearchDescription() {
		IWBundle bundle = iwma.getBundle(IW_BUNDLE_IDENTIFIER);
		return bundle.getResourceBundle(IWContext.getInstance()).getLocalizedString(SEARCH_DESCRIPTION_LOCALIZABLE_KEY,
				"Searches whole websites as indexed the websearch block.");
	}
	
}