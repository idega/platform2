package com.idega.block.websearch.business;



import java.io.InputStream;
import java.util.List;

/**
 * <p><code>ContentHandler</code> Interface for content handlers. </p>
* This class is a part of the websearch webcrawler and search engine block. <br>
* It is based on the <a href="http://lucene.apache.org">Lucene</a> java search engine from the Apache group and loosly <br>
* from the work of David Duddleston of i2a.com.<br>
*
* @copyright Idega Software 2002
* @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 */


public interface ContentHandler {
    
    
    /**
     * Return author
     */
    public String getAuthor();
    /**
     * Return categories (from META tags)
     */
    public String getCategories();
    /**
     *	Return contents
     */
    public String getContents();
    /**
     *	Return description (from META tags)
     */
    public String getDescription();
    /**
     *	Return META HREF
     */
    public String getHREF();
    /**
     * Return keywords (from META tags)
     */
    public String getKeywords();
    /**
     * Return links
     */
    public List getLinks();
    /**
     *	Return published date (from META tag)
     */
    public long getPublished();
    /**
     *	Return description (from META tags)
     */
    public boolean getRobotFollow();
    /**
     *	Return description (from META tags)
     */
    public boolean getRobotIndex();
    /**
     *		Return page title
     */
    public String getTitle();
    /**
     * Parse Content.
     */
    public void parse(InputStream in);
}