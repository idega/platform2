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
        
        query = q;
        hits = h;
        hitsPerSet = hps;
        setLimit = hps;
        
        
    }
    public int getHitsPerSet() {
        return hitsPerSet;
    }
    public int getPosition() {
        return position;
    }
    public String getQuery() {
        return query;
    }
    public int getSetEndPosition() {
        return setLimit < hits.length() ? setLimit: hits.length();
    }
    public int getSetStartPosition() {
        return setLimit - hitsPerSet;
    }
    public int getTotalHits() {
        return hits.length();
    }
    public boolean hasNext() {
        
        return position < hits.length();
        
    }
    public boolean hasNextInSet() {
        
        return position < setLimit && position < hits.length();
        
    }
    public boolean hasNextSet() {
        
        return setLimit < hits.length();
        
    }
    public boolean hasPreviousSet() {
        
        return setLimit - hitsPerSet >= hitsPerSet;
        
    }
    public int length() {
        
        try {
            return hits.length();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public WebSearchHit next() {
        
        
        WebSearchHit hit = null;
        
        if (hasNext()) {
            try {
                position++;
                hit = new WebSearchHit(hits.doc(position - 1), position, hits.score(position - 1));
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
        
        if (setLimit < hits.length() + 1) {
            setLimit = setLimit + hitsPerSet;
            position = setLimit - hitsPerSet;
        }
        
    }
    public void previousSet() {
        
        if (setLimit - hitsPerSet >= hitsPerSet) {
            setLimit = setLimit - hitsPerSet;
            position = setLimit - hitsPerSet;
        }
        
    }
}
