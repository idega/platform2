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
  
  private final static String HITS_ITERATOR_SESSION_PARAM = "iw_bl_ws_hitsiterator";
  
   
      
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
  private boolean canEdit = false;
    
  
  private String queryString = null;
  private String direction = null;

  private int hitsPerSet = 10;
  private int publishedFromDays = 0;
  private int report = 0;
	
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
  	else if( iwc.isParameterSet(CRAWL_PARAM) ){
  		crawl = true;
  		String sReport = iwc.getParameter(CRAWL_REPORT_PARAM);
  		if( sReport != null ) report = Integer.parseInt(sReport);
  		
  		
  	}
  	
  	canEdit =  this.hasEditPermission();
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
			Table table = new Table(2,2);
			TextInput search = new TextInput(SEARCH_PARAM);
			SubmitButton button = new SubmitButton(iwrb.getLocalizedString("search","Search"));
			
			table.add(search,1,1);
			table.add(button,2,1);
			
			searchForm.add(table);
			
			add(searchForm);
			
			if(canEdit){
				Link crawl = new Link("Index this site");
				crawl.addParameter(CRAWL_PARAM,"true");
				crawl.addParameter(CRAWL_REPORT_PARAM,INDEX_DETAILED_REPORT);
				table.add(crawl,1,2);				
			}
		
			
		}
	}

	private void search(IWContext iwc){
		

		WebSearchHitIterator hits = (WebSearchHitIterator) iwc.getSessionAttribute(HITS_ITERATOR_SESSION_PARAM+queryString);
		
		if( hits==null ){
			try{
				com.idega.block.websearch.business.WebSearcher searcher = new com.idega.block.websearch.business.WebSearcher(WebSearchManager.getIndex("main"));
				//hits per page
				searcher.setHitsPerSet(hitsPerSet);
				// exact phrase
				searcher.setPhraseSearch(exact);	
				// from days
				if (publishedFromDays>0) {
					searcher.setFromDays(publishedFromDays);
				}
				hits = searcher.search(queryString);
				iwc.setSessionAttribute(HITS_ITERATOR_SESSION_PARAM+queryString,hits);
				
			}
			catch(Exception e){
				e.printStackTrace();
				add("You need to crawl and index first!");	
			}
		}
		
		if(hits!=null){
			// Get Set via direction
			if (direction!=null && direction.equals(DIRECTION_NEXT)) hits.nextSet();
			if (direction!=null && direction.equals(DIRECTION_PREV)) hits.previousSet();
					
			if (hits.hasPreviousSet()) { 
				Link prev = new Link("previous");
				prev.addParameter(DIRECTION_PARAM,DIRECTION_PREV);
				prev.addParameter(SEARCH_PARAM,queryString);
				add(prev);
			}
			
			add("&nbsp;&nbsp;"+hits.getSetStartPosition());
			add(" - ");
			add(hits.getSetEndPosition()+"&nbsp;&nbsp;");
	
		    if (hits.hasNextSet()) { 
		   		Link next = new Link("next");
				next.addParameter(DIRECTION_PARAM,DIRECTION_NEXT);
				next.addParameter(SEARCH_PARAM,queryString);
				add(next);
			}
			
			addBreak();
			add("Query : "+hits.getQuery());
			addBreak();
			add("Total hits : "+ hits.getTotalHits());
			Table results = new Table();
			results.setWidth(Table.HUNDRED_PERCENT);
			results.setHeight(Table.HUNDRED_PERCENT);
			results.setCellpaddingAndCellspacing(0);
			
			int row = 1;
			int row2 = 1;
			
			while (hits.hasNextInSet()) {
				WebSearchHit hit = hits.next();
				String bgColor = (hit.getRank()%2==0)?"#BBBBBB":"#CDCDCD";
				Table hitTable = new Table();
				hitTable.setWidth(Table.HUNDRED_PERCENT);
				hitTable.setHeight(Table.HUNDRED_PERCENT);
			
				hitTable.setColor(bgColor);
				hitTable.add(new Text("Rank: "+hit.getRank()),1,row++);
				//if detailed ?
				//hitTable.add(new Text("Score: "+hit.getScore()),1,row++);
				//hitTable.add(new Text("Published: "+ hit.getPublishedFormated()),1,row++);
				//hitTable.add(new Text("Content Type: "+hit.getContentType()),1,row++);
				//hitTable.add(new Text("Keywords: "+hit.getKeywords()),1,row++); veit ekki afhverju thetta skilar alltaf null !?
				//hitTable.add(new Text("Categories: "+hit.getCategories()),1,row++);
				//hitTable.add(new Text("Description: "+hit.getDescription()),1,row++);
				hitTable.add(new Text("Title: "+hit.getTitle()),1,row++);
				String contents = hit.getContents();
				if(contents!=null)	hitTable.add(new Text(contents+"..."),1,row++);
				hitTable.add(new Link(hit.getURL(),hit.getHREF()),1,row);
				row = 1;
				results.add(hitTable,1,row2++);
			} 
			
			add(results);
		}
	
	
		
	}

}