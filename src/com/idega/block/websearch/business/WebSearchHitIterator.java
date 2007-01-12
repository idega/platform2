package com.idega.block.websearch.business;


import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;

import com.idega.block.websearch.data.WebSearchHit;


/**
 * <p><code>WebSearchHitIterator</code> Iterator for search results which contain WebSearchHit documents.<br>
* This class is a part of the websearch webcrawler and search engine block. <br>
* It is based on the <a href="http://lucene.apache.org">Lucene</a> java search engine from the Apache group and loosly <br>
* from the work of David Duddleston of i2a.com.<br>
*
* @copyright Idega Software 2002
* @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 */

public final class WebSearchHitIterator {
    
    private String query;
    private Hits hits;
    
    private int position; // current posistion
    private int hitsPerSet;
    private int setLimit;
    private Document document; // current document
    
    /**
     * Insert the method's description here.
     * Creation date: (2/16/2001 2:06:31 PM)
     */
    private WebSearchHitIterator() {
        
        
    }
    /**
     * Insert the method's description here.
     * Creation date: (2/16/2001 2:06:31 PM)
     */
    public WebSearchHitIterator(String q, Hits h, int hps) {
        
        this.query = q;
        this.hits = h;
        this.hitsPerSet = hps;
        this.setLimit = hps;
        
        
    }
    public int getHitsPerSet() {
        return this.hitsPerSet;
    }
    public int getPosition() {
        return this.position;
    }
    public String getQuery() {
        return this.query;
    }
    public int getSetEndPosition() {
        return this.setLimit < this.hits.length() ? this.setLimit: this.hits.length();
    }
    public int getSetStartPosition() {
        return this.setLimit - this.hitsPerSet;
    }
    public int getTotalHits() {
        return this.hits.length();
    }
    public boolean hasNext() {
        
        return this.position < this.hits.length();
        
    }
    public boolean hasNextInSet() {
        
        return this.position < this.setLimit && this.position < this.hits.length();
        
    }
    public boolean hasNextSet() {
        
        return this.setLimit < this.hits.length();
        
    }
    public boolean hasPreviousSet() {
        
        return this.setLimit - this.hitsPerSet >= this.hitsPerSet;
        
    }
    public int length() {
        
        try {
            return this.hits.length();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public WebSearchHit next() {
        
        
        WebSearchHit hit = null;
        
        if (hasNext()) {
            try {
                this.position++;
                hit = new WebSearchHit(this.hits.doc(this.position - 1), this.position, this.hits.score(this.position - 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return hit;
        
    }
/*
    OLD CODE...
 
    WebSearchHit hit = null;
 
    if (hasNext()) {
        try {
            position++;
            Document doc = hits.doc(position - 1);
            WebSearchURL url = database.get(doc.get("keyURL"));
            hit = new WebSearchHit(url, doc, position, hits.score(position - 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    return hit;
 */
    public void nextSet() {
        
        if (this.setLimit < this.hits.length() + 1) {
            this.setLimit = this.setLimit + this.hitsPerSet;
            this.position = this.setLimit - this.hitsPerSet;
        }
        
    }
    public void previousSet() {
        
        if (this.setLimit - this.hitsPerSet >= this.hitsPerSet) {
            this.setLimit = this.setLimit - this.hitsPerSet;
            this.position = this.setLimit - this.hitsPerSet;
        }
        
    }
}
