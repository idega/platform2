package com.idega.block.websearch.business;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.idega.block.websearch.data.WebSearchIndex;

/**
 * <p><code>WebSearchManager</code> Manages WebSearchIndexes.
* This class is a part of the websearch webcrawler and search engine block. <br>
* It is based on the <a href="http://lucene.apache.org">Lucene</a> java search engine from the Apache group and loosly <br>
* from the work of David Duddleston of i2a.com.<br>
*
* @copyright Idega Software 2002
* @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 */
public final class WebSearchManager {
    
    private static WebSearchManager manager = new WebSearchManager();
    private static HashMap indexes;
    
    public WebSearchManager() {
        
        indexes = new HashMap();
    }
    
    public static WebSearchIndex getIndex(String key) {
        
        WebSearchIndex index = (WebSearchIndex)indexes.get(key);
        if (index == null) {
            System.out.println("no index: " + key);
        }
        return index;
        
    }
    
    public static void addIndex(String name, String index,
            String[] seeds, String[] scopes) {
        
        indexes.put(name, new WebSearchIndex(index, seeds, scopes));
        
    }
    
    /**
	 * Use JDOM to parse database configuration XML
	 * Creation date: (11/7/99 7:30:26 PM)
	 */
	public static void parseConfigXML(String configURI) {
		//System.out.println("Loading and parsing WebSearchManager config XML");
		//System.out.println(configURI);
		try {
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build("file://"+configURI);
			Element root = doc.getRootElement();
			
			// Get indexes 
			List indexElements = root.getChildren("index");
			
			// Iterate elements Elements and add to Types
			for (int i = 0; i < indexElements.size(); i++) {
			   Element indexElement = (Element)indexElements.get(i);
				// add new index to indexes HashMap
               String name = indexElement.getChild("name").getTextTrim();
               String indexURI = indexElement.getChild("indexURI").getTextTrim();
               indexURI = LinkParser.getRealPath(configURI, indexURI, File.separator);

               List seedElements = indexElement.getChildren("seed");
               String[] seeds = new String[seedElements.size()];
               for (int i2 = 0; i2 < seedElements.size(); i2++) {
                   seeds[i2] = ((Element)seedElements.get(i2)).getTextTrim();
               }
               
               List scopeElements = indexElement.getChildren("scope");
               String[] scopes = new String[scopeElements.size()];
               for (int i2 = 0; i2 < scopeElements.size(); i2++) {
                   scopes[i2] = ((Element)scopeElements.get(i2)).getTextTrim();
               }
                      
               WebSearchIndex webSearchIndex =  new WebSearchIndex(
                        indexURI, seeds, scopes);
               
               indexes.put(name, webSearchIndex);		
			}
		
		} /*catch (JDOMException e) {
			if (e.getRootCause() != null) {
				e.getRootCause().printStackTrace();
			} else {
				e.printStackTrace();
			}}*/
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("WebSearch: Error reading config file, pointing index to http://localhost by default");
			String localhost = new String("http://localhost/");
			
			String indexURI = LinkParser.getRealPath(configURI, "../search/main", File.separator);
			String[] seeds = {localhost};
			String[] scopes = {localhost};

			
			WebSearchIndex webSearchIndex =  new WebSearchIndex(indexURI, seeds, scopes);
               
            indexes.put("main", webSearchIndex);
               
               
		}
	}
}
