package com.idega.block.websearch.business;



import java.io.File;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.Iterator;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import com.idega.block.websearch.data.WebSearchIndex;
import com.idega.idegaweb.IWURL;
import com.idega.util.FileUtil;
import com.idega.util.text.TextSoap;


/**
 * <p><code>Crawler</code> Web crawler.</p>
* This class is a part of the websearch webcrawler and search engine block. <br>
* It is based on the <a href="http://lucene.apache.org">Lucene</a> java search engine from the Apache group and loosly <br>
* from the work of David Duddleston of i2a.com.<br>
*
* @copyright Idega Software 2002
* @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 */

public final class Crawler {
    
    private WebSearchIndex index;
    private IndexReader reader;
    private IndexWriter writer;
    
    private java.util.Stack linkQueue;
    private java.util.TreeSet links;
    private ContentHandler handler;
    
    private String rootURL;
    private String seedURL[];
    private String scopeURL[];
    private String indexPath; // search index path
    private boolean created; // if search index has been created
    private String cookie;
    private Collection ignoreParameters;
    // reporting
    private int reporting; // reporting level

    
    // Current URL data
    private java.net.URL currentURL;
    private String currentURLPath; // path of URL (no file)
    // private String href;
    private String contentType;
    private long lastModified;
    
    private ContentHandler htmlHandler = new HTMLHandler();
    private ContentHandler pdfHandler = new PDFHandler();
    
    
    
    
    /**
     *
     */
    private Crawler() {
    }
    /**
     *
     */
    public Crawler(WebSearchIndex index) {
        this(index, 0);
    }
    
    public Crawler(WebSearchIndex index, int reporting) {
        try {
            
            this.index = index;
            this.seedURL = index.getSeed();
            this.scopeURL = index.getScope();
            this.indexPath = index.getIndexPath();
            this.rootURL = seedURL[0].substring(0, seedURL[0].indexOf("/", 8));
            
            this.created = false;
            
            this.reporting = reporting;
            
            linkQueue = new java.util.Stack();
            links = new java.util.TreeSet();
            for (int i = 0; i < seedURL.length; i++) {
                links.add(seedURL[i].toLowerCase());
                linkQueue.push(seedURL[i]);
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        };
    }
    
    public void addIgnoreParameters(Collection parameters) {
    	ignoreParameters = parameters;
    }
    
    public void crawl() {
        try {
            
            if (reporting > 0) System.out.println("Websearch: START CRAWLING");
 
            File file =  new File(indexPath);
            
            if (!file.exists()) {
				//create directory structure                
                System.out.println("Websearch: creating index folders...");
                System.out.println("Websearch: "+indexPath);
             	FileUtil.createFileAndFolder(indexPath,"segments");
             
                if (reporting > 0) System.out.println("create new index");
                //IndexWriter writer = new IndexWriter(indexPath, new StopAnalyzer(), true);
                //writer.close();
            }
            /*else {
                // delete all files for now and build new index.
                // implement incremental index later.
				if (reporting > 0) System.out.println("index exists, delete all files");
                //FileUtil.delete(indexPath);
                
                //delete all
                IndexReader reader = IndexReader.open(indexPath);
                int count = reader.numDocs();
                if (reporting > 0) {
                    System.out.println("deleting " + count + " records");
                }
                for (int i = 0; i < count; i++) {
                    //if (reporting > 1) System.out.println("deleted " + i);
                    reader.delete(i);
                }
                reader.close();
                
                System.out.println("Websearch: creating index folders...");
                System.out.println("Websearch: "+indexPath);
             	FileUtil.createFileAndFolder(indexPath,"segments");
            }*/
            
            
            
            // create new IndexWriter
            writer = new IndexWriter(indexPath, new StopAnalyzer(), true);
            
            String url;
            //System.out.println(linkQueue.toString());
            if(linkQueue==null) System.out.println("WebSearch crawler: linkQueue is null! check that a trailing / is in the seed url");
            
            while (linkQueue!=null && !linkQueue.empty()) {
                url = (String)linkQueue.pop();

                if (!url.startsWith(rootURL)) {
                    // root has changed.  
                    // example http://www.12a.com to https://secure.i2a.com/
                    this.rootURL = url.substring(0, url.indexOf("/", 8));
                }
                
                if (reporting > 1) {
                		 System.out.println();
                    System.out.print("SCANNING : " + url);
                }
                
                String result = scanPage(url);

                if (result.equals("good")) {
                    if (reporting > 1) System.out.print(" status: " + result);
                    if (reporting > 2) {
                        System.out.println(" lastModified : " + lastModified);
                        System.out.println(" contentType : " + contentType);
                        System.out.println(" robot rules: index=" + handler.getRobotIndex()
                        + "  follow=" + handler.getRobotFollow());
                        System.out.println(" HREF : " +handler.getHREF());
                        System.out.println(" title : " +handler.getTitle());
                        System.out.println(" author : " +handler.getAuthor());
                        System.out.println(" published : " +handler.getPublished());
                        System.out.println(" description : " +handler.getDescription());
                        System.out.println(" keywords : " +handler.getKeywords());
                        System.out.println(" links : " + handler.getLinks());
                        if (reporting > 3) {
                            System.out.println(" contents : " +handler.getContents());
                        }
                    }
                    
                } else {
                    if (reporting == 1) {
                        System.out.println();
                        System.out.println("SCANNED : " + url);
                    }
                    if (reporting > 0) System.out.println(" *status: " + result);
                }
                
            }
            if (reporting > 0) {
                System.out.println();
                System.out.println();
                System.out.println("DONE CRAWLING");
                System.out.println("links crawled");
                java.util.Iterator it = links.iterator();
                while (it.hasNext()) {
                    System.out.println(it.next());
                }
                System.out.println();
            }
            
            //optinmize the search speed
            writer.optimize();
            
            writer.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public final void handleHTML(HttpURLConnection httpCon) throws Exception {
        
        handler = htmlHandler;
        handler.parse(httpCon.getInputStream());
        
        if (handler.getRobotFollow()) {
            java.util.List links = handler.getLinks();
            //System.out.println("link count : " + links.size());
            for (int i = 0; i < links.size(); i++) {
                handleLink((String)links.get(i));
            }
        }
        
        if (handler.getRobotIndex()) indexLucene();
        
    }
    
    
    public void handleLink(String url) {
	    	String normalizedURL = normalizedURL(url);
	    	String lowerCaseURL = normalizedURL.toLowerCase();
	    	
	    	if (!(normalizedURL.startsWith("http://") || normalizedURL.startsWith("https://"))) {
	    		// link needs to be evaluated, parsed and completed
	    		normalizedURL = parseHREF(normalizedURL, lowerCaseURL);
	    		if (normalizedURL != null) {
	    			lowerCaseURL = normalizedURL.toLowerCase();
	    		}
	    		// is full URL
	    	} 
	    	
	    	if (normalizedURL != null && inScope(normalizedURL)) {
	    		// is full URL and in scope.
	    		if (!links.contains(lowerCaseURL)) {
	    			links.add(lowerCaseURL);
	    			linkQueue.push(normalizedURL);
	    		}
	    	}
    	
    }
    
    
    
    /**
     * Takes an url and rearranges it so all query parameters are in the same order as another url with the same query parameters.
     * This helps eliminate duplicates. It also changes servlet/IBMainServlet/? and servlet/IBMainServlet? to index.jsp?
	 * @param url
	 * @return
	 */
	protected String normalizedURL(String url) {
        //change servlet path to index.jsp if crawling an idegaweb application
        String normalized = TextSoap.findAndReplace(url,"servlet/IBMainServlet/?", "index.jsp?");
        normalized = TextSoap.findAndReplace(url,"servlet/IBMainServlet?", "index.jsp?");
        
        IWURL temp = new IWURL(normalized);
      
        return temp.getFullURL();
	}
	
	public final void handlePDF(HttpURLConnection httpCon) throws Exception {
        
        handler = pdfHandler;
        handler.parse(httpCon.getInputStream());
        
        indexLucene();
        
    }
/*
 * Index the html in lucene.
 * Path is the url path, and contents is the parsed html
 */
    private void indexLucene() {
        try {
            
            //IndexWriter writer;
            //if (!created) {
            //writer = new IndexWriter(this.indexPath, new StopAnalyzer(), true);
            //created = true;
            //} else {
            //writer = new IndexWriter(this.indexPath, new StopAnalyzer(), false);
            //}
            Document mydoc = new Document();
            mydoc.add(new Field("uid", currentURL.toString().toLowerCase(), false, true, false));
            mydoc.add(Field.Text("url", currentURL.toString()));
            mydoc.add(Field.Text("contentType", contentType));
            mydoc.add(Field.Keyword("lastModified",DateField.timeToString(lastModified)));
            String contents = handler.getContents();
                      
            
            if( contents!=null ){
            	//clean more!
            	contents = TextSoap.findAndCut(contents,">");
            	contents = TextSoap.findAndCut(contents,"<");
            	contents = TextSoap.findAndCut(contents,"•?");
            	
            	 mydoc.add(Field.Text("contents", contents));
            }
            
            if (handler.getTitle() != null) {
                mydoc.add(Field.Text("title", handler.getTitle()));
            }
            if (handler.getKeywords() != null) {
                mydoc.add(Field.Text("keywords", handler.getKeywords()));
            }
            if (handler.getDescription() != null) {
                mydoc.add(Field.Text("description", handler.getDescription()));
            }
            if (handler.getCategories() != null) {
                mydoc.add(Field.Text("categories", handler.getCategories()));
            }
            if (handler.getPublished() != -1) {
                // use meta tag
                mydoc.add(Field.Keyword("published",
                DateField.timeToString(handler.getPublished())));
            } else {
                // use lastmodified from http header.
                mydoc.add(Field.Keyword("published",
                DateField.timeToString(lastModified)));
            }
            if (handler.getPublished() != -1) {
                // use meta tag
                mydoc.add(Field.Keyword("published",
                DateField.timeToString(handler.getPublished())));
            } else {
                // use lastmodified from http header.
                
            }
            if (handler.getHREF() != null) {
                // Replace $link with url.
                String href = handler.getHREF();
                int pos = href.indexOf("$link");
                href = href.substring(0, pos) + currentURL.toString()
                + href.substring(pos + 5, href.length());
                mydoc.add(Field.UnIndexed("href", href));
            }
            writer.addDocument(mydoc);
                    
            //writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    public boolean inScope(String url) {
        if (containsIgnoreParameter(url)) {
        	return false;
        }
    	
        for (int i = 0; i < scopeURL.length; i++) {
            if (url.startsWith(scopeURL[i])) {
                // in scope
                return true;
            }
        }
        // not in scope
        return false;
        
    }
    
    public boolean containsIgnoreParameter(String url) {
    		if (ignoreParameters != null) {
    			Iterator iter = ignoreParameters.iterator();
				while (iter.hasNext()) {
					String parameter = (String) iter.next();
					if (url.indexOf(parameter) != -1) {
						return true;
					}
				}
    		}
    		return false;
    }
    
    public String parseHREF(String url, String urlLowCase) {
        
        // Looks for incomplete URL and completes them
        if (urlLowCase.startsWith("/")) {
            url = rootURL + url;
        } else if (urlLowCase.startsWith("./")) {
            url = currentURLPath + url.substring(1, url.length());
        } else if (urlLowCase.startsWith("../")) {
            int back = 1;
            while (urlLowCase.indexOf("../", back*3) != -1) back++;
            int pos = currentURLPath.length();
            int count = back;
            while (count-- > 0) {
                pos = currentURLPath.lastIndexOf("/", pos) - 1;
            }
            url = currentURLPath.substring(0, pos+2) + url.substring(3*back, url.length());
        } else if (urlLowCase.startsWith("javascript:")) {
            // ignore javascript:...
            url = null;
        } else if (urlLowCase.startsWith("#")) {
            // internal anchor... ignore.
            url = null;
        } else if (urlLowCase.startsWith("mailto:")) {
            // handle mailto:...
            url = null;
        } else {
            url = currentURLPath + "/" + url;
        }
        
        // strip anchor if exists otherwise crawler may index content multiple times
        // links to the same url but with unique anchors would be considered unique
        // by the crawler when they should not be
        //int i;
        if (url != null) {
            int i;
            if ((i = url.indexOf("#")) != -1) {
                url = url.substring(0,i);
            }
        }
        return url;
        
    }

    public String scanPage(String urlString) {
        
        String status = "good";
        try {
            currentURL = new java.net.URL(urlString);
            currentURLPath = urlString.substring(0, urlString.lastIndexOf("/"));
            HttpURLConnection httpCon = (HttpURLConnection)currentURL.openConnection();
            
            httpCon.setRequestProperty("User-Agent", "idegaWeb Web Search Engine Crawler http://www.idega.com");
            
            if (cookie != null) {
                httpCon.setRequestProperty("Cookie", this.cookie);
            }
            httpCon.connect();
            
            
            lastModified = httpCon.getLastModified();
            
            if (httpCon.getHeaderField("Set-Cookie") != null) {
                cookie = stripCookie(httpCon.getHeaderField("Set-Cookie"));
                if (reporting > 1) System.out.print(" got cookie : " + cookie);
            }
            
            if (httpCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                contentType = httpCon.getContentType();
                if (contentType.indexOf("text/html") != -1) {
                    handleHTML(httpCon);
                } else if (contentType.indexOf("application/pdf") != -1) {
                    handlePDF(httpCon);
                } else {
                    status = "Not an excepted content type : " + contentType;
                }
            } else {
                status = "bad";
            }
            httpCon.disconnect();
        } catch (java.net.MalformedURLException mue) {
            status = mue.toString();
        } catch (java.net.UnknownHostException uh) {
            status = uh.toString(); // Mark as a bad URL
        } catch (java.io.IOException ioe) {
            status = ioe.toString(); // Mark as a bad URL
        } catch (Exception e) {
            status = e.toString(); // Mark as a bad URL
        }
        
        return status;
    }
    public static String stripCookie(String cookie) {
        
        int loc = cookie.indexOf(";");
        return (loc > 0) ? cookie.substring(0, loc) : cookie;
        
    }
}
