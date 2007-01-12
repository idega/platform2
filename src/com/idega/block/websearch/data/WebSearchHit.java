package com.idega.block.websearch.data;

import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;




/**
 * <p><code>WebSearchHit</code> Represents a document returned by a search result.<br>
* This class is a part of the websearch webcrawler and search engine block. <br>
* It is based on the <a href="http://lucene.apache.org">Lucene</a> java search engine from the Apache group and loosly <br>
* from the work of David Duddleston of i2a.com.<br>
*
* @copyright Idega Software 2002
* @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 */ 

public final class WebSearchHit {
    
    private int rank;
    private float score;
    private Document document;
    

    private WebSearchHit() {
        
        
    }


    public WebSearchHit(Document d, int r, float s) {
        
        this.document = d;
        this.rank = r;
        this.score = s;
        
    }
    public String getCategories() {
        
        return this.document.get("categories");
        
    }
    public String getContentType() {
        
        return this.document.get("contentType");
        
    }
    public String getDescription() {
        
        return this.document.get("description");
        
    }
    public String getHREF() {
        
        String href = this.document.get("href");
        return (href != null) ? href : this.document.get("url");
        
    }
    public String getKeywords() {
        
        return this.document.get("keywords");
        
    }
    public long getPublished() {
        
        return DateField.stringToTime(this.document.get("published"));
        
    }
    public String getPublishedFormated() {
        
        SimpleDateFormat dateFormatter =
        new SimpleDateFormat("yyyy.MM.dd hh:mm:ss z");
        return dateFormatter.format(
        DateField.stringToDate(this.document.get("published")));
        
    }
    public int getRank() {
        return this.rank;
    }
    public float getScore() {
        
        return this.score;
        
    }
    public String getTitle() {
        
        return this.document.get("title");
        
    }
    public String getURL() {
        return this.document.get("url");
    }
    
    public String getContents() {
        return this.document.get("contents");
    }
    
    
	/**
	 * Method getContents. Gets a "centered" view of the content surrounding the first search word that is found.
	 * @param queryString
	 * @return String
	 */
    public String getContents(String queryString) {
        String contents =  this.document.get("contents");
        
        
        if( contents!=null ){
        	StringTokenizer tokens = new StringTokenizer(queryString);
        	int length = contents.length();
        	int maxLength = 200;	
        	
	        while ( tokens.hasMoreTokens() ){
	        	String word = tokens.nextToken();
	        	if( word.equals("AND") || word.equals("OR") || word.equals("NOT") ) {
					continue;
				}
	        	
	        	int middle = contents.indexOf(word);
	        	int wordLength = word.length();
	        	int wordLengthAndindex = middle+wordLength;
	        	int start = 0;
	        	int end = length;
	        	int half = maxLength/2;
	        	int left = half;
	        	
	        	if( middle!=-1 ){
	        		if( middle>=half ){
	        			start = middle-half;	
	        		}
	        		else{
	        			int margin = (half-middle);
	        			left+=margin;	
	        		}
	        		
	        		String temp = contents.substring(start, middle);
	        		        		
	        		temp+="<b>"+word+"</b>";
	        		
	        		if( (wordLengthAndindex+(left)) < length ){
	        			end = wordLengthAndindex+(left);
	        		}
	        	
	        			        		
	        		temp += contents.substring( wordLengthAndindex, end ) ;
	        		
	        		return temp;	        		   		
	        	}
				else {
					continue;
				}
	        }
	        
	        return contents.substring( 0 , Math.min(maxLength,length)) ;
        }
 
        
        return contents;
    }
    
    
    
}
