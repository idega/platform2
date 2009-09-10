package com.idega.block.websearch.business;


/**
 * <p><code>LinkParser</code>
 *	Utility class for parsing and completing links (URL/URI). </p>
* This class is a part of the websearch webcrawler and search engine block. <br>
* It is based on the <a href="http://lucene.apache.org">Lucene</a> java search engine from the Apache group and loosly <br>
* from the work of David Duddleston of i2a.com.<br>
*
* @copyright Idega Software 2002
* @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 */
public final class LinkParser {
    
    /**
     * @param args  */    
    public static void main(String[] args) {
        System.out.println("hello");
        String rel = "\\a\\b\\c\\d\\y.x";
        //String rel =  "a/b/c/d/y.x";
        String chd = "./w.z";
        //String sep = "/";
        String sep = java.io.File.separator;
        
        System.out.println("sep:" + sep);
        System.out.println("path: " + getRealPath(rel, chd, sep));
    }
    
    /**
     * @param referencePath parent file path
     * @param relativePath child file path
     * @return <CODE>java.lang.String</CODE>
     */    
    public static String getRealPath(String referencePath, String relativePath,
                String seperator) {
                    
        String realPath = relativePath;
        // Looks for relative path and complete it.
        if (relativePath.startsWith("./")) {
           realPath = referencePath.substring(0, referencePath.lastIndexOf(seperator))
                    + relativePath.substring(1, relativePath.length());
        } else if (relativePath.startsWith("../")) {
            int back = 1;
            while (relativePath.indexOf("../", back*3) != -1) {
				back++;
			}
            int pos = referencePath.length();
            int count = back + 1;
            while (count-- > 0) {
                pos = referencePath.lastIndexOf(seperator, pos) - 1;
            }
            realPath = referencePath.substring(0, pos+2) 
                    + relativePath.substring(3*back, relativePath.length());
        } 
        
        return realPath;
    }
    
}
