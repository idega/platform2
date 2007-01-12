package com.idega.block.websearch.data;

/**
 * <p><code>WebSearchIndex</code>
 *	A WebSearchIndex contains information on where to store indexes, what
 *  URLs to scan and what scopes the crawler is limited to.
 * </p>
* This class is a part of the websearch webcrawler and search engine block. <br>
* It is based on the <a href="http://lucene.apache.org">Lucene</a> java search engine from the Apache group and loosly <br>
* from the work of David Duddleston of i2a.com.<br>
*
* @copyright Idega Software 2002
* @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 */
public final class WebSearchIndex {
    
    private String indexPath;
    private String[] seed;
    private String[] scope;
    
    public WebSearchIndex(String path) {
        
        this.indexPath = path + "/index";
        
    }
    public WebSearchIndex(String path, String[] seed, String[] scope) {
        
        this.indexPath = path + "/index";
        this.seed = seed;
        this.scope = scope;
    }
    public String getIndexPath() {
        
        return this.indexPath;
        
    }
    public String[] getScope() {
        
        return this.scope;
        
    }
    public String[] getSeed() {
        
        return this.seed;
        
    }
}
