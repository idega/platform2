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
  
  private final static String INDEX_NO_REPORT="0";
  private final static String INDEX_MINOR_REPORT="1";
  private final static String INDEX_NORMAL_REPORT="2";
  private final static String INDEX_DETAILED_REPORT="3";
  
  private WebSearchIndex index;
  private Crawler crawler;
  
  
  private boolean showResults = false;
  private boolean crawl = false;
  
  
  private IWBundle iwb = null;
  private IWResourceBundle iwrb = null;
    
      
  public WebSearcher(){}
       
  public String getBundleIdentifier(){
	  return IW_BUNDLE_IDENTIFIER;
  }
  
  private void parseAction(IWContext iwc){
  	if( iwc.isParameterSet(SEARCH_PARAM) ){
  		showResults = true;	
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

}