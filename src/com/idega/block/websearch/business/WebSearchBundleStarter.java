package com.idega.block.websearch.business;

/**
 * This class is a part of the websearch webcrawler and search engine block.
 * <br>
 * It is based on the <a href="http://lucene.apache.org">Lucene </a> java search
 * engine from the Apache group and loosly <br>
 * from the work of David Duddleston of i2a.com. <br>
 * 
 * @copyright Idega Software 2002
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson </a>
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import com.idega.block.websearch.data.WebSearchIndex;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.util.EventTimer;
import com.idega.util.FileUtil;

public class WebSearchBundleStarter implements IWBundleStartable, ActionListener {

	private static final String IW_WEB_SEARCHER = "IW_WEB_SEARCHER";
	private static final String IGNORE_PARAMETERS = "ignore_parameters";

	private EventTimer timer;

	public static Collection ignoreParameters;

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.websearch";

	/*
	 * private static final String xml1 = " <?xml version=\"1.0\"
	 * encoding=\"ISO-8859-1\"?> \n <web-search>\n <!-- configure the indexes
	 * you wish to maintain here. \n The web search application can have several
	 * indexes. --> \n <index>\n <!-- Name of the index. This can be anything
	 * you want. \n note that the demo is setup to use the name \"main\"\n -->\n
	 * <name>main </name>\n \n <!-- Location where the index will be stored.
	 * Make sure the full\n directory path is already created and the
	 * application has permission\n to write and read at the specified
	 * location.\n You can specify a full path or a relative path based on the
	 * location\n of this file \"websearch.xml\". To define a relative path,
	 * start with a\n period (\".\"). To back out of the directory, use
	 * (\"../\").\n Example:\n full: z:/home/websearch/indexes or
	 * /home/websearch/indexes\n reative: ./indexes or ../indexes or
	 * ../../indexes \n -->\n <indexURI>../search/main </indexURI>\n <!-- The
	 * seed URL that the crawler will start. Can have multiple seeds --> \n
	 * <seed>"; private static final String xml2 =" </seed>\n <!-- The scope
	 * that the crawer will stay in. Put your servers domain to \n keep the
	 * crawler from indexing links outside of your site. \n Can have multiple
	 * scopes\n -->\n <scope>"; private static final String xml3 =" </scope>\n
	 * </index>\n </web-search>";
	 */

	public WebSearchBundleStarter() {
	}

	public void start(IWBundle bundle) {
		String xmlFile = bundle.getResourcesRealPath() + FileUtil.getFileSeparator() + "websearch.xml";

		System.out.println("WebSearch: Starting up...");
		System.out.println("WebSearch: loading configuration from : " + xmlFile);
		System.out.println("WebSearch: REMEMBER to edit the configuration file before indexing for the first time!");

		WebSearchManager.parseConfigXML(xmlFile);
		
		String parameters = bundle.getProperty(IGNORE_PARAMETERS, "");
		if (parameters != null && parameters.length() > 0) {
			ignoreParameters = new ArrayList();
			
			StringTokenizer tokens = new StringTokenizer(parameters, ",");
			while (tokens.hasMoreTokens()) {
				ignoreParameters.add(tokens.nextToken());
			}
		}

		timer = new EventTimer(EventTimer.THREAD_SLEEP_24_HOURS, IW_WEB_SEARCHER);
		timer.addActionListener(this);
		//Starts the thread while waiting for 10 mins. before the idegaWebApp
		// starts up.
		timer.start(10 * 60 * 1000);
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void actionPerformed(ActionEvent event) {
		//THIS ASSUMES THERE IS ONLY ONE INDEX AND SCOPE
		if (event.getActionCommand().equalsIgnoreCase(IW_WEB_SEARCHER)) {
			WebSearchIndex index = WebSearchManager.getIndex("main");
			if (index != null) {
				if (!index.getScope()[0].startsWith("http://localhost")) {//if true it needs to be indexed manually
					Crawler crawler = new Crawler(index, 2);
					if (ignoreParameters != null) {
						crawler.addIgnoreParameters(ignoreParameters);
					}
					crawler.crawl();
				}
			}
		}
	}

	/**
	 * @see com.idega.idegaweb.IWBundleStartable#stop(IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
		if (timer != null) {
			timer.stop();
			timer = null;
		}
	}
}