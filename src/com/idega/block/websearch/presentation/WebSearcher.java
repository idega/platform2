package com.idega.block.websearch.presentation;

/**
* This class is a part of the websearch webcrawler and search engine block. <br>
* It is based on the <a href="http://lucene.apache.org">Lucene</a> java search engine from the Apache group and loosly <br>
* from the work of David Duddleston of i2a.com.<br>
*
* @copyright Idega Software 2002
* @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 */


import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.block.websearch.business.*;
import com.idega.block.websearch.data.*;

public class WebSearcher extends Block {
    
  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.websearch";
  private final static String SEARCH_PARAM="iw_bl_ws_search";
  private final static String CRAWL_PARAM="iw_bl_ws_crawl";
  private final static String CRAWL_REPORT_PARAM="iw_bl_ws_cr_re";
  private final static String HITS_PER_SET_PARAM="iw_bl_ws_hi_p_s";
  private final static String EXACT_PHRASE_PARAM="iw_bl_ws_ex_ph";
  private final static String PUBLISHED_FROM_PARAM="iw_bl_ws_pb_fr";
  private final static String DETAILED_PARAM="iw_bl_ws_de";
  private final static String DIRECTION_PARAM="iw_bl_ws_set_dir";
   
      
  private final static String INDEX_NO_REPORT="0";
  private final static String INDEX_MINOR_REPORT="1";
  private final static String INDEX_NORMAL_REPORT="2";
  private final static String INDEX_DETAILED_REPORT="3";
  
  private final static String DIRECTION_NEXT="next";
  private final static String DIRECTION_PREV="prev";
  
  private WebSearchIndex index;
  private Crawler crawler;
  
  
  private boolean showResults = false;
  private boolean crawl = false;
  private boolean exact = false;
  private boolean detailed = false;
  
  private String queryString = null;
  private String direction = null;
  private int hitsPerSet = 10;
  private int publishedFromDays = 0;
  	
	
  private IWBundle iwb = null;
  private IWResourceBundle iwrb = null;
    
      
  public WebSearcher(){}
       
  public String getBundleIdentifier(){
	  return IW_BUNDLE_IDENTIFIER;
  }
  
  private void parseAction(IWContext iwc){
  	if( iwc.isParameterSet(SEARCH_PARAM) ){
  		showResults = true;	
		queryString = iwc.getParameter(SEARCH_PARAM);
		exact = iwc.isParameterSet(EXACT_PHRASE_PARAM);
		detailed = iwc.isParameterSet(DETAILED_PARAM);
		String fromDays = iwc.getParameter(PUBLISHED_FROM_PARAM);
		if( fromDays!=null ) publishedFromDays = Integer.parseInt(fromDays);	
		String perSet = iwc.getParameter(HITS_PER_SET_PARAM);
		if( perSet!=null ) hitsPerSet = Integer.parseInt(perSet);
		direction = iwc.getParameter(DIRECTION_PARAM);
  	}
  }


	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		
		parseAction(iwc);
		
		if( showResults ){
			search(iwc);
		}
		else if( crawl ){
			index = WebSearchManager.getIndex("main");//this should not be hard coded
			int report = Integer.parseInt(iwc.getParameter("report"));
			
			if ( report  > 0) {
				crawler = new Crawler(index, report, iwc.getWriter() );//change so that crawler return an arraylist and then print that or something
			}
			else{//no report
				crawler = new Crawler(index);
			}

			crawler.crawl();
			add("Done");
		}
		else{	
			Form searchForm = new Form();
			Table table = new Table(2,1);
			TextInput search = new TextInput(SEARCH_PARAM);
			SubmitButton button = new SubmitButton(iwrb.getLocalizedString("search","Search"));
			
			table.add(search,1,1);
			table.add(button,2,1);
			
			searchForm.add(table);
			
			add(searchForm);
		}
	}

	private void search(IWContext iwc){
		com.idega.block.websearch.business.WebSearcher searcher = new com.idega.block.websearch.business.WebSearcher(WebSearchManager.getIndex("main"));
		
		//hits per page
		searcher.setHitsPerSet(hitsPerSet);
		// exact phrase
		searcher.setPhraseSearch(exact);	
		// from days
		if (publishedFromDays>0) {
			searcher.setFromDays(publishedFromDays);
		}

		WebSearchHitIterator hits = searcher.search(queryString);
		//WebSearchHitIterator hits = (WebSearchHitIterator)session.getAttribute("hits"); 

		// Get Set via direction
		if (direction.equals(DIRECTION_NEXT)) hits.nextSet();
		if (direction.equals(DIRECTION_PREV)) hits.previousSet();
				
		if (hits.hasPreviousSet()) { 
			Link next = new Link("previous");
			next.addParameter(DIRECTION_PARAM,DIRECTION_PREV);
		}
		
		add("&nbsp;&nbsp;"+hits.getSetStartPosition());
		add(" - ");
		add(hits.getSetEndPosition()+"&nbsp;&nbsp;");

	    if (hits.hasNextSet()) { 
	   		Link next = new Link("next");
			next.addParameter(DIRECTION_PARAM,DIRECTION_NEXT);
		}
		
		addBreak();
		add("Query : "+hits.getQuery());
		addBreak();
		add("Total hits : "+ hits.getTotalHits());
		Table results = new Table();
		
		int row = 1;
		int row2 = 1;
		
		while (hits.hasNextInSet()) {
			WebSearchHit hit = hits.next();
			String bgColor = (hit.getRank()%2==0)?"#BBBBBB":"#CDCDCD";
			Table hitTable = new Table();
			hitTable.setColor(bgColor);
			hitTable.add(new Text("Rank: "+hit.getRank()),1,row++);
			hitTable.add(new Text("Score: "+hit.getScore()),1,row++);
			hitTable.add(new Text("Published: "+ hit.getPublishedFormated()),1,row++);
			hitTable.add(new Text("Content Type: "+hit.getContentType()),1,row++);
			hitTable.add(new Text("Keywords: "+hit.getKeywords()),1,row++);
			hitTable.add(new Text("Categories: "+hit.getCategories()),1,row++);
			hitTable.add(new Text("Description: "+hit.getDescription()),1,row++);
			hitTable.add(new Text("Title: "+hit.getTitle()),1,row++);
			hitTable.add(new Link(hit.getURL(),hit.getHREF()),1,row);
			row = 1;
			results.add(hitTable,1,row2++);
		} 
		
		add(results);
	
	
		
	}

}