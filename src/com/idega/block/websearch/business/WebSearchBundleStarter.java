package com.idega.block.websearch.business;

/**
* This class is a part of the websearch webcrawler and search engine block. <br>
* It is based on the <a href="http://lucene.apache.org">Lucene</a> java search engine from the Apache group and loosly <br>
* from the work of David Duddleston of i2a.com.<br>
*
* @copyright Idega Software 2002
* @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 */

import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.util.FileUtil;

public class WebSearchBundleStarter implements IWBundleStartable {
    
  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.websearch";
      
  public WebSearchBundleStarter(){}
    
  public void start(IWBundle bundle){
  	String xmlFile = bundle.getResourcesRealPath()+FileUtil.getFileSeparator()+"websearch.xml";
  	System.out.println("WebSearch: Starting up...");
  	System.out.println("WebSearch: loading configuration from : "+xmlFile);
  	WebSearchManager.parseConfigXML(xmlFile);
  }
    
  public String getBundleIdentifier(){
	  return IW_BUNDLE_IDENTIFIER;
  }


}